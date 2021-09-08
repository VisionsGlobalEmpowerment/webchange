(ns webchange.editor-v2.activity-form.common.object-form.components.scale.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.activity-form.common.object-form.state :as state]))

(re-frame/reg-sub
  ::show-flip-control?
  (fn [[_ id]]
    (re-frame/subscribe [::state/form-component-available? id :flip]))
  (fn [show-control?]
    show-control?))

(re-frame/reg-sub
  ::show-scale-control?
  (fn [[_ id]]
    (re-frame/subscribe [::state/form-component-available? id :scale]))
  (fn [show-control?]
    show-control?))

;; Scale

(defn- get-current-scale
  [db id]
  (-> (state/get-current-data db id)
      (get :scale)))

(re-frame/reg-sub
  ::current-scale
  (fn [[_ id]]
    {:pre [(some? id)]}
    [(re-frame/subscribe [::state/current-data id])])
  (fn [[current-data]]
    (let [{:keys [x y]} (get current-data :scale)]
      (or (Math/abs x) (Math/abs y) 1))))

(re-frame/reg-event-fx
  ::set-scale
  (fn [{:keys [db]} [_ id scale-value]]
    (let [{:keys [x]} (get-current-scale db id)
          x-sign (/ x (Math/abs x))]
      {:dispatch [::state/update-current-data id {:scale {:x (* scale-value x-sign)
                                                          :y scale-value}}]})))

;; Flip

(re-frame/reg-event-fx
  ::flip-animation
  (fn [{:keys [db]} [_ id]]
    (let [{:keys [x y]} (get-current-scale db id)]
      (print "current-scale" id (get-current-scale db id))
      {:dispatch [::state/update-current-data id {:scale {:x (- x)
                                                          :y y}}]})))
