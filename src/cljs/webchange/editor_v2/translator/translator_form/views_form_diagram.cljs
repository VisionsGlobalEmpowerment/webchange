(ns webchange.editor-v2.translator.translator-form.views-form-diagram
  (:require
    [webchange.editor-v2.diagram.widget :refer [diagram-widget]]))

(defn diagram-block
  [{:keys [graph]}]
  [diagram-widget {:graph graph
                   :mode  :translation}])

