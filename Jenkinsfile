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

        stage('Build and Test') {
            steps {
                script {
                    // Run tests and generate the code coverage report
                    sh './mvnw clean verify'
                }
            }
        }

        stage('Post Coverage Check') {
            steps {
                script {
                    // Parse the JaCoCo coverage report and fail if coverage is below 60%
                    def coverageReport = readFile('target/site/jacoco/index.html')
                    def coveragePercentage = sh(
                        script: 'grep "<span class=\\"percentage\\">" target/site/jacoco/index.html | sed -E \'s/.*<span class=\\"percentage\\">([^<]+)<.*/\\1/\'',
                        returnStdout: true
                    ).trim()
                    echo "Code coverage: ${coveragePercentage}%"
                    if (coveragePercentage.toFloat() < 60) {
                        error "Code coverage is below the 60% threshold!"
                    }
                }
            }
        }
    }
}

