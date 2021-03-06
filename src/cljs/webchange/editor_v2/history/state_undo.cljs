(ns webchange.editor-v2.history.state-undo
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.history.state :as history]
    [webchange.editor-v2.translator.translator-form.state.concepts :as concepts]
    [webchange.editor-v2.translator.translator-form.state.scene :as scene]))

(re-frame/reg-event-fx
  ::apply-undo
  (fn [{:keys [db]} [_]]
    (let [{:keys [concept-id type path from]} (-> db (history/history) (last))
          options {:suppress-history? true}]
      {:dispatch-n (list (case type
                           :scene-action [::scene/update-action path from options]
                           :scene-object [::scene/update-object path from options]
                           :concept-action [::concepts/update-concept concept-id path from options])
                         [::history/remove-history-event])})))
