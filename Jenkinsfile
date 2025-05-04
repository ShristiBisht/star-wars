pipeline {
    agent any

    environment {
        DOCKER_IMAGE_BACKEND = "star-wars-backend"
        DOCKER_IMAGE_FRONTEND = "star-wars-frontend"
        DOCKER_IMAGE_KAFKA_UI = "kafka-ui"
        DOCKER_REGISTRY = "shristibisht"  // Replace with your Docker Hub username or registry
    }

    stages {
        stage('Checkout Code') {
            steps {
                git 'https://github.com/ShristiBisht/star-wars.git'  // Replace with your Git repository URL
            }
        }
        
        stage('Build Backend Docker Image') {
            steps {
                script {
                    // Build the backend Docker image
                    sh "docker build -t ${DOCKER_REGISTRY}/${DOCKER_IMAGE_BACKEND} ./backend"
                }
            }
        }

        stage('Build Frontend Docker Image') {
            steps {
                script {
                    // Build the frontend Docker image
                    sh "docker build -t ${DOCKER_REGISTRY}/${DOCKER_IMAGE_FRONTEND} ./frontend"
                }
            }
        }

        stage('Build Kafka UI Docker Image') {
            steps {
                script {
                    // Pull the Kafka UI image (or build if you have customizations)
                    sh "docker pull provectuslabs/kafka-ui:latest"
                }
            }
        }

        stage('Push Docker Images') {
            steps {
                script {
                    // Push the backend Docker image
                    sh "docker push ${DOCKER_REGISTRY}/${DOCKER_IMAGE_BACKEND}"
                    // Push the frontend Docker image
                    sh "docker push ${DOCKER_REGISTRY}/${DOCKER_IMAGE_FRONTEND}"
                    // Push the Kafka UI image
                    sh "docker push provectuslabs/kafka-ui:latest"
                }
            }
        }

        stage('Deploy Services with Docker Compose') {
            steps {
                script {
                    // Use Docker Compose to deploy the containers
                    sh 'docker-compose -f docker-compose.yml up -d'
                }
            }
        }

        stage('Health Check') {
            steps {
                script {
                    // Optionally, wait for services to become healthy
                    sh 'docker-compose -f docker-compose.yml ps'
                }
            }
        }
    }

    post {
        always {
            // Clean up, remove containers after pipeline run
            sh 'docker-compose -f docker-compose.yml down'
        }

        success {
            // Notify or perform any actions when the pipeline succeeds
            echo 'Pipeline completed successfully.'
        }

        failure {
            // Notify or perform any actions when the pipeline fails
            echo 'Pipeline failed.'
        }
    }
}
x