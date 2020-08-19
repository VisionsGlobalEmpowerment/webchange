(ns webchange.editor-v2.graph-builder.graph.cycling-letters-test
  (:require
    [cljs.test :refer [deftest testing]]
    [webchange.editor-v2.graph-builder.graph :refer [get-diagram-graph]]
    [webchange.editor-v2.graph-builder.graph.utils :refer [compare-results]]
    [webchange.editor-v2.graph-builder.graph.data.concept-sample :as concept]
    [webchange.editor-v2.graph-builder.graph.data.cycling-letters.source :as source]
    [webchange.editor-v2.graph-builder.graph.data.cycling-letters.dialog--chant :as chant]
    [webchange.editor-v2.graph-builder.graph.data.cycling-letters.dialog--correct :as correct]
    [webchange.editor-v2.graph-builder.graph.data.cycling-letters.dialog--welcome :as welcome]
    [webchange.editor-v2.graph-builder.graph.data.cycling-letters.dialog--wrong :as wrong]))

(let [diagram-mode :translation
      scene-data source/data
      params {:concept-data {:current-concept concept/data}}]

  (deftest test-get-dialog-graph--cycling-letters--chant
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :current-concept-sound-x3))
                     chant/data))

  (deftest test-get-dialog-graph--cycling-letters--correct
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :mari-correct))
                     correct/data))

  (deftest test-get-dialog-graph--cycling-letters--welcome
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :mari-welcome-audio))
                     welcome/data))

  (deftest test-get-dialog-graph--cycling-letters--wrong
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :mari-wrong))
                     wrong/data)))
