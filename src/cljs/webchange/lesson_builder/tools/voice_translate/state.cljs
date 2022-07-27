(ns webchange.lesson-builder.tools.voice-translate.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.lesson-builder.tools.script.state :as script-state]))

(def path-to-db :lesson-builder/voice-translate)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

(def selected-audio-key :selected-audio)

(re-frame/reg-sub
  ::selected-audio
  :<- [path-to-db]
  #(get % selected-audio-key))

(re-frame/reg-event-fx
  ::set-selected-audio
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ value]]
    {:db (assoc db selected-audio-key value)}))

(re-frame/reg-sub
  ::show-audio-editor?
  :<- [::script-state/selected-action]
  :<- [::selected-audio]
  (fn [[selected-action selected-audio]]
    (and (some? selected-action)
         (some? selected-audio))))
