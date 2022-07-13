(ns webchange.lesson-builder.tools.settings.views
  (:require
    [webchange.lesson-builder.widgets.not-implemented.views :refer [not-implemented]]))

(defn settings
  []
  [:div.widget--settings
   [not-implemented]])
