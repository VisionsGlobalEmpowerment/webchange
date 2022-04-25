(ns webchange.admin.widgets.layout.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.admin.widgets.layout.state :as state]
    [webchange.ui-framework.components.index :as c]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn- top-bar-action
  [{:keys [active? icon route text]}]
  (let [handle-click #(re-frame/dispatch [::state/open-page route])]
    [:div {:class-name (get-class-name {"top-bar-action" true
                                        "active"         active?})
           :on-click   handle-click}
     [c/icon {:icon       icon
              :class-name "icon"}]
     [:div.text text]]))

(defn- top-bar-actions
  []
  (let [actions @(re-frame/subscribe [::state/top-bar-items])]
    [:div.top-bar-actions
     (for [{:keys [id] :as action} actions]
       ^{:key id}
       [top-bar-action action])]))

(defn- top-bar
  []
  [:div.top-bar
   [top-bar-actions]])

(defn layout
  []
  [:div.layout
   [top-bar]
   [:div.content-wrapper
    (->> (r/current-component)
         (r/children)
         (into [:div.content]))]])
