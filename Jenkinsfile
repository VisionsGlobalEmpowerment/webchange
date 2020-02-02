node {
    checkout scm

    stage('Prepare') {
        sh 'cp -rf $HOME/build-cache/node_modules ./'
        sh 'cp -rf $HOME/build-cache/profiles.clj ./'
    }

    stage('Test') {
        sh 'lein test'
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
            sh '/srv/www/webchange/migrate'
        }

        stage('Restart') {
            sh 'sudo systemctl restart webchange'
        }
    }
}
