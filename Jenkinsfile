pipeline {
    agent {
        label "pipeline"
    }

    stages {
        stage('Checkout') {
            steps {
                git url: "https://github.com/QuantumEmpress/calculator.git", branch: "main"
            }
        }

        stage('Check Docker Installation') {
            steps {
                bat "docker version"
            }
        }

        stage('Unit Test') {
            steps {
                bat "./gradlew test"
            }
        }

        stage('Code Coverage') {
            steps {
                bat "./gradlew jacocoTestReport"
                publishHTML(target: [
                    allowMissing: false,
                    alwaysLinkToLastBuild: false,
                    keepAll: true,
                    reportDir: 'build/reports/jacoco/test/html',
                    reportFiles: 'index.html',
                    reportName: 'JaCoCo Report'
                ])
                bat "./gradlew jacocoTestCoverageVerification"
            }
        }

        stage("Static Code Analysis") {
            steps {
                bat "./gradlew checkstyleMain"
                publishHTML(target: [
                    allowMissing: false,
                    alwaysLinkToLastBuild: false,
                    keepAll: true,
                    reportDir: 'build/reports/checkstyle',
                    reportFiles: 'main.html',
                    reportName: 'Checkstyle Report'
                ])
            }
        }

        stage('Build') {
            steps {
                bat "./gradlew clean"
                bat "./gradlew build"
            }
        }

        stage("Docker Login") {
            steps {
                withCredentials([[$class: 'UsernamePasswordMultiBinding',
                                  credentialsId: 'docker-hub',
                                  usernameVariable: 'USERNAME',
                                  passwordVariable: 'PASSWORD']]) {
                    bat "docker login --username %USERNAME% --password %PASSWORD%"
                }
            }
        }

        stage('Docker Build') {
            steps {
                bat "docker build -t QuantumEmpress/calculator ."
            }
        }

        stage('Deploy') {
            steps {
                bat "docker push QuantumEmpress/calculator"
            }
        }
    }

    post {
        always {
            mail to: 'prexcy99@gmail.com',
                 subject: "Completed Pipeline: ${currentBuild.fullDisplayName}",
                 body: "Your build completed, please check: ${env.BUILD_URL}"

            slackSend channel: '#oma-test-channel',
                      color: 'green',
                      message: "The pipeline ${currentBuild.fullDisplayName} result."
        }
    }
}
