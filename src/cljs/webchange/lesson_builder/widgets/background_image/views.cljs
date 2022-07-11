(ns webchange.lesson-builder.widgets.background-image.views
  (:require
    [webchange.lesson-builder.widgets.not-implemented.views :refer [not-implemented]]))

(defn background-image
  []
  [:div.widget--background-image
   [:h1 "Background Image"]
   [not-implemented]])
