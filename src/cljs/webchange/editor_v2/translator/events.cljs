(ns webchange.editor-v2.translator.events
  (:require
    [re-frame.core :as re-frame]
    [ajax.core :refer [json-request-format json-response-format]]
    [webchange.editor-v2.translator.db :refer [path-to-db]]
    [webchange.editor-v2.translator.translator-form.events :as translator-form-events]
    [webchange.editor-v2.translator.translator-form.audio-assets.events :as audio-assets]))

(re-frame/reg-event-fx
  ::open-translator-modal
  (fn [{:keys [db]} [_]]
    {:db         (assoc-in db (path-to-db [:translator-modal-state]) true)
     :dispatch-n (list [:complete-request :login]
                       [::audio-assets/init-state])}))

(re-frame/reg-event-fx
  ::close-translator-modal
  (fn [{:keys [db]} [_]]
    {:db         (assoc-in db (path-to-db [:translator-modal-state]) false)
     :dispatch-n (list [::translator-form-events/clean-current-selected-action]
                       [::translator-form-events/reset-edited-data]
                       [::translator-form-events/reset-current-concept])}))
