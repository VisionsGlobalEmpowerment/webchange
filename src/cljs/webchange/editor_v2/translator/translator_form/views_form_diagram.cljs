(ns webchange.editor-v2.translator.translator-form.views-form-diagram
  (:require
    [webchange.editor-v2.diagram.graph-builder.graph :refer [get-diagram-graph]]
    [webchange.editor-v2.diagram.widget :refer [diagram-widget]]))

(defn diagram-block
  [{:keys [scene-data action-name concepts-scheme]}]
  (let [graph (when-not (nil? action-name)
                (get-diagram-graph scene-data :translation {:start-node action-name
                                                            :concepts   (get-in concepts-scheme [:scheme :fields])}))]
    [diagram-widget {:graph graph
                     :mode  :translation}]))

