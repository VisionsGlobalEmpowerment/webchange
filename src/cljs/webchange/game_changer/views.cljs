(ns webchange.game-changer.views
  (:require
    [webchange.ui-framework.layout.views :refer [layout]]))

(defn form
  []
  [:div.game-changer-form
   [:div.title
    "Title"]
   [:div.timeline
    "Timeline"]
   [:div.content
    "Content"]])

(defn index
  []
  [layout {:show-navigation? false}
   [form]])
