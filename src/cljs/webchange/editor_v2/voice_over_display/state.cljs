(ns webchange.editor-v2.voice-over-display.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.translator.translator-form.state.actions :as translator-form.actions]))

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
   (let [current-phrase-action (translator-form.actions/current-phrase-action db)]
     {:db (assoc-in db modal-state-path true)})))

(re-frame/reg-event-fx
 ::cancel
 (fn [{:keys [db]} [_]]
   {:db (assoc-in db modal-state-path false)}))
