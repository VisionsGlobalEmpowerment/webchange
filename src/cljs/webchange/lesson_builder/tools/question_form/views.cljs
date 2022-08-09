(ns webchange.lesson-builder.tools.question-form.views
  (:require
    [reagent.core :as r]
    [webchange.lesson-builder.components.toolbox.views :refer [toolbox]]))

(defn question-params
  []
  [:div "question-params"])

(defn question-options
  []
  [toolbox
   "question-options !"])
