(ns webchange.editor-v2.translator.translator-form.audio-assets.add-audio.state.record-panel
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.translator.translator-form.audio-assets.add-audio.state.db :refer [path-to-db]]))

;; Subs

(re-frame/reg-sub
  ::state
  (fn [db]
    (get-in db (path-to-db [:record-audio-panel]))))

;; Events

(re-frame/reg-event-fx
  ::show-start-record-panel
  (fn [{:keys [db]} _]
    {:db (assoc-in db (path-to-db [:record-audio-panel]) :start-record)}))

(re-frame/reg-event-fx
  ::show-stop-record-panel
  (fn [{:keys [db]} _]
    {:db (assoc-in db (path-to-db [:record-audio-panel]) :stop-record)}))

(re-frame/reg-event-fx
  ::show-play-record-panel
  (fn [{:keys [db]} _]
    {:db (assoc-in db (path-to-db [:record-audio-panel]) :play-record)}))

(re-frame/reg-event-fx
  ::show-set-record-params-panel
  (fn [{:keys [db]} _]
    {:db (assoc-in db (path-to-db [:record-audio-panel]) :set-params)}))