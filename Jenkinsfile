node {
    checkout scm

    stage('Prepare') {
        sh 'cp -rf $HOME/build-cache/node_modules ./'
        sh 'cp -rf $HOME/build-cache/profiles.clj ./'
    }

    stage('Migrate test') {
        sh 'lein with-profile test migratus migrate'
    }

    stage('Test clj') {
        sh 'lein test'
    }

    stage('Test cljs') {
        sh 'lein doo chrome-headless test once'
    }

    stage('Build') {
        sh 'lein clean'
        sh 'lein uberjar'
    }

    stage('Cache results') {
        sh 'cp -rf ./node_modules $HOME/build-cache/'
    }

    if (env.BRANCH_NAME == 'master') {
        stage('Deploy') {
            sh "cp ./target/webchange.jar /srv/www/webchange/releases/${currentBuild.id}-webchange.jar"
            sh "ln -nsf /srv/www/webchange/releases/${currentBuild.id}-webchange.jar /srv/www/webchange/current.jar"
        }

        stage('Migrate') {
	    environment {
                config='/srv/www/webchange/config.edn'
            }
	    steps {
                sh 'java -jar /srv/www/webchange/current.jar migrate'
	    }
        }

        stage('Sync') {
	    environment {
                config='/srv/www/webchange/config.edn'
            }
	    steps {
                sh 'java -jar /srv/www/webchange/current.jar download-course-data 1'
            }
        }

        stage('Restart') {
            sh 'sudo systemctl restart webchange'
        }
    }
}
