(ns webchange.editor-v2.graph-builder.graph.park-poem-test
  (:require
    [cljs.test :refer [deftest testing]]
    [webchange.editor-v2.graph-builder.graph :refer [get-diagram-graph]]
    [webchange.editor-v2.graph-builder.graph.utils :refer [compare-results]]
    [webchange.editor-v2.graph-builder.graph.data.concept-sample :as concept]
    [webchange.editor-v2.graph-builder.graph.data.park-poem.source :as source]
    [webchange.editor-v2.graph-builder.graph.data.park-poem.dialog--finish :as finish]
    [webchange.editor-v2.graph-builder.graph.data.park-poem.dialog--poem :as poem]
    [webchange.editor-v2.graph-builder.graph.data.park-poem.dialog--repeat :as repeat]
    [webchange.editor-v2.graph-builder.graph.data.park-poem.dialog--welcome :as welcome]
    [webchange.editor-v2.graph-builder.graph.data.park-poem.scene-translation :as full-scene]))

(deftest test-get-diagram-graph--park-poem
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

  (deftest test-get-dialog-graph--park-poem--finish
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :mari-voice-finish))
                     finish/data))

  (deftest test-get-dialog-graph--park-poem--poem
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :run-concept-poem))
                     poem/data))

  (deftest test-get-dialog-graph--park-poem--repeat
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :mari-voice-now-repeat))
                     repeat/data))

  (deftest test-get-dialog-graph--park-poem--welcome
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :mari-voice-welcome))
                     welcome/data)))
