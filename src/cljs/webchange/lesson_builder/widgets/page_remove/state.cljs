(ns webchange.lesson-builder.widgets.page-remove.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.state :as state]
    [webchange.lesson-builder.state-flipbook :as flipbook-state]
    [webchange.utils.flipbook :as flipbook-utils]))

(re-frame/reg-sub
  ::page-idx
  :<- [::state/activity-data]
  :<- [::flipbook-state/current-stage]
  (fn [[activity-data current-stage-idx] [_ side]]
    (let [pages-idx (-> (flipbook-utils/get-stage-data activity-data current-stage-idx)
                        (get :pages-idx []))]
      (case side
        "left" (first pages-idx)
        "right" (last pages-idx)))))

(re-frame/reg-sub
  ::page-removable?
  (fn [[_ side]]
    [(re-frame/subscribe [::state/activity-data])
     (re-frame/subscribe [::page-idx side])])
  (fn [[activity-data page-idx]]
    (-> (flipbook-utils/get-page-data activity-data page-idx)
        (get :removable? false)
        (not))))

(re-frame/reg-event-fx
  ::remove-page
  (fn [{:keys [_]} [_ page-idx]]
    {:dispatch [::flipbook-state/remove-page page-idx]}))
