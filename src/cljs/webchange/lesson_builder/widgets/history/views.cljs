(ns webchange.lesson-builder.widgets.history.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.widgets.history.state :as state]
    [webchange.ui.index :as ui]))

(defn- history-item
  [{:keys [id current? date description time]}]
  (let [loading? @(re-frame/subscribe [::state/restore-loading? id])
        handle-click #(re-frame/dispatch [::state/restore-version id])]
    [:<>
     [:div.history-item--date date]
     [:div.history-item--time time]
     [:div.history-item--description description]
     [:div.history-item--action
      (if current?
        "Current Version"
        [ui/button {:on-click handle-click
                    :loading? loading?
                    :color    "transparent"
                    :shape    "rounded"}
         "Restore"])]]))

(defn- activity-history-form
  []
  (let [versions @(re-frame/subscribe [::state/activity-versions])]
    [:div {:class-name "widget--activity-history-form"}
     (for [{:keys [id] :as version-data} versions]
       ^{:key id}
       [history-item version-data])]))

(defn activity-history-window
  []
  (let [open? @(re-frame/subscribe [::state/window-open?])
        handle-close #(re-frame/dispatch [::state/close-window])]
    (when open?
      [ui/dialog {:title      "History"
                  :class-name "widget--activity-history-dialog"
                  :on-close   handle-close}
       [activity-history-form]])))
