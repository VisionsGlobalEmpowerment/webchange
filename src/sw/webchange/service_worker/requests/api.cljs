(ns webchange.service-worker.requests.api
  (:require
    [clojure.string :refer [join]]
    [webchange.service-worker.broadcast.core :refer [redirect-to-login]]
    [webchange.service-worker.db.general :as general]
    [webchange.service-worker.logger :as logger]
    [webchange.service-worker.requests.core :as core]
    [webchange.service-worker.wrappers :refer [catch promise promise-reject then]]))

(defn get-url
  ([key]
   (get-url key {}))
  ([key params]
   (-> (general/get-current-course)
       (then (fn [current-course]
               (let [base-url (case key
                                :web-app-resources "/api/resources/web-app"
                                :current-progress (str "/api/courses/" current-course "/current-progress"))]
                 (str base-url (core/get-params-str params)))))
       (catch (fn [error]
                (logger/warn (str "Can not get URL for key " key) error)
                (promise-reject error))))))


(defn get-web-app-resources
  []
  (-> (get-url :web-app-resources)
      (then (fn [url]
              (core/http-get url)))))

(defn get-current-progress
  [params]
  (-> (get-url :current-progress)
      (then (fn [url]
              (core/http-get url params)))
      (catch (fn [error]
               (logger/warn "Get current progress from server failed:" error)
               (redirect-to-login)))))

(defn post-current-progress
  [progress-data]
  (-> (get-url :current-progress)
      (then (fn [url]
              (core/http-post url progress-data)))
      (catch (fn [error]
               (logger/warn "Post current progress to server failed:" error)
               (redirect-to-login)))))
