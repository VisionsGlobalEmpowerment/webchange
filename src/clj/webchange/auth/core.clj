(ns webchange.auth.core
  (:require [buddy.hashers :as hashers]
            [webchange.db.core :refer [*db*] :as db]
            [java-time :as jt]
            [clojure.tools.logging :as log]))

(def error-invalid-credentials {:errors {:form "Invalid credentials"}})

(defn prepare-register-data
  [{:keys [last-name first-name email password]}]
  {:last_name last-name
   :first_name first-name
   :email email
   :password password})

(defn create-user!
  [options]
  (let [created-at (jt/local-date-time)
        last-login (jt/local-date-time)]
    (-> options
        prepare-register-data
        (assoc :created_at created-at)
        (assoc :last_login last-login)
        (assoc :active false)
        db/create-user!)))

(defn create-user-with-credentials!
  [options]
  (let [hashed-password (hashers/derive (:password options))]
    (-> options
        (assoc :password hashed-password)
        create-user!)))

(defn activate-user!
  [user-id]
  (db/activate-user! {:id user-id}))

(defn credentials-valid?
  [user password]
  (and
    (:active user)
    (hashers/check password (:password user))))

(defn visible-user [user]
  (select-keys user [:id :first-name :last-name :email :school-id :teacher-id :student-id]))

(defn visible-student [student]
  (-> (select-keys student [:id :user-id :class-id :school-id :gender :date-of-birth :user :class])
      (update :date-of-birth str)))

(defn user->teacher [{user-id :id :as user}]
  (let [{teacher-id :id school-id :school-id} (db/get-teacher-by-user {:user_id user-id})]
    (assoc user :teacher-id teacher-id :school-id school-id)))

(defn teacher-login!
  [{:keys [email password]}]
  (if-let [user (db/find-user-by-email {:email email})]
    (if (credentials-valid? user password)
      [true (-> user user->teacher visible-user)]
      [false error-invalid-credentials])
    [false error-invalid-credentials]))

(defn student-login!
  [{:keys [school-id access-code]}]
  (if-let [student (db/find-student-by-code {:school_id school-id :access_code access-code})]
    [true {:id (:user-id student)
           :school-id (:school-id student)}]
    [false error-invalid-credentials]))

(defn register-user!
  [user-data]
  (create-user-with-credentials! user-data)
  (if-let [user (db/find-user-by-email {:email (:email user-data)})]
    [true (visible-user user)]
    [false {:errors {:form "Invalid registration data"}}]))

(defn prepare-student-data
  [{:keys [first-name last-name]}]
  {:first_name first-name
   :last_name last-name})

(defn update-student-user!
  [user-id options]
  (-> options
      prepare-student-data
      (assoc :id user-id)
      db/update-student-user!))