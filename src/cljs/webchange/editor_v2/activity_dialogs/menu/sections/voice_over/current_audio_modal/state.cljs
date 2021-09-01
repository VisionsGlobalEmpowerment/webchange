(ns webchange.editor-v2.activity-dialogs.menu.sections.voice-over.current-audio-modal.state
  (:require
    [re-frame.core :as re-frame]))

(def modal-state-path [:editor-v2 :translator :text :voice-modal-state])

(re-frame/reg-sub
 ::modal-state
 (fn [db]
   (-> db
       (get-in modal-state-path)
       boolean)))

(re-frame/reg-event-fx
 ::open
 (fn [{:keys [db]} [_]]
     {:db (assoc-in db modal-state-path true)}))

(re-frame/reg-event-fx
 ::cancel
 (fn [{:keys [db]} [_]]
   {:db (assoc-in db modal-state-path false)}))  