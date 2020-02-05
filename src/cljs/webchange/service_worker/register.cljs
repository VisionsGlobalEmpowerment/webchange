(ns webchange.service-worker.register
  (:require [re-frame.core :as re-frame]
            [webchange.service-worker.events :as events]
            [webchange.service-worker.subscribe :refer [subscribe-to-notifications]]))

(defn check-registration
  [registration]
  (when (.-active registration)
    (re-frame/dispatch [::events/set-offline-mode :ready]))
  (when (.-installing registration)
    (re-frame/dispatch [::events/set-offline-mode :in-progress]))
  (when (not (or (.-active registration) (.-installing registration)))
    (re-frame/dispatch [::events/set-offline-mode :not-started])))

(defn check-state
  [registration]
  (check-registration registration)
  (when-let [installing (.-installing registration)]
    (.addEventListener installing "statechange" #(check-registration registration))))

(defn register
  [service-worker path]
  (-> (.register service-worker path)
      (.then check-state)
      (.catch #(println (str "[ServiceWorker] Registration failed:" %)))))

(defn unregister
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
        (unregister service-worker)))))
