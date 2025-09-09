pipeline {
    agent any
    tools{
        maven 'maven_3_9_9'
    }
    stages{
        stage('Build maven'){
            steps{
                checkout scmGit(branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/vicho314/TINGESO-2-Backend']])
                sh 'mvn -DskipTests clean package'
            }
        }

        stage('Unit Tests') {
            steps {
                // Run Maven 'test' phase. It compiles the test sources and runs the unit tests
                sh 'mvn test' // Use 'bat' for Windows agents or 'sh' for Unix/Linux agents
            }
        }

        stage('Build docker image'){
            steps{
                script{
                    sh 'docker build -t vicho314/toolrent-backend:latest .'
                }
            }
        }
        stage('Push image to Docker Hub'){
            steps{
                script{
                   withCredentials([usernamePassword(credentialsId: 'passwid', usernameVariable: 'USERDOCK', passwordVariable: 'PASSWDOCK')]) {
                        sh 'docker login -u $USERDOCK -p $PASSWDOCK'
                   }
                   sh 'docker push vicho314/toolrent-backend:latest'
                }
            }
        }
    }
}
