(ns webchange.auth.roles
  (:require
    [webchange.db.core :as db]))

(defn is-admin?
  [user-id]
  (let [{admin? :result} (db/user-has-type? {:id user-id :types ["admin" "bbs-admin"]})]
    admin?))

(defn is-super-admin?
  [user-id]
  (let [{admin? :result} (db/user-has-type? {:id user-id :types ["admin"]})]
    admin?))
