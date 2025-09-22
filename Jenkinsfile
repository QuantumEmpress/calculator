pipeline {
    agent {label "pipeline"}

    environment {
        DOCKER_IMAGE = "quantumempress/calculator"
        DOCKER_TAG = "${env.BUILD_ID}"
    }

    stages {
        stage('Checkout') {
            steps {
                git url: 'https://github.com/QuantumEmpress/calculator.git', branch: 'master'
            }
        }

        stage('Compile') {
            steps {
                bat "./gradlew compileJava"
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
                bat "./gradlew clean build"
            }
        }

        stage('Docker Build & Push') {
            // Add a condition to skip this stage if credentials are missing
            when {
                expression {
                    try {
                        withCredentials([usernamePassword(credentialsId: 'docker-hub-credentials', usernameVariable: 'TEST_USER', passwordVariable: 'TEST_PASS')]) {
                            return true
                        }
                    } catch (Exception e) {
                        echo "Docker credentials not found, skipping Docker stage"
                        return false
                    }
                }
            }
            environment {
                DOCKER_CREDS = credentials('docker-hub-credentials')
            }
            steps {
                script {
                    // Login to Docker Hub
                    bat "docker login -u ${DOCKER_CREDS_USR} -p ${DOCKER_CREDS_PSW}"

                    // Build with a specific tag
                    bat "docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} ."
                    bat "docker tag ${DOCKER_IMAGE}:${DOCKER_TAG} ${DOCKER_IMAGE}:latest"

                    // Push both specific tag and latest
                    bat "docker push ${DOCKER_IMAGE}:${DOCKER_TAG}"
                    bat "docker push ${DOCKER_IMAGE}:latest"

                    // Logout from Docker Hub
                    bat "docker logout"
                }
            }
        }
    }

    post {
        always {
            mail to: 'prexcy99@gmail.com',
                subject: "Completed Pipeline: ${currentBuild.fullDisplayName}",
                body: "Your build completed, please check: ${env.BUILD_URL}"

            // Wrap Slack notification in a try-catch to prevent pipeline failure
            script {
                try {
                    slackSend channel: '#oma-test-channel',
                              color: currentBuild.currentResult == 'SUCCESS' ? 'green' : 'red',
                              message: "Pipeline ${currentBuild.fullDisplayName} finished with result: ${currentBuild.currentResult}"
                } catch (Exception e) {
                    echo "Slack notification failed: ${e.getMessage()}"
                    // Continue pipeline without failing
                }
            }
        }
    }
}