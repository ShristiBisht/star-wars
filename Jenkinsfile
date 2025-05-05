pipeline {
    agent any

    environment {
        DOCKER_HOME = tool 'myDocker'
        PATH = "${DOCKER_HOME}/bin:${env.PATH}"
    }

    stages {
        stage('Initialize') {
            steps {
                sh 'docker --version'
            }
        }

        stage('Docker Test') {
            steps {
                sh 'docker ps'
            }
        }
    }
}
