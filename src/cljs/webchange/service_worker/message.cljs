(ns webchange.service-worker.message
  (:require
    [webchange.logger :as logger]))

(defn- get-worker
  []
  (let [service-worker (.-serviceWorker js/navigator)]
    (if-not (nil? service-worker)
      (-> (.-ready service-worker)
          (.then (fn [sw]
                   (.-active sw))))
      (js/Promise.reject (js/Error. "Service worker is not defined.")))))

(def message-types {:cache-scenes           "update-cached-scenes"
                    :get-cached-resources   "get-cached-resources"
                    :get-last-update        "get-last-update"
                    :cache-start-activities "cache-start-activities"})

(defn- post-message
  [message]
  (-> (get-worker)
      (.then (fn [sw]
               (if-not (nil? sw)
                 (->> message
                      (clj->js)
                      (.postMessage sw))
                 (logger/warn "Can not post message: Service worker is not defined."))))
      (.catch (fn [e] (logger/warn "Can not post message." e)))))

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
