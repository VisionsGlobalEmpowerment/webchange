(ns webchange.editor-v2.graph-builder.graph.swings-test
  (:require
    [cljs.test :refer [deftest testing]]
    [webchange.editor-v2.graph-builder.graph :refer [get-diagram-graph]]
    [webchange.editor-v2.graph-builder.graph.utils :refer [compare-results]]
    [webchange.editor-v2.graph-builder.graph.data.concept-sample :as concept]
    [webchange.editor-v2.graph-builder.graph.data.swings.source :as source]
    [webchange.editor-v2.graph-builder.graph.data.swings.dialog--finish :as finish]
    [webchange.editor-v2.graph-builder.graph.data.swings.dialog--move-to-start :as move-to-start]
    [webchange.editor-v2.graph-builder.graph.data.swings.dialog--story :as story]
    [webchange.editor-v2.graph-builder.graph.data.swings.dialog--try-another :as try-another]
    [webchange.editor-v2.graph-builder.graph.data.swings.dialog--welcome :as welcome]
    [webchange.editor-v2.graph-builder.graph.data.swings.scene-translation :as full-scene]))

(deftest test-get-diagram-graph--swings
  (let [diagram-mode :phrases
        scene-data source/data
        params {:start-node nil}]
    (compare-results (get-diagram-graph scene-data diagram-mode params)
                     full-scene/data
                     {:pick-data?   false
                      :keep-phrase? false})))

(let [diagram-mode :translation
      scene-data source/data
      params {:concept-data {:current-concept concept/data}}]

  (deftest test-get-dialog-graph--swings--finish
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :mari-finish))
                     finish/data))

  (deftest test-get-dialog-graph--swings--move-to-start
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :mari-move-to-start))
                     move-to-start/data))

  (deftest test-get-dialog-graph--swings--story
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :dialog-var))
                     story/data))

  (deftest test-get-dialog-graph--swings--try-another
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :try-another))
                     try-another/data))

  (deftest test-get-dialog-graph--swings--welcome
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :welcome-audio))
                     welcome/data)))
