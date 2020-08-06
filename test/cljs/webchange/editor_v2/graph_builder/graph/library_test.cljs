(ns webchange.editor-v2.graph-builder.graph.library-test
  (:require
    [cljs.test :refer [deftest testing]]
    [webchange.editor-v2.graph-builder.graph :refer [get-diagram-graph]]
    [webchange.editor-v2.graph-builder.graph.utils :refer [compare-results]]
    [webchange.editor-v2.graph-builder.graph.data.concept-sample :as concept]
    [webchange.editor-v2.graph-builder.graph.data.library.source :as source]
    [webchange.editor-v2.graph-builder.graph.data.library.dialog--agree :as agree]
    [webchange.editor-v2.graph-builder.graph.data.library.dialog--start :as start]
    [webchange.editor-v2.graph-builder.graph.data.library.dialog--welcome :as welcome]))

(let [diagram-mode :translation
      scene-data source/data
      params {:concept-data {:current-concept concept/data}}]

  (deftest test-get-dialog-graph--library--agree
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :vera-agree))
                     agree/data))

  (deftest test-get-dialog-graph--library--start
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :start-reading))
                     start/data))

  (deftest test-get-dialog-graph--library--welcome
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :welcome))
                     welcome/data)))
