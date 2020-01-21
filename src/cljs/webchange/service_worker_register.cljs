(ns webchange.service-worker-register
  (:require [re-frame.core :as re-frame]
            [webchange.events :as events]))

(defn check-registration
  [registration]
  (if (.-active registration)
    (re-frame/dispatch [::events/set-offline-mode :ready])
    (re-frame/dispatch [::events/set-offline-mode :in-progress])))

(defn check-state
  [registration]
  (println (str "[ServiceWorker] Registration done:" registration))
  (check-registration registration)
  (when-let [installing (.-installing registration)]
    (.addEventListener installing "statechange" #(check-registration registration))))

(defn register
  [service-worker path]
  (js/console.log "register?")
  (-> (.register service-worker path)
      (.then check-state)
      (.catch #(println (str "[ServiceWorker] Registration failed:" %)))))

(defn unregister
  [service-worker]
  (js/console.log "unregister?")
  (-> (.getRegistrations service-worker)
      (.then (fn [registrations]
               (when (> (count registrations) 0)
                 (->> (.shift registrations)
                      (.unregister)))))))

(defn setup
  [use-cache path]
  (let [service-worker (.-serviceWorker js/navigator)]
    (js/console.log "service worker?" service-worker)
    (when service-worker
      (if use-cache
        (register service-worker path)
        (unregister service-worker)))))
