(ns webchange.editor-v2.translator.translator-form.views-form-diagram
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.diagram.widget :refer [diagram-widget]]
    [webchange.editor-v2.translator.translator-form.subs :as translator-form-subs]))

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
  []
  (let [graph @(re-frame/subscribe [::translator-form-subs/graph])]
    [diagram-widget {:graph graph
                     :mode  :translation}]))
