(ns webchange.auth.core
  (:require
    [buddy.hashers :as hashers]
    [clojure.tools.logging :as log]
    [java-time :as jt]
    [webchange.accounts.core :as accounts]
    [webchange.db.core :as db]))

(def error-invalid-credentials {:errors {:form "Invalid credentials"}})
(def error-user-not-found {:errors {:form "User not found"}})
(def error-email-not-confirmed {:errors {:form "Could not confirm your email address"}})

(defn- credentials-valid?
  [user password]
  (hashers/check password (:password user)))

(defn- user->teacher [{user-id :id :as user}]
  (let [{teacher-id :id school-id :school-id} (db/get-teacher-by-user {:user_id user-id})]
    (assoc user :teacher-id teacher-id :school-id school-id)))

(defn- update-user-last-login
  [user-data]
  (->> (jt/local-date-time)
       (accounts/update-account-last-login (:id user-data))))

(defn teacher-login!
  [{:keys [email password]}]
  (let [user (db/find-user-by-email {:email email})]
    (cond
      (nil? user) [false error-user-not-found]
      (not (credentials-valid? user password)) [false error-invalid-credentials]
      (not (:active user)) [false error-email-not-confirmed]
      :else (do (update-user-last-login user)
                [true (-> user user->teacher accounts/visible-user)]))))

(defn student-login!
  [{:keys [school-id access-code]}]
  (if-let [student (db/find-student-by-code {:school_id school-id :access_code access-code})]
    (let [user (-> (db/get-user {:id (:user-id student)}) accounts/visible-user)
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
  (if-let [user (db/get-user {:id user-id})]
    (let [school-id (or school-id (-> (db/get-teacher-by-user {:user_id user-id}) :school-id))]
      [true {:id         user-id
             :school-id  school-id
             :first-name (:first-name user)
             :last-name  (:last-name user)
             :last-login (:last-login user)
             :created-at (:created-at user)
             :type       (:type user)}])
    [false error-invalid-credentials]))

(defn- prepare-student-data
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
        (assoc :type "live")
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
        accounts/visible-user)))

(defn get-user-id-by-website!
  [website-user]
  (-> (replace-user-from-website! website-user) :id))

(defn user->identity [user]
  (select-keys user [:id :school-id :teacher-id :student-id]))

(defn with-updated-identity [request response identity]
  (assoc response :session (merge (:session request) {:identity identity})))

(defn logout [response]
  (assoc response :session {}))
