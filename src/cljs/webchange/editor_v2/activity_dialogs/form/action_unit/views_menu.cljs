(ns webchange.editor-v2.activity-dialogs.form.action-unit.views-menu
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.activity-dialogs.form.state :as state]
    [webchange.ui-framework.components.index :refer [icon-button]]))

(defn unit-menu
  [{:keys [action-path]}]
  (let [handle-remove-click #(re-frame/dispatch [::state/remove-action action-path])]
    [:div {:class-name "action-unit-menu"}
     [icon-button {:icon       "remove"
                   :size       "small"
                   :title      "Remove action"
                   :class-name "remove-button"
                   :on-click   handle-remove-click}]]))
