(defproject webchange "0.1.0-SNAPSHOT"
  :resource-paths ["native/vosk" "resources/"]
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [org.clojure/clojurescript "1.10.520"]
                 [org.clojure/java.jdbc "0.7.8"]
                 [org.clojure/test.check "0.9.0"]
                 [org.clojure/tools.logging "0.4.1"]
                 [ch.qos.logback/logback-classic "1.2.3"]
                 [clojure.java-time "0.3.2"]
                 [reagent "0.7.0"]
                 [re-frame "0.10.5"]
                 [compojure "1.6.1"]
                 [metosin/spec-tools "0.10.1"]
                 [metosin/compojure-api "2.0.0-alpha31"]
                 [yogthos/config "0.8"]
                 [ring "1.8.0"]
                 [ring/ring-core "1.8.0"]
                 [ring/ring-json "0.5.0"]
                 [cljsjs/react "16.6.0-0"]
                 [cljsjs/react-dom "16.6.0-0"]
                 [org.clojars.melodylane/cljs-react-material-ui "1.4.0-0.1" :exclusions [cljsjs/material-ui]]
                 [cljsjs/material-ui "3.9.3-0"]
                 [reanimated "0.6.1"]
                 [cljs-http "0.1.45"]
                 [clj-http "3.10.1"]
                 [tupelo "0.9.185"]
                 [day8.re-frame/test "0.1.5"]
                 [day8.re-frame/http-fx "0.1.6"]
                 [environ "1.1.0"]
                 [luminus-migrations "0.6.3"]
                 [org.postgresql/postgresql "42.2.5"]
                 [cljs-ajax "0.8.0"]
                 [buddy/buddy-auth "2.1.0"]
                 [buddy/buddy-hashers "1.3.0"]
                 [cheshire "5.10.0"]
                 [conman "0.8.3"]
                 [mount "0.1.15"]
                 [camel-snake-kebab "0.4.1"]
                 [bidi "2.1.5"]
                 [kibu/pushy "0.3.8"]
                 [net.mikera/imagez "0.12.0"]
                 [cljsjs/enzyme "3.8.0"]
                 [cljs-idxdb "0.1.0"]
                 [phrase "0.3-alpha4"]
                 [funcool/promesa "5.0.0"]
                 [clj-http "3.10.1"]
                 [clj-http-fake "1.0.3"]
                 [ring/ring-codec "1.1.2"]
                 [com.cemerick/url "0.1.1"]
                 [org.clojure/data.csv "1.0.0"]
                 [me.raynes/fs "1.4.6"]
                 [io.djy/ezzmq "0.8.2"]
                 [com.taoensso/tempura "1.2.1"]]

  ;:node-dependencies [[source-map-support "0.2.8"]]

  :plugins [[lein-cljsbuild "1.1.7"]
            [lein-cooper "1.2.2"]
            [lein-environ "1.1.0"]
            [lein-sass "0.5.0"]
            [migratus-lein "0.7.0"]]

  :prep-tasks ["javac" "compile"]
  :jvm-opts ["-Xmx2g" "-Djava.library.path=native/vosk/"]
  :java-source-paths ["native/vosk/"]

  :min-lein-version "2.5.3"

  :source-paths ["src/clj" "src/cljc" "src/cljs"]
  :test-paths ["test/clj" "test/cljc"]

  :clean-targets ^{:protect false} ["resources/public/js/compiled" "target"
                                    "test/js"]

  :figwheel {:css-dirs       ["resources/public/css"]
             :ring-handler   webchange.handler/dev-handler
             :server-ip      "0.0.0.0"
             :server-logfile "log/figwheel.log"}


  :migratus {:store         :database
             :migration-dir "migrations"
             :init-script   "init.sql"
             :db            ~(get (System/getenv) "DATABASE_URL")}

  :sass {:src              "src/cljs/webchange/ui_framework/styles/index"
         :output-directory "resources/public/css"
         :command          :sass
         :source-maps      false}

  :cooper {"styles" ["lein" "sass" "auto"]
           "cljs"   ["lein" "cljsbuild" "auto" "dev"]
           "clj"    ["lein" "run"]}

  :aliases {"dev"       ["cooper"]
            "resources" ["run" "-m" "webchange.resources.generate-resources-list"]}

  :profiles
  {:dev
            {:dependencies [[binaryage/devtools "0.9.10"]
                            [day8.re-frame/re-frame-10x "0.3.3"]
                            [day8.re-frame/tracing "0.5.1"]
                            [ring/ring-mock "0.3.2"]
                            [mockery "0.1.4"]
                            [figwheel-sidecar "0.5.20"]]
             :plugins      [[lein-doo "0.1.8"]]
             :main         webchange.server-dev
             :repl-options {:init-ns webchange.server
                            :init    (dev)}
             :source-paths ["env/dev/clj" "env/dev/cljs"]}
   :prod    {:dependencies [[day8.re-frame/tracing-stubs "0.5.1"]]}
   :uberjar {:source-paths       ["env/prod/clj"]
             :dependencies       [[day8.re-frame/tracing-stubs "0.5.1"]]
             :omit-source        false
             :main               webchange.server
             :aot                [webchange.server]
             :uberjar-name       "webchange.jar"
             :jar-exclusions     [#"public/raw/.*" #"public/upload/.*"]
             :uberjar-exclusions [#"public/raw/.*" #"public/upload/.*"]
             :prep-tasks         ["compile" ["sass" "once"]
                                  "compile" ["cljsbuild" "once" "sw"]
                                  "compile" ["cljsbuild" "once" "min"]]}}

  :cljsbuild
  {:builds
   [{:id           "dev"
     :source-paths ["src/cljs" "src/cljc"]
     :figwheel     {:websocket-host :js-client-host
                    :on-jsload      "webchange.core/mount-root"}
     :compiler     {:main                 webchange.core
                    :output-to            "resources/public/js/compiled/app.js"
                    :output-dir           "resources/public/js/compiled/out"
                    :asset-path           "/js/compiled/out"
                    :source-map-timestamp true
                    :preloads             [devtools.preload
                                           day8.re-frame-10x.preload]
                    :closure-defines      {"re_frame.trace.trace_enabled_QMARK_"        true
                                           "day8.re_frame.tracing.trace_enabled_QMARK_" true}
                    :external-config      {:devtools/config {:features-to-install :all}}
                    :npm-deps             {:gsap                    "2.1.2"
                                           :wavesurfer.js           "5.1.0"
                                           :svg-arc-to-cubic-bezier "3.2.0"}

                    :foreign-libs         [{:file        "src/libs/pixi-build.js"
                                            :provides    ["pixi"]
                                            :module-type :commonjs}
                                           {:file        "src/libs/audio-script.js"
                                            :provides    ["audio-script"]
                                            :module-type :commonjs}]
                    :install-deps         true
                    :optimizations        :none
                    :language-in          :ecmascript6}}

    {:id           "sw"
     :source-paths ["src/sw"]
     :compiler     {:main          webchange.service-worker
                    :output-to     "resources/public/js/compiled/service-worker.js"
                    :output-dir    "resources/public/js/compiled/out-sw"
                    :optimizations :advanced
                    :pretty-print  false}}
    {:id           "sw-dev"
     :source-paths ["src/sw"]
     :compiler     {:main            webchange.service-worker
                    :output-to       "resources/public/js/compiled/service-worker.js"
                    :output-dir      "resources/public/js/compiled/out-sw-dev"
                    :optimizations   :simple
                    :source-map      "resources/public/js/compiled/service-worker.js.map"
                    :source-map-path "/js/compiled/out-sw-dev/"
                    :pretty-print    true}}
    {:id           "min"
     :source-paths ["src/cljs"]
     :jar          true
     :compiler     {:main            webchange.core
                    :output-to       "resources/public/js/compiled/app.js"
                    :optimizations   :simple
                    :closure-defines {goog.DEBUG false}
                    :pretty-print    false
                    :npm-deps        {:gsap                    "2.1.2"
                                      :wavesurfer.js           "5.1.0"
                                      :svg-arc-to-cubic-bezier "3.2.0"}

                    :foreign-libs    [{:file        "src/libs/pixi-build.js"
                                       :provides    ["pixi"]
                                       :module-type :commonjs}
                                      {:file        "src/libs/audio-script.js"
                                       :provides    ["audio-script"]
                                       :module-type :commonjs}]
                    :install-deps    true
                    :language-in     :ecmascript6}}

    {:id           "test"
     :source-paths ["src/cljs" "src/cljc" "test/cljs" "test/cljc"]
     :compiler     {:main          webchange.runner
                    :output-to     "resources/public/js/compiled/test.js"
                    :output-dir    "resources/public/js/compiled/test/out"
                    :npm-deps      {:gsap                    "2.1.2"
                                    :wavesurfer.js           "5.1.0"
                                    :svg-arc-to-cubic-bezier "3.2.0"}

                    :foreign-libs  [{:file        "src/libs/pixi-build.js"
                                     :provides    ["pixi"]
                                     :module-type :commonjs}
                                    {:file        "src/libs/audio-script.js"
                                     :provides    ["audio-script"]
                                     :module-type :commonjs}]
                    :install-deps  true
                    :optimizations :none}}]})
