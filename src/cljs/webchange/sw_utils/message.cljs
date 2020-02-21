(ns webchange.sw-utils.message
  (:require
    [webchange.sw-utils.config :as config]))

(defn- get-worker
  []
  (-> (.. js/navigator -serviceWorker -ready)
      (.then #(.-active %))))

(defn- post-message
  [message]
  (-> (get-worker)
      (.then #(->> message
                   (clj->js)
                   (.postMessage %)))))

(defn get-cached-resources
  [course-id]
  (post-message {:type (:get-cached-resources config/messages)
                 :data {:course course-id}}))

(defn get-last-update
  []
  (post-message {:type (:get-last-update config/messages)}))

(defn set-cached-scenes
  [data]
  (post-message {:type (:cache-scenes config/messages)
                 :data data}))

(defn cache-course
  [course-id]
  (post-message {:type (:cache-course config/messages)
                 :data {:course course-id}}))
