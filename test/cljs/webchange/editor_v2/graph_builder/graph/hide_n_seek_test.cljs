(ns webchange.editor-v2.graph-builder.graph.hide-n-seek-test
  (:require
    [cljs.test :refer [deftest testing]]
    [webchange.editor-v2.graph-builder.graph :refer [get-diagram-graph]]
    [webchange.editor-v2.graph-builder.graph.utils :refer [compare-results]]
    [webchange.editor-v2.graph-builder.graph.data.concept-sample :as concept]
    [webchange.editor-v2.graph-builder.graph.data.hide-n-seek.source :as source]
    [webchange.editor-v2.graph-builder.graph.data.hide-n-seek.dialog--correct :as correct]
    [webchange.editor-v2.graph-builder.graph.data.hide-n-seek.dialog--riddle :as riddle]
    [webchange.editor-v2.graph-builder.graph.data.hide-n-seek.dialog--try-again :as try-again]
    [webchange.editor-v2.graph-builder.graph.data.hide-n-seek.dialog--welcome :as welcome]
    [webchange.editor-v2.graph-builder.graph.data.hide-n-seek.scene-translation :as full-scene]))

(deftest test-get-diagram-graph--hide-n-seek
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

  (deftest test-get-dialog-graph--hide-n-seek--correct
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :mari-audio-correct))
                     correct/data))

  (deftest test-get-dialog-graph--hide-n-seek--riddle
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :make-riddle))
                     riddle/data))

  (deftest test-get-dialog-graph--hide-n-seek--try-again
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :mari-audio-try-again))
                     try-again/data))

  (deftest test-get-dialog-graph--hide-n-seek--welcome
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :mari-welcome-audio))
                     welcome/data)))
