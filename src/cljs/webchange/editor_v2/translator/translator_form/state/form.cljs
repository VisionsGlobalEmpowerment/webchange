(ns webchange.editor-v2.translator.translator-form.state.form
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor.events :as editor]
    [webchange.editor-v2.translator.translator-form.state.db :as db]
    [webchange.editor-v2.translator.translator-form.state.audios :as translator-form.audios]
    [webchange.editor-v2.translator.translator-form.state.concepts :as translator-form.concepts]
    [webchange.editor-v2.translator.translator-form.state.scene :as translator-form.scene]
    [webchange.editor-v2.translator.translator-form.state.window-confirmation]))

;; Subs

(re-frame/reg-sub
  ::has-changes
  (fn []
    [(re-frame/subscribe [::translator-form.concepts/edited-concepts])
     (re-frame/subscribe [::translator-form.scene/has-changes])])
  (fn [[edited-concepts scene-has-changes]]
    (or scene-has-changes
        (not (empty? edited-concepts)))))

;; Events

(re-frame/reg-event-fx
  ::init-state
  (fn [{:keys [_]} [_ {:keys [components]}]]
    {:dispatch-n (list [::translator-form.scene/init-state]
                       [::translator-form.concepts/init-state]
                       [::translator-form.audios/init-state]
                       [::set-components-settings components])}))

(re-frame/reg-event-fx
  ::reset-state
  (fn [{:keys [db]} [_]]
    {:db (update-in db (db/path-to-parent-db) dissoc db/db-key)}))

(re-frame/reg-event-fx
  ::save-changes
  (fn [{:keys [db]} [_]]
    (let [concepts (translator-form.concepts/concepts-patch-data db)
          edited-concepts-ids (translator-form.concepts/edited-concepts db)
          edited-concepts (select-keys concepts edited-concepts-ids)
          current-dataset (translator-form.concepts/current-dataset-concept db)
          current-dataset-patch-data (translator-form.concepts/current-dataset-concept-patch-data db)
          scene-id (translator-form.scene/scene-id db)
          actions (translator-form.scene/actions-data db)
          assets (translator-form.scene/assets-data db)
          objects (translator-form.scene/objects-data db)
          metadata (translator-form.scene/metadata-data db)]
      {:dispatch-n         (->> edited-concepts
                                (map (fn [[id data]] [::editor/update-dataset-item id data]))
                                (concat (list [::editor/reset-scene-actions scene-id actions]
                                              [::editor/reset-scene-assets scene-id assets]
                                              [::editor/reset-scene-objects scene-id objects]
                                              [::editor/reset-scene-metadata scene-id metadata]
                                              [::editor/save-current-scene scene-id]
                                              [::translator-form.scene/reset-changes]
                                              (when (not-empty edited-concepts-ids)
                                                [::editor/edit-dataset (:id current-dataset) current-dataset-patch-data ]))))})))

(re-frame/reg-event-fx
  ::set-components-settings
  (fn [{:keys [db]} [_ settings]]
    {:db (assoc-in db (db/path-to-db [:settings]) settings)}))

(re-frame/reg-sub
  ::components-settings
  (fn [db [_ component]]
    (get-in db (db/path-to-db [:settings component]))))
