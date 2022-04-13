(ns webchange.user
  (:require
   [webchange.server :as webchange-server]
   [shadow.cljs.devtools.server :as shadow-server]))

(webchange-server/dev)
(shadow.cljs.devtools.server/start!)
