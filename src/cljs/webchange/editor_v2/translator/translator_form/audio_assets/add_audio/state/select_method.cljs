(ns webchange.editor-v2.translator.translator-form.audio-assets.add-audio.state.select-method
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.translator.translator-form.audio-assets.add-audio.state.db :refer [path-to-db]]
    [webchange.editor-v2.translator.translator-form.audio-assets.add-audio.state.record-panel :as record-panel]))

;; Subs

(re-frame/reg-sub
  ::current-method
  (fn [db]
    (get-in db (path-to-db [:add-audio-panel]))))

;; Events

(re-frame/reg-event-fx
  ::init-state
  (fn [{:keys [_]} _]
    {:dispatch-n (list [::show-select-method-panel])}))

(re-frame/reg-event-fx
  ::show-select-method-panel
  (fn [{:keys [db]} _]
    {:db (assoc-in db (path-to-db [:add-audio-panel]) :select-method)}))

(re-frame/reg-event-fx
  ::show-record-file-form
  (fn [{:keys [db]} _]
    {:db         (assoc-in db (path-to-db [:add-audio-panel]) :record-file)
     :dispatch-n (list [::record-panel/show-start-record-panel])}))

(re-frame/reg-event-fx
  ::show-upload-file-form
  (fn [{:keys [db]} _]
    {:db (assoc-in db (path-to-db [:add-audio-panel]) :upload-file)}))
