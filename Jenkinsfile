pipeline {
  agent any

  tools {
      maven '3.9.3'
    }

  stages {

    stage("build"){
      steps {
        echo 'building the app..'
      }
    }

    stage('SonarQube analysis') {
      steps {
        sh 'mvn --version'
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
