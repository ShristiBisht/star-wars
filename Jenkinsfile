pipeline {
    agent any
    stages {
        stage('Docker CLI Test') {
            steps {
                sh 'docker --version'
                sh 'docker ps'
            }
        }
    }
}
