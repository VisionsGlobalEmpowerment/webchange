(ns webchange.accounts.core
  (:require
    [buddy.hashers :as hashers]
    [webchange.db.core :as db]
    [webchange.course.core :refer [get-course-info]]
    [clojure.tools.logging :as log]
    [java-time :as jt]
    [compojure.api.exception :as ex]
    [camel-snake-kebab.extras :refer [transform-keys]]
    [camel-snake-kebab.core :refer [->snake_case_keyword]]))

(defn visible-user [user]
  (select-keys user [:id :first-name :last-name :email :school-id :teacher-id :student-id :website-id :type]))

(defn visible-account [user & {:keys [more-fields] :or {more-fields []}}]
  (->> [:id :first-name :last-name :email :active :last-login :created-at :type]
       (concat more-fields)
       (select-keys user)))

(defn visible-student [student]
  (-> (select-keys student [:id :user-id :class-id :school-id :gender :date-of-birth :user :class])
      (update :date-of-birth str)))

(defn- prepare-register-data
  [{:keys [last-name first-name email password type]}]
  {:last_name last-name
   :first_name first-name
   :email email
   :password password
   :type type})

(defn create-user!
  [{:keys [active email] :as options :or {active true}}]
  (when (db/find-user-by-email {:email email})
    (throw (ex-info "Email already in use"
                    {:type ::ex/request-validation
                     :errors {:email "Email already in use"}})))
  (let [created-at (jt/local-date-time)
        last-login (jt/local-date-time)]
    (-> options
        prepare-register-data
        (assoc :created_at created-at)
        (assoc :last_login last-login)
        (assoc :active active)
        (assoc :website_id nil)
        db/create-user!)))

(defn create-user-with-credentials!
  [options]
  (let [hashed-password (hashers/derive (:password options))]
    (-> options
        (assoc :password hashed-password)
        create-user!)))

(defn edit-teacher-user!
  [user-id data]
  (db/edit-teacher-user! {:id user-id
                          :first_name (:first-name data)
                          :last_name (:last-name data)})
  (when (some? (:password data))
    (let [hashed-password (hashers/derive (:password data))]
      (db/change-password! {:id user-id
                            :password hashed-password}))))

(defn accounts-by-type
  [type page]
  (let [limit 30
        offset (* limit (dec page))
        accounts (->> (db/accounts-by-type {:type type :limit limit :offset offset})
                      (map visible-account))
        pages (-> (db/count-accounts-by-type {:type type})
                  :result
                  (/ limit)
                  (Math/ceil)
                  (int))]
    {:accounts accounts
     :current-page page
     :pages pages
     :total 34}))                                            ;; ToDo: Get total accounts number

(defn- with-children
  [{:keys [id] :as account}]
  (let [children (->> (db/find-users-by-parent {:parent_id id})
                      (map #(visible-account % :more-fields [:data]))
                      (map (fn [{:keys [data] :as child}]
                             (assoc child :course (get-course-info (:course-slug data))))))]
    (assoc account :children children)))

(defn get-account
  [id]
  (-> (db/get-user {:id id})
      (visible-account)
      (with-children)))

(defn delete-account
  [id]
  (-> (db/get-user {:id id})
      (visible-account)))

(defn create-account
  [data]
  (let [[{id :id}] (create-user-with-credentials! data)]
    (-> data
        (assoc :id id))))

(defn edit-account
  [user-id data]
  (db/edit-account! {:id user-id
                     :first_name (:first-name data)
                     :last_name (:last-name data)
                     :type (:type data)})
  (get-account user-id))

(defn change-password
  [user-id {:keys [password]}]
  (let [hashed-password (hashers/derive password)]
    (db/change-password! {:id user-id
                          :password hashed-password})
    (get-account user-id)))

(defn set-account-status
  [user-id {:keys [active]}]
  (db/set-account-status! {:id user-id
                           :active active})
  {:id user-id
   :active active})
