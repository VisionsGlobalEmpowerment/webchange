(ns webchange.sw-utils.message
  (:require
    [webchange.sw-utils.config :as config]
    [webchange.logger :as logger]))

(defn- get-worker
  []
  (let [service-worker (.-serviceWorker js/navigator)]
    (if-not (nil? service-worker)
      (-> (.-ready service-worker)
          (.then (fn [sw]
                   (.-active sw))))
      (js/Promise.reject (js/Error. "Service worker is not defined.")))))

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

(defn get-current-state
  []
  (post-message {:type (:get-current-state config/messages)}))

(defn set-cached-data
  [data]
  (post-message {:type (:update-cached-scenes config/messages)
                 :data data}))

(defn cache-course
  [course-id]
  (post-message {:type (:cache-course config/messages)
                 :data {:course course-id}}))
