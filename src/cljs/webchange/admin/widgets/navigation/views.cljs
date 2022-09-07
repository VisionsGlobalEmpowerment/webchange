(ns webchange.admin.widgets.navigation.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.widgets.navigation.state :as state]
    [webchange.ui.index :as ui]
    [webchange.ui-framework.components.index :as c]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn- handle-item-click
  [event route]
  (.stopPropagation event)
  (re-frame/dispatch [::state/open-page route]))

(declare children-list)

(defn- children-item
  [{:keys [active? children route text]}]
  (let [handle-click #(handle-item-click % route)]
    [:div {:class-name (get-class-name {"children-item" true
                                        "active-item"   active?})
           :on-click   handle-click}
     [:div.text text]
     (when-not (empty? children)
       [:<>
        [c/icon {:icon       "chevron-right"
                 :class-name "icon"}]
        [children-list {:items children}]])]))

(defn- children-list
  [{:keys [items position]
    :or   {position "right"}}]
  [:div {:class-name (get-class-name {"children-list"            true
                                      (str "position-" position) true})}
   (for [{:keys [id] :as item} items]
     ^{:key id}
     [children-item item])])

(defn- root-item
  [{:keys [active? children icon route text]}]
  (let [handle-click #(handle-item-click % route)]
    [:div {:class-name (get-class-name {"root-item"       true
                                        "navigation-item" true
                                        "active-item"     active?})
           :on-click   handle-click}
     [ui/navigation-icon {:icon       icon
                          :class-name "root-icon"}]
     [:div.text text]
     (when-not (empty? children)
       [:<>
        [ui/icon {:icon       "caret-down"
                  :class-name "expand-icon"}]
        [children-list {:items    children
                        :position "bottom"}]])]))

(defn navigation
  []
  (let [items @(re-frame/subscribe [::state/navigation-items])]
    [:div.top-bar--navigation
     [ui/logo-with-name {:class-name "logo"}]
     (for [{:keys [id] :as item} items]
       ^{:key id}
       [root-item item])]))
