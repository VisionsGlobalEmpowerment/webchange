(ns webchange.service-worker.virtual-server.handlers.current-progress
  (:require
    [promesa.core :as p]
    [webchange.service-worker.logger :as logger]
    [webchange.service-worker.virtual-server.handlers.utils.responses-store :as store]
    [webchange.service-worker.virtual-server.handlers.utils.requests_queue :as queue]
    [webchange.service-worker.common.db :as db]
    [webchange.service-worker.virtual-server.handlers.utils.activated-users :as activated-users]
    [webchange.service-worker.wrappers :refer [js-fetch promise-resolve body-json response-clone request-clone then catch data->response require-status-ok!]]))

(defn store-current-progress!
  [{progress :progress offline :offline}]
  (-> (activated-users/get-current-user)
      (then (fn [{:keys [id] :as user}]
              (if-not (nil? user)
                (db/add-item db/progress-store-name {:progress progress
                                                     :offline  offline
                                                     :id       id})
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

(def handlers {"GET"  {:online  get-online
                       :offline get-offline}
               "POST" {:online  post-online
                       :offline post-offline}})
