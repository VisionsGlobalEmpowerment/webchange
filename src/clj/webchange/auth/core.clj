(ns webchange.auth.core
  (:require [buddy.hashers :as hashers]
            [webchange.db.core :refer [*db*] :as db]
            [java-time :as jt]
            [clojure.tools.logging :as log]
            [clj-http.client :as http]
            [config.core :refer [env]]))

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
        (assoc :website_id nil)
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

(defn edit-user!
  [user-id]
  (db/activate-user! {:id user-id}))

(defn visible-user [user]
  (select-keys user [:id :first-name :last-name :email :school-id :teacher-id :student-id :website-id]))

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
    (let [user (-> (db/get-user {:id (:user-id student)}) visible-user)
          class (-> (db/get-class {:id (:class-id student)}))
          course (-> (db/get-course-by-id {:id (:course-id class)}))]
      [true {:id         (:user-id student)
             :course-slug   (:slug course)
             :school-id  (:school-id student)
             :first-name (:first-name user)
             :last-name  (:last-name user)}])
    [false error-invalid-credentials]))

(defn get-current-user
  [user-id school-id]
  (if-let [user (-> (db/get-user {:id user-id}) visible-user)]
    [true {:id         user-id
           :school-id  school-id
           :first-name (:first-name user)
           :last-name  (:last-name user)}]
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

(defn- website->platform-user
  [{:keys [id first_name last_name email] :as response}]
  {:first_name first_name
   :last_name  last_name
   :email      email
   :website_id id
   :password   nil})

(defn- create-user-from-website!
  [user-data]
  (let [created-at (jt/local-date-time)
        last-login (jt/local-date-time)]
    (-> user-data
        (assoc :created_at created-at)
        (assoc :last_login last-login)
        (assoc :active true)
        db/create-user!
        first)))

(defn- update-user-from-website!
  [user-data]
  (let [last-login (jt/local-date-time)]
    (-> user-data
        (assoc :last_login last-login)
        db/update-website-user!)))

(defn- has-website-user?
  [website-user-id]
  (boolean (db/find-user-by-website-id {:website_id website-user-id})))

(defn replace-user-from-website!
  [{website-user-id :id :as website-user}]
  (let [user-data (website->platform-user website-user)]
    (if (has-website-user? website-user-id)
      (update-user-from-website! user-data)
      (create-user-from-website! user-data))
    (-> (db/find-user-by-website-id {:website_id website-user-id})
        visible-user)))

(defn get-user-id-by-website!
  [website-user]
  (-> (replace-user-from-website! website-user) :id))

(defn user->identity [user]
  (select-keys user [:id :school-id :teacher-id :student-id]))

(defn with-updated-identity [request response identity]
  (assoc response :session (merge (:session request) {:identity identity})))

(defn edit-user!
  [user-id data]
  (let [hashed-password (hashers/derive (:password data))]
    (db/edit-user! {:id user-id
                    :first_name (:fist-name data)
                    :last_name (:last-name data)
                    :password hashed-password})))
