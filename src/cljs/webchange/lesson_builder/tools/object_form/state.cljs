(ns webchange.lesson-builder.tools.object-form.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.interpreter.renderer.state.editor :as editor-state]
    [webchange.interpreter.renderer.state.scene :as state-renderer]
    [webchange.lesson-builder.stage-actions :as stage]
    [webchange.lesson-builder.layout.stage.state :as stage-state]
    [webchange.lesson-builder.layout.menu.state :as menu-state]
    [webchange.utils.flipbook :as flipbook-utils]
    [webchange.lesson-builder.state-flipbook-screenshot :as flipbook-screenshot]
    [webchange.lesson-builder.widgets.confirm.state :as confirm]))

(def path-to-db :lesson-builder/object-form)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

(defn- secondary-object-names
  [objects main-object-name]
  (->> (get-in objects [main-object-name :metadata :links])
       (filter #(= (:type %) "secondary"))
       (map :id)
       (map keyword)))

(defn- secondary-objects
  [activity-data main-object-name]
  (select-keys (:objects activity-data) (-> activity-data
                                            :objects
                                            (secondary-object-names main-object-name))))

(re-frame/reg-event-fx
  ::init-object
  [(re-frame/inject-cofx :activity-data)
   (i/path path-to-db)]
  (fn [{:keys [db activity-data]} [_ object-name]]
    (when-not (-> db (get-in [:targets object-name :objects object-name]) some?)
      (let [objects (merge {object-name (get-in activity-data [:objects object-name])}
                           (secondary-objects activity-data object-name))]
        {:db (update-in db [:targets object-name :objects] merge objects)}))))

(re-frame/reg-event-fx
  ::init-group
  [(re-frame/inject-cofx :activity-data)
   (i/path path-to-db)]
  (fn [{:keys [db activity-data]} [_ object-name {:keys [children]}]]
    (let [objects (->> children
                       (map keyword)
                       (select-keys (:objects activity-data)))]
      {:db (update-in db [:targets object-name :objects] merge objects)})))

(re-frame/reg-sub
  ::target
  :<- [path-to-db]
  (fn [db]
    (get db :target)))

(re-frame/reg-sub
  ::object-data
  :<- [path-to-db]
  (fn [db [_ object-name]]
    (let [target (:target db)]
      (get-in db [:targets target :objects object-name]))))

(re-frame/reg-event-fx
  ::change-object
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ object-name data-patch]]
    (let [target (:target db)]
      (if-let [secondary-object-name (-> db
                                         (get-in [:targets target :objects])
                                         (secondary-object-names object-name)
                                         first)]
        {:db       (-> db
                       (update-in [:targets target :objects object-name] merge data-patch)
                       (update-in [:targets target :objects secondary-object-name] merge data-patch))
         :dispatch-n [[::state-renderer/set-scene-object-state object-name data-patch]
                      [::state-renderer/set-scene-object-state secondary-object-name data-patch]]}
        {:db       (update-in db [:targets target :objects object-name] merge data-patch)
         :dispatch [::state-renderer/set-scene-object-state object-name data-patch]}))))

(re-frame/reg-event-fx
  ::add-asset
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ asset]]
    (let [target (:target db)]
      {:db (update-in db [:targets target :assets] conj asset)})))

(re-frame/reg-event-fx
  ::change-activity-data-object
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ object-name data-patch]]
    (let [target (:target db)]
      (if-let [secondary-object-name (-> db
                                         (get-in [:targets target :objects])
                                         (secondary-object-names object-name)
                                         first)]
        {:db       (-> db
                       (update-in [:targets target :objects (keyword object-name)] merge data-patch)
                       (update-in [:targets target :objects secondary-object-name] merge data-patch))}
        {:db       (update-in db [:targets target :objects (keyword object-name)] merge data-patch)}))))

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
    (let [target (:target db)
          state (-> db
                    (get-in [:targets target :objects object-name])
                    (select-keys [:src :scale]))
          tag (-> db (get-in [:targets target :objects object-name :editable? :edit-tags]) (first))
          object-keys (get-object-keys-by-tag activity-data tag)
          objects (as-> (:objects activity-data) o
                        (select-keys o object-keys)
                        (map (fn [[key object]]
                               [key (merge object state)]) o)
                        (into {}))
          render-events (map (fn [object-key]
                               [::state-renderer/set-scene-object-state object-key state])
                             object-keys)]
      {:db         (update-in db [:targets target :objects] merge objects)
       :dispatch-n render-events})))

(editor-state/register-select-object-handler
  "main"
  :main-editor-frame
  [::show-object-form])

(editor-state/register-update-object-handler
  :main-editor-frame
  [::change-activity-data-object])

(re-frame/reg-event-fx
  ::show-object-form
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ target]]
    {:db       (-> db (assoc :target target))
     :dispatch-n [[::menu-state/open-component :object-form]
                  [::init-object target]]}))

(re-frame/reg-event-fx
  ::apply
  [(re-frame/inject-cofx :activity-data)
   (i/path path-to-db)]
  (fn [{:keys [db activity-data]} [_ object-name]]
    (let [objects (get-in db [:targets object-name :objects])
          assets (get-in db [:targets object-name :assets])
          flipbook? (flipbook-utils/flipbook-activity? activity-data)]
      {:dispatch-n [[::stage/update-objects {:objects objects :assets assets}]
                    [::editor-state/deselect-object]
                    [::menu-state/history-back]
                    (when flipbook?
                      [::flipbook-screenshot/take-current-stage-screenshot])]})))

(re-frame/reg-event-fx
  ::reset
  [(re-frame/inject-cofx :activity-data)
   (i/path path-to-db)]
  (fn [{:keys [db activity-data]} [_ object-name]]
    (let [objects (get-in db [:targets object-name :objects])
          has-changes? (-> activity-data :objects (#(select-keys % (keys objects))) (not= objects))]
      {:db (update db :targets dissoc object-name)
       :dispatch-n [(when has-changes?
                      [::stage-state/reset])]})))

(re-frame/reg-event-fx
  ::confirm-close
  [(re-frame/inject-cofx :activity-data)
   (i/path path-to-db)]
  (fn [{:keys [db activity-data]} [_ object-name]]
    (let [objects (get-in db [:targets object-name :objects])
          has-changes? (-> activity-data :objects (#(select-keys % (keys objects))) (not= objects))]
      (if has-changes?
        {:dispatch [::confirm/show-confirm-window {:message "Do you want to apply or discard changes?"
                                                   :title "You have changed the form"
                                                   :on-confirm [::apply object-name]
                                                   :on-cancel [::reset object-name]
                                                   :confirm-text "Apply"
                                                   :cancel-text "Discard"}]}
        {:db (update db :targets dissoc object-name)}))))

(comment
  @(re-frame/subscribe [path-to-db]))
