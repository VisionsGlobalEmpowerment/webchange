node {
    checkout scm

    stage('Prepare') {
        sh 'cp -rf $HOME/build-cache/node_modules ./'
    }

    stage('Build') {
        sh 'lein uberjar'
    }

    stage('Cache results') {
        sh 'cp -rf ./node_modules $HOME/build-cache/'
    }

    if (env.BRANCH_NAME == 'master') {
        stage('Deploy') {
            sh "cp ./target/webchange.jar /srv/www/webchange/releases/${currentBuild.id}-webchange.jar"
            sh "ln -nsf /srv/www/webchange/releases/${currentBuild.id}-webchange.jar /srv/www/webchange/current.jar"
            sh 'sudo systemctl restart webchange'
        }
    }
}
