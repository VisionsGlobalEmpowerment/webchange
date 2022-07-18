(ns webchange.lesson-builder.tools.script.dialog-item.text-animation.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.tools.script.dialog-item.text-animation.state :as state]
    [webchange.lesson-builder.tools.script.target-selector.views :refer [target-selector]]
    [webchange.lesson-builder.tools.script.text-editor.views :refer [text-editor]]))

(defn text-animation
  [{:keys [action-path]}]
  (let [target @(re-frame/subscribe [::state/target action-path])
        text @(re-frame/subscribe [::state/text action-path])
        handle-phrase-text-change #(re-frame/dispatch [::state/set-text action-path %])
        handle-target-change #(re-frame/dispatch [::state/set-target action-path %])]
    [:div {:class-name "dialog-item dialog-item--text-animation"}
     [target-selector {:value     target
                       :type      :text-animation
                       :on-change handle-target-change}]
     [text-editor {:value     text
                   :on-change handle-phrase-text-change
                   :type      :text-animation
                   :actions   [{:icon     "trash"
                                :title    "Delete phrase"
                                :on-click #(print "Delete" action-path)}]}]]))