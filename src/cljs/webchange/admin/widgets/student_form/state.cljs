(ns webchange.admin.widgets.student-form.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.ui.components.form.data :refer [init]]
    [webchange.admin.widgets.state :as widgets]
    [webchange.state.warehouse :as warehouse]))

(def path-to-db :widget/class-form)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

;; Form Data

(def form-data (init :form-data))
(def get-form-data (:get-data form-data))
(def set-form-data (:set-data form-data))
(def reset-form-data (:reset-data form-data))
(def update-form-data (:update-data form-data))

(re-frame/reg-sub
  ::form-data
  :<- [path-to-db]
  #(get-form-data %))

;; Flags

(def flags-data (init :flags))
(def get-flags-data (:get-data flags-data))
(def reset-flags-data (:reset-data flags-data))
(def update-flags-data (:update-data flags-data))

(re-frame/reg-sub
  ::flags-data
  :<- [path-to-db]
  #(get-flags-data %))

;; -- student-loading?

(def student-loading-key :student-loading?)

(defn- set-student-loading
  [db value]
  (update-flags-data db {student-loading-key value}))

(re-frame/reg-sub
  ::student-loading?
  :<- [::flags-data]
  #(get % student-loading-key false))

;; -- classes-loading?

(def classes-loading-key :classes-loading?)

(defn- set-classes-loading
  [db value]
  (update-flags-data db {classes-loading-key value}))

(re-frame/reg-sub
  ::classes-loading?
  :<- [::flags-data]
  #(get % classes-loading-key false))

;; -- student-saving?

(def student-saving-key :student-saving?)

(defn- set-student-saving
  [db value]
  (update-flags-data db {student-saving-key value}))

(re-frame/reg-sub
  ::student-saving?
  :<- [::flags-data]
  #(get % student-saving-key false))

;; data-loading?

(re-frame/reg-sub
  ::data-loading?
  :<- [::student-loading?]
  :<- [::classes-loading?]
  (fn [[student-loading? classes-loading?]]
    (or student-loading? classes-loading?)))

;; Class options

(def class-options-key :class-options)

(defn- set-class-options
  [db data]
  (assoc db class-options-key data))

(re-frame/reg-sub
  ::class-options
  :<- [path-to-db]
  (fn [data]
    (->> (get data class-options-key [])
         (concat [{:text  "Select Class"
                   :value ""}]))))

;;

(re-frame/reg-event-fx
  ::init-add-form
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [school-id]}]]
    {:db       (-> db
                   (assoc :school-id school-id)
                   (reset-form-data)
                   (set-classes-loading true))
     :dispatch-n [[::warehouse/load-school-classes {:school-id school-id}
                   {:on-success [::load-school-classes-success]}]
                  [::warehouse/generate-school-access-code {:school-id school-id}
                   {:on-success [::init-access-code-success]}]]}))

(re-frame/reg-event-fx
  ::init-access-code-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [access-code]}]]
    {:db (update-form-data db {:access-code access-code})}))

(re-frame/reg-event-fx
  ::init-edit-form
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [school-id student-id]}]]
    {:db       (-> db
                   (assoc :school-id school-id)
                   (assoc :student-id student-id)
                   (reset-form-data)
                   (set-student-loading true))
     :dispatch [::warehouse/load-student {:student-id student-id}
                {:on-success [::load-student-success]}]}))

(re-frame/reg-event-fx
  ::load-student-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [student]}]]
    {:db       (-> db
                   (set-form-data (-> student
                                      (merge (:user student))
                                      (dissoc :user)))
                   (set-classes-loading true)
                   (set-student-loading false))
     :dispatch [::warehouse/load-school-classes {:school-id (:school-id student)}
                {:on-success [::load-school-classes-success]}]}))

(re-frame/reg-event-fx
  ::load-school-classes-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [classes]}]]
    (let [class-options (map (fn [{:keys [id name]}]
                               {:value id
                                :text  name})
                             classes)]
      {:db (-> db
               (set-classes-loading false)
               (set-class-options class-options))})))

(re-frame/reg-event-fx
  ::save
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ data {:keys [on-success]}]]
    (let [student-id (:student-id db)]
      {:db       (set-student-saving db true)
       :dispatch [::warehouse/edit-student {:student-id student-id :data data}
                  {:on-success [::save-success on-success data]
                   :on-failure [::save-failure]}]})))

(re-frame/reg-event-fx
  ::save-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ on-success student-data]]
    {:db                (set-student-saving db false)
     ::widgets/callback [on-success student-data]}))

(re-frame/reg-event-fx
  ::save-failure
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (set-student-saving db false)}))

(re-frame/reg-event-fx
  ::create
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ data {:keys [on-success]}]]
    (let [school-id (:school-id db)]
      {:db       (set-student-saving db true)
       :dispatch [::warehouse/create-student {:school-id school-id :data data}
                  {:on-success [::create-success on-success data]
                   :on-failure [::create-failure]}]})))

(re-frame/reg-event-fx
  ::create-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ on-success student-data]]
    {:db                (set-student-saving db false)
     ::widgets/callback [on-success student-data]}))

(re-frame/reg-event-fx
  ::create-failure
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (set-student-saving db false)}))
