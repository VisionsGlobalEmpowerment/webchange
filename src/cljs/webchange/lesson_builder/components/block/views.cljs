(ns webchange.lesson-builder.components.block.views
  (:require
    [reagent.core :as r]
    [webchange.ui.index :as ui]))

(defn block
  [{:keys [border class-name title]}]
  [:div {:class-name (ui/get-class-name {"component--block"                       true
                                         (str "component--block--border-" border) (some? border)
                                         class-name                               (some? class-name)})}
   (when (some? title)
     [:div.component--block--header
      [:h2 title]])
   (->> (r/current-component)
        (r/children)
        (into [:div.component--block--content]))])