(ns webchange.lesson-builder.tools.script.block.views
  (:require
    [reagent.core :as r]
    [webchange.ui.index :as ui]))

(defn block
  [{:keys [class-name title]}]
  [:div {:class-name (ui/get-class-name {"component--block" true
                                         class-name         (some? class-name)})}
   [:div.component--header title]
   (->> (r/current-component)
        (r/children)
        (into [:div.block--content]))])
