(ns webchange.lesson-builder.tools.script.dialog-item.wrapper.views
  (:require
    [reagent.core :as r]
    [webchange.ui.index :as ui]))

(defn item-wrapper
  [{:keys [actions class-name parallel?]
    :or {parallel? false}}]
  [:div {:class-name (ui/get-class-name {"component--item-wrapper" true
                                         "component--item-wrapper--parallel" parallel?})}
   [ui/icon {:icon       "move"
             :class-name "component--item-wrapper--move-icon"}]
   (->> (r/current-component)
        (r/children)
        (into [:div {:class-name (ui/get-class-name {"component--item-wrapper--content" true
                                                     class-name              (some? class-name)})}]))
   (when-not (empty? actions)
     [:div.component--item-wrapper--actions
      (for [[idx action] (map-indexed vector actions)]
        ^{:key idx}
        [ui/button (merge {:class-name "component--item-wrapper--action"}
                          action)])])])
