(ns webchange.lesson-builder.widgets.audio-add.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.stage-actions :as stage-actions]))

(re-frame/reg-event-fx
  ::add-audio-asset
  (fn [{:keys [_]} [_ data]]
    (print "::add-audio-asset" data)
    {:dispatch [::stage-actions/add-asset data]}))
