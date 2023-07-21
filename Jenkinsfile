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
                sh 'chmod +x mvnw && ./mvnw clean org.sonarsource.scanner.maven:sonar-maven-plugin:3.9.0.2155:sonar'
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
