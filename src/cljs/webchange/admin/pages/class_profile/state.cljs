(ns webchange.admin.pages.class-profile.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.state.warehouse :as warehouse]))

(def path-to-db :class-profile)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

;; Side Bar Content

(def side-bar-key :side-bar)

(defn- set-side-bar
  [db value]
  (assoc db side-bar-key value))

(re-frame/reg-sub
  ::side-bar
  :<- [path-to-db]
  #(get % side-bar-key :add-student))

(re-frame/reg-event-fx
  ::open-add-student-form
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (set-side-bar db :add-student)}))

(re-frame/reg-event-fx
  ::open-class-form
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (set-side-bar db :class-form)}))

;; Class Form

(def form-editable-key :form-editable?)

(re-frame/reg-sub
  ::form-editable?
  :<- [path-to-db]
  #(get % form-editable-key false))

(defn- set-form-editable
  [db value]
  (assoc db form-editable-key value))

(re-frame/reg-event-fx
  ::set-form-editable
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ value]]
    {:db (set-form-editable db value)}))

;; Class data

(re-frame/reg-sub
  ::class-data
  :<- [path-to-db]
  :class-data)

(defn- set-class-data
  [db class-data]
  (assoc db :class-data class-data))

(re-frame/reg-event-fx
  ::set-class-data
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ data]]
    {:db (-> db
             (set-class-data  data)
             (set-form-editable false))}))

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [class-id]}]]
    {:db       (assoc db :class-id class-id)
     :dispatch [::warehouse/load-class {:class-id class-id}
                {:on-success [::load-class-success]}]}))

(re-frame/reg-event-fx
  ::load-class-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [class]}]]
    (let [class-data (select-keys class [:course-id :name])]
      {:db (set-class-data db class-data)})))
