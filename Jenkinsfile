pipeline {
    agent { dockerfile true }

    stages {
        stage('Build') {
            steps {
                sh './gradlew clean check'
            }

            post {
                always {
                    junit '**/TEST*.xml'
                }
            }
        }
    }
}
