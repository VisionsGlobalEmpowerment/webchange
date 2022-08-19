(ns webchange.lesson-builder.tools.object-form.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.interpreter.renderer.state.editor :as editor-state]
    [webchange.interpreter.renderer.state.scene :as state-renderer]
    [webchange.lesson-builder.stage-actions :as stage]
    [webchange.lesson-builder.blocks.stage.state :as stage-state]
    [webchange.lesson-builder.blocks.menu.state :as menu-state]))

(def path-to-db :lesson-builder/object-form)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

(re-frame/reg-event-fx
  ::init-object
  [(re-frame/inject-cofx :activity-data)
   (i/path path-to-db)]
  (fn [{:keys [db activity-data]} [_ object-name]]
    {:db (assoc-in db [:objects object-name] (get-in activity-data [:objects object-name]))}))

(re-frame/reg-event-fx
  ::init-group
  [(re-frame/inject-cofx :activity-data)
   (i/path path-to-db)]
  (fn [{:keys [db activity-data]} [_ {:keys [children]}]]
    (let [objects (->> children
                       (map keyword)
                       (select-keys (:objects activity-data)))]
      {:db (update db :objects merge objects)})))

(re-frame/reg-sub
  ::target
  :<- [path-to-db]
  (fn [db]
    (get db :target)))

(re-frame/reg-sub
  ::object-data
  :<- [path-to-db]
  (fn [db [_ object-name]]
    (get-in db [:objects object-name])))

(re-frame/reg-event-fx
  ::change-object
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ object-name data-patch]]
    {:db       (update-in db [:objects object-name] merge data-patch)
     :dispatch [::state-renderer/set-scene-object-state object-name data-patch]}))

(defn- get-object-keys-by-tag
  "Return object names for each object in scene with given tag"
  [activity-data tag]
  (let [objects (:objects activity-data)]
    (->> objects
         (filter (fn [[_ object]]
                   (some #{tag} (get-in object [:editable? :edit-tags]))))
         (map first))))

(re-frame/reg-event-fx
  ::apply-to-all
  [(re-frame/inject-cofx :activity-data)
   (i/path path-to-db)]
  (fn [{:keys [db activity-data]} [_ object-name]]
    (let [state (-> db
                    (get-in [:objects object-name])
                    (select-keys [:src :scale]))
          tag (-> db (get-in [:objects object-name :editable? :edit-tags]) (first))
          object-keys (get-object-keys-by-tag activity-data tag)
          objects (as-> (:objects activity-data) o
                        (select-keys o object-keys)
                        (map (fn [[key object]]
                               [key (merge object state)]) o)
                        (into {}))
          render-events (map (fn [object-key]
                               [::state-renderer/set-scene-object-state object-key state])
                             object-keys)]
      {:db         (update db :objects merge objects)
       :dispatch-n render-events})))

(editor-state/register-select-object-handler
  "main"
  :main-editor-frame
  [::show-object-form])

(re-frame/reg-event-fx
  ::show-object-form
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ target]]
    {:db       (-> db
                   (assoc :target target)
                   (assoc :objects {}))
     :dispatch [::menu-state/open-component :object-form]}))

(re-frame/reg-event-fx
  ::apply
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [objects (:objects db)]
      {:dispatch-n [[::stage/update-objects {:objects objects}]
                    [::menu-state/history-back]]})))

(re-frame/reg-event-fx
  ::reset
  [(re-frame/inject-cofx :activity-data)
   (i/path path-to-db)]
  (fn [{:keys [db activity-data]} [_]]
    (let [objects (:objects db)
          has-changes? (-> activity-data :objects (#(select-keys % (keys objects))) (not= objects))]
      (when has-changes?
        {:db       (assoc db :objects {})
         :dispatch [::stage-state/reset]}))))

(comment
  @(re-frame/subscribe [path-to-db]))
