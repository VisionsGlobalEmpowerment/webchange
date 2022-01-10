(ns webchange.editor-v2.activity-dialogs.menu.sections.text-animation.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.activity-dialogs.menu.sections.common.options-list.views :refer [options-list]]
    [webchange.editor-v2.activity-dialogs.menu.sections.common.section-block.views :refer [section-block]]
    [webchange.editor-v2.activity-dialogs.menu.sections.text-animation.state :as state]
    [webchange.ui-framework.components.index :refer [icon-button]]))

(defn- settings
  []
  (let [handle-click #(re-frame/dispatch [::state/open-text-animation-window])]
    [:div.settings
     [icon-button {:icon     "settings"
                   :size     "small"
                   :on-click handle-click}
      "Open Settings"]]))

(defn- actions
  []
  (let [available-actions @(re-frame/subscribe [::state/available-actions])]
    [:div.actions
     [options-list {:options       available-actions
                    :get-drag-data (fn [{:keys [value]}]
                                     {:action      "add-text-animation-action"
                                      :destination value})}]]))

(defn form
  []
  (let [show-current? @(re-frame/subscribe [::state/show-current?])]
    [:div.text-animation-form
     (when show-current?
       [settings])
     [section-block {:title "Add"}
      [actions]]]))
