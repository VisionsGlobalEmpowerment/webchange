(ns webchange.editor-v2.graph-builder.graph.running-test
  (:require
    [cljs.test :refer [deftest testing]]
    [webchange.editor-v2.graph-builder.graph :refer [get-diagram-graph]]
    [webchange.editor-v2.graph-builder.graph.utils :refer [compare-results]]
    [webchange.editor-v2.graph-builder.graph.data.concept-sample :as concept]
    [webchange.editor-v2.graph-builder.graph.data.running.source :as source]
    [webchange.editor-v2.graph-builder.graph.data.running.dialog--chant :as chant]
    [webchange.editor-v2.graph-builder.graph.data.running.dialog--correct :as correct]
    [webchange.editor-v2.graph-builder.graph.data.running.dialog--welcome :as welcome]
    [webchange.editor-v2.graph-builder.graph.data.running.dialog--wrong :as wrong]
    [webchange.editor-v2.graph-builder.graph.data.running.scene-translation :as full-scene]))

(deftest test-get-diagram-graph--running
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

  (deftest test-get-dialog-graph--running--chant
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :current-concept-chant))
                     chant/data))

  (deftest test-get-dialog-graph--running--correct
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :mari-voice-correct))
                     correct/data))

  (deftest test-get-dialog-graph--running--welcome
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :mari-voice-welcome))
                     welcome/data))

  (deftest test-get-dialog-graph--running--wrong
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :mari-voice-wrong))
                     wrong/data)))
