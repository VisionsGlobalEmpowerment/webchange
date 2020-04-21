(ns webchange.editor-v2.translator.translator-form.audio-assets.add-audio.events
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.translator.translator-form.audio-assets.add-audio.db :refer [path-to-db]]))

(re-frame/reg-event-fx
  ::init-state
  (fn [{:keys [_]} _]
    {:dispatch-n (list [::show-select-method-panel])}))

(re-frame/reg-event-fx
  ::show-select-method-panel
  (fn [{:keys [db]} _]
    {:db (assoc-in db (path-to-db [:state]) :select-method)}))

(re-frame/reg-event-fx
  ::show-record-file-form
  (fn [{:keys [db]} _]
    {:db         (assoc-in db (path-to-db [:state]) :record-file)
     :dispatch-n (list [::show-start-record-panel])}))

(re-frame/reg-event-fx
  ::show-upload-file-form
  (fn [{:keys [db]} _]
    {:db (assoc-in db (path-to-db [:state]) :upload-file)}))

(re-frame/reg-event-fx
  ::show-start-record-panel
  (fn [{:keys [db]} _]
    {:db (assoc-in db (path-to-db [:record-panel-state]) :start-record)}))

(re-frame/reg-event-fx
  ::show-stop-record-panel
  (fn [{:keys [db]} _]
    {:db (assoc-in db (path-to-db [:record-panel-state]) :stop-record)}))

(re-frame/reg-event-fx
  ::show-play-record-panel
  (fn [{:keys [db]} _]
    {:db (assoc-in db (path-to-db [:record-panel-state]) :play-record)}))

(re-frame/reg-event-fx
  ::show-set-record-params-panel
  (fn [{:keys [db]} _]
    {:db (assoc-in db (path-to-db [:record-panel-state]) :set-params)}))
