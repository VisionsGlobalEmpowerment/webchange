(ns webchange.admin.widgets.page.side-bar-page.block.views
  (:require
    [reagent.core :as r]
    [webchange.ui.index :refer [get-class-name] :as ui]))

(defn block
  [{:keys [actions class-name class-name-content focused? icon title]}]
  [:<>
   (when focused?
     [ui/focus-overlay])
   [:div {:class-name (get-class-name {"widget--side-bar-page--block"          true
                                       "widget--side-bar-page--block--focused" focused?
                                       class-name                              (some? class-name)})}
    [:div {:class-name (get-class-name {"block--header" true})}
     (when (some? icon)
       [ui/icon {:icon icon}])
     [:span.title-text title]
     (when-not (empty? actions)
       [:div {:class-name (get-class-name {"block--actions" true})}
        (for [[idx action] (map-indexed vector actions)]
          ^{:key idx}
          [ui/button (merge {:color "blue-1"}
                            action)])])]
    (->> (r/current-component)
         (r/children)
         (into [:div {:class-name (get-class-name {"block--content"   true
                                                   class-name-content (some? class-name-content)})}]))]])
