(ns webchange.auth.roles
  (:require
    [webchange.db.core :as db]))

(defn is-admin?
  [user-id]
  (let [{admin? :result} (db/is-admin? {:id user-id})]
    admin?))
