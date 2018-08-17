pipeline {
    agent none

    stages {
        stage('Prepare') {
            agent {
                dockerfile {
                    filename 'Dockerfile'
                    dir '.'
                    label 'openjdk-1.8'
                    additionalBuildArgs  '--build-arg version=1.0'
                }
            }
        }

        stage('Build') {
            agent {
                docker { image 'openjdk-1.8:1.0' }
            }

            steps {
                sh './gradlew'
            }
        }
    }
}
