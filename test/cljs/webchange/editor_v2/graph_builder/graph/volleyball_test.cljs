(ns webchange.editor-v2.graph-builder.graph.volleyball-test
  (:require
    [cljs.test :refer [deftest testing]]
    [webchange.editor-v2.graph-builder.graph :refer [get-diagram-graph]]
    [webchange.editor-v2.graph-builder.graph.utils :refer [compare-results]]
    [webchange.editor-v2.graph-builder.graph.data.concept-sample :as concept]
    [webchange.editor-v2.graph-builder.graph.data.volleyball.source :as source]
    [webchange.editor-v2.graph-builder.graph.data.volleyball.dialog--correct :as correct]
    [webchange.editor-v2.graph-builder.graph.data.volleyball.dialog--welcome :as welcome]
    [webchange.editor-v2.graph-builder.graph.data.volleyball.dialog--word-ardilla :as word-ardilla]
    [webchange.editor-v2.graph-builder.graph.data.volleyball.dialog--word-iman :as word-iman]
    [webchange.editor-v2.graph-builder.graph.data.volleyball.dialog--word-oso :as word-oso]
    [webchange.editor-v2.graph-builder.graph.data.volleyball.dialog--wrong :as wrong]
    [webchange.editor-v2.graph-builder.graph.data.volleyball.scene-translation :as full-scene]))

(deftest test-get-diagram-graph--volleyball
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

  (deftest test-get-dialog-graph--volleyball--correct
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :mari-say-correct))
                     correct/data))

  (deftest test-get-dialog-graph--volleyball--welcome
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :mari-welcome-audio-1))
                     welcome/data))

  (deftest test-get-dialog-graph--volleyball--word-ardilla
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :word-ardilla-high))
                     word-ardilla/data))

  (deftest test-get-dialog-graph--volleyball--word-iman
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :word-incendio-high))
                     word-iman/data))

  (deftest test-get-dialog-graph--volleyball--word-oso
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :word-oso-high))
                     word-oso/data))

  (deftest test-get-dialog-graph--volleyball--wrong
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :mari-audio-wrong))
                     wrong/data)))
