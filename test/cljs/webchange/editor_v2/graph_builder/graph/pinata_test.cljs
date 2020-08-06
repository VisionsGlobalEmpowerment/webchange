(ns webchange.editor-v2.graph-builder.graph.pinata-test
  (:require
    [cljs.test :refer [deftest testing]]
    [webchange.editor-v2.graph-builder.graph :refer [get-diagram-graph]]
    [webchange.editor-v2.graph-builder.graph.utils :refer [compare-results]]
    [webchange.editor-v2.graph-builder.graph.data.concept-sample :as concept]
    [webchange.editor-v2.graph-builder.graph.data.pinata.source :as source]
    [webchange.editor-v2.graph-builder.graph.data.pinata.dialog--chant :as chant]
    [webchange.editor-v2.graph-builder.graph.data.pinata.dialog--correct :as correct]
    [webchange.editor-v2.graph-builder.graph.data.pinata.dialog--welcome :as welcome]
    [webchange.editor-v2.graph-builder.graph.data.pinata.dialog--wrong :as wrong]))

(let [diagram-mode :translation
      scene-data source/data
      params {:concept-data {:current-concept concept/data}}]

  (deftest test-get-dialog-graph--pinata--chant
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :mari-says-task))
                     chant/data))

  (deftest test-get-dialog-graph--pinata--correct
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :mari-correct))
                     correct/data))

  (deftest test-get-dialog-graph--pinata--welcome
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :mari-voice-welcome))
                     welcome/data))

  (deftest test-get-dialog-graph--pinata--wrong
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :mari-wrong))
                     wrong/data)))
