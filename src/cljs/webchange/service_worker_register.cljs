(ns webchange.service-worker-register)

(defn register
  [path]
  (let [service-worker (.-serviceWorker js/navigator)]
    (when service-worker
      (-> (.register service-worker path)
          (.then #(println (str "[ServiceWorker] Registration done:" %)))
          (.catch #(println (str "[ServiceWorker] Registration failed:" %)))))))
