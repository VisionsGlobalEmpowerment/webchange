(ns webchange.editor-v2.events
  (:require
    [re-frame.core :as re-frame]
    [webchange.interpreter.events :as ie]
    [webchange.editor.events :as ee]
    [webchange.editor-v2.translator.events :as translator-events]))

(re-frame/reg-event-fx
  ::init-editor
  (fn [_ [_ course-id scene-id]]
    {:dispatch-n (list [::ie/start-course course-id scene-id]
                       [::ee/load-datasets]
                       [::set-diagram-mode :full-scene])}))

(re-frame/reg-event-fx
  ::set-diagram-mode
  (fn [{:keys [db]} [_ mode]]
    {:db (assoc-in db [:editor-v2 :diagram-mode] mode)}))

(re-frame/reg-event-fx
  ::set-current-action
  (fn [{:keys [db]} [_ action]]
    {:db (assoc-in db [:editor-v2 :current-action] action)}))

(re-frame/reg-event-fx
  ::show-translator-form
  (fn [{:keys [_]} [_ action]]
    {:dispatch-n (list [::set-current-action action]
                       [::translator-events/open-translator-modal])}))
