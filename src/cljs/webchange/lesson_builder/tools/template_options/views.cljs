(ns webchange.lesson-builder.tools.template-options.views
  (:require
    [webchange.lesson-builder.widgets.not-implemented.views :refer [not-implemented]]))

(defn template-options
  []
  [:div.widget--template-options
   [:h1 "Template Options"]
   [not-implemented]])
