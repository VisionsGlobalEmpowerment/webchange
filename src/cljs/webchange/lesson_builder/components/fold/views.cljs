(ns webchange.lesson-builder.components.fold.views
  (:require
    [reagent.core :as r]
    [webchange.ui.index :as ui]))

(defn fold
  [{:keys [title]}]
  (r/with-let [expanded? (r/atom true)
               toggle-expanded #(swap! expanded? not)]
    [:div.component--fold
     [:div {:class-name "fold--header"
            :on-click   toggle-expanded}
      [ui/icon {:icon       (if @expanded? "caret-up" "caret-down")
                :class-name "fold--icon"}]
      [:span title]]
     (when @expanded?
       (->> (r/current-component)
            (r/children)
            (into [:div.fold--content])))]))
