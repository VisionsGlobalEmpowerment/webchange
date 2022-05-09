(ns webchange.admin.components.counter.views
  (:require
    [webchange.ui-framework.components.index :as c]))

(defn- counter-item-action
  [{:keys [icon title on-click]}]
  (let [handle-click (fn [event]
                       (.stopPropagation event)
                       (when (fn? on-click)
                         (on-click)))]
    [:div {:class-name "counter-action"
           :on-click   handle-click}
     [:div.action-name title]
     [c/icon {:icon       icon
              :class-name "action-icon"
              :variant    "light"}]]))

(defn- counter-item
  [{:keys [actions title value on-click]}]
  (let [handle-click #(when (fn? on-click) (on-click))]
    [:div.counter-item
     [:div {:on-click   handle-click
            :class-name (c/get-class-name {"display" true
                                           "clickable"    (fn? on-click)})}
      [:div.value value]
      [:div.title title]]
     [:div.actions
      (for [[idx action] (map-indexed vector actions)]
        ^{:key idx}
        [counter-item-action action])]]))

(defn counter
  [{:keys [items]}]
  [:div.component--counter
   (for [{:keys [id] :as item} items]
     ^{:key id}
     [counter-item item])])
