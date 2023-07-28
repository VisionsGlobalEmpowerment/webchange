(ns webchange.user
  (:require
    [webchange.server :as webchange-server]
    [shadow.cljs.devtools.api :as shadow]
    [shadow.cljs.devtools.server :as server]))

(defn start-server
  []
  (webchange-server/dev))

(defn cljs-repl
  "Connects to a given build-id. Defaults to `:app`."
  ([]
   (cljs-repl :app))
  ([build-id]
   (server/start!)
   (shadow/watch build-id)
   (shadow/nrepl-select build-id)))
