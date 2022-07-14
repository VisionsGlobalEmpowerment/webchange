(ns webchange.lesson-builder.tools.script.dialog-item.phrase.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.tools.script.dialog-item.phrase.state :as state]))

(defn phrase
  [{:keys [action-path]}]
  (let [target-character @(re-frame/subscribe [::state/target-character action-path])]
    [:div.dialog-item--phrase
     target-character]))
