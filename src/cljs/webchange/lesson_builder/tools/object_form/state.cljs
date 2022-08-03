(ns webchange.lesson-builder.tools.object-form.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.interpreter.renderer.state.editor :as editor-state]
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
  ::object
  :<- [path-to-db]
  (fn [db [_ object-name]]
    (get-in db [:objects object-name])))

(editor-state/register-select-object-handler
 :show-object-form
 [::show-object-form])

(re-frame/reg-event-fx
  ::show-object-form
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ target]]
    {:db (-> db
             (assoc :target target)
             (assoc :objects {}))
     :dispatch-n [[::menu-state/set-current-tab :scene-layers]
                  [::menu-state/set-current-component :object-form]]}))

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
        {:dispatch [::stage-state/reset]}))))

(comment
  @(re-frame/subscribe [path-to-db]))
