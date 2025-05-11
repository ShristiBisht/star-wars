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
                    def coveragePercentage = sh(
                        script: '''
                        grep -o '<td class="ctr2">[0-9]*%</td>' target/site/jacoco/index.html | \
                        sed -E 's/.*>([0-9]+)%<.*/\\1/' | head -n 1
                        ''',
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

