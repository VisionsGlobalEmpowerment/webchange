(ns webchange.auth.core
  (:require [buddy.hashers :as hashers]
            [webchange.db.core :refer [*db*] :as db]
            [java-time :as jt]
            [clojure.tools.logging :as log]
            [clj-http.client :as http]))

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
    (let [user (-> (db/get-user {:id (:user-id student)}) visible-user)]
      [true {:id         (:user-id student)
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

(def website-host "webchange.com")

(defn website-user-resource
  [website-user-id]
  (str "http://" website-host "/api/user/" website-user-id))

(defn- website->platform-user
  [{:keys [id first-name last-name email] :as response}]
  {:last_name last-name
   :first_name first-name
   :email email
   :website_id id
   :password nil})

(defn create-user-from-website!
  [website-user-id]
  (let [url (website-user-resource website-user-id)
        user-data (-> (http/get url {:accept :json})
                      :body
                      (website->platform-user))
        created-at (jt/local-date-time)
        last-login (jt/local-date-time)]
    (-> user-data
        (assoc :created_at created-at)
        (assoc :last_login last-login)
        (assoc :active true)
        db/create-user!
        first)))

(defn get-user-id-by-website-id!
  [website-user-id]
  (if-let [{id :id} (db/find-user-by-website-id {:website_id website-user-id})]
    id
    (-> (create-user-from-website! website-user-id)
        :id)))
