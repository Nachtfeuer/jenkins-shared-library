pipeline {
    agent {
        docker {
            image 'centos:7' args '-u root'
        }
    }

    stages {
        stage('Build') {
            steps {
                sh 'yum -y install java-1.8.0-openjdk'
            }
        }
    }
}
