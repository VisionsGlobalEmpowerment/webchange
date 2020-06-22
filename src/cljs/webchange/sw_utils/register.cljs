(ns webchange.sw-utils.register
  (:require [re-frame.core :as re-frame]
            [webchange.sw-utils.state.status :as status]
            [webchange.sw-utils.subscribe :refer [subscribe-to-notifications]]))

(defn- worker-ready
  [_]
  (re-frame/dispatch [::status/set-sync-status :installed]))

(defn- track-installing
  [worker]
  (re-frame/dispatch [::status/set-sync-status :installing])
  (.addEventListener worker "statechange" (fn []
                                            (when (= "activated" (.-state worker))
                                              (worker-ready worker)))))

(defn- check-state
  [registration]
  (if-let [active (.-active registration)]
    (worker-ready active)
    (if-let [installing (.-installing registration)]
      (track-installing installing)
      (.addEventListener registration "updatefound" #(track-installing (.-installing registration))))))

(defn- register
  [service-worker path]
  (-> (.register service-worker path (clj->js {:scope "/"}))
      (.then check-state)
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
