(ns webchange.lesson-builder.widgets.audio-list.views
  (:require
    [webchange.lesson-builder.widgets.not-implemented.views :refer [not-implemented]]))

(defn audio-list
  []
  [:div.widget--audio-manager
   [not-implemented {:text "Audio List"}]])
