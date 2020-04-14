(ns webchange.editor-v2.translator.translator-form.views-form-diagram
  (:require
    [webchange.editor-v2.diagram.widget :refer [diagram-widget]]))

(defn- update-graph
  [graph data-store current-concept-id]
  (reduce (fn [graph [[action-name concept-id] action-data]]
            (if (or (nil? concept-id)
                    (= concept-id current-concept-id))
              (update-in graph [action-name :data] merge (:data action-data))
              graph))
          graph
          data-store))

(defn diagram-block
  [{:keys [graph]}]
  [diagram-widget {:graph graph
                   :mode  :translation}])

