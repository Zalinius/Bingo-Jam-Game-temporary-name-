@Library('zalinius-shared-library') _

pipeline {
    agent any
    tools {
        maven 'maven3'
    }
    stages {
        // Note that the agent automatically checks out the source code from Github	
        stage('Build') { 
            steps {
                clean()
                buildLibrary()
            }}
        stage('Package') {
            environment {
                SOURCE_JDK_WINDOWS = '/var/jenkins_home/downloads/jdk-17-windows'
                SOURCE_JDK_LINUX = '/var/jenkins_home/downloads/jdk-17-linux'
                
                GAME_NAME = sh script: 'mvn help:evaluate -Dexpression=project.name -q -DforceStdout', returnStdout: true 
                GAME_VERSION = sh script: 'mvn help:evaluate -Dexpression=project.version -q -DforceStdout', returnStdout: true 
                GAME_TARGET = sh script: 'mvn help:evaluate -Dexpression=project.build.directory -q -DforceStdout', returnStdout: true 
                GAME_ARTIFACT = sh script: 'mvn help:evaluate -Dexpression=project.artifactId -q -DforceStdout', returnStdout: true 

                JAR = '${GAME_TARGET}/${GAME_ARTIFACT}-${GAME_VERSION}-jar-with-dependencies.jar'

                LAUNCH4J_HOME = tool name: 'launch4j', type: 'com.cloudbees.jenkins.plugins.customtools.CustomTool'
            }
            steps {
                makeJRE(["jdk": SOURCE_JDK_WINDOWS, "jar": JAR, "outputdir": GAME_TARGET+'/windows/jre'])
                packageWindows(["jar": GAME_TARGET +'/' + GAME_ARTIFACT + '-' + GAME_VERSION + '-jar-with-dependencies.jar',
                				"icon": 'res/icon.ico',
                                "exeName":GAME_TARGET+'/windows/'+ GAME_NAME + '-' + GAME_VERSION + '.exe'])
                makeJRE(["jdk": SOURCE_JDK_LINUX, "jar": JAR, "outputdir": GAME_TARGET + '/linux/jre'])
                packageLinux(["jre": '/jre',
                              "jar": JAR,
                              "packingdir": GAME_TARGET+'/linux', 
                              "outputname":GAME_TARGET+'/linux/'+'${GAME_NAME}-${GAME_VERSION}'])
        }}
        stage('Deploy') {
            when { branch 'main'}
            environment {
                SONAR_CREDS = credentials('sonar')

                GAME_NAME = sh script: 'mvn help:evaluate -Dexpression=project.name -q -DforceStdout', returnStdout: true 
                GAME_VERSION = sh script: 'mvn help:evaluate -Dexpression=project.version -q -DforceStdout', returnStdout: true 
                GAME_TARGET = sh script: 'mvn help:evaluate -Dexpression=project.build.directory -q -DforceStdout', returnStdout: true 
                GAME_ARTIFACT = sh script: 'mvn help:evaluate -Dexpression=project.artifactId -q -DforceStdout', returnStdout: true 
            
                JAR = '${GAME_TARGET}/${GAME_ARTIFACT}-${GAME_VERSION}-jar-with-dependencies.jar'
                
                ITCHIO_NAME = sh script: 'mvn help:evaluate -Dexpression=itchIOName -q -DforceStdout', returnStdout: true 
                ITCHIO_AUTHOR = sh script: 'mvn help:evaluate -Dexpression=itchIOAuthor -q -DforceStdout', returnStdout: true 

                BUTLER_HOME = tool name: 'butler', type: 'com.cloudbees.jenkins.plugins.customtools.CustomTool'
                BUTLER_API_KEY = credentials('butler')
            }
            steps {
                sonarScan()
                butlerDeploy(["artifact": '"'+JAR+'"', "tag": "win-linux-mac"])
                butlerDeploy(["artifact": '"'+GAME_TARGET+'/windows/"', "tag": "windows"])
                butlerDeploy(["artifact": '"'+GAME_TARGET+'/linux/"', "tag": "linux"])
    }}}
    post {
        always  { testReport() }    
        success { githubSuccess() }    
        failure { githubFailure() }    
    }
}
