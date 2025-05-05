pipeline {
    agent any
    stages {
        stage('Start services') {
            steps {
                script {
                    // This is where your docker-compose command goes
                    sh 'docker-compose up -d --build'
                }
            }
        }
    }
}
