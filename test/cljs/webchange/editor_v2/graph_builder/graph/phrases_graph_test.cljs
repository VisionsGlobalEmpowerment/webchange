(ns webchange.editor-v2.graph-builder.graph.phrases-graph-test
  (:require
    [cljs.test :refer [deftest testing is]]
    [utils.compare-maps :refer [print-maps-comparison]]
    [webchange.editor-v2.graph-builder.graph :refer [get-diagram-graph]]
    [webchange.editor-v2.graph-builder.graph.phrases-graph--home-source :as home-source]
    [webchange.editor-v2.graph-builder.graph.phrases-graph--home-expected :as home-expected]
    [webchange.editor-v2.graph-builder.graph.phrases-graph--sandbox-source :as sandbox-source]
    [webchange.editor-v2.graph-builder.graph.phrases-graph--sandbox-expected :as sandbox-expected]
    [webchange.editor-v2.graph-builder.graph.phrases-graph--see-saw-source :as see-saw-source]
    [webchange.editor-v2.graph-builder.graph.phrases-graph--see-saw-expected :as see-saw-expected]
    [webchange.editor-v2.graph-builder.graph.phrases-graph--swings-source :as swings-source]
    [webchange.editor-v2.graph-builder.graph.phrases-graph--swings-expected :as swings-expected]
    [webchange.editor-v2.graph-builder.graph.phrases-graph--volleyball-source :as volleyball-source]
    [webchange.editor-v2.graph-builder.graph.phrases-graph--volleyball-expected :as volleyballs-expected]))

(defn remove-actions-data
  [graph]
  (reduce (fn [result [node-name node-data]]
            (assoc result node-name (dissoc node-data :data)))
          {}
          graph))

(deftest test-get-diagram-graph--home
  (let [scene-data home-source/data
        diagram-mode :phrases
        params {:start-node nil}]
    (let [actual-result (-> (get-diagram-graph scene-data diagram-mode params)
                            (remove-actions-data))
          expected-result home-expected/data]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))

(deftest test-get-diagram-graph--sandbox
  (let [scene-data sandbox-source/data
        diagram-mode :phrases
        params {:start-node nil}]
    (let [actual-result (-> (get-diagram-graph scene-data diagram-mode params)
                            (remove-actions-data))
          expected-result sandbox-expected/data]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))

(deftest test-get-diagram-graph--see-saw
  (let [scene-data see-saw-source/data
        diagram-mode :phrases
        params {:start-node nil}]
    (let [actual-result (-> (get-diagram-graph scene-data diagram-mode params)
                            (remove-actions-data))
          expected-result see-saw-expected/data]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))

(deftest test-get-diagram-graph--swings
  (let [scene-data swings-source/data
        diagram-mode :phrases
        params {:start-node nil}]
    (let [actual-result (-> (get-diagram-graph scene-data diagram-mode params)
                            (remove-actions-data))
          expected-result swings-expected/data]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))

(deftest test-get-diagram-graph--volleyball
  (let [scene-data volleyball-source/data
        diagram-mode :phrases
        params {:start-node nil}]
    (let [actual-result (-> (get-diagram-graph scene-data diagram-mode params)
                            (remove-actions-data))
          expected-result volleyballs-expected/data]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))
