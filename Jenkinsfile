
void setBuildStatus(String message, String state) {
  step([
      $class: "GitHubCommitStatusSetter",
      reposSource: [$class: "ManuallyEnteredRepositorySource", url: env.GIT_URL],
      contextSource: [$class: "ManuallyEnteredCommitContextSource", context: "ci/jenkins/build-status"],
      errorHandlers: [[$class: "ChangingBuildStatusErrorHandler", result: "UNSTABLE"]],
      statusResultSource: [ $class: "ConditionalStatusResultSource", results: [[$class: "AnyBuildResult", message: message, state: state]] ]
  ]);
}


pipeline {
    agent any
    tools {
        maven 'maven3'
	launch4j 'launch4j'
    }
    environment{
        SONAR_CREDS=credentials('sonar')
    }
	
	stages {
		// Note that the agent automatically checks out the source code from Github	
        stage('Build') {
            steps {
                sh 'mvn --batch-mode clean test'
            }
        }
        stage('Package Jar') {
            steps {
                sh 'mvn --batch-mode clean package'
            }
        }
        stage('Deploy') {
        	when {
 				branch 'main'
	       	}
			environment {
				GAME_VERSION = sh script: 'mvn help:evaluate -Dexpression=project.version -q -DforceStdout', returnStdout: true 
				PROJECT_NAME = sh script: 'mvn help:evaluate -Dexpression=project.artifactId -q -DforceStdout', returnStdout: true 
				ITCHIO_NAME = 'bingo-jam-game-temporary-name'
				
				JRE_WIN = '/usr/local/bin/OpenJDK11U-jre_x64_windows_hotspot_11.0.10_9.zip'
			}
			steps {			
                sh 'mvn sonar:sonar -Dsonar.host.url=$SONARQUBE_HOST -Dsonar.login=$SONAR_CREDS' //Send test coverage to Sonarqube, and let it know there is a new version of main to cover
			
				//Make EXE
				sh 'mkdir target/windows'
				sh 'launch4j windows_exe_config.xml'

				//Get JRE
				unzip zipFile: '/usr/local/bin/OpenJDK11U-jre_x64_windows_hotspot_11.0.10_9.zip', dir: 'target/windows/jre/'
				
				sh 'sudo butler push target/windows/ 							zalinius/${ITCHIO_NAME}:windows 	  -i /home/zalinius/.config/itch/butler_creds --userversion $GAME_VERSION --fix-permissions --if-changed'				
				sh 'sudo butler push target/${PROJECT_NAME}-${GAME_VERSION}.jar zalinius/${ITCHIO_NAME}:win-linux-mac -i /home/zalinius/.config/itch/butler_creds --userversion $GAME_VERSION --fix-permissions --if-changed'
	       	}
	    }
	}
	post {
	
	    always {
            junit '**/target/*-reports/TEST-*.xml'
        }
    	
    	success {
       		setBuildStatus('Build succeeded', 'SUCCESS');
    	}
    	failure {
       		setBuildStatus('Build failed', 'FAILURE');
    	}		
	}
}
