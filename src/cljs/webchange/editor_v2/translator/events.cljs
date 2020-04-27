(ns webchange.editor-v2.translator.events
  (:require
    [re-frame.core :as re-frame]
    [ajax.core :refer [json-request-format json-response-format]]
    [webchange.editor-v2.translator.translator-form.events :as translator-form-events]
    [webchange.editor-v2.translator.translator-form.audio-assets.events :as audio-assets]))

(re-frame/reg-event-fx
  ::open-translator-modal
  (fn [{:keys [db]} [_]]
    {:db         (assoc-in db [:editor-v2 :translator :translator-modal-state] true)
     :dispatch-n (list [:complete-request :login]
                       [::audio-assets/init-state])}))

(re-frame/reg-event-fx
  ::close-translator-modal
  (fn [{:keys [db]} [_]]
    {:db         (-> db
                     (assoc-in [:editor-v2 :translator :translator-modal-state] false)
                     (assoc-in [:editor-v2 :translator :selected-action] nil)
                     (assoc-in [:editor-v2 :translator :current-concept] nil))
     :dispatch-n (list [::translator-form-events/reset-edited-data])}))

(re-frame/reg-event-fx
  ::set-blocking-progress
  (fn [{:keys [db]} [_ value]]
    {:db (assoc-in db [:editor-v2 :translator :blocking-progress] value)}))

(re-frame/reg-event-fx
  ::set-current-selected-action
  (fn [{:keys [db]} [_ action]]
    {:db (assoc-in db [:editor-v2 :translator :selected-action] action)}))

(re-frame/reg-event-fx
  ::clean-current-selected-action
  (fn [{:keys [db]} [_]]
    {:db (assoc-in db [:editor-v2 :translator :selected-action] nil)}))

(re-frame/reg-event-fx
  ::set-current-concept
  (fn [{:keys [db]} [_ data]]
    {:db (assoc-in db [:editor-v2 :translator :current-concept] data)}))
