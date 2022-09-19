(ns webchange.lesson-builder.widgets.page-remove.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.widgets.page-remove.state :as state]
    [webchange.ui.index :as ui]))

(defn- remove-button
  [{:keys [side]}]
  (let [page-idx @(re-frame/subscribe [::state/page-idx side])
        disabled? @(re-frame/subscribe [::state/page-removable? side])
        handle-click #(re-frame/dispatch [::state/remove-page page-idx])]
    [ui/button {:icon      "trash"
                :color     "blue-1"
                :title     "Remove page"
                :disabled? disabled?
                :on-click  handle-click}]))

(defn remove-flipbook-page
  [{:keys [class-name]}]
  [:div {:class-name (ui/get-class-name {"widget--select-page" true
                                         class-name            (some? class-name)})}
   [remove-button {:side "left"}]
   [remove-button {:side "right"}]])
