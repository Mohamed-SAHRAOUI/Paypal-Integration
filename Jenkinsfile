pipeline {
  agent any


  stages {

    stage("build"){
      steps {
        echo 'building the app..'
      }
    }

    stage("SonarQube"){
      steps {
          withSonarQubeEnv(installationName: 'sq1') {
                sh './mvnw clean org.sonarsource.scanner.maven:sonar-maven-plugin:4.8.0.2856:sonar'
          }
      }
    }

    
    stage("deploy"){
      steps {
        echo 'deploying the app..'
      }
    }
    
  }
  
}
