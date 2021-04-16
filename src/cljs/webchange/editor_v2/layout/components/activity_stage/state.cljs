(ns webchange.editor-v2.layout.components.activity-stage.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.layout.state :refer [path-to-db]]
    [webchange.interpreter.renderer.state.scene :as scene]
    [webchange.state.state :as state]))

(defn current-stage
  [db]
  (get-in db (path-to-db [:current-stage])))

(re-frame/reg-sub
  ::current-stage
  (fn [db]
    (current-stage db)))

(defn scene-stages
  [db scene-id]
  (get-in db [:scenes scene-id :metadata :stages] []))

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

(defn- next-stage-available?
  [db]
  (let [scene-id (:current-scene db)
        stages-count (-> (scene-stages db scene-id) (count))
        current-stage-idx (current-stage db)]
    (< current-stage-idx (dec stages-count))))

(re-frame/reg-sub
  ::next-stage-available?
  (fn [db]
    (next-stage-available? db)))

(re-frame/reg-event-fx
  ::select-next-stage
  (fn [{:keys [db]} [_]]
    (let [current-stage-idx (current-stage db)]
      (cond-> {}
              (next-stage-available? db) (assoc :dispatch [::select-stage (inc current-stage-idx)])))))

(defn- prev-stage-available?
  [db]
  (let [current-stage-idx (current-stage db)]
    (> current-stage-idx 0)))

(re-frame/reg-sub
  ::prev-stage-available?
  (fn [db]
    (prev-stage-available? db)))

(re-frame/reg-event-fx
  ::select-prev-stage
  (fn [{:keys [db]} [_]]
    (let [current-stage-idx (current-stage db)]
      (cond-> {}
              (> current-stage-idx 0) (assoc :dispatch [::select-stage (dec current-stage-idx)])))))

(re-frame/reg-event-fx
  ::select-flipbook-stage
  (fn [{:keys [db]} [_ idx]]
    (let [stage (-> (get-in db [:current-scene-data :metadata])
                    (get-in [:stages idx]))]
      {:db       (assoc-in db (path-to-db [:current-stage]) idx)
       :dispatch [::show-flipbook-stage (:idx stage)]})))

(re-frame/reg-event-fx
  ::show-flipbook-stage
  (fn [{:keys [db]} [_ spread-index {:keys [hide-generated-pages?]}]]
    (let [metadata (state/scene-metadata db)
          book-name (get metadata :flipbook-name)
          scene-id (:current-scene db)
          component-wrapper @(get-in db [:transitions scene-id book-name])]
      {:flipbook-show-spread {:component-wrapper     component-wrapper
                              :spread-idx            spread-index
                              :hide-generated-pages? hide-generated-pages?}})))

(re-frame/reg-fx
  :flipbook-show-spread
  (fn [{:keys [component-wrapper spread-idx hide-generated-pages?]}]
    ((:show-spread component-wrapper) spread-idx {:hide-generated-pages? hide-generated-pages?})))
