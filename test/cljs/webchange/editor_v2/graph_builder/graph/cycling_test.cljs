(ns webchange.editor-v2.graph-builder.graph.cycling-test
  (:require
    [cljs.test :refer [deftest testing]]
    [webchange.editor-v2.graph-builder.graph :refer [get-diagram-graph]]
    [webchange.editor-v2.graph-builder.graph.utils :refer [compare-results]]
    [webchange.editor-v2.graph-builder.graph.data.concept-sample :as concept]
    [webchange.editor-v2.graph-builder.graph.data.cycling.source :as source]
    [webchange.editor-v2.graph-builder.graph.data.cycling.dialog--chant :as chant]
    [webchange.editor-v2.graph-builder.graph.data.cycling.dialog--correct :as correct]
    [webchange.editor-v2.graph-builder.graph.data.cycling.dialog--welcome :as welcome]
    [webchange.editor-v2.graph-builder.graph.data.cycling.dialog--wrong :as wrong]
    [webchange.editor-v2.graph-builder.graph.data.cycling.scene-translation :as full-scene]))

(deftest test-get-diagram-graph--cycling
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

  (deftest test-get-dialog-graph--cycling--chant
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :voice-high-var))
                     chant/data))

  (deftest test-get-dialog-graph--cycling--correct
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :mari-says-correct))
                     correct/data))

  (deftest test-get-dialog-graph--cycling--welcome
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :mari-welcome-audio-1))
                     welcome/data))

  (deftest test-get-dialog-graph--cycling--wrong
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :mari-says-wrong))
                     wrong/data)))
