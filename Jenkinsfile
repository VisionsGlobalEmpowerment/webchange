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
        sh 'npm install'
	sh 'shadow-cljs compile ci'
        sh 'karma start --single-run'
    }

    stage('Cache results') {
        sh 'cp -rf ./node_modules $HOME/build-cache/'
    }

    if (env.BRANCH_NAME == 'master') {
        stage('Build') {
            sh 'lein clean'
	    sh "shadow-cljs release app --config-merge '{:release-version \"${currentBuild.id}\"}'"
	    sh 'cp -f ./resources/public/index.html.source ./resources/public/index.html'
	    sh "sed -i 's/\\/js\\/compiled\\/app.js/\\/js\\/compiled\\/app.${currentBuild.id}.js/' ./resources/public/index.html"
	    sh 'sass ./src/cljs/webchange/ui_framework/styles/index/:./resources/public/css/'
            sh 'lein uberjar'
    	}
    
        stage('Deploy') {
	    sh "scp ./target/webchange.jar deploy@stage-env:/srv/www/webchange/releases/${currentBuild.id}-webchange.jar"
	    sh "ssh deploy@stage-env 'ln -nsf /srv/www/webchange/releases/${currentBuild.id}-webchange.jar /srv/www/webchange/current.jar'"
            sh "ssh deploy@stage-env 'sudo systemctl stop webchange.service'"
        }

        stage('Migrate') {
	    sh "ssh deploy@stage-env 'java -Dconfig=/srv/www/webchange/config.edn -jar /srv/www/webchange/current.jar migrate'"
        }

        stage('Update Templates') {
	    sh "ssh deploy@stage-env 'java -Dconfig=/srv/www/webchange/config.edn -jar /srv/www/webchange/current.jar update-templates'"
        }

        stage('Restart') {
       	    sh "ssh deploy@stage-env 'sudo systemctl start webchange.service'"
        }
    }
}
