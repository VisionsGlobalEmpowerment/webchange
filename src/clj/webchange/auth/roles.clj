(ns webchange.auth.roles
  (:require
    [webchange.db.core :as db]))

(defn is-admin?
  [user-id]
  (let [{admin? :result} (db/user-has-type? {:id user-id :types ["admin" "bbs-admin"]})]
    admin?))

(defn is-at-least-teacher?
  [user-id]
  (let [{admin? :result} (db/user-has-type? {:id user-id :types  ["admin" "bbs-admin" "teacher"]})]
    admin?))

(defn is-super-admin?
  [user-id]
  (let [{admin? :result} (db/user-has-type? {:id user-id :types ["admin"]})]
    admin?))

(defn is-live-user?
  [user-id]
  (let [{result :result} (db/user-has-type? {:id user-id :types ["live"]})]
    result))
