(ns webchange.auth.core
  (:require [buddy.hashers :as hashers]
            [webchange.db.core :refer [*db*] :as db]
            [java-time :as jt]
            [clojure.tools.logging :as log]))

(def error-invalid-credentials {:errors {:form "Invalid credentials"}})

(defn create-user!
  [options]
  (let [hashed-password (hashers/derive (:password options))
        created-at (jt/local-date-time)
        last-login (jt/local-date-time)]
    (-> options
        (assoc :password hashed-password)
        (assoc :created_at created-at)
        (assoc :last_login last-login)
        (assoc :active false)
        db/create-user!)))

(defn activate-user!
  [user-id]
  (db/activate-user! {:id user-id}))

(defn credentials-valid?
  [user password]
  (and
    (:active user)
    (hashers/check password (:password user))))

(defn visible-user
  [user]
  {:first-name (:first_name user)
   :last-name (:last_name user)
   :email (:email user)})

(defn login!
  [{:keys [email password]}]
  (if-let [user (db/find-user-by-email {:email email})]
    (if (credentials-valid? user password)
      [true (visible-user user)]
      [false error-invalid-credentials])
    [false error-invalid-credentials]))

(defn prepare-register-data
  [{:keys [last-name first-name email password]}]
  {:last_name last-name
   :first_name first-name
   :email email
   :password password})

(defn register-user!
  [user-data]
  (let [prepared-data (prepare-register-data user-data)]
    (create-user! prepared-data)
    (if-let [user (db/find-user-by-email {:email (:email user-data)})]
      [true (visible-user user)]
      [false {:errors {:form "Invalid registration data"}}])))

(defn user-id-from-identity
  [identity]
  (let [user (db/find-user-by-email {:email identity})]
    (:id user)))