pipeline {
    agent { label "pipeline" }

    environment {
        // Make sure kubectl can find kubeconfig
        // Jenkins user must already have kubectl access
        KUBECONFIG = "${env.HOME}/.kube/config"
    }

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

        stage('Unit Tests') {
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

        stage('Build Application') {
            steps {
                bat "./gradlew build"
            }
        }

        stage('Docker Build & Push') {
            steps {
                script {
                    bat "docker build -t quantumempress/calculator:latest ."
                    bat "docker push quantumempress/calculator:latest"
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                bat '''
                kubectl apply -f hazelcast.yaml
                kubectl apply -f service.yaml
                kubectl apply -f deployment.yaml
                kubectl get pods
                kubectl get services
                '''
            }
        }
    }

    post {
        always {
            mail to: 'prexcy99@gmail.com',
                 subject: "Pipeline Completed: ${currentBuild.fullDisplayName}",
                 body: "Build finished with status: ${currentBuild.currentResult}\n\n${env.BUILD_URL}"

            slackSend channel: '#oma-test-channel',
                      color: currentBuild.currentResult == 'SUCCESS' ? 'green' : 'red',
                      message: "Pipeline ${currentBuild.fullDisplayName} finished with result: ${currentBuild.currentResult}"
        }
    }
}
