(ns webchange.lesson-builder.tools.script.dialog-item.phrase.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.tools.script.dialog-item.phrase.state :as state]
    [webchange.lesson-builder.tools.script.target-selector.views :refer [target-selector]]
    [webchange.lesson-builder.tools.script.text-editor.views :refer [text-editor]]))

(defn phrase
  [{:keys [action-path]}]
  (let [phrase-text @(re-frame/subscribe [::state/phrase-text action-path])
        target @(re-frame/subscribe [::state/target action-path])
        handle-phrase-text-change #(re-frame/dispatch [::state/set-phrase-text action-path %])
        handle-target-change #(re-frame/dispatch [::state/set-target action-path %])]
    [:div {:class-name "dialog-item dialog-item--phrase"}
     [target-selector {:value     target
                       :on-change handle-target-change}]
     [text-editor {:value     phrase-text
                   :on-change handle-phrase-text-change
                   :actions   [{:icon     "trash"
                                :title    "Delete phrase"
                                :on-click #(print "Delete" action-path)}]}]]))
