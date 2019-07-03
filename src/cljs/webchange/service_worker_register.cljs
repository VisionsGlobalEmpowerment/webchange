(ns webchange.service-worker-register)

(defn register
  [service-worker path]
  (-> (.register service-worker path)
      (.then #(println (str "[ServiceWorker] Registration done:" %)))
      (.catch #(println (str "[ServiceWorker] Registration failed:" %)))))

(defn unregister
  [service-worker]
  (-> (.getRegistrations service-worker)
      (.then (fn [registrations]
               (->> (.shift registrations)
                    (.unregister))))))

(defn setup
  [use-cache path]
  (let [service-worker (.-serviceWorker js/navigator)]
    (when service-worker
      (if use-cache
        (register service-worker path)
        (unregister service-worker)))))
