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
                        sh "${scannerHome}/bin/sonar-scanner"
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
