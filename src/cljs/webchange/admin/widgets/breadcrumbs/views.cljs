(ns webchange.admin.widgets.breadcrumbs.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.widgets.breadcrumbs.state :as state]
    [webchange.ui-framework.components.index :as c]))

(defn breadcrumb-item
  [{:keys [last-item? title route]}]
  (let [handle-click #(re-frame/dispatch [::state/go-to-route route])]
    [:<>
     [:div (cond-> {:class-name (c/get-class-name {"breadcrumbs-item" true
                                                   "available"        (not last-item?)})}
                   (not last-item?) (assoc :on-click handle-click))
      title]
     (when-not last-item?
       [:div "|"])]))

(defn breadcrumbs
  []
  (let [items @(re-frame/subscribe [::state/breadcrumbs])
        back-button-available? (-> (count items) (> 1))
        handle-back-click #(re-frame/dispatch [::state/go-to-route (-> items butlast last :route)])]
    [:div.widget--breadcrumbs
     (when back-button-available?
       [c/icon-button {:icon       "arrow-left"
                       :variant    "light"
                       :class-name "back-button"
                       :on-click   handle-back-click}])
     (for [{:keys [id] :as item} items]
       ^{:key id}
       [breadcrumb-item item])]))
