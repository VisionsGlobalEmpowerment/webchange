(ns webchange.service-worker.virtual-server.handlers.current-progress
  (:require
    [promesa.core :as p]
    [webchange.service-worker.db.progress :as db-progress]
    [webchange.service-worker.db.events :as db-events]
    [webchange.service-worker.db.users :as db-users]
    [webchange.service-worker.virtual-server.logger :as logger]
    [webchange.service-worker.requests.api :as api]
    [webchange.service-worker.wrappers :refer [request-clone js-fetch promise-all promise-resolve promise-reject body-json request-clone then catch data->response require-status-ok!]]))

(defn store-current-progress!
  [{progress :progress events :events offline :offline}]
  (-> (db-users/get-current-user)
      (then (fn [user]
              (if-not (nil? user)
                (do (db-progress/save-progress progress user offline)
                    (when offline
                      (doseq [event events] (db-events/save-event event user))))
                (logger/warn "Progress can not be stored: user is not defined."))))))

(defn get-current-progress
  []
  (logger/debug "[current-progress] get-current-progress")
  (-> (db-users/get-current-user)
      (then (fn [current-user]
              (logger/debug "[current-progress] current-user" current-user)
              (if-not (nil? current-user)
                (db-progress/get-progress current-user)
                (do (logger/warn "Can not get current progress: current user is not defined")
                    (promise-resolve nil)))))))

(defn store-body!
  [body offline]
  (let [cloned (.clone body)]
    (-> (body-json cloned)
        (then (fn [body-cloned]
                (-> body-cloned
                    (js->clj :keywordize-keys true)
                    (assoc :offline offline)
                    store-current-progress!))))
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
             (-> (api/get-current-progress)
                 (then require-status-ok!)
                 (then #(store-body! % false))
                 (catch #(get-offline cloned)))))))

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
  (-> (db-users/get-current-user)
      (then (fn [current-user]
              (if-not (nil? current-user)
                (promise-all [(db-progress/get-progress current-user)
                              (db-events/get-events current-user)])
                (promise-reject "Cant not flush progress data. User is undefined."))))
      (then (fn [[progress-data events-data]]
              (let [progress (merge progress-data {:events (map :data events-data)})]
                (logger/debug "Flush progress" (clj->js progress))
                (-> (api/post-current-progress progress)
                    (then (fn []
                            (doseq [event events-data]
                              (logger/debug "Remove stored event" (clj->js event))
                              (db-events/remove-by-date (get-in event [:data :created-at])))))))))))

(def handlers {"GET"  {:online  get-online
                       :offline get-offline}
               "POST" {:online  post-online
                       :offline post-offline}
               :flush flush})
