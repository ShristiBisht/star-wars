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
                    def coveragePercentage = sh(
                        script: '''
                        grep -o '<td class="ctr2">[0-9]*%</td>' target/site/jacoco/index.html | \
                        sed -E 's/.*>([0-9]+)%<.*/\\1/' | head -n 1
                        ''',
                        returnStdout: true
                    ).trim()

                    echo "Code coverage: ${coveragePercentage}%"
                    if (coveragePercentage.toFloat() < 70) {
                        error "Code coverage is below the 70% threshold!"
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

