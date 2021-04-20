(ns webchange.game-changer.views
  (:require
    [webchange.ui-framework.components.index :refer [timeline]]
    [webchange.ui-framework.layout.views :refer [layout]]

    [webchange.game-changer.templates-list.views :refer [templates-list]]
    ))

(def timeline-items [{:title      "Choose Activity"
                      :completed? true}
                     {:title      "Name Activity"
                      :completed? true}
                     {:title   "Add Content"
                      :active? true}
                     {:title "Select Images"}
                     {:title "Finish & Publish"}])

(defn- form
  []
  [:div.game-changer-form
   [:div.title
    "Title"]
   [:div.timeline
    [timeline {:items timeline-items}]]
   [:div.content
    [templates-list]]])

(defn index
  []
  [layout {:show-navigation? false}
   [form]])
