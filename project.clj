(defproject webchange "0.1.0"
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [org.clojure/data.json "2.4.0"] ;;read json files
                 
                 ;;logging
                 [org.clojure/tools.logging "0.4.1"]
                 [ch.qos.logback/logback-classic "1.2.3"]
                 [ch.qos.logback/logback-core "1.2.3"]

                 ;;db
                 [org.clojure/java.jdbc "0.7.8"]
                 [org.postgresql/postgresql "42.2.5"]
                 [conman "0.8.3"] ;; HugSQL + connection pool
                 
                 [clojure.java-time "0.3.2"] ;;date formatting for DB
                 [cheshire "5.10.0"] ;;json formatting for DB
                 [camel-snake-kebab "0.4.1"] ;;map keys formatting for DB (kebab -> snake, snake -> kebab)
                 
                 ;;routing
                 [compojure "1.7.0"]
                 [metosin/spec-tools "0.10.5"]
                 [metosin/compojure-api "2.0.0-alpha31"]
                 [metosin/ring-swagger "0.26.2"] ;;used only for website api

                 ;;http
                 [ring "1.10.0"]
                 [ring/ring-core "1.10.0"]
                 [ring/ring-devel "1.10.0"]
                 [ring/ring-json "0.5.1"]
                 [ring/ring-codec "1.2.0"] ;;url-encode url-decode
                 
                 [yogthos/config "1.2.0"] ;;load config
                 [environ "1.2.0"]
                 [clj-http "3.12.3"] ;;downloading files, website api (deprecated)

                 [luminus-migrations "0.6.3"] ;;migrations

                 [buddy/buddy-auth "2.1.0"] ;;authentication
                 [buddy/buddy-hashers "1.3.0"]

                 [io.djy/ezzmq "0.8.2"] ;;ZeroMQ library
                 [org.clojure/core.async "1.4.627"] ;;async workers with ZeroMQ

                 [mount "0.1.15"]

                 [net.mikera/imagez "0.12.0"] ;;scale uploaded images

                 [phrase "0.3-alpha4"] ;;human readable validation messages
                 
                 [com.cemerick/url "0.1.1"] ;;URL in files - TODO: remove

                 [com.draines/postal "2.0.5"] ;;emails
                 [hiccup "1.0.5"] ;;emails markup
                 
                 [com.google.cloud/google-cloud-speech "4.2.0"] ;; voice recognition
                 
                 [org.apache.poi/poi "3.17"] ;;export transcription as docx
                 [org.apache.poi/poi-ooxml "3.17"]
                 [org.apache.poi/ooxml-schemas "1.3"]]

  :plugins [[lein-environ "1.2.0"] ;;generate and read .lein-env
            [migratus-lein "0.7.0"]]

  :prep-tasks ["javac" "compile"]
  :jvm-opts ["-Xmx2g" "-Djava.library.path=native/vosk/"]
  :java-source-paths ["native/vosk/"]
  :resource-paths ["native/vosk/" "resources/"]

  :min-lein-version "2.5.3"

  :source-paths ["src/clj" "src/cljc"]
  :test-paths ["test/clj" "test/cljc"]

  :clean-targets ^{:protect false} ["resources/public/js/compiled"
                                    "target"
                                    ".shadow-cljs"
                                    "test/js"]

  :migratus {:store         :database
             :migration-dir "migrations"
             :init-script   "init.sql"
             :db            ~(get (System/getenv) "DATABASE_URL")}

  :profiles
  {:dev
   {:dependencies [[binaryage/devtools "0.9.10"] ;;presentation of ClojureScript values in DevTools
                   [ring/ring-mock "0.3.2"] ;;mock ring requests in tests
                   [clj-http-fake "1.0.3"] ;;mock clj-http requests in tests
                   [mockery "0.1.4"] ;;mock 
                   [thheller/shadow-cljs "2.25.2"]
                   [org.clojure/clojurescript "1.11.60"]]
    :main webchange.server-dev
    :repl-options {:init-ns webchange.user
                   :nrepl-middleware
                   [shadow.cljs.devtools.server.nrepl/middleware]}
    :source-paths ["env/dev/clj"]}
   :cljs
   {:source-paths ["src/cljs" "src/cljc" "src/libs"]
    :dependencies [[thheller/shadow-cljs "2.25.2"]
                   [org.clojure/clojurescript "1.11.60"]
                   [reagent "1.2.0"]
                   [re-frame "1.3.0"]
                   [day8.re-frame/http-fx "0.2.4"] ;;requests to backend
                   [cljs-ajax "0.8.4"] ;;request and response formats for cljs-http
                   [cljs-http "0.1.45"] ;;load audio TODO: refactor to cljs-ajax
                   [camel-snake-kebab "0.4.1"] ;;trnasform keys for react components
                   [bidi "2.1.5"] ;; routes
                   [kibu/pushy "0.3.8"] ;;html5 history
                   [phrase "0.3-alpha4"] ;;human readable validation messages
                   [com.taoensso/tempura "1.2.1"] ;;i18n translation for text

                   [cljs-idxdb "0.1.0"] ;;ServiceWorker db, TODO: remove service worker
                   [funcool/promesa "5.0.0"] ;;promises in ServiceWorker, TODO: remove service worker
                   ]}
   :cljs-test
   {:source-paths ["test/cljs" "test/cljc"]
    :dependencies [[org.clojure/test.check "0.9.0"]
                   [day8.re-frame/tracing "0.6.2"]
                   [day8.re-frame/re-frame-10x "1.8.1"]
                   [day8.re-frame/test "0.1.5"]]}
   :prod    {}
   :uberjar {:source-paths       ["env/prod/clj"]
             :auto-clean         false
             :omit-source        true
             :main               webchange.server
             :aot                [webchange.server]
             :uberjar-name       "webchange.jar"
             :jar-exclusions     [#"public/raw/.*" #"public/upload/.*"]
             :uberjar-exclusions [#"public/raw/.*" #"public/upload/.*"]
             :prep-tasks         ["compile"]}})
