(defproject webchange "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [org.clojure/clojurescript "1.10.516"]
                 [org.clojure/java.jdbc "0.7.8"]
                 [org.clojure/tools.logging "0.4.1"]
                 [ch.qos.logback/logback-classic "1.2.3"]
                 [clojure.java-time "0.3.2"]
                 [reagent "0.7.0"]
                 [re-frame "0.10.5"]
                 [compojure "1.5.0"]
                 [yogthos/config "0.8"]
                 [ring "1.4.0"]
                 [ring/ring-json "0.4.0"]
                 [cljsjs/react "16.6.0-0"]
                 [cljsjs/react-dom "16.6.0-0"]
                 [reanimated "0.6.1"]
                 [cljs-http "0.1.45"]
                 [com.degel/sodium "0.12.0"]
                 [day8.re-frame/test "0.1.5"]
                 [day8.re-frame/http-fx "0.1.6"]
                 [environ "1.1.0"]
                 [luminus-migrations "0.6.3"]
                 [org.postgresql/postgresql "42.2.5"]
                 [cljs-ajax "0.8.0"]
                 [buddy/buddy-auth "2.1.0"]
                 [buddy/buddy-hashers "1.3.0"]
                 [cheshire "5.8.1"]
                 [conman "0.8.3"]
                 [mount "0.1.15"]
                 [camel-snake-kebab "0.4.0"]
                 [bidi "2.1.5"]
                 [kibu/pushy "0.3.8"]
                 ]

  :plugins [[lein-cljsbuild "1.1.7"]
            [lein-environ "1.1.0"]
            [migratus-lein "0.7.0"]]

  :jvm-opts ["-Xmx2g"]

  :min-lein-version "2.5.3"

  :source-paths ["src/clj" "src/cljs"]
  :test-paths ["test/clj"]

  :clean-targets ^{:protect false} ["resources/public/js/compiled" "target"
                                    "test/js"]

  :figwheel {:css-dirs ["resources/public/css"]
             :ring-handler webchange.handler/dev-handler
             :server-ip   "192.168.1.43" }

  :migratus {:store :database
             :migration-dir "migrations"
             :init-script "init.sql"
             :db ~(get (System/getenv) "DATABASE_URL")}

  :profiles
  {:dev
   {:dependencies [[binaryage/devtools "0.9.10"]
                   ;[day8.re-frame/re-frame-10x "0.3.3"]
                   [day8.re-frame/tracing "0.5.1"]
                   [ring/ring-mock "0.3.2"]]

    :plugins      [[lein-figwheel "0.5.17"]
                   [lein-doo "0.1.8"]]
    :main         webchange.server}
   :prod { :dependencies [[day8.re-frame/tracing-stubs "0.5.1"]]}
   :uberjar {:source-paths ["env/prod/clj"]
             :dependencies [[day8.re-frame/tracing-stubs "0.5.1"]]
             :omit-source  true
             :main         webchange.server
             :aot          [webchange.server]
             :uberjar-name "webchange.jar"
             :prep-tasks   ["compile" ["cljsbuild" "once" "min"]]}
   }

  :cljsbuild
  {:builds
   [{:id           "dev"
     :source-paths ["src/cljs"]
     :figwheel     {:websocket-host :js-client-host
                    :on-jsload "webchange.core/mount-root"}
     :compiler     {:main                 webchange.core
                    :output-to            "resources/public/js/compiled/app.js"
                    :output-dir           "resources/public/js/compiled/out"
                    :asset-path           "/js/compiled/out"
                    :source-map-timestamp true
                    :preloads             [devtools.preload
                                           ]
                                           ;day8.re-frame-10x.preload]
                    :closure-defines      {"re_frame.trace.trace_enabled_QMARK_" true
                                           "day8.re_frame.tracing.trace_enabled_QMARK_" true}
                    :external-config      {:devtools/config {:features-to-install :all}}
                    :npm-deps
                                          {
                                           :konva "2.5.0"
                                           :react "16.6.0"
                                           :react-dom "16.6.0"
                                           :react-spring "5.8.0"
                                           :react-konva "16.6.0"
                                           }
                    :foreign-libs [{:file "src/libs/spine-canvas.js"
                                    :provides ["spine"]
                                    :module-type :commonjs}]
                    :install-deps true
                    :optimizations   :none
                    }}

    {:id           "min"
     :source-paths ["src/cljs"]
     :jar true
     :compiler     {:main            webchange.core
                    :output-to       "resources/public/js/compiled/app.js"
                    :optimizations   :whitespace
                    :closure-defines {goog.DEBUG false}
                    :pretty-print    false
                    :npm-deps
                                     {
                                      :konva "2.5.0"
                                      :react "16.6.0"
                                      :react-dom "16.6.0"
                                      :react-spring "5.8.0"
                                      :react-konva "16.6.0"
                                      }
                    :foreign-libs [{:file "src/libs/spine-canvas.js"
                                    :provides ["spine"]
                                    :module-type :commonjs}]
                    :install-deps true}}

    {:id           "test"
     :source-paths ["src/cljs" "test/cljs"]
     :compiler     {:main          webchange.runner
                    :output-to     "resources/public/js/compiled/test.js"
                    :output-dir    "resources/public/js/compiled/test/out"
                    :npm-deps
                                   {
                                    :konva "2.5.0"
                                    :react "16.6.0"
                                    :react-dom "16.6.0"
                                    :react-spring "5.8.0"
                                    :react-konva "16.6.0"
                                    }
                    :foreign-libs [{:file "src/libs/spine-canvas.js"
                                    :provides ["spine"]
                                    :module-type :commonjs}]
                    :install-deps true
                    :optimizations :none}}
    ]}
  )
