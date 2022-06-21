(ns webchange.admin.widgets.breadcrumbs.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.widgets.breadcrumbs.state :as state]
    [webchange.ui.index :as ui]))

(defn breadcrumbs
  []
  (let [items @(re-frame/subscribe [::state/breadcrumbs])
        handle-click #(re-frame/dispatch [::state/go-to-route %])]
    (when-not (empty? items)
      [ui/breadcrumbs {:items    items
                       :on-click handle-click}])))
