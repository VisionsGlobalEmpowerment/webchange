(ns webchange.editor-v2.translator.translator-form.state.form
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor.events :as editor]
    [webchange.editor-v2.translator.translator-form.state.actions :as translator-form.actions]
    [webchange.editor-v2.translator.translator-form.state.audios :as translator-form.audios]
    [webchange.editor-v2.translator.translator-form.state.concepts :as translator-form.concepts]
    [webchange.editor-v2.translator.translator-form.state.scene :as translator-form.scene]))

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
  (fn [{:keys [_]} [_]]
    {:dispatch-n (list [::translator-form.scene/init-state]
                       [::translator-form.concepts/init-state]
                       [::translator-form.audios/init-state])}))

(re-frame/reg-event-fx
  ::reset-state
  (fn [{:keys [_]} [_]]
    {:dispatch-n (list [::translator-form.actions/reset-state]
                       [::translator-form.concepts/reset-state])}))

(re-frame/reg-event-fx
  ::save-changes
  (fn [{:keys [db]} [_]]
    (let [concepts (translator-form.concepts/concepts-data db)
          edited-concepts-ids (translator-form.concepts/edited-concepts db)
          edited-concepts (select-keys concepts edited-concepts-ids)

          scene-id (translator-form.scene/scene-id db)
          actions (translator-form.scene/actions-data db)
          assets (translator-form.scene/assets-data db)]
      {:dispatch-n (->> edited-concepts
                        (map (fn [[id {:keys [data]}]] [::editor/update-dataset-item id data]))
                        (concat (list [::editor/reset-scene-actions scene-id actions]
                                      [::editor/reset-scene-assets scene-id assets]
                                      [::editor/save-current-scene scene-id])))})))
