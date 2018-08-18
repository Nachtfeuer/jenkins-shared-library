@Library('jenkins-shared-library@master') _

pipeline {
    agent { dockerfile true }

    stages {
        stage('Build') {
            steps {
                gradle.build()
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
