#!groovy

def command = (env.BRANCH_NAME == 'develop') ? 'deploy' : 'verify'

pipeline {

    agent { node { label 'develop' } }

    tools {
        jdk 'jdk-latest11'
    }

    triggers {
        cron('@daily')
    }

    stages {

        stage("Build") {
            steps {
                echo "Kjører bygg"
                sh "mvn -T1C clean install"
            }
        }

        stage("Deploy or Verify") {
             steps {
                 echo "Kjører bygg med kommando: ${command}"
                 sh "mvn -T1C clean ${command} source:jar javadoc:jar"
             }
        }
    }
}