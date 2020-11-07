(ns webchange.editor-v2.graph-builder.graph.volleyball-letters-test
  (:require
    [cljs.test :refer [deftest testing]]
    [webchange.editor-v2.graph-builder.graph :refer [get-diagram-graph]]
    [webchange.editor-v2.graph-builder.graph.utils :refer [compare-results]]
    [webchange.editor-v2.graph-builder.graph.data.concept-sample :as concept]
    [webchange.editor-v2.graph-builder.graph.data.volleyball-letters.source :as source]
    [webchange.editor-v2.graph-builder.graph.data.volleyball-letters.dialog--chant :as chant]
    [webchange.editor-v2.graph-builder.graph.data.volleyball-letters.dialog--correct :as correct]
    [webchange.editor-v2.graph-builder.graph.data.volleyball-letters.dialog--welcome :as welcome]
    [webchange.editor-v2.graph-builder.graph.data.volleyball-letters.dialog--wrong :as wrong]))

(let [diagram-mode :translation
      scene-data source/data
      params {:concept-data {:current-concept concept/data}}]

  (deftest test-get-dialog-graph--volleyball-letters--chant
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :voice-high-var))
                     chant/data))

  (deftest test-get-dialog-graph--volleyball-letters--correct
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :mari-correct))
                     correct/data))

  (deftest test-get-dialog-graph--volleyball-letters--welcome
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :mari-welcome-audio))
                     welcome/data))

  (deftest test-get-dialog-graph--volleyball-letters--wrong
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :mari-wrong))
                     wrong/data)))
