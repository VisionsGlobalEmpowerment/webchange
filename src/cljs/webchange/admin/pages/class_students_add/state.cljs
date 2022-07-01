(ns webchange.admin.pages.class-students-add.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.admin.routes :as routes]
    [webchange.state.warehouse :as warehouse]
    [webchange.admin.widgets.add-class-students.state :as widget-state]
    [webchange.utils.date :refer [date-str->locale-date ms->time]]))

(def path-to-db :page/class-students-add)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

;; class

(def class-key :class)

(defn- set-class
  [db value]
  (assoc db class-key value))

(re-frame/reg-sub
  ::class
  :<- [path-to-db]
  #(get % class-key))

(re-frame/reg-sub
  ::class-name
  :<- [::class]
  #(get % :name))

;; selected students

(def selected-students-key :selected-students)

(re-frame/reg-sub
  ::selected-students
  :<- [path-to-db]
  #(get % selected-students-key []))

(re-frame/reg-sub
  ::selected-students-number
  :<- [::selected-students]
  #(count %))

(re-frame/reg-event-fx
  ::set-selected-students
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ value]]
    {:db (assoc db selected-students-key value)}))

;;

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [class-id school-id]}]]
    {:db (assoc db :school-id school-id :class-id class-id)
     :dispatch [::warehouse/load-class {:class-id class-id} {:on-success [::load-class-success]}]}))

(re-frame/reg-event-fx
  ::reset
  (fn [{:keys [db]} [_]]
    {:db (dissoc db path-to-db)}))

(re-frame/reg-event-fx
  ::load-class-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [class]}]]
    {:db (-> db (set-class class))}))

(re-frame/reg-event-fx
  ::save
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:dispatch [::widget-state/save]}))

(re-frame/reg-event-fx
  ::open-class-students
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [{:keys [school-id class-id]} db]
      {:dispatch [::routes/redirect :class-students :school-id school-id :class-id class-id ]})))
