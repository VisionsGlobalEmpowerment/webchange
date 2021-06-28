(ns webchange.editor-v2.dialog.dialog-text-form.side-menu.main.effects.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.dialog.dialog-text-form.side-menu.main.common.section-block.views :refer [section-block]]
    [webchange.editor-v2.dialog.dialog-text-form.side-menu.main.common.options-list.views :refer [options-list]]
    [webchange.editor-v2.dialog.dialog-text-form.side-menu.main.effects.state :as state]
    [webchange.ui-framework.components.index :refer [icon-button]]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn- effects-list
  []
  (let [available-effects @(re-frame/subscribe [::state/available-effects])
        handle-click #(re-frame/dispatch [::state/set-selected-effect %])]
    [:div
     [:span.input-label "Select effect:"]
     [options-list {:options  available-effects
                    :on-click handle-click}]]))

(defn- actions
  []
  (let [selected-effect @(re-frame/subscribe [::state/selected-effect])
        available-actions [["Before" "insert-before" #(re-frame/dispatch [::state/add-current-effect-action :before])]
                           ["After" "insert-after" #(re-frame/dispatch [::state/add-current-effect-action :after])]
                           ["Parallel" "insert-parallel" #(re-frame/dispatch [::state/add-current-effect-action :parallel])]]]
    (when (some? selected-effect)
      [:div.actions
       [:span.input-label "Add:"]
       (for [[idx [text icon handler]] (map-indexed vector available-actions)]
         ^{:key idx}
         [icon-button
          {:icon     icon
           :size     "small"
           :on-click handler}
          text])])))

(defn form
  []
  [:div.effects-form
   [section-block {:title "Add effect"}
    [effects-list]
    [actions]]])
