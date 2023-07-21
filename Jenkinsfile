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
                    def scannerHome = tool "sonarQube"
                    withSonarQubeEnv("sonarQube") {
                      sh "${scannerHome}/bin/sonar-scanne
                      -Dsonar.projectKey=test \
                      -Dsonar.host.url=http://localhost:9000 \
                      -Dsonar.login=sqp_09447f0697ea54e39e8c13fdf4ca8f567fe8b757"
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
