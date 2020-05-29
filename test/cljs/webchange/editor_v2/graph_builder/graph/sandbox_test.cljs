(ns webchange.editor-v2.graph-builder.graph.sandbox-test
  (:require
    [cljs.test :refer [deftest testing]]
    [webchange.editor-v2.graph-builder.graph :refer [get-diagram-graph]]
    [webchange.editor-v2.graph-builder.graph.utils :refer [compare-results]]
    [webchange.editor-v2.graph-builder.graph.data.concept-sample :as concept]
    [webchange.editor-v2.graph-builder.graph.data.sandbox.source :as source]
    [webchange.editor-v2.graph-builder.graph.data.sandbox.dialog--learn-concept :as learn-concept]
    [webchange.editor-v2.graph-builder.graph.data.sandbox.dialog--more-words :as mode-words]
    [webchange.editor-v2.graph-builder.graph.data.sandbox.dialog--welcome :as welcome]
    [webchange.editor-v2.graph-builder.graph.data.sandbox.dialog--word-1 :as word-1]
    [webchange.editor-v2.graph-builder.graph.data.sandbox.dialog--word-2 :as word-2]
    [webchange.editor-v2.graph-builder.graph.data.sandbox.dialog--word-3 :as word-3]
    [webchange.editor-v2.graph-builder.graph.data.sandbox.dialog--word-4 :as word-4]
    [webchange.editor-v2.graph-builder.graph.data.sandbox.scene-translation :as full-scene]))

(deftest test-get-diagram-graph--sandbox
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

  (deftest test-get-dialog-graph--home--learn-concept
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :letter-intro))
                     learn-concept/data))

  (deftest test-get-dialog-graph--home--mode-words
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :mari-more-audio))
                     mode-words/data))

  (deftest test-get-dialog-graph--home--welcome
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :mari-welcome-audio))
                     welcome/data))

  (deftest test-get-dialog-graph--home--word-1
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :word-1-state-var))
                     word-1/data))

  (deftest test-get-dialog-graph--home--word-2
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :word-2-state-var))
                     word-2/data))

  (deftest test-get-dialog-graph--home--word-3
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :word-3-state-var))
                     word-3/data))

  (deftest test-get-dialog-graph--home--word-4
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :word-4-state-var))
                     word-4/data)))
