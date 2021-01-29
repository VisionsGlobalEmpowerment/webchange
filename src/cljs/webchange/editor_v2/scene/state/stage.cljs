(ns webchange.editor-v2.scene.state.stage
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.scene.state.db :refer [path-to-db]]
    [webchange.interpreter.renderer.state.scene :as scene]))

(re-frame/reg-sub
  ::stage-options
  (fn [db]
    (let [scene-id (:current-scene db)
          data (get-in db [:scenes scene-id] {})]
      (if-let [stages (-> data (get-in [:metadata :stages]))]
        (map-indexed (fn [idx stage]
                       (assoc stage :idx idx))
                     stages)))))

(re-frame/reg-sub
  ::current-stage
  (fn [db]
    (get-in db (path-to-db [:current-stage]))))

(re-frame/reg-sub
  ::stage-key
  (fn [db]
    (get-in db (path-to-db [:stage-key]) "default")))

(re-frame/reg-event-fx
  ::reset-stage
  (fn [{:keys [db]} [_]]
    {:db (assoc-in db (path-to-db [:stage-key]) (-> (random-uuid) str))}))

(re-frame/reg-event-fx
  ::select-stage
  (fn [{:keys [db]} [_ idx]]
    (let [metadata (get-in db [:current-scene-data :metadata])]
      (if (contains? metadata :flipbook-name)
        {:dispatch [::select-flipbook-stage idx]}
        (let [objects (get-in db [:current-scene-data :scene-objects])
              stage (get-in db [:current-scene-data :metadata :stages idx])
              visible? (fn [name] (some #{name} (:objects stage)))]
          {:db         (assoc-in db (path-to-db [:current-stage]) idx)
           :dispatch-n (->> objects
                            flatten
                            (map (fn [object-name]
                                   [::scene/change-scene-object (keyword object-name) [[:set-visibility {:visible (visible? object-name)}]]])))})))))

(re-frame/reg-event-fx
  ::select-flipbook-stage
  (fn [{:keys [db]} [_ idx]]
    (let [metadata (get-in db [:current-scene-data :metadata])
          book-name (get metadata :flipbook-name)
          stage (get-in metadata [:stages idx])
          scene-id (:current-scene db)
          component-wrapper @(get-in db [:transitions scene-id book-name])]
      {:db                   (assoc-in db (path-to-db [:current-stage]) idx)
       :flipbook-show-spread {:component-wrapper component-wrapper
                              :spread-idx        (:idx stage)}})))

(re-frame/reg-fx
  :flipbook-show-spread
  (fn [{:keys [component-wrapper spread-idx]}]
    ((:show-spread component-wrapper) spread-idx)))
