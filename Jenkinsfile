pipeline {
    agent any

    environment {
        DOCKER_HOME = tool 'myDocker'
        PATH = "${DOCKER_HOME}/bin:${env.PATH}"
    }

    stages {
        stage('Initialize') {
            steps {
                sh '''
                    docker --version
                    # Add jenkins user to docker group
                    groupadd docker || true
                    usermod -aG docker jenkins || true
                    newgrp docker || true
                '''
            }
        }

        stage('Docker Test') {
            steps {
                sh 'sudo docker ps'
            }
        }
    }
}
