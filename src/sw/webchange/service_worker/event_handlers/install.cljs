(ns webchange.service-worker.event-handlers.install
  (:require
    [webchange.service-worker.logger :as logger]
    [webchange.service-worker.wrappers :refer [js-fetch promise-all response-json then catch]]
    [webchange.service-worker.common.cache :refer [cache-app-resources
                                                   cache-game-resources
                                                   cache-endpoints]]
    [webchange.service-worker.common.db :as db]
    [webchange.service-worker.common.fetch :refer [fetch-game-app-resources
                                                   fetch-web-app-resources]]
    [webchange.service-worker.event-handlers.message :as message]))

(defn- load-game-app-resources
  []
  (-> (fetch-game-app-resources)
      (then (fn [resources]
              (promise-all [(-> resources (aget "resources") (cache-game-resources))
                            (-> resources (aget "endpoints") (cache-endpoints))])))))

(defn- load-web-app-resources
  []
  (-> (fetch-web-app-resources)
      (then (fn [resources]
              (promise-all [(-> resources (aget "resources") (cache-app-resources))
                            (-> resources (aget "endpoints") (cache-endpoints))])))))

(defn- set-last-update-date
  []
  (let [date (-> (js/Date.) (.toString))]
    (db/set-value "last-update" date message/send-last-update)))

(defn- install
  []
  (promise-all [(load-game-app-resources)
                (load-web-app-resources)]))

(defn handle
  [event]
  (logger/debug "Install...")
  (.waitUntil event (-> (install)
                        (.then #(do (logger/log "Installation done.")
                                    (.skipWaiting js/self)
                                    (set-last-update-date)))
                        (.catch #(do (logger/warn "Installation failed." (.-message %))
                                     (throw %))))))
