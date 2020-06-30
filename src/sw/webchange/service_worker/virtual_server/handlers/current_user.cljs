(ns webchange.service-worker.virtual-server.handlers.current-user
  (:require
    [webchange.service-worker.logger :as logger]
    [webchange.service-worker.db.users :as db-users]
    [webchange.service-worker.wrappers :refer [js-fetch request-clone then catch data->response require-status-ok!]]))

(defn get-offline
  [request]
  (logger/debug "[current-user] [GET] [offline]")
  (-> (db-users/get-current-user)
      (then data->response)
      (catch #(data->response nil))))

(defn get-online
  [request]
  (logger/debug "[current-user] [GET] [online]")
  (let [cloned (request-clone request)
        response-promise (js-fetch request)]
    (-> response-promise
        (then require-status-ok!)
        (catch #(get-offline cloned)))))

(def handlers {"GET" {:online  get-online
                      :offline get-offline}})
