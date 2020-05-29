(ns webchange.editor-v2.graph-builder.graph.writing-lesson-test
  (:require
    [cljs.test :refer [deftest testing]]
    [webchange.editor-v2.graph-builder.graph :refer [get-diagram-graph]]
    [webchange.editor-v2.graph-builder.graph.utils :refer [compare-results]]
    [webchange.editor-v2.graph-builder.graph.data.concept-sample :as concept]
    [webchange.editor-v2.graph-builder.graph.data.writing-lesson.source :as source]
    [webchange.editor-v2.graph-builder.graph.data.writing-lesson.dialog--draw :as draw]
    [webchange.editor-v2.graph-builder.graph.data.writing-lesson.dialog--draw-prepare :as draw-prepare]
    [webchange.editor-v2.graph-builder.graph.data.writing-lesson.dialog--intro-letter :as intro-letter]
    [webchange.editor-v2.graph-builder.graph.data.writing-lesson.dialog--intro-picture :as intro-picture]
    [webchange.editor-v2.graph-builder.graph.data.writing-lesson.dialog--invite-user :as invite-user]
    [webchange.editor-v2.graph-builder.graph.data.writing-lesson.dialog--welcome :as welcome]
    [webchange.editor-v2.graph-builder.graph.data.writing-lesson.scene-translation :as full-scene]))

(deftest test-get-diagram-graph--writing-lesson
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

  (deftest test-get-dialog-graph--writing-lesson--draw
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :letter-drawing))
                     draw/data))

  (deftest test-get-dialog-graph--writing-lesson--draw-prepare
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :letter-drawing-prepare))
                     draw-prepare/data))

  (deftest test-get-dialog-graph--writing-lesson--intro-letter
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :introduce-letter))
                     intro-letter/data))

  (deftest test-get-dialog-graph--writing-lesson--intro-picture
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :introduce-picture))
                     intro-picture/data))

  (deftest test-get-dialog-graph--writing-lesson--invite-user
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :invite-user))
                     invite-user/data))

  (deftest test-get-dialog-graph--writing-lesson--welcome
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :welcome-voice))
                     welcome/data)))
