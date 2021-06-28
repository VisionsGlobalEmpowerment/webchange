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
        {:icon     icon
         :size     "small"
         :on-click handler}
        text])]))

(defn- settings
  []
  (let [show-settings? @(re-frame/subscribe [::state/show-settings?])
        handle-click #(re-frame/dispatch [::state/open-text-animation-window])]
    (when show-settings?
      [:div.settings
       [:span.input-label "Settings:"]
       [icon-button {:icon     "settings"
                     :size     "small"
                     :on-click handle-click}
        "Open"]])))

(defn form
  []
  [:div.text-animation-form
   [section-block {:title "Add text animation"}
    [actions]
    [settings]]])
