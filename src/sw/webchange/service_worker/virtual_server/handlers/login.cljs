(ns webchange.service-worker.virtual-server.handlers.login
  (:require
    [webchange.service-worker.logger :as logger]
    [webchange.service-worker.virtual-server.handlers.utils.activated-users :as activated-users]
    [webchange.service-worker.wrappers :refer [js-fetch promise-all request-clone body-json then catch data->response]]))

(defn post-offline
  [request]
  (logger/debug "[login] [POST] [offline]")
  (-> (-> request body-json)
      (then #(js->clj % :keywordize-keys true))
      (then #(get % :access-code))
      (then activated-users/store-current-code!)
      (then activated-users/get-activated-user-by-code)
      (then data->response)))

(defn post-online
  [request]
  (logger/debug "[login] [POST] [online]")
  (let [cloned (request-clone request)
        request-body-promise (-> request request-clone body-json)
        response-promise (js-fetch request)]
    (-> (promise-all [request-body-promise response-promise])
        (then (fn [[request-body response]]
                (activated-users/store-activated-user (-> request-body (js->clj :keywordize-keys true) :access-code) response)
                response))
        (catch #(post-offline cloned)))))

(def handlers {"POST" {:online  post-online
                       :offline post-offline}})