(ns webchange.events)

(def handlers (atom {}))

(defn dispatch
  [{type :type :as data}]
  (let [event-handlers (get @handlers type)]
    (doseq [handler (vals event-handlers)]
      (handler data))))

(defn reg
  [id type handler]
  (swap! handlers update-in [type] assoc id handler))