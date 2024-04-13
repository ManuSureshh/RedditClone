pipeline{
    agent any
    
    tools{
        jdk 'jdk17'
        nodejs 'node16'
    }
    
    environment{
        SCANNER_HOME=tool 'sonar-scanner'
    }
    
    stages{
        
        stage('clean workspace'){
            steps{
                cleanWs()
            }
        }
        
        stage('code checkout'){
            steps{
                git branch: 'main', url: 'https://github.com/ManuSureshh/RedditClone.git'
            }
        }
        
        stage('Install Dependencies') {
            steps {
                sh "npm install"
            }
        }
        
        stage("Sonarqube Analysis "){
            steps{
                withSonarQubeEnv('sonar-server') {
                    sh ''' $SCANNER_HOME/bin/sonar-scanner -Dsonar.projectName=Reddit \
                    -Dsonar.projectKey=Reddit '''
                }
            }
        }
        
        stage("quality gate"){
           steps {
                script {
                    waitForQualityGate abortPipeline: false, credentialsId: 'SonarQube Token' 
                }
            } 
        }
        
        stage('OWASP FS SCAN') {
            steps {
                dependencyCheck additionalArguments: '--scan ./ --disableYarnAudit --disableNodeAudit', odcInstallation: 'DP-Check'
                dependencyCheckPublisher pattern: '**/dependency-check-report.xml'
            }
        }
        
        stage('TRIVY FS SCAN') {
            steps {
                sh "trivy fs . > trivyfs.txt"
            }
        }
        
        stage("Docker Build & Push"){
            steps{
                script{
                   withDockerRegistry(credentialsId: 'docker', toolName: 'docker'){   
                       sh "docker build -t reddit ."
                       sh "docker tag reddit manusuresh126/reddit:latest "
                       sh "docker push manusuresh126/reddit:latest "
                    }
                }
            }
        }
        
        stage("TRIVY"){
            steps{
                sh "trivy image sevenajay/reddit:latest > trivy.txt" 
            }
        }
        
        stage('Deploy to container'){
            steps{
                sh 'docker run -d --name reddit -p 3000:3000 manusuresh126/reddit:latest'
            }
        }
        
        stage('k8sdemo'){
            steps{
                script{
                    withKubeConfig(caCertificate: '', clusterName: '', contextName: '', credentialsId: 'k8s', namespace: '', restrictKubeConfigAccess: false, serverUrl: ''){
                        sh 'kubectl get node'
                    }
                }
            }
        }
    }
}
