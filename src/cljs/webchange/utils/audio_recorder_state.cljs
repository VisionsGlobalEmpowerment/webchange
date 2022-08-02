(ns webchange.utils.audio-recorder-state
  (:require
    [re-frame.core :as re-frame]
    [webchange.utils.audio-recorder :as recorder]))

(re-frame/reg-fx
  ::start-recording
  (fn [callback]
    (recorder/start #(re-frame/dispatch (conj callback %)))))

(re-frame/reg-fx
  ::stop-recording
  (fn [callback]
    (recorder/stop #(re-frame/dispatch (conj callback %)))))
