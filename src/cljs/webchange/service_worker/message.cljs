(ns webchange.service-worker.message)

(defn- get-worker
  []
  (-> (.. js/navigator -serviceWorker -ready)
      (.then #(.-active %))))

(def message-types {:cache-scenes           "update-cached-scenes"
                    :get-cached-resources   "get-cached-resources"
                    :get-last-update        "get-last-update"
                    :cache-start-activities "cache-start-activities"})

(defn- post-message
  [message]
  (-> (get-worker)
      (.then #(->> message
                   (clj->js)
                   (.postMessage %)))))

(defn get-cached-resources
  []
  (post-message {:type (:get-cached-resources message-types)}))

(defn get-last-update
  []
  (post-message {:type (:get-last-update message-types)}))

(defn set-cached-scenes
  [data]
  (post-message {:type (:cache-scenes message-types)
                 :data data}))

(defn cache-start-activities
  [course-id]
  (post-message {:type (:cache-start-activities message-types)
                 :data {:course course-id}}))
