pipeline {

    agent any

    stages {
        stage('Checkout') {
            steps {
                git url: "https://github.com/QuantumEmpress/Calculator.git", branch: "main"
            }
        }
        stage('Linux Permission') {
            steps {
                sh "chmod +x gradlew"
            }
        }
        stage('Unit Test') {
            steps {
                sh "./gradlew test "
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
        stage("Static code analysis") {
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
        stage('Build') {
            steps {
                sh "./gradlew build"
            }
        }
        stage('Deploy') {
            steps {
                sh "docker tag dorati-app localhost:5000/dorati"
                sh "docker push localhost:5000/dorati"
            }
        }
        }
    post {
        always {
            mail to: 'prexcy99@gmail.com',
                subject: "Completed Pipeline: ${currentBuild.fullDisplayName}",
                body: "Your build completed, please check: ${env.BUILD_URL}"
            slackSend channel: '#oma-test-channel', color: 'green', message: "The pipeline ${currentBuild.fullDisplayName} result."
        }
    }
}