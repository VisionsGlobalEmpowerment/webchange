(ns webchange.ui-framework.layout.right-menu.views-menu-section
  (:require
    [reagent.core :as r]
    [webchange.ui-framework.components.index :refer [icon]]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn menu-section
  [{:keys [expanded? title-icon title-text]
    :or   {expanded? false}}]
  (r/with-let [this (r/current-component)
               expand? (r/atom expanded?)
               handle-header-click (fn []
                                     (swap! expand? not))]
    [:div.menu-section
     [:div.menu-header {:on-click handle-header-click}
      [icon {:icon       title-icon
             :class-name "title-icon"}]
      [:span.title-text title-text]
      [icon {:icon       "arrow-right"
             :class-name (get-class-name {"expand-icon" true
                                          "expanded"    @expand?
                                          "collapsed"   (not @expand?)})}]]
     (when @expand?
       (into [:div.menu-body]
             (r/children this)))]))
