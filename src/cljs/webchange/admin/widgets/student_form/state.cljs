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

(def callbacks-key :callbacks)

(defn- set-callbacks
  [db data]
  (assoc db callbacks-key data))

(defn- get-callback
  [db callback-name]
  (get-in db [callbacks-key callback-name] #()))

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
  (fn [{:keys [db]} [_ {:keys [init-data school-id] :or {init-data {}}}]]
    {:db         (-> db
                     (assoc :school-id school-id)
                     (set-form-data init-data)
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
  (fn [{:keys [db]} [_ {:keys [school-id student-id] :as props}]]
    {:db       (-> db
                   (assoc :school-id school-id)
                   (assoc :student-id student-id)
                   (set-callbacks (select-keys props [:on-remove :on-remove-from-class]))
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

(re-frame/reg-event-fx
  ::reset-form
  (fn [{:keys [db]} [_ {:keys [_]}]]
    {:db (dissoc db path-to-db)}))

;; Remove

(def remove-window-state-key :remove-window-state)

(def remove-window-default-state {:open?        false
                                  :in-progress? false
                                  :done?        false})

(defn- update-remove-window-state
  [db data-patch]
  (update db remove-window-state-key merge data-patch))

(re-frame/reg-sub
  ::remove-window-state
  :<- [path-to-db]
  #(get % remove-window-state-key remove-window-default-state))

(re-frame/reg-event-fx
  ::open-remove-window
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (update-remove-window-state db {:open? true})}))

(re-frame/reg-event-fx
  ::close-remove-window
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (update-remove-window-state db remove-window-default-state)}))

(re-frame/reg-event-fx
  ::remove-student
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ student-id]]
    {:db       (update-remove-window-state db {:in-progress? true})
     :dispatch [::warehouse/delete-student
                {:student-id student-id}
                {:on-success [::remove-student-success]
                 :on-failure [::remove-student-failure]}]}))

(re-frame/reg-event-fx
  ::remove-student-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (update-remove-window-state db {:in-progress? false
                                         :done?        true})}))

(re-frame/reg-event-fx
  ::remove-student-failure
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (update-remove-window-state db {:in-progress? false})}))

(re-frame/reg-event-fx
  ::handle-removed
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [success-handler (get-callback db :on-remove)]
      {:dispatch  [::close-remove-window]
       ::callback [success-handler]})))

;; remove from class

(def remove-from-class-window-state-key :remove-from-class-window-state)

(def remove-from-class-window-default-state {:open?        false
                                             :in-progress? false
                                             :done?        false})

(defn- update-remove-from-class-window-state
  [db data-patch]
  (update db remove-from-class-window-state-key merge data-patch))

(re-frame/reg-sub
  ::remove-from-class-window-state
  :<- [path-to-db]
  #(get % remove-from-class-window-state-key remove-from-class-window-default-state))

(re-frame/reg-event-fx
  ::open-remove-from-class-window
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (update-remove-from-class-window-state db {:open? true})}))

(re-frame/reg-event-fx
  ::close-remove-from-class-window
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (update-remove-from-class-window-state db remove-from-class-window-default-state)}))

(re-frame/reg-event-fx
  ::remove-from-class-student
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ student-id]]
    {:db       (update-remove-from-class-window-state db {:in-progress? true})
     :dispatch [::warehouse/remove-student-from-class
                {:student-id student-id}
                {:on-success [::remove-from-class-student-success]
                 :on-failure [::remove-from-class-student-failure]}]}))

(re-frame/reg-event-fx
  ::remove-from-class-student-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (update-remove-from-class-window-state db {:in-progress? false
                                                    :done?        true})}))

(re-frame/reg-event-fx
  ::remove-from-class-student-failure
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (update-remove-from-class-window-state db {:in-progress? false})}))

(re-frame/reg-event-fx
  ::handle-removed-from-class
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [success-handler (get-callback db :on-remove-from-class)]
      {:dispatch  [::close-remove-from-class-window]
       ::callback [success-handler]})))

;;

(re-frame/reg-fx
  ::callback
  (fn [[callback & params]]
    (when (fn? callback)
      (apply callback params))))
