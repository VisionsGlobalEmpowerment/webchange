(ns webchange.interpreter.renderer.scene.components.flipbook.state-scene-actions
  (:require
    [re-frame.core :as re-frame]
    [webchange.common.events :as ce]))

(ce/reg-simple-executor :flipbook-init ::execute-flipbook-init)
(ce/reg-simple-executor :flipbook-flip-forward ::execute-flipbook-flip-forward)
(ce/reg-simple-executor :flipbook-flip-backward ::execute-flipbook-flip-backward)

;; Init

(re-frame/reg-event-fx
  ::execute-flipbook-init
  (fn [{:keys [db]} [_ action]]
    (let [scene-id (:current-scene db)
          component-wrapper @(get-in db [:transitions scene-id (:target action)])]
      {:flipbook-init {:component-wrapper component-wrapper}
       :dispatch      (ce/success-event action)})))

(re-frame/reg-fx
  :flipbook-init
  (fn [{:keys [component-wrapper]}]
    ((:init component-wrapper))))

;; Flip forward

(re-frame/reg-event-fx
  ::execute-flipbook-flip-forward
  (fn [{:keys [db]} [_ action]]
    (let [scene-id (:current-scene db)
          component-wrapper @(get-in db [:transitions scene-id (:target action)])]
      {:flipbook-flip-forward {:component-wrapper component-wrapper
                               :on-end            #(ce/dispatch-success-fn action)}})))

(re-frame/reg-fx
  :flipbook-flip-forward
  (fn [{:keys [component-wrapper on-end]}]
    ((:flip-forward component-wrapper) {:on-end on-end})))

;; Flip backward

(re-frame/reg-event-fx
  ::execute-flipbook-flip-backward
  (fn [{:keys [db]} [_ action]]
    (let [scene-id (:current-scene db)
          component-wrapper @(get-in db [:transitions scene-id (:target action)])]
      {:flipbook-flip-backward {:component-wrapper component-wrapper
                                :on-end            #(ce/dispatch-success-fn action)}})))

(re-frame/reg-fx
  :flipbook-flip-backward
  (fn [{:keys [component-wrapper on-end]}]
    ((:flip-backward component-wrapper) {:on-end on-end})))
