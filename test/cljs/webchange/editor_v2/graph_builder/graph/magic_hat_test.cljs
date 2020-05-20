(ns webchange.editor-v2.graph-builder.graph.magic-hat-test
  (:require
    [cljs.test :refer [deftest testing]]
    [webchange.editor-v2.graph-builder.graph :refer [get-diagram-graph]]
    [webchange.editor-v2.graph-builder.graph.utils :refer [compare-results]]
    [webchange.editor-v2.graph-builder.graph.data.concept-sample :as concept]
    [webchange.editor-v2.graph-builder.graph.data.magic-hat.source :as source]
    [webchange.editor-v2.graph-builder.graph.data.magic-hat.dialog--chant :as chant]
    [webchange.editor-v2.graph-builder.graph.data.magic-hat.dialog--correct :as correct]
    [webchange.editor-v2.graph-builder.graph.data.magic-hat.dialog--sound :as sound]
    [webchange.editor-v2.graph-builder.graph.data.magic-hat.dialog--welcome :as welcome]
    [webchange.editor-v2.graph-builder.graph.data.magic-hat.dialog--wrong :as wrong]
    [webchange.editor-v2.graph-builder.graph.data.magic-hat.scene-translation :as full-scene]))

;(deftest test-get-diagram-graph--magic-hat
;  (let [diagram-mode :phrases
;        scene-data source/data
;        params {:start-node nil}]
;    (compare-results (get-diagram-graph scene-data diagram-mode params)
;                     full-scene/data
;                     {:pick-data?   false
;                      :keep-phrase? false})))

(let [diagram-mode :translation
      scene-data source/data
      params {:concept-data {:current-concept concept/data}}]

  (deftest test-get-dialog-graph--magic-hat--chant
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :current-concept-chant))
                     chant/data))

  (deftest test-get-dialog-graph--magic-hat--correct
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :mari-says-correct-answer))
                     correct/data))

  (deftest test-get-dialog-graph--magic-hat--sound
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :current-concept-sound-x3))
                     sound/data))

  (deftest test-get-dialog-graph--magic-hat--welcome
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :intro))
                     welcome/data))

  (deftest test-get-dialog-graph--magic-hat--wrong
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :mari-says-wrong-answer))
                     wrong/data)))
