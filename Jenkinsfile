pipeline {
  agent any

  stages {

    stage("build"){
      steps {
        echo 'building the app..'
      }
    }
    stage('SonarQube Analysis') {
            steps {
                script {
                    def scannerHome = tool 'sq1'
                    withSonarQubeEnv(installationName: 'sq1') {
                        sh "${scannerHome}/bin/sonar-scanner \
                      -Dsonar.projectKey=test \
                      -Dsonar.host.url=http://192.168.253.1:9000 \
                      -Dsonar.login=sqp_78715bc215024df004b4ac2e89e8a9579fc01c52"
                    }
                }
            }
        }
    stage("test"){
      steps {
        echo 'testing the app..'
      }
    }
    stage("deploy"){
      steps {
        echo 'deploying the app..'
      }
    }
    
  }
  
}
