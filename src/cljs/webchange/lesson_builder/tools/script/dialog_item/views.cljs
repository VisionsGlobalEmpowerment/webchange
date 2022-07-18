(ns webchange.lesson-builder.tools.script.dialog-item.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.tools.script.dialog-item.phrase.views :refer [phrase]]
    [webchange.lesson-builder.tools.script.dialog-item.text-animation.views :refer [text-animation]]
    [webchange.lesson-builder.tools.script.dialog-item.state :as state]))

(def available-items {:phrase         phrase
                      :text-animation text-animation})

(defn dialog-item
  [{:keys [action-path] :as props}]
  (let [action-type @(re-frame/subscribe [::state/action-type action-path])
        item-component (get available-items action-type)]
    (print "action-type" action-type)
    (when (some? item-component)
      [item-component props])))
