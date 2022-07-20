(ns webchange.lesson-builder.tools.script.dialog-item.text-animation.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.tools.script.dialog-item.text-animation.state :as state]
    [webchange.lesson-builder.tools.script.dialog-item.wrapper.views :refer [item-wrapper]]
    [webchange.lesson-builder.tools.script.target-selector.views :refer [target-selector]]
    [webchange.lesson-builder.tools.script.text-editor.views :refer [text-editor]]))

(defn text-animation
  [{:keys [action-path]}]
  (let [target @(re-frame/subscribe [::state/target action-path])
        text @(re-frame/subscribe [::state/text action-path])
        handle-phrase-text-change #(re-frame/dispatch [::state/set-text action-path %])
        handle-target-change #(re-frame/dispatch [::state/set-target action-path %])
        handle-remove-click #(re-frame/dispatch [::state/remove action-path])]
    [item-wrapper {:class-name "dialog-item dialog-item--text-animation"
                   :actions    [{:icon     "trash"
                                 :title    "Delete phrase"
                                 :on-click handle-remove-click}]
                   :data       {:path action-path}}
     [target-selector {:value     target
                       :type      :text-animation
                       :on-change handle-target-change}]
     [text-editor {:value     text
                   :on-change handle-phrase-text-change
                   :type      :text-animation}]]))
