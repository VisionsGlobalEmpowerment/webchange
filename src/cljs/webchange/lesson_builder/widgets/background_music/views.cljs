(ns webchange.lesson-builder.widgets.background-music.views
  (:require
    [webchange.lesson-builder.widgets.not-implemented.views :refer [not-implemented]]))

(defn background-music
  []
  [:div.widget--background-music
   [:h1 "Background Music"]
   [not-implemented]])
