@Library('zalinius-shared-library') _

pipeline {
    agent any
    tools {
        maven 'maven3'
    }
    stages {
   	    // Note that the agent automatically checks out the source code from Github	
        stage('Build') { steps { buildAndTest()}}
        stage('Deploy') {
            when { branch 'main'}
            environment {
                SONAR_CREDS = credentials('sonar')

                BUTLER_API_KEY=credentials('butler')
                ITCHIO_NAME = 'bingo-jam-game-temporary-name'
            }
            steps {
                sonarScan()
                deployGame()
    }}}
    post {
        always  { testReport() }    
        success { githubSuccess() }    
        failure { githubFailure() }    
    }
}
