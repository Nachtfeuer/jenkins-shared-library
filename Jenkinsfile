@Library('jenkins-shared-library@master') _

pipeline {
    agent { dockerfile true }

    stages {
        stage('Build') {
            steps {
                script {
                    xgradle.build()
                }
            }

            post {
                always {
                    junit '**/TEST*.xml'
                    jacoco()
                }
            }
        }
    }
}
