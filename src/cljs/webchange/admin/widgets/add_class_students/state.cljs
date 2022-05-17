(ns webchange.admin.widgets.add-class-students.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.admin.widgets.state :as widgets]
    [webchange.state.warehouse :as warehouse]
    [webchange.utils.list :refer [in-list? toggle-item]]))

(def path-to-db :widget/add-class-students)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

;; Selected Students

(def selected-students-key :selected-students)

(defn- get-selected-students
  [db]
  (get db selected-students-key []))

(defn- reset-selected-students
  [db]
  (assoc db selected-students-key []))

(defn- toggle-selected-student
  [db student-id]
  (update db selected-students-key toggle-item student-id))

(re-frame/reg-event-fx
  ::select-student
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ student-id]]
    {:db (toggle-selected-student db student-id)}))

(re-frame/reg-sub
  ::selected-students
  :<- [path-to-db]
  get-selected-students)

;; Available Students

(def available-students-key :unassigned-students)

(defn- reset-available-students
  [db]
  (assoc db available-students-key []))

(defn- set-available-students
  [db data]
  (assoc db available-students-key data))

(re-frame/reg-sub
  ::available-students
  :<- [path-to-db]
  #(get % available-students-key []))

;; Students List

(re-frame/reg-sub
  ::students
  (fn []
    [(re-frame/subscribe [::available-students])
     (re-frame/subscribe [::selected-students])])
  (fn [[available-students selected-students]]
    (->> available-students
         (map (fn [{:keys [id user]}]
                {:id        id
                 :avatar    nil
                 :name      (str (:first-name user) " " (:last-name user))
                 :selected? (in-list? selected-students id)})))))

;; Indicators

(def indicators-keys :indicators)

(defn- update-indicators
  [db data-patch]
  (update db indicators-keys merge data-patch))

(re-frame/reg-sub
  ::indicators
  :<- [path-to-db]
  #(get % indicators-keys {}))

(def data-loading-key :data-loading?)

(defn- set-data-loading
  [db value]
  (update-indicators db {data-loading-key value}))

(re-frame/reg-sub
  ::data-loading?
  :<- [::indicators]
  #(get % data-loading-key false))

(def data-saving-key :data-saving?)

(defn- set-data-saving
  [db value]
  (update-indicators db {data-saving-key value}))

(re-frame/reg-sub
  ::data-saving?
  :<- [::indicators]
  #(get % data-saving-key false))

;;

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db       (-> db
                   (reset-available-students)
                   (reset-selected-students)
                   (set-data-loading true))
     :dispatch [::warehouse/load-unassigned-students
                {:on-success [::load-students-success]}]}))

(re-frame/reg-event-fx
  ::load-students-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [students]}]]
    {:db (-> db
             (set-data-loading false)
             (set-available-students students))}))

(re-frame/reg-event-fx
  ::reset
  (fn [{:keys [db]} [_]]
    {:db (dissoc db path-to-db)}))

(re-frame/reg-event-fx
  ::save
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ class-id on-save]]
    (let [selected-students (get-selected-students db)]
      {:db       (-> db
                     (set-data-saving true))
       :dispatch [::warehouse/add-students-to-class
                  {:class-id class-id
                   :data     selected-students}
                  {:on-success [::save-success on-save]
                   :on-failure [::save-failure]}]})))

(re-frame/reg-event-fx
  ::save-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ on-save response]]
    {:db                (-> db
                            (set-data-saving false))
     ::widgets/callback [on-save response]}))

(re-frame/reg-event-fx
  ::save-failure
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (-> db
             (set-data-saving false))}))
