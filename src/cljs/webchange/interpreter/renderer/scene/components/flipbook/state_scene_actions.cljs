(ns webchange.interpreter.renderer.scene.components.flipbook.state-scene-actions
  (:require
    [re-frame.core :as re-frame]
    [webchange.common.events :as ce]))

(ce/reg-simple-executor :flipbook-init ::execute-flipbook-init)
(ce/reg-simple-executor :flipbook-read-cover ::execute-flipbook-read-cover)
(ce/reg-simple-executor :flipbook-flip-forward ::execute-flipbook-flip-forward)
(ce/reg-simple-executor :flipbook-flip-backward ::execute-flipbook-flip-backward)
(ce/reg-simple-executor :flipbook-read-left ::execute-flipbook-read-left)
(ce/reg-simple-executor :flipbook-read-right ::execute-flipbook-read-right)

(re-frame/reg-cofx
  :flipbook-metadata
  (fn [{:keys [db] :as co-effects}]
    (let [scene-data (ce/get-scene-data db)
          flipbook-metadata (get-in scene-data [:metadata :flipbook])]
      (assoc co-effects :flipbook-metadata flipbook-metadata))))

;; Init

(defn- read-page?
  [action metadata]
  (cond
    (contains? action :read) (get action :read)
    (contains? metadata :read-page?) (get metadata :read-page?)
    :default true))

(re-frame/reg-event-fx
  ::execute-flipbook-init
  (fn [{:keys [db]} [_ {:keys [target] :as action}]]
    (let [scene-id (:current-scene db)
          component-wrapper @(get-in db [:transitions scene-id target])]
      {:flipbook-init {:component-wrapper component-wrapper
                       :on-end            #(ce/dispatch-success-fn action)}})))

(re-frame/reg-fx
  :flipbook-init
  (fn [{:keys [component-wrapper on-end]}]
    ((:init component-wrapper) {:on-end on-end})))

(re-frame/reg-event-fx
  ::execute-flipbook-read-cover
  [(re-frame/inject-cofx :flipbook-metadata)]
  (fn [{:keys [db flipbook-metadata]} [_ {:keys [target] :as action}]]
    (let [scene-id (:current-scene db)
          component-wrapper @(get-in db [:transitions scene-id target])
          read? (read-page? action flipbook-metadata)]
      {:flipbook-read-cover {:component-wrapper component-wrapper
                             :read              read?
                             :on-end            #(ce/dispatch-success-fn action)}})))

(re-frame/reg-fx
  :flipbook-read-cover
  (fn [{:keys [component-wrapper read on-end]}]
    ((:read-cover component-wrapper) {:read   read
                                      :on-end on-end})))

;; Flip forward

(re-frame/reg-event-fx
  ::execute-flipbook-flip-forward
  [(re-frame/inject-cofx :flipbook-metadata)]
  (fn [{:keys [db flipbook-metadata]} [_ {:keys [target] :as action}]]
    (let [scene-id (:current-scene db)
          component-wrapper @(get-in db [:transitions scene-id target])
          read? (read-page? action flipbook-metadata)]
      {:flipbook-flip-forward {:component-wrapper component-wrapper
                               :read              read?
                               :on-end            #(ce/dispatch-success-fn action)}})))

(re-frame/reg-fx
  :flipbook-flip-forward
  (fn [{:keys [component-wrapper read on-end]}]
    ((:flip-forward component-wrapper) {:read   read
                                        :on-end on-end})))

;; Flip backward

(re-frame/reg-event-fx
  ::execute-flipbook-flip-backward
  [(re-frame/inject-cofx :flipbook-metadata)]
  (fn [{:keys [db flipbook-metadata]} [_ {:keys [target] :as action}]]
    (let [scene-id (:current-scene db)
          component-wrapper @(get-in db [:transitions scene-id target])
          read? (read-page? action flipbook-metadata)]
      {:flipbook-flip-backward {:component-wrapper component-wrapper
                                :read              read?
                                :on-end            #(ce/dispatch-success-fn action)}})))

(re-frame/reg-fx
  :flipbook-flip-backward
  (fn [{:keys [component-wrapper read on-end]}]
    ((:flip-backward component-wrapper) {:read   read
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
