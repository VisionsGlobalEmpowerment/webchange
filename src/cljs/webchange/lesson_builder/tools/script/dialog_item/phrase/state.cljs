(ns webchange.lesson-builder.tools.script.dialog-item.phrase.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.tools.script.dialog-item.state :as state]
    [webchange.utils.scene-action-data :as action-utils]))

(re-frame/reg-sub
  ::target-character
  (fn [[_ action-path]]
    (re-frame/subscribe [::state/action-data action-path]))
  (fn [action-data]
    (print "action-data" action-data)
    (let [inner-action (action-utils/get-inner-action action-data)]
      (print "inner-action" inner-action)
      (get inner-action :target))))