(ns webchange.admin.pages.add-school.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.admin.routes :as routes]
    [webchange.state.warehouse :as warehouse]
    [webchange.validation.specs.school-spec :as school-spec]
    [webchange.validation.validate :refer [validate]]))

(def path-to-db :add-school)

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
  (fn [{:keys [db]} [_]]
    {:db (-> db
             (assoc :errors {}))}))

(re-frame/reg-event-fx
  ::create-school
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ school-data]]
    (let [validation-errors (validate ::school-spec/create-school school-data)]
      (if (nil? validation-errors)
        {:db (assoc db :errors nil)
         :dispatch [::warehouse/create-school {:data school-data}
                    {:on-success [::create-school-success]}]}
        {:db (assoc db :errors validation-errors)}))))

(re-frame/reg-event-fx
  ::create-school-success
  (fn [{:keys [_]} [_]]
    {:dispatch [::routes/redirect :schools]}))
