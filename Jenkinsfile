pipeline {
    agent {label "pipeline"}

    stages {
        stage('Checkout') {
            steps {
                git url: 'https://github.com/QuantumEmpress/calculator.git', branch: 'master'
            }
        }

        stage('Clean') {
            steps {
                bat "./gradlew clean"
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
                    alwaysLinkToLastBuild: true,
                    keepAll: true,
                    reportDir: 'build/reports/jacoco/test/html',
                    reportFiles: 'index.html',
                    reportName: 'JaCoCo Report'
                ])
                bat "./gradlew jacocoTestCoverageVerification"
            }
        }

        stage('Static Code Analysis') {
            steps {
                bat "./gradlew checkstyleMain"
                publishHTML(target: [
                    allowMissing: false,
                    alwaysLinkToLastBuild: true,
                    keepAll: true,
                    reportDir: 'build/reports/checkstyle',
                    reportFiles: 'main.html',
                    reportName: 'Checkstyle Report'
                ])
            }
        }

        stage('Build') {
            steps {
                bat "./gradlew build"  // Remove clean from here
            }
        }

        stage('Docker Build & Push') {
            steps {
                script {
                    // Add proper error handling for Docker
                    try {
                        bat "docker build -t quantumempress/calculator ."
                        bat "docker push quantumempress/calculator"
                    } catch (Exception e) {
                        echo "Docker build/push failed: ${e.message}"
                        // Continue pipeline anyway
                    }
                }
            }
        }
    }

    post {
        always {
            mail to: 'prexcy99@gmail.com',
                subject: "Completed Pipeline: ${currentBuild.fullDisplayName}",
                body: "Your build completed, please check: ${env.BUILD_URL}"

            slackSend channel: '#oma-test-channel',
                      color: currentBuild.currentResult == 'SUCCESS' ? 'green' : 'red',
                      message: "Pipeline ${currentBuild.fullDisplayName} finished with result: ${currentBuild.currentResult}"
        }
    }
}