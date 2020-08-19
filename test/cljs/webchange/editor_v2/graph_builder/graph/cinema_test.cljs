(ns webchange.editor-v2.graph-builder.graph.cinema-test
  (:require
    [cljs.test :refer [deftest testing]]
    [webchange.editor-v2.graph-builder.graph :refer [get-diagram-graph]]
    [webchange.editor-v2.graph-builder.graph.utils :refer [compare-results]]
    [webchange.editor-v2.graph-builder.graph.data.concept-sample :as concept]
    [webchange.editor-v2.graph-builder.graph.data.cinema.source :as source]
    [webchange.editor-v2.graph-builder.graph.data.cinema.dialog--chant :as chant]
    [webchange.editor-v2.graph-builder.graph.data.cinema.dialog--finish :as finish]
    [webchange.editor-v2.graph-builder.graph.data.cinema.dialog--intro :as intro]
    [webchange.editor-v2.graph-builder.graph.data.cinema.dialog--next-photo :as next-photo]
    [webchange.editor-v2.graph-builder.graph.data.cinema.dialog--one-more-round :as one-more-round]))

(let [diagram-mode :translation
      scene-data source/data
      params {:concept-data {:current-concept concept/data}}]

  (deftest test-get-dialog-graph--cinema--chant
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :chant-current-letter))
                     chant/data))

  (deftest test-get-dialog-graph--cinema--finish
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :vaca-voice-finish))
                     finish/data))

  (deftest test-get-dialog-graph--cinema--intro
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :intro))
                     intro/data))

  (deftest test-get-dialog-graph--cinema--next-photo
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :vaca-voice-next))
                     next-photo/data))

  (deftest test-get-dialog-graph--cinema--one-more-round
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :vaca-voice-one-more-round))
                     one-more-round/data)))
