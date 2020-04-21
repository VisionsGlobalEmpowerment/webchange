(ns webchange.editor-v2.translator.translator-form.audio-assets.add-audio.subs
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.translator.translator-form.audio-assets.add-audio.db :refer [path-to-db]]))

(re-frame/reg-sub
  ::current-method
  (fn [db]
    (get-in db (path-to-db [:state]))))

(re-frame/reg-sub
  ::record-panel-state
  (fn [db]
    (get-in db (path-to-db [:record-panel-state]))))

