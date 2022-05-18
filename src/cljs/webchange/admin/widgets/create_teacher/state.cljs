(ns webchange.admin.widgets.create-teacher.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.admin.widgets.state :as widgets]
    [webchange.state.warehouse :as warehouse]))

(def path-to-db :widget/create-teacher)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

;; Flags

(def flags-key :form-flags)

(defn- update-flags
  [db data-patch]
  (update db flags-key merge data-patch))

(re-frame/reg-sub
  ::flags-data
  :<- [path-to-db]
  #(get % flags-key {}))

(defn- set-data-saving
  [db value]
  (update-flags db {:data-saving? value}))

(re-frame/reg-sub
  ::data-saving?
  :<- [::flags-data]
  #(get % :data-saving? false))

;;

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {}))

(re-frame/reg-event-fx
  ::reset
  (fn [{:keys [db]} [_]]
    {:db (dissoc db path-to-db)}))

(re-frame/reg-event-fx
  ::save
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ school-id teacher-data {:keys [on-success]}]]
    {:db       (set-data-saving db true)
     :dispatch [::warehouse/create-teacher
                {:school-id school-id :data teacher-data}
                {:on-success [::save-success on-success]
                 :on-failure [::save-failure]}]}))

(re-frame/reg-event-fx
  ::save-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ success-handler {:keys [data]}]]
    {:db                (set-data-saving db false)
     ::widgets/callback [success-handler data]}))

(re-frame/reg-event-fx
  ::save-failure
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (set-data-saving db false)}))
