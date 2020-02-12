(ns webchange.service-worker.virtual-server.handlers.utils.activated-users
  (:require
    [webchange.service-worker.logger :as logger]
    [webchange.service-worker.common.db :as db]
    [webchange.service-worker.wrappers :refer [js-fetch promise promise-all promise-resolve response-new response-clone request-clone body-json then catch]]))

(defn store-current-code!
  [code]
  (db/set-value "current-code" code identity)
  code)

(defn get-current-code
  []
  (promise #(db/get-value "current-code" %)))

(defn store-activated-user
  [code response]
  (logger/debug "[store-activated-user]" code)
  (store-current-code! code)
  (let [body (-> response response-clone body-json)]
    (-> body
        (then #(js->clj % :keywordize-keys true))
        (then #(assoc % :code code))
        (then #(db/add-item db/activated-users-store-name % identity)))))

(defn get-activated-user-by-code
  [code]
  (logger/debug "[get-activated-user-by-code]" code)
  (promise #(db/get-by-key db/activated-users-store-name code %)))

(defn get-current-user
  []
  (let [code-promise (get-current-code)]
    (-> code-promise
        (then get-activated-user-by-code))))