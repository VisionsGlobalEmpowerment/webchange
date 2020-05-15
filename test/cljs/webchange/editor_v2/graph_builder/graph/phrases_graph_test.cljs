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
    [webchange.editor-v2.graph-builder.graph.phrases-graph--volleyball-expected :as volleyball-expected]
    [webchange.editor-v2.graph-builder.graph.phrases-graph--library-source :as library-source]
    [webchange.editor-v2.graph-builder.graph.phrases-graph--library-expected :as library-expected]
    [webchange.editor-v2.graph-builder.graph.phrases-graph--book-source :as book-source]
    [webchange.editor-v2.graph-builder.graph.phrases-graph--book-expected :as book-expected]
    [webchange.editor-v2.graph-builder.graph.phrases-graph--hide-n-seek-source :as hide-n-seek-source]
    [webchange.editor-v2.graph-builder.graph.phrases-graph--hide-n-seek-expected :as hide-n-seek-expected]
    [webchange.editor-v2.graph-builder.graph.phrases-graph--cycling-source :as cycling-source]
    [webchange.editor-v2.graph-builder.graph.phrases-graph--cycling-expected :as cycling-expected]
    [webchange.editor-v2.graph-builder.graph.phrases-graph--cinema-source :as cinema-source]
    [webchange.editor-v2.graph-builder.graph.phrases-graph--cinema-expected :as cinema-expected]))

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
          expected-result volleyball-expected/data]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))

(deftest test-get-diagram-graph--library
  (let [scene-data library-source/data
        diagram-mode :phrases
        params {:start-node nil}]
    (let [actual-result (-> (get-diagram-graph scene-data diagram-mode params)
                            (remove-actions-data))
          expected-result library-expected/data]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))

(deftest test-get-diagram-graph--book
  (let [scene-data book-source/data
        diagram-mode :phrases
        params {:start-node nil}]
    (let [actual-result (-> (get-diagram-graph scene-data diagram-mode params)
                            (remove-actions-data))
          expected-result book-expected/data]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))

(deftest test-get-diagram-graph--hide-n-seek
  (let [scene-data hide-n-seek-source/data
        diagram-mode :phrases
        params {:start-node nil}]
    (let [actual-result (-> (get-diagram-graph scene-data diagram-mode params)
                            (remove-actions-data))
          expected-result hide-n-seek-expected/data]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))

(deftest test-get-diagram-graph--cycling
  (let [scene-data cycling-source/data
        diagram-mode :phrases
        params {:start-node nil}]
    (let [actual-result (-> (get-diagram-graph scene-data diagram-mode params)
                            (remove-actions-data))
          expected-result cycling-expected/data]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))

(deftest test-get-diagram-graph--cinema
  (let [scene-data cinema-source/data
        diagram-mode :phrases
        params {:start-node nil}]
    (let [actual-result (-> (get-diagram-graph scene-data diagram-mode params)
                            (remove-actions-data))
          expected-result cinema-expected/data]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))
