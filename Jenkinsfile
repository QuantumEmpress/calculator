pipeline {
    agent any   // 👈 works on any available agent

    stages {
        stage('Checkout') {
            steps {
                git url: 'https://github.com/QuantumEmpress/Calculator.git', branch: 'main'
            }
        }

        stage('Compile') {
            steps {
                sh "chmod +x gradlew"
                sh "./gradlew compileJava"
            }
        }

        stage('Unit Test') {
            steps {
                sh "./gradlew test"
            }
        }

        stage('Code Coverage') {
            steps {
                sh "./gradlew jacocoTestReport"
                publishHTML(target: [
                    allowMissing: false,
                    alwaysLinkToLastBuild: false,
                    keepAll: true,
                    reportDir: 'build/reports/jacoco/test/html',
                    reportFiles: 'index.html',
                    reportName: 'JaCoCo Report'
                ])
                sh "./gradlew jacocoTestCoverageVerification"
            }
        }

        stage("Static Code Analysis") {
            steps {
                sh "./gradlew checkstyleMain"
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
    }

    post {
        always {
            mail to: 'prexcy99@gmail.com',
                subject: "Completed Pipeline: ${currentBuild.fullDisplayName}",
                body: "Your build completed. Please check: ${env.BUILD_URL}"

            slackSend channel: '#my-dev-diary',
                color: (currentBuild.currentResult == 'SUCCESS' ? 'good' : 'danger'),
                message: "Pipeline *${currentBuild.fullDisplayName}* finished with status: *${currentBuild.currentResult}*"
        }
    }
}