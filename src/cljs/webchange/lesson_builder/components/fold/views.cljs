(ns webchange.lesson-builder.components.fold.views
  (:require
    [reagent.core :as r]
    [webchange.ui.index :as ui]))

(defn fold
  [{:keys [title expanded? height-restricted?]
    :or   {expanded?          true
           height-restricted? true}}]
  (r/with-let [expanded? (r/atom expanded?)
               toggle-expanded #(swap! expanded? not)]
    (let [children (->> (r/current-component)
                        (r/children))
          children-empty? (->> children
                               (remove nil?)
                               empty?)]
      [:div.component--fold
       [:div {:class-name "fold--header"
              :on-click   toggle-expanded}
        [ui/icon {:icon       (if @expanded? "caret-up" "caret-down")
                  :class-name "fold--icon"}]
        [:span title]]
       (when (and @expanded?
                  (not children-empty?))
         (into [:div {:class-name (ui/get-class-name {"fold--content"     true
                                                      "height-restricted" height-restricted?})}]
               children))])))
