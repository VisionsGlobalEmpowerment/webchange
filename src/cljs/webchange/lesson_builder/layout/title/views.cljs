(ns webchange.lesson-builder.layout.title.views
  (:require
    [webchange.lesson-builder.widgets.activity-title.views :refer [activity-title]]))

(defn block-title
  [{:keys [class-name]}]
  [:div {:id         "block--title"
         :class-name class-name}
   [activity-title]])
