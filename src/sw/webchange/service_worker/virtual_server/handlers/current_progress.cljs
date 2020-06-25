(ns webchange.service-worker.virtual-server.handlers.current-progress
  (:require
    [promesa.core :as p]
    [webchange.service-worker.logger :as logger]
    [webchange.service-worker.common.cache-common :refer [get-current-course]]
    [webchange.service-worker.common.db :as db]
    [webchange.service-worker.common.fetch :refer [post-data]]
    [webchange.service-worker.virtual-server.handlers.utils.activated-users :as activated-users]
    [webchange.service-worker.wrappers :refer [request-clone js-fetch promise-all promise-resolve promise-reject body-json request-clone then catch data->response require-status-ok!]]))

(defn get-url
  [current-course]
  (str "/api/courses/" current-course "/current-progress"))

(defn store-current-progress!
  [{progress :progress events :events offline :offline}]
  (-> (activated-users/get-current-user)
      (then (fn [{:keys [id] :as user}]
              (if-not (nil? user)
                (do (db/add-item db/progress-store-name {:progress progress
                                                         :offline  offline
                                                         :id       id})
                    (when offline
                      (doseq [event events]
                        (db/add-item db/events-store-name {:data    event
                                                           :user    id
                                                           :created (:created-at event)}))))
                (logger/warn "Progress can not be stored: user is not defined."))))))

(defn- with-debug
  [x comment]
  (logger/debug "[with-debug]" "[" comment "]" x)
  x)

(defn get-current-progress
  []
  (-> (activated-users/get-current-user)
      (then (fn [current-user]
              (if-not (nil? current-user)
                (db/get-by-key db/progress-store-name (:id current-user))
                (do (logger/warn "Can not get current progress: current user is not defined")
                    (promise-resolve nil)))))))

(defn store-body!
  [body offline]
  (let [cloned (.clone body)]
    (-> (body-json cloned)
        (then #(js->clj % :keywordize-keys true))
        (then #(assoc % :offline offline))
        (then store-current-progress!))
    body))

(defn get-offline
  [request]
  (logger/debug "[current-progress] [GET] [offline]")
  (-> (get-current-progress)
      (then data->response)))

(defn post-offline
  [request]
  (logger/debug "[current-progress] [POST] [offline]")
  (store-body! request true)
  (data->response {}))

(defn get-online
  [request]
  (logger/debug "[current-progress] [GET] [online]")
  (let [cloned (request-clone request)]
    (p/let [stored-progress (get-current-progress)
            offline (:offline stored-progress)]
           (if offline
             (get-offline request)
             (-> (js-fetch request)
                 (then require-status-ok!)
                 (then #(store-body! % false))
                 (catch #(get-offline cloned)))
             ))))

(defn post-online
  [request]
  (logger/debug "[current-progress] [POST] [online]")
  (let [cloned (request-clone request)]
    (store-body! request false)
    (-> (js-fetch request)
        (then require-status-ok!)
        (catch #(post-offline cloned)))))

(defn flush
  []
  (-> (activated-users/get-current-user)
      (then (fn [current-user]
              (if-not (nil? current-user)
                (promise-all [(db/get-by-key db/progress-store-name (:id current-user))
                              (db/find-in-index db/events-store-name "user" {:user (:id current-user)})
                              (get-current-course)])
                (promise-reject "Cant not flush progress data. User is undefined."))))
      (then (fn [[progress-data events-data current-course]]
              (let [progress (merge progress-data {:events (map :data events-data)})]
                (logger/debug "Flush progress" current-course (clj->js progress))
                (-> (post-data (get-url current-course) progress)
                    (then (fn []
                            (doseq [event events-data]
                              (logger/debug "Remove stored event" (clj->js event))
                              (db/remove-item db/events-store-name (get-in event [:data :created-at])))))))))))

(def handlers {"GET"  {:online  get-online
                       :offline get-offline}
               "POST" {:online  post-online
                       :offline post-offline}
               :flush flush})
