(ns webchange.sw-utils.register
  (:require [re-frame.core :as re-frame]
            [webchange.sw-utils.state.status :as status]
            [webchange.sw-utils.subscribe :refer [subscribe-to-notifications]]))

(defn- register
  [service-worker path]
  (-> (.register service-worker path (clj->js {:scope "/"}))
      (.catch #(println (str "[ServiceWorker] Registration failed:" %)))))

(defn- unregister
  [service-worker]
  (-> (.getRegistrations service-worker)
      (.then (fn [registrations]
               (when (> (count registrations) 0)
                 (->> (.shift registrations)
                      (.unregister)))))))

(defn setup
  [use-cache path]
  (let [service-worker (.-serviceWorker js/navigator)]
    (when service-worker
      (if use-cache
        (do (register service-worker path)
            (subscribe-to-notifications))
        (do (unregister service-worker)
            (re-frame/dispatch [::status/set-sync-status :disabled]))))))
