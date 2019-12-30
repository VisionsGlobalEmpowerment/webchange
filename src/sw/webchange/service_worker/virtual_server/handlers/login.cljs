(ns webchange.service-worker.virtual-server.handlers.login
  (:require
    [webchange.service-worker.logger :as logger]
    [webchange.service-worker.virtual-server.handlers.utils.db :as db]
    [webchange.service-worker.wrappers :refer [js-fetch promise promise-all promise-resolve response-new response-clone request-clone body-json then]]))

(defn- store-activated-user
  [code response]
  (logger/debug "[store-activated-user]" code)
  (let [body (-> response response-clone body-json)]
    (-> body
        (then #(js->clj % :keywordize-keys true))
        (then #(assoc % :code code))
        (then #(db/add-item db/activated-users-store-name % identity)))))

(defn- get-activated-user-by-code
  [code]
  (promise #(db/get-by-key db/activated-users-store-name code %)))

(defn- data->response
  [data]
  (let [headers {"Content-Type" "application/json"}]
    (response-new (clj->js data) (clj->js headers))))

(defn post-online
  [request]
  (logger/debug "[login] [POST] [online]")
  (let [request-body-promise (-> request request-clone body-json)
        response-promise (js-fetch request)]
    (-> (promise-all [request-body-promise response-promise])
        (then (fn [[request-body response]]
                (logger/debug "[login] k" (js->clj request-body :keywordize-keys true))
                (logger/debug "[login] no opt" (js->clj request-body))
                (store-activated-user (-> request-body (js->clj :keywordize-keys true) :access-code) response)
                response)))))

(defn post-offline
  [request]
  (logger/debug "[login] [POST] [offline]")
  (-> (-> request body-json)
      (then :access-code)
      (then get-activated-user-by-code)
      (then data->response)))

(def handlers {"POST" {:online  post-online
                       :offline post-offline}})