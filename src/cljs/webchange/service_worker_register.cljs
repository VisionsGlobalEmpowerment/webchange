(ns webchange.service-worker-register)

(defn register
  [service-worker path]
  (js/console.log "register?")
  (-> (.register service-worker path)
      (.then #(println (str "[ServiceWorker] Registration done:" %)))
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
