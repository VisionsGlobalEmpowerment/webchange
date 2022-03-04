(ns webchange.student-dashboard.toolbar.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.student-dashboard.toolbar.state :as state]))

(defn- toolbar-item
  [{:keys [img on-click title]}]
  [:div {:class-name "toolbar-item"
         :title      title
         :on-click   on-click}
   [:img {:src img}]])

(defn- toolbar-button
  [{:keys [on-click]}]
  [:div.toolbar-button-background
   (into [:button {:class-name "toolbar-button"
                   :on-click   on-click}]
         (-> (r/current-component)
             (r/children)))])

(defn toolbar
  []
  (let [items (->> @(re-frame/subscribe [::state/toolbar-items])
                   (map (fn [{:keys [id] :as item}]
                          (assoc item :on-click #(re-frame/dispatch [::state/handle-toolbar-item-click id])))))]
    [:div.toolbar
     (for [{:keys [id] :as item} items]
       ^{:key id}
       [toolbar-item item])]))
