(defproject webchange "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.10.439"]
                 [reagent "0.7.0"]
                 [re-frame "0.10.5"]
                 [compojure "1.5.0"]
                 [yogthos/config "0.8"]
                 [ring "1.4.0"]
                 [ring/ring-json "0.4.0"]
                 [cljsjs/react "16.6.0-0"]
                 [cljsjs/react-dom "16.6.0-0"]
                 [reanimated "0.6.1"]
                 [cljs-http "0.1.45"]]

  :plugins [[lein-cljsbuild "1.1.7"]]

  :min-lein-version "2.5.3"

  :source-paths ["src/clj" "src/cljs"]

  :clean-targets ^{:protect false} ["resources/public/js/compiled" "target"
                                    "test/js"]

  :figwheel {:css-dirs ["resources/public/css"]
             :ring-handler webchange.handler/dev-handler}

  :profiles
  {:dev
   {:dependencies [[binaryage/devtools "0.9.10"]
                   [day8.re-frame/re-frame-10x "0.3.3"]
                   [day8.re-frame/tracing "0.5.1"]]

    :plugins      [[lein-figwheel "0.5.17"]
                   [lein-doo "0.1.8"]]}
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
     :figwheel     {:on-jsload "webchange.core/mount-root"}
     :compiler     {:main                 webchange.core
                    :output-to            "resources/public/js/compiled/app.js"
                    :output-dir           "resources/public/js/compiled/out"
                    :asset-path           "js/compiled/out"
                    :source-map-timestamp true
                    :preloads             [devtools.preload
                                           day8.re-frame-10x.preload]
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
                    :install-deps true
                    :optimizations   :none
                    }}

    {:id           "min"
     :source-paths ["src/cljs"]
     :jar true
     :compiler     {:main            webchange.core
                    :output-to       "resources/public/js/compiled/app.js"
                    :optimizations   :advanced
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
                    :install-deps true
                    :optimizations :none}}
    ]}
  )
