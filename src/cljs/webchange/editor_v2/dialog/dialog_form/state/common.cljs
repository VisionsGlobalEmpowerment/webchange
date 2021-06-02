(ns webchange.editor-v2.dialog.dialog-form.state.common
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.dialog.dialog-form.state.actions :as actions]
    [webchange.editor-v2.dialog.dialog-form.state.actions-utils :as utils]
    [webchange.editor-v2.dialog.dialog-form.state.concepts :as concepts]))

(re-frame/reg-event-fx
  ::remove-action
  (fn [{:keys [_]} [_ {:keys [concept-action? node-data]}]]
    (let [in-concept? (and concept-action? (utils/delete-in-concept-available? node-data))]
      {:dispatch [(if in-concept?
                    ::concepts/delete-phrase-in-concept-action
                    ::actions/delete-phrase-action)
                  node-data]})))
