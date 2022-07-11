(ns webchange.lesson-builder.widgets.image-add.views
  (:require
    [webchange.lesson-builder.widgets.not-implemented.views :refer [not-implemented]]))

(defn image-add
  []
  [:div.widget--image-add
   [:h1 "Add Image"]
   [not-implemented]])
