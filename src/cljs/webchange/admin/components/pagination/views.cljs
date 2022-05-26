(ns webchange.admin.components.pagination.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.admin.components.pagination.state :as state]
    [webchange.ui-framework.components.index :as ui]))

(defn- page-item
  [{:keys [active? on-click title value]}]
  (let [handle-click #(when (and (not active?)
                                 (fn? on-click))
                        (on-click %))]
    [ui/button {:class-name (ui/get-class-name {"pagination--button" true
                                                "active"             active?})
                :on-click   #(handle-click value)}
     title]))

(defn pagination
  []
  (r/create-class
    {:display-name "Pagination"

     :component-did-mount
     (fn [this]
       (re-frame/dispatch [::state/init (r/props this)]))

     :reagent-render
     (fn [{:keys [on-click]}]
       (let [buttons @(re-frame/subscribe [::state/buttons])]
         [:div.component--pagination
          (for [{:keys [value] :as button} buttons]
            ^{:key value}
            [page-item (assoc button
                         :on-click on-click)])]))}))
