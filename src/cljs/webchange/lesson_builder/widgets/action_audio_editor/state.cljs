(ns webchange.lesson-builder.widgets.action-audio-editor.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.state :as state]
    [webchange.utils.scene-data :as utils]
    [webchange.utils.scene-action-data :as action-utils]))

(re-frame/reg-sub
  ::action-data
  :<- [::state/activity-data]
  (fn [activity-data [_ action-path]]
    (utils/get-action activity-data action-path)))

(re-frame/reg-sub
  ::audio-url
  (fn [[_ action-path]]
    (re-frame/subscribe [::action-data action-path]))
  (fn [action-data]
    (-> (action-utils/get-inner-action action-data)
        (get :audio))))
