(ns webchange.service-worker.virtual-server.handlers.login
  (:require
    [webchange.service-worker.logger :as logger]
    [webchange.service-worker.db.users :as db-users]
    [webchange.service-worker.db.state :as db-state]
    [webchange.service-worker.virtual-server.handlers.current-progress :as current-progress]
    [webchange.service-worker.wrappers :refer [js-fetch promise-all request-clone body-json then catch data->response require-status-ok!]]))

(defn post-offline
  [request]
  (logger/debug "[login] [POST] [offline]")
  (-> (-> request body-json)
      (then #(js->clj % :keywordize-keys true))
      (then #(get % :access-code))
      (then db-state/set-current-code)
      (then db-users/get-current-user)
      (then data->response)))

(defn post-online
  [request]
  (logger/debug "[login] [POST] [online]")
  (let [cloned (request-clone request)
        request-body-promise (-> request request-clone body-json)
        response-promise (js-fetch request)]
    (-> (promise-all [request-body-promise response-promise])
        (then (fn [[request-body response]]
                (require-status-ok! response)
                (-> (db-users/save-user (-> request-body (js->clj :keywordize-keys true) :access-code) response)
                    (then (current-progress/flush-current-progress)))
                response))
        (catch #(do
                  (logger/warn "[login] [POST] [online] failed with" (-> % .-message))
                  (post-offline cloned))))))

(def handlers {"POST" {:online  post-online
                       :offline post-offline}})