(ns webchange.service-worker.db.users
  (:require
    [webchange.service-worker.db.core :as core]
    [webchange.service-worker.db.db-course :as db-course]
    [webchange.service-worker.wrappers :refer [then]]

    [webchange.service-worker.logger :as logger]
    [webchange.service-worker.db.state :as db-state]
    [webchange.service-worker.wrappers :refer [js-fetch promise promise-all promise-resolve promise-reject response-new response-clone request-clone body-json then catch]]))

(def store-name db-course/users-store-name)

(defn save-progress
  [progress user offline?]
  (-> (db-course/get-db)
      (then (fn [db]
              (core/add-item db store-name {:progress progress
                                            :offline  offline?
                                            :id       (:id user)})))))

(defn- response->user
  [response code]
  (-> response
      response-clone
      body-json
      (then (fn [body-data]
              (-> body-data
                  (js->clj :keywordize-keys true)
                  (assoc :code code))))))

(defn save-user
  [code response]
  (logger/debug "[store-activated-user]" code)
  (db-state/set-current-code code)
  (-> (db-course/get-db)
      (then (fn [db]
              (-> (response->user response code)
                  (then (fn [user]
                          (core/add-item db store-name user))))))))

(defn get-user-by-code
  [code]
  (if-not (nil? code)
    (-> (db-course/get-db)
        (then (fn [db]
                (core/get-by-key db store-name code))))
    (promise-reject "Can not get user: activation code is not defined")))

(defn get-current-user
  []
  (-> (db-state/get-current-code)
      (then (fn [code]
              (if-not (nil? code)
                (do (logger/debug "[get-activated-user-by-code]" code)
                    (get-user-by-code code))
                (promise-resolve nil))))))
