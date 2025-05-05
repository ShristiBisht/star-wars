pipeline {
    agent {
        docker {
            image 'docker:24.0-cli'  // Lightweight image with Docker CLI
            args '-v /var/run/docker.sock:/var/run/docker.sock'
        }
    }
    stages {
        stage('Docker Test') {
            steps {
                sh 'docker ps'
            }
        }
    }
}
