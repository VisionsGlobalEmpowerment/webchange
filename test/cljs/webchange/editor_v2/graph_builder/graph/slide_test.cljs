(ns webchange.editor-v2.graph-builder.graph.slide-test
  (:require
    [cljs.test :refer [deftest testing]]
    [webchange.editor-v2.graph-builder.graph :refer [get-diagram-graph]]
    [webchange.editor-v2.graph-builder.graph.utils :refer [compare-results]]
    [webchange.editor-v2.graph-builder.graph.data.concept-sample :as concept]
    [webchange.editor-v2.graph-builder.graph.data.slide.source :as source]
    [webchange.editor-v2.graph-builder.graph.data.slide.dialog--correct :as correct]
    [webchange.editor-v2.graph-builder.graph.data.slide.dialog--riddle :as riddle]
    [webchange.editor-v2.graph-builder.graph.data.slide.dialog--try-another :as try-another]
    [webchange.editor-v2.graph-builder.graph.data.slide.dialog--welcome :as welcome]
    [webchange.editor-v2.graph-builder.graph.data.slide.dialog--wrong :as wrong]
    [webchange.editor-v2.graph-builder.graph.data.slide.scene-translation :as full-scene]))

(deftest test-get-diagram-graph--slide
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

  (deftest test-get-dialog-graph--slide--correct
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :mari-voice-correct))
                     correct/data))

  (deftest test-get-dialog-graph--slide--riddle
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :riddle))
                     riddle/data))

  (deftest test-get-dialog-graph--slide--try-another
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :mari-voice-try-another))
                     try-another/data))

  (deftest test-get-dialog-graph--slide--welcome
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :mari-voice-welcome))
                     welcome/data))

  (deftest test-get-dialog-graph--slide--wrong
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :mari-voice-wrong))
                     wrong/data)))
