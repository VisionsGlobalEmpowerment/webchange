(ns webchange.accounts.core
  (:require
    [buddy.hashers :as hashers]
    [webchange.db.core :as db]
    [webchange.course.core :refer [get-course-info]]
    [clojure.tools.logging :as log]
    [java-time :as jt]
    [compojure.api.exception :as ex]
    [camel-snake-kebab.extras :refer [transform-keys]]
    [camel-snake-kebab.core :refer [->snake_case_keyword]]
    [webchange.emails.core :as e]))

(defn visible-user [user]
  (select-keys user [:id :first-name :last-name :email :school-id :teacher-id :student-id :website-id :type :last-login]))

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

(defn- requested-type->account-types
  [type]
  (case type
    "admin" ["admin" "bbs-admin"]
    [type]))

(defn accounts-by-type
  [requested-type page query]
  (let [limit 30
        offset (* limit (dec page))
        types (requested-type->account-types requested-type)
        query (when query (str "%" query "%"))
        accounts (->> (db/accounts-by-types {:types types :limit limit :offset offset :q query})
                      (map visible-account))
        total (-> (db/count-accounts-by-types {:types types :q query})
                  :result)
        pages (-> total
                  (/ limit)
                  (Math/ceil)
                  (int))]
    {:accounts accounts
     :current-page page
     :pages pages
     :total total}))

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

(defn register-account
  [data]
  (let [data (-> data
                 (assoc :type "live")
                 (assoc :active false))
        [{id :id}] (create-user-with-credentials! data)
        result (assoc data :id id)]
    (e/request-email-confirmation! result)
    data))

(defn edit-account
  [user-id data]
  (db/edit-account! {:id user-id
                     :first_name (:first-name data)
                     :last_name (:last-name data)
                     :type (:type data)})
  (get-account user-id))

(defn update-account-last-login
  [user-id last-login]
  (db/update-account-last-login! {:id         user-id
                                  :last_login last-login})
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


(defn string->uuid [value]
  (try
    (java.util.UUID/fromString value)
    (catch Exception e nil)))

(defn confirm-email
  [code]
  (when (string->uuid code)
    (let [uuid (java.util.UUID/fromString code)]
      (when-let [{{:keys [user-id]} :metadata} (db/get-code {:code uuid})]
        (db/set-account-status! {:id user-id
                                 :active true})
        (db/delete-code! {:code uuid})
        true))))

(defn reset-password-for-email
  [{:keys [email]}]
  (if-let [{id :id} (db/find-user-by-email {:email email})]
    (do (e/reset-password {:user-id id
                           :email email})
        {:message "ok"})
    (throw (ex-info "Email not found"
                    {:type ::ex/request-validation
                     :errors {:email "Not found"}}))))

(defn reset-password-by-code
  [code {:keys [password]}]
  (when-not (string->uuid code)
    (throw (ex-info "The code is invalied"
                    {:type ::ex/request-validation
                     :errors {:code "Code is invalid"}})))
  (let [uuid (java.util.UUID/fromString code)]
    (if-let [{{:keys [user-id]} :metadata} (db/get-code {:code uuid})]
      (let [hashed-password (hashers/derive password)]
        (db/change-password! {:id user-id
                              :password hashed-password})
        (db/delete-code! {:code uuid})
        {:message "ok"})
      (throw (ex-info "The code is not found"
                      {:type ::ex/request-validation
                       :errors {:code "Code is not found"}})))))
