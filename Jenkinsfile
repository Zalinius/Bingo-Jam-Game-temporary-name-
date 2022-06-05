@Library('zalinius-shared-library') _

pipeline {
    agent any
    tools {
        maven 'maven3'
    }
    environment{
        SONAR_CREDS=credentials('sonar')
        BUTLER_API_KEY=credentials('butler')
    }
    stages {
   	    // Note that the agent automatically checks out the source code from Github	
        stage('Build') { steps { buildAndTest()}}
        stage('Deploy') {
            when { branch 'main'}
            environment {
                GAME_VERSION = sh script: 'mvn help:evaluate -Dexpression=project.version -q -DforceStdout', returnStdout: true 
                PROJECT_NAME = sh script: 'mvn help:evaluate -Dexpression=project.artifactId -q -DforceStdout', returnStdout: true 
                ITCHIO_NAME = 'bingo-jam-game-temporary-name'
                
                LAUNCH4J_HOME = tool name: 'launch4j', type: 'com.cloudbees.jenkins.plugins.customtools.CustomTool'
                BUTLER_HOME = tool name: 'butler', type: 'com.cloudbees.jenkins.plugins.customtools.CustomTool'
                JRE_WIN = '/var/jenkins_home/downloads/jre17.zip'
            }
            steps {
                sonarScan(sonarcubeHost: "${SONARQUBE_HOST}", sonarcubeCredentials: credentials('sonar'))
                //Make EXE
                sh 'mkdir target/windows'
                sh '${LAUNCH4J_HOME}/launch4j windows_exe_config.xml'

                //Get JRE
                unzip zipFile: "${JRE_WIN}", dir: 'target/windows/jre/'
                
                sh '${BUTLER_HOME} push target/windows/ zalinius/${ITCHIO_NAME}:windows --userversion $GAME_VERSION --fix-permissions --if-changed'				
                sh '${BUTLER_HOME} push target/${PROJECT_NAME}-${GAME_VERSION}.jar zalinius/${ITCHIO_NAME}:win-linux-mac --userversion $GAME_VERSION --fix-permissions --if-changed'
    }}}
    post {
        always  { testReport() }    
        success { githubSuccess() }    
        failure { githubFailure() }    
    }
}
