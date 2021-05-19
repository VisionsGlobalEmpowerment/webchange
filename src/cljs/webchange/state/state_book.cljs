(ns webchange.state.state-book
  (:require
    [re-frame.core :as re-frame]
    [webchange.interpreter.renderer.state.scene :as state-scene]
    [webchange.state.state :as state]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:book])
       (state/path-to-db)))

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
  ::current-stage-idx
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
    (let [objects (get-in db [:current-scene-data :scene-objects])
          stage (get-in db [:current-scene-data :metadata :stages idx])
          visible? (fn [name] (some #{name} (:objects stage)))]
      {:db (assoc-in db (path-to-db [:current-stage]) idx)
       :dispatch-n (->> objects
                        flatten
                        (map (fn [object-name]
                               [::state-scene/change-scene-object (keyword object-name) [[:set-visibility {:visible (visible? object-name)}]]])))})))
