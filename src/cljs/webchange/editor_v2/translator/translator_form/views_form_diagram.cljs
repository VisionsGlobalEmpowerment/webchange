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
  [{:keys [graph current-concept edited-data]}]
  (let [prepared-graph (update-graph graph edited-data (:id current-concept))]
    [diagram-widget {:graph prepared-graph
                     :mode  :translation}]))

