(ns webchange.editor-v2.dialog.dialog-text-form.side-menu.main.text-animation.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.dialog.dialog-text-form.side-menu.main.common.section-block.views :refer [section-block]]
    [webchange.editor-v2.dialog.dialog-text-form.side-menu.main.text-animation.state :as state]
    [webchange.ui-framework.components.index :refer [icon-button]]))

(defn- actions
  []
  (let [available-actions [["Before" "insert-before" #(re-frame/dispatch [::state/add-text-animation-action :before])]
                           ["After" "insert-after" #(re-frame/dispatch [::state/add-text-animation-action :after])]]]
    [:div.actions
     [:span.input-label "Add:"]
     (for [[idx [text icon handler]] (map-indexed vector available-actions)]
       ^{:key idx}
       [icon-button
        {:icon       icon
         :size       "small"
         :variant    "outlined"
         :class-name "action-button"
         :on-click   handler}
        text])]))

(defn- settings
  []
  (let [handle-click #(re-frame/dispatch [::state/open-text-animation-window])]
    [:div.settings
     [icon-button {:icon     "settings"
                   :size     "small"
                   :on-click handle-click}
      "Open Settings"]]))

(defn form
  []
  (let [show-current? @(re-frame/subscribe [::state/show-current?])]
    [:div.text-animation-form
     (when show-current?
       [section-block {:title "Current selection"}
        [settings]])
     [section-block {:title "Add text animation"}
      [actions]]]))
