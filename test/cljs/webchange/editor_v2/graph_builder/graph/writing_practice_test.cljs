(ns webchange.editor-v2.graph-builder.graph.writing-practice-test
  (:require
    [cljs.test :refer [deftest testing]]
    [webchange.editor-v2.graph-builder.graph :refer [get-diagram-graph]]
    [webchange.editor-v2.graph-builder.graph.utils :refer [compare-results]]
    [webchange.editor-v2.graph-builder.graph.data.concept-sample :as concept]
    [webchange.editor-v2.graph-builder.graph.data.writing-practice.source :as source]
    [webchange.editor-v2.graph-builder.graph.data.writing-practice.dialog--free-drawing :as free-drawing]
    [webchange.editor-v2.graph-builder.graph.data.writing-practice.dialog--go-next :as go-next]
    [webchange.editor-v2.graph-builder.graph.data.writing-practice.dialog--welcome :as welcome]))

(let [diagram-mode :translation
      scene-data source/data
      params {:concept-data {:current-concept concept/data}}]

  (deftest test-get-dialog-graph--writing-practice--free-drawing
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :mari-voice-intro-2))
                     free-drawing/data))

  (deftest test-get-dialog-graph--writing-practice--go-next
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :mari-voice-go-next-practice))
                     go-next/data))

  (deftest test-get-dialog-graph--writing-practice--welcome
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :mari-voice-intro))
                     welcome/data)))
