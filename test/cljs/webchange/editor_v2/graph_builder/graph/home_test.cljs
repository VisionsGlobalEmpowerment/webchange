(ns webchange.editor-v2.graph-builder.graph.home-test
  (:require
    [cljs.test :refer [deftest testing]]
    [webchange.editor-v2.graph-builder.graph :refer [get-diagram-graph]]
    [webchange.editor-v2.graph-builder.graph.utils :refer [compare-results]]
    [webchange.editor-v2.graph-builder.graph.data.concept-sample :as concept]
    [webchange.editor-v2.graph-builder.graph.data.home.source :as source]
    [webchange.editor-v2.graph-builder.graph.data.home.dialog--chant-concept :as chant-concept]
    [webchange.editor-v2.graph-builder.graph.data.home.dialog--goodbye-concept :as goodbye-concept]
    [webchange.editor-v2.graph-builder.graph.data.home.dialog--intro :as intro]
    [webchange.editor-v2.graph-builder.graph.data.home.dialog--introduce-concept :as introduce-concept]
    [webchange.editor-v2.graph-builder.graph.data.home.dialog--repeat-or-finish :as repeat-or-finish]
    [webchange.editor-v2.graph-builder.graph.data.home.dialog--touch-first-box :as touch-first-box]
    [webchange.editor-v2.graph-builder.graph.data.home.dialog--touch-second-box :as touch-second-box]
    [webchange.editor-v2.graph-builder.graph.data.home.dialog--touch-third-box :as touch-third-box]
    [webchange.editor-v2.graph-builder.graph.data.home.dialog--wrong-answer :as wrong-answer]
    [webchange.editor-v2.graph-builder.graph.data.home.scene-translation :as full-scene]))

(deftest test-get-diagram-graph--home
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

  (deftest test-get-dialog-graph--home--chant-concept
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :concept-chant))
                     chant-concept/data))

  (deftest test-get-dialog-graph--home--goodbye-concept
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :vaca-goodbye-var))
                     goodbye-concept/data))

  (deftest test-get-dialog-graph--home--intro
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :senora-vaca-audio-1))
                     intro/data))

  (deftest test-get-dialog-graph--home--introduce-concept
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :concept-intro))
                     introduce-concept/data))

  (deftest test-get-dialog-graph--home--repeat-or-finish
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :mari-finish))
                     repeat-or-finish/data))

  (deftest test-get-dialog-graph--home--touch-first-box
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :senora-vaca-audio-2))
                     touch-first-box/data))

  (deftest test-get-dialog-graph--home--touch-second-box
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :senora-vaca-audio-touch-second-box))
                     touch-second-box/data))

  (deftest test-get-dialog-graph--home--touch-third-box
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :senora-vaca-audio-touch-third-box))
                     touch-third-box/data))

  (deftest test-get-dialog-graph--home--wrong-answer
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :pick-wrong))
                     wrong-answer/data)))
