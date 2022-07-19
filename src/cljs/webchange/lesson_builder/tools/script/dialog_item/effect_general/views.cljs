(ns webchange.lesson-builder.tools.script.dialog-item.effect-general.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.tools.script.dialog-item.effect-general.state :as state]
    [webchange.lesson-builder.tools.script.dialog-item.wrapper.views :refer [item-wrapper]]
    [webchange.ui.index :as ui]))

(defn effect-general
  [{:keys [action-path]}]
  (let [name @(re-frame/subscribe [::state/name action-path])
        handle-remove-click #(re-frame/dispatch [::state/remove action-path])]
    [item-wrapper {:class-name "dialog-item--effect-general"
                   :actions    [{:icon     "trash"
                                 :title    "Delete phrase"
                                 :on-click handle-remove-click}]}
     [ui/icon {:icon       "effects"
               :class-name "effect-general--icon"}]
     name]))
