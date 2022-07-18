(ns webchange.lesson-builder.tools.script.dialog-item.effect-general.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.tools.script.dialog-item.state :as state]
    [webchange.lesson-builder.tools.stage-actions :as stage-actions]
    [webchange.utils.scene-action-data :as action-utils]))

(re-frame/reg-sub
  ::name
  (fn [[_ action-path]]
    (re-frame/subscribe [::state/action-data action-path]))
  (fn [action-data]
    (print "action-data" action-data)
    (let [inner-action (action-utils/get-inner-action action-data)]
      (-> (or (get inner-action :id)
              (get inner-action :type))
          (clojure.string/replace "-" " ")
          (clojure.string/capitalize)))))
