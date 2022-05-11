(ns webchange.admin.pages.teacher-profile.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.admin.routes :as routes]
    [webchange.state.warehouse :as warehouse]
    [webchange.validation.specs.teacher :as teacher-spec]
    [webchange.validation.validate :refer [validate]]))

(def path-to-db :teacher-profile)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

(re-frame/reg-sub
  ::errors
  :<- [path-to-db]
  (fn [data]
    (get data :errors)))

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [school-id teacher-id]}]]
    {:db (-> db
             (assoc :teacher-id teacher-id)
             (assoc :school-id school-id)
             (dissoc :teacher)
             (assoc :errors {}))
     :dispatch [::warehouse/load-teacher {:teacher-id teacher-id}
                {:on-success [::load-teacher-success]}]}))

(re-frame/reg-event-fx
  ::load-teacher-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [teacher]}]]
    {:db (assoc db :teacher {:first-name (-> teacher :user :first-name)
                             :last-name (-> teacher :user :last-name)
                             :type (:type teacher)})}))

(re-frame/reg-sub
  ::teacher
  :<- [path-to-db]
  (fn [data]
    (get data :teacher)))

(re-frame/reg-event-fx
  ::edit-teacher
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ data]]
    (let [teacher-id (:teacher-id db)
          validation-errors (validate ::teacher-spec/edit-teacher data)]
      (if (nil? validation-errors)
        {:db (assoc db :errors nil)
         :dispatch [::warehouse/edit-teacher {:teacher-id teacher-id :data data}
                    {:on-success [::edit-teacher-success]}]}
        {:db (assoc db :errors validation-errors)}))))

(re-frame/reg-event-fx
  ::edit-teacher-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [school-id (:school-id db)]
      {:db (dissoc db :teacher)
       :dispatch [::routes/redirect :teachers :school-id school-id]})))
