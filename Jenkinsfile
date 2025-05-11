pipeline {
    agent any
    stages {
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
                    def rawOutput = sh(
                script: "grep -m1 -o '<td class=\"ctr2\">[0-9]\\+%</td>' target/site/jacoco/index.html",
                returnStdout: true
            ).trim()

            // Extract just the number part from the HTML line
            def coveragePercentage = rawOutput.replaceAll(/[^\d]/, '')  // Removes all non-digit characters

                    echo "Code coverage: ${coveragePercentage}%"
                    if (coveragePercentage.toInteger() < 90) {
                        error "Code coverage is below the 90% threshold!"
                    }
                }
            }
        }
        stage('Start services') {
            steps {
                script {
                    sh 'docker-compose down'
                    // This is where your docker-compose command goes
                    sh 'docker-compose up -d --build'
                }
            }
        }
    }
}

