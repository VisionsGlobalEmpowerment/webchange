(ns webchange.lesson-builder.tools.script.dialog-item.phrase.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.tools.script.dialog-item.phrase.state :as state]
    [webchange.lesson-builder.tools.script.dialog-item.wrapper.views :refer [item-wrapper]]
    [webchange.lesson-builder.tools.script.target-selector.views :refer [target-selector]]
    [webchange.lesson-builder.tools.script.text-editor.views :refer [text-editor]]
    [webchange.lesson-builder.layout.menu.state :as menu-state]))

(defn phrase
  [{:keys [action-path]}]
  (let [phrase-text @(re-frame/subscribe [::state/phrase-text action-path])
        has-issue? @(re-frame/subscribe [::state/has-issue? action-path])
        target @(re-frame/subscribe [::state/target action-path])
        handle-phrase-text-change #(re-frame/dispatch [::state/set-phrase-text action-path %])
        handle-target-change #(re-frame/dispatch [::state/set-target action-path %])
        handle-remove-click #(re-frame/dispatch [::state/remove action-path])
        handle-settings-click #(re-frame/dispatch [::menu-state/open-component :dialog-item-prop])]
    [item-wrapper {:class-name  "dialog-item--phrase"
                   :actions     [{:icon     "edit"
                                  :title    "Settings"
                                  :on-click handle-settings-click}
                                 {:icon     "trash"
                                  :title    "Delete phrase"
                                  :on-click handle-remove-click}]
                   :action-path action-path}
     [target-selector {:value     target
                       :on-change handle-target-change}]
     ^{:key (apply str action-path)}
     [text-editor {:value phrase-text
                   :has-issue? has-issue?
                   :on-change handle-phrase-text-change}]]))
