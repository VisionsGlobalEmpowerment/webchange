(ns webchange.lesson-builder.tools.scene-layers.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.logger.index :as logger]
    [webchange.lesson-builder.state :as state]
    [webchange.lesson-builder.blocks.stage.state :as stage-state]
    [webchange.lesson-builder.stage-actions :as stage]
    [webchange.interpreter.renderer.state.editor :as editor-state]))

(def path-to-db :lesson-builder/scene-layers)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

(defn- get-object-type
  [object-data]
  (cond
    (get-in object-data [:metadata :uploaded-image?]) :uploaded-image
    (get-in object-data [:metadata :added-character?]) :added-character
    (get-in object-data [:metadata :question?]) :question
    :else (-> object-data :type keyword)))

(defn- remove-available?
  [object-data]
  (some #{(get-object-type object-data)}
        [:added-character :uploaded-image :question :anchor]))

(defn- get-layers
  [activity-data]
  (let [objects-data (get activity-data :objects)]
    (->> objects-data
         (filter (fn [[_ {:keys [editable? metadata]}]]
                   (when (some? editable?)
                     (logger/warn "Property 'editable?' is deprecated. Use 'metadata instead.'"))
                   (or (:show-in-tree? editable?)           ;; deprecated
                       (get-in metadata [:objects-tree :show?]))))
         (sort-by #(get-in (second %) [:metadata :objects-tree :sort-order]))
         (map (fn [[object-name {:keys [alias metadata type] :as object-data}]]
                (let [{:keys [display-name]} metadata
                      actions (or (get-in metadata [:objects-tree :actions])
                                  (cond-> ["visibility" "edit"]
                                          (remove-available? object-data) (conj "remove")))]
                  (when (some? alias)
                    (logger/warn "Property 'alias?' is deprecated. Use 'metadata.display-name instead.'"))
                  {:alias   (or display-name alias object-name)
                   :name    object-name
                   :object-type (get-object-type object-data)
                   :type    type
                   :actions actions
                   :visible (get object-data :visible true)})))
         (filter #(not= :question (:object-type %))))))

(re-frame/reg-event-fx
  ::init
  [(re-frame/inject-cofx :activity-data)
   (i/path path-to-db)]
  (fn [{:keys [db activity-data]} [_]]
    (let [layers (get-layers activity-data)]
      {:db (-> db
               (assoc :layers layers))})))

(re-frame/reg-sub
  ::layers
  :<- [::state/activity-data]
  #(->> (get-layers %)
        (repeat)
        (take 3)
        (flatten)))

(re-frame/reg-event-fx
  ::toggle-visibility
  [(i/path path-to-db)]
  (fn [{:keys [_]} [_ object-name]]
    {:dispatch [::stage/toggle-object-visibility object-name]}))

(re-frame/reg-event-fx
  ::remove-object
  [(re-frame/inject-cofx :activity-data)
   (i/path path-to-db)]
  (fn [{:keys [db activity-data]} [_ object-name]]
    (let [object-type (-> (get-in activity-data [:objects object-name])
                          (get-object-type))]
      {:dispatch [::state/remove-object {:object-name (name object-name)
                                         :object-type object-type
                                         :on-success [::stage-state/reset]}]})))

(re-frame/reg-event-fx
  ::edit-object
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ object-name]]
    {:dispatch [::editor-state/select-object object-name]}))
