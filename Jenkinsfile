@Library('jenkins-shared-library@master') _

pipeline {
    agent { dockerfile true }

    stages {
        stage('Build') {
            steps {
                gradle.clean().check()
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
