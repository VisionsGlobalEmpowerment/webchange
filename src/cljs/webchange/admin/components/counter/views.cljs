(ns webchange.admin.components.counter.views
  (:require
    [webchange.ui.index :as ui]))

(defn- counter-item-action
  [{:keys [color icon title on-click]
    :or   {color "orange"}}]
  (let [handle-click (fn [event]
                       (.stopPropagation event)
                       (when (fn? on-click)
                         (on-click)))]
    [:div {:class-name (ui/get-class-name {"counter-action"     true
                                           (str "color-" color) true})
           :on-click   handle-click}
     [:div.action-name title]
     (when (some? icon)
       [ui/icon {:icon       icon
                :class-name "action-icon"
                :variant    "light"}])]))

(defn- counter-item
  [{:keys [actions color icon icon-background title value]
    :or   {color           "yellow"
           icon-background "yellow"}}]
  (let [value (if (some? value) value "-")]
    [:div {:class-name (ui/get-class-name {"counter-item"       true
                                           (str "color-" color) true})}
     [:div {:class-name (ui/get-class-name {"display"                      true
                                            (str "icon-background-" color) true})}
      (if (some? icon)
        [:div.icon-value
         [ui/icon {:icon       icon
                  :class-name "icon"}]
         [:div.value value]]
        [:div.text-value
         value])
      [:div.title title]]
     [:div.actions
      (for [[idx action] (map-indexed vector actions)]
        ^{:key idx}
        [counter-item-action action])]]))

(defn counter
  [{:keys [items]}]
  [:div {:class-name (ui/get-class-name {"component--counter"           true
                                         (str "counter-" (max 3 (count items)) "-columns") true})}
   (for [{:keys [id] :as item} items]
     ^{:key id}
     [counter-item item])])
