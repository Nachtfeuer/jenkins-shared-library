pipeline {
    agent { dockerfile true }

    stages {
        stage('Build') {
            steps {
                sh './gradlew'
            }

            post {
                always {
                    junit '**/TEST*.xml'
                }
            }
        }
    }
}
