(ns webchange.service-worker.requests.api
  (:require
    [clojure.string :refer [join]]
    [webchange.service-worker.db.general :as general]
    [webchange.service-worker.requests.core :as core]
    [webchange.service-worker.wrappers :refer [promise then]]))

(defn get-url
  ([key]
   (get-url key {}))
  ([key params]
   (-> (general/get-current-course)
       (then (fn [current-course]
               (let [base-url (case key
                                :web-app-resources "/api/resources/web-app"
                                :current-progress (str "/api/courses/" current-course "/current-progress")
                                :game-resources (str "/api/resources/game-app/" current-course "/lessons-resources"))]
                 (str base-url (core/get-params-str params))))))))

(defn get-game-resources
  [lessons]
  (-> (get-url :game-resources {:lessons (join "," lessons)})
      (then (fn [url]
              (core/get url)))))

(defn get-web-app-resources
  []
  (-> (get-url :web-app-resources)
      (then (fn [url]
              (core/get url)))))

(defn post-current-progress
  [progress-data]
  (-> (get-url :current-progress)
      (then (fn [url]
              (core/post url progress-data)))))
