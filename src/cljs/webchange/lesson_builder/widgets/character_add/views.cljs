(ns webchange.lesson-builder.widgets.character-add.views
  (:require
    [webchange.lesson-builder.widgets.not-implemented.views :refer [not-implemented]]))

(defn character-add
  []
  [:div.widget--character-add
   [:h1 "Add Character"]
   [not-implemented]])
