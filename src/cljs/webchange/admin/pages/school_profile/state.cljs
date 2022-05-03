(ns webchange.admin.pages.school-profile.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.admin.routes :as routes]
    [webchange.state.warehouse :as warehouse]
    [webchange.validation.specs.school-spec :as school-spec]
    [webchange.validation.validate :refer [validate]]))

(def path-to-db :school-profile)

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
             (dissoc :school-data))
     :dispatch [::warehouse/load-school {:school-id school-id}
                {:on-success [::load-school-success]}]}))

(re-frame/reg-event-fx
  ::load-school-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [school]}]]
    {:db (assoc db :school-data school)}))

(re-frame/reg-sub
  ::school-data
  :<- [path-to-db]
  #(get % :school-data))

(re-frame/reg-event-fx
  ::edit-school
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ school-data]]
    (let [school-id (:school-id db)
          _ (js/console.log "edit" school-data)
          validation-errors (validate ::school-spec/edit-school school-data)]
      (if (nil? validation-errors)
        {:db (assoc db :errors nil)
         :dispatch [::warehouse/edit-school {:school-id school-id :data school-data}
                    {:on-success [::edit-school-success]}]}
        {:db (assoc db :errors validation-errors)}))))

(re-frame/reg-event-fx
  ::edit-school-success
  (fn [{:keys [_]} [_]]
    {:dispatch [::routes/redirect :schools]}))
