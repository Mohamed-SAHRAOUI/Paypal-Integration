pipeline {
  agent any

  stages {

    stage("build"){
      steps {
        echo 'building the app..'
      }
    }

    stage('SonarQube analysis') {
    withSonarQubeEnv(credentialsId: 'jenkins-sonar', installationName: 'sq1') { // You can override the credential to be used
      sh 'mvn org.sonarsource.scanner.maven:sonar-maven-plugin:4.8.0.2856:sonar'
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
