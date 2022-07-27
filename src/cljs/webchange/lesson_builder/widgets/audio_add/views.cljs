(ns webchange.lesson-builder.widgets.audio-add.views
  (:require
    [webchange.ui.index :as ui]))

(defn- index
  []
  [:div.audio-add--index
   [ui/button {:icon      "record"
               :icon-side "left"
               :shape     "rounded"
               :color     "blue-1"}
    "Record Voice"]
   [ui/button {:shape     "rounded"}
    "Upload Audio"]])

(defn audio-add
  []
  [:div.widget--audio-add
   [index]])
