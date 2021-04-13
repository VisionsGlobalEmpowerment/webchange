(ns webchange.auth.roles
  (:require
    [config.core :refer [env]]))

(defn is-admin?
  [user-id]
  (let [admins (get-in env [:roles :admin] [])]
    (some #(= user-id %) admins)))
