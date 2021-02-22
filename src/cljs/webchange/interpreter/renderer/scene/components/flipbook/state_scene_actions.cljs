(ns webchange.interpreter.renderer.scene.components.flipbook.state-scene-actions
  (:require
    [re-frame.core :as re-frame]
    [webchange.common.events :as ce]))

(ce/reg-simple-executor :flipbook-init ::execute-flipbook-init)
(ce/reg-simple-executor :flipbook-flip-forward ::execute-flipbook-flip-forward)
(ce/reg-simple-executor :flipbook-flip-backward ::execute-flipbook-flip-backward)
(ce/reg-simple-executor :flipbook-read-left ::execute-flipbook-read-left)
(ce/reg-simple-executor :flipbook-read-right ::execute-flipbook-read-right)

;; Init

(re-frame/reg-event-fx
  ::execute-flipbook-init
  (fn [{:keys [db]} [_ {:keys [target read] :or {read true} :as action}]]
    (let [scene-id (:current-scene db)
          component-wrapper @(get-in db [:transitions scene-id target])]
      {:flipbook-init {:component-wrapper component-wrapper
                       :read read
                       :on-end #(ce/dispatch-success-fn action)}})))

(re-frame/reg-fx
  :flipbook-init
  (fn [{:keys [component-wrapper read on-end]}]
    ((:init component-wrapper) {:read   read
                                :on-end on-end})))

;; Flip forward

(re-frame/reg-event-fx
  ::execute-flipbook-flip-forward
  (fn [{:keys [db]} [_ {:keys [target read] :or {read true} :as action}]]
    (let [scene-id (:current-scene db)
          component-wrapper @(get-in db [:transitions scene-id target])]
      {:flipbook-flip-forward {:component-wrapper component-wrapper
                               :read              read
                               :on-end            #(ce/dispatch-success-fn action)}})))

(re-frame/reg-fx
  :flipbook-flip-forward
  (fn [{:keys [component-wrapper read on-end]}]
    ((:flip-forward component-wrapper) {:read read
                                        :on-end on-end})))

;; Flip backward

(re-frame/reg-event-fx
  ::execute-flipbook-flip-backward
  (fn [{:keys [db]} [_ {:keys [target read] :or {read true} :as action}]]
    (let [scene-id (:current-scene db)
          component-wrapper @(get-in db [:transitions scene-id target])]
      {:flipbook-flip-backward {:component-wrapper component-wrapper
                                :read              read
                                :on-end            #(ce/dispatch-success-fn action)}})))

(re-frame/reg-fx
  :flipbook-flip-backward
  (fn [{:keys [component-wrapper read on-end]}]
    ((:flip-backward component-wrapper) {:read read
                                         :on-end on-end})))

(re-frame/reg-event-fx
  ::execute-flipbook-read-left
  (fn [{:keys [db]} [_ {:keys [target] :as action}]]
    (let [scene-id (:current-scene db)
          {read-left :read-left} @(get-in db [:transitions scene-id target])]
      (read-left #(ce/dispatch-success-fn action)))))

(re-frame/reg-event-fx
  ::execute-flipbook-read-right
  (fn [{:keys [db]} [_ {:keys [target] :as action}]]
    (let [scene-id (:current-scene db)
          {read-right :read-right} @(get-in db [:transitions scene-id target])]
      (read-right #(ce/dispatch-success-fn action)))))
