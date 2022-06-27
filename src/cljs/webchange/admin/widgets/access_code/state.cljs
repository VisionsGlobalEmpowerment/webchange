(ns webchange.admin.widgets.access-code.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.state.warehouse :as warehouse]))

(re-frame/reg-event-fx
  ::generate-access-code
  (fn [{:keys [_]} [_ school-id {:keys [on-success]}]]
    {:dispatch [::warehouse/generate-school-access-code
                {:school-id school-id}
                {:on-success [::generate-access-code-success on-success]}]}))

(re-frame/reg-event-fx
  ::generate-access-code-success
  (fn [{:keys [_]} [_ on-success {:keys [access-code]}]]
    {::callback [on-success access-code]}))

(re-frame/reg-fx
  ::callback
  (fn [[callback & params]]
    (when (fn? callback)
      (apply callback params))))
