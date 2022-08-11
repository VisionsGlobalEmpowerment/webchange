(ns webchange.lesson-builder.tools.question-form.question-option.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.lesson-builder.tools.question-form.state :as state]))

(re-frame/reg-sub
  ::object-data
  :<- [::state/activity-data]
  :<- [::state/current-object]
  (fn [[activity-data current-object]]
    (get-in activity-data [:objects current-object])))

(re-frame/reg-sub
  ::form-param
  :<- [::object-data]
  (fn [object-data]
    (-> object-data
        (get-in [:metadata :question-form-param])
        (keyword))))

(re-frame/reg-event-fx
  ::handle-data-change
  [(i/path state/path-to-db)]
  (fn [{:keys [db]} [_ param-name value]]
    {:db (cond-> db
                 (some? value) (state/update-form-data param-name value))}))
