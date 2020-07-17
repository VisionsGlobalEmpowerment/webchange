(ns webchange.service-worker.db.users
  (:require
    [webchange.service-worker.db.core :as core]
    [webchange.service-worker.db.db-general :as db]
    [webchange.service-worker.wrappers :refer [then]]
    [webchange.service-worker.logger :as logger]
    [webchange.service-worker.db.general :as db-general-state]
    [webchange.service-worker.wrappers :refer [js-fetch promise promise-all promise-resolve promise-reject response-new response-clone request-clone body-json then catch]]))

(def store-name db/users-store-name)

(defn save-progress
  [progress user offline?]
  (-> (db/get-db)
      (then (fn [db]
              (core/add-item db store-name {:progress progress
                                            :offline  offline?
                                            :id       (:id user)})))))

(defn- response->user
  [response code]
  (-> response
      (body-json)
      (then (fn [body-data]
              (-> body-data
                  (js->clj :keywordize-keys true)
                  (assoc :code code))))))

(defn save-user
  [code response]
  (logger/debug "[store-activated-user]" code)
  (db-general-state/set-current-code code)
  (-> (db/get-db)
      (then (fn [db]
              (-> (response->user response code)
                  (then (fn [user]
                          (core/add-item db store-name user))))))
      (catch (fn [error]
               (logger/error "Save user failed:" error)))))

(defn get-user-by-code
  [code]
  (if-not (nil? code)
    (-> (db/get-db)
        (then (fn [db]
                (core/get-by-key db store-name code))))
    (promise-reject "Can not get user: activation code is not defined")))

(defn get-current-user
  []
  (-> (db-general-state/get-current-code)
      (then (fn [code]
              (logger/debug "Get current user. Activation code:" code)
              (if-not (nil? code)
                (get-user-by-code code)
                (promise-resolve nil))))))
