pipeline {
    agent { label "pipeline" }

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

        stage('check version') {
                    steps {
                        bat "docker version"
                        bat "wsl -d ubuntu ansible --version"
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
                bat "./gradlew build"
            }
        }

        stage('Docker Build & Push') {
            steps {
                script {
                    try {
                        bat "docker build -t quantumempress/calculator ."
                        bat "docker push quantumempress/calculator"
                    } catch (Exception e) {
                        echo "Docker build/push failed: ${e.message}"
                    }
                }
            }
        }

        stage('Docker Run') {
            steps {
                script {
//                     // Stop old container if running
//                     bat "docker stop calculator-om || echo 'No existing container to stop'"
//                     bat "docker rm calculator-om || echo 'No existing container to remove'"
//
//                     // Run new container safely on a free port
                    bat "docker run -d -p 9091:9090 --name calculator-oma-2-licha quantumempress/calculator"
                }
            }
        }

            stage('Docker') {
                    steps {
                        bat "wsl -d ubuntu ansible-playbook -i /home/omalicha/ansible/hosts /home/omalicha/docker_playbook.yml"
                    }
                }


        stage('Deploy') {
            steps {
                bat "wsl -d ubuntu ansible-playbook -i /home/omalicha/ansible/hosts /home/omalicha/hazelcat.yml"
            }
        }
    }

    post {
        always {
            mail to: 'prexcy99@gmail.com',
                 subject: "Completed Pipeline: ${currentBuild.fullDisplayName}",
                 body: "Your build completed. Check it here: ${env.BUILD_URL}"

            slackSend channel: '#oma-test-channel',
                      color: currentBuild.currentResult == 'SUCCESS' ? 'green' : 'red',
                      message: "Pipeline ${currentBuild.fullDisplayName} finished with result: ${currentBuild.currentResult}"

             bat "docker stop calculator-oma-2-licha"
             bat "docker rm  calculator-oma-2-licha"
        }
    }
}