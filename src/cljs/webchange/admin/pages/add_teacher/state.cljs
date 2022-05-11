(ns webchange.admin.pages.add-teacher.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.admin.routes :as routes]
    [webchange.state.warehouse :as warehouse]
    [webchange.validation.specs.teacher :as teacher-spec]
    [webchange.validation.validate :refer [validate]]))

(def path-to-db :add-teacher)

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
  (fn [{:keys [db]} [_ {:keys [school-id]}]]
    {:db (-> db
             (assoc :school-id school-id)
             (assoc :errors {}))}))

(re-frame/reg-event-fx
  ::create-teacher
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ data]]
    (let [school-id (:school-id db)
          validation-errors (validate ::teacher-spec/create-teacher data)]
      (if (nil? validation-errors)
        {:db (assoc db :errors nil)
         :dispatch [::warehouse/create-teacher {:school-id school-id :data data}
                    {:on-success [::create-teacher-success]}]}
        {:db (assoc db :errors validation-errors)}))))

(re-frame/reg-event-fx
  ::create-teacher-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [school-id (:school-id db)]
      {:dispatch [::routes/redirect :teachers :school-id school-id]})))
