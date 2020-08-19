(ns webchange.editor-v2.graph-builder.graph.letter-intro-test
  (:require
    [cljs.test :refer [deftest testing]]
    [webchange.editor-v2.graph-builder.graph :refer [get-diagram-graph]]
    [webchange.editor-v2.graph-builder.graph.utils :refer [compare-results]]
    [webchange.editor-v2.graph-builder.graph.data.concept-sample :as concept]
    [webchange.editor-v2.graph-builder.graph.data.letter-intro.source :as source]
    [webchange.editor-v2.graph-builder.graph.data.letter-intro.dialog--current-sound :as current-sound]
    [webchange.editor-v2.graph-builder.graph.data.letter-intro.dialog--finish :as finish]
    [webchange.editor-v2.graph-builder.graph.data.letter-intro.dialog--intro :as intro]
    [webchange.editor-v2.graph-builder.graph.data.letter-intro.dialog--stage-big :as stage-big]
    [webchange.editor-v2.graph-builder.graph.data.letter-intro.dialog--stage-small :as stage-small]
    [webchange.editor-v2.graph-builder.graph.data.letter-intro.dialog--touch-big :as touch-big]
    [webchange.editor-v2.graph-builder.graph.data.letter-intro.dialog--touch-small :as touch-small]
    [webchange.editor-v2.graph-builder.graph.data.letter-intro.dialog--two-ways :as two-ways]))

(let [diagram-mode :translation
      scene-data source/data
      params {:concept-data {:current-concept concept/data}}]

  (deftest test-get-dialog-graph--letter-intro--current-sound
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :current-sound))
                     current-sound/data))

  (deftest test-get-dialog-graph--letter-intro--finish
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :mari-voice-finish))
                     finish/data))

  (deftest test-get-dialog-graph--letter-intro--intro
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :stage-intro))
                     intro/data))

  (deftest test-get-dialog-graph--letter-intro--stage-big
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :stage-big))
                     stage-big/data))

  (deftest test-get-dialog-graph--letter-intro--stage-small
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :stage-small))
                     stage-small/data))

  (deftest test-get-dialog-graph--letter-intro--touch-big
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :touch-big-letter))
                     touch-big/data))

  (deftest test-get-dialog-graph--letter-intro--touch-small
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :touch-small-letter))
                     touch-small/data))

  (deftest test-get-dialog-graph--letter-intro--two-ways
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :vaca-voice-2-ways-write))
                     two-ways/data)))
