(ns webchange.admin.pages.add-school.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.routes :as routes]))

(re-frame/reg-event-fx
  ::open-school-list
  (fn [{:keys [_]} [_]]
    {:dispatch [::routes/redirect :schools]}))

(re-frame/reg-event-fx
  ::open-school-profile
  (fn [{:keys [_]} [_ {:keys [id]}]]
    {:dispatch [::routes/redirect :school-profile :school-id id]}))
