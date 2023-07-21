pipeline {
  agent any

  tools {
      mvn '3.9.3'
    }

  stages {

    stage("build"){
      steps {
        echo 'building the app..'
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
