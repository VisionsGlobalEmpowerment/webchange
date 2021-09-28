(ns webchange.editor-v2.activity-form.generic.components.background-music.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.state.state-activity :as state-activity]))

(re-frame/reg-sub
  ::music-options
  (fn [_]
    [{:name "Map"
      :url  "/raw/audio/background/Map.mp3"}
     {:name "Casa"
      :url  "/raw/audio/background/Casa.mp3"}
     {:name "Parque"
      :url  "/raw/audio/background/Parque.mp3"}]))

(re-frame/reg-event-fx
  ::save
  (fn [{:keys []} [_ type data on-success]]
    (let []
      {:dispatch [::state-activity/call-activity-common-action
                  {:action type
                   :data   data}
                  (cond-> {}
                          (some? on-success) (assoc :on-success on-success))]})))
