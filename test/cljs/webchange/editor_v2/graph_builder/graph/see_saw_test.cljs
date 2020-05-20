(ns webchange.editor-v2.graph-builder.graph.see-saw-test
  (:require
    [cljs.test :refer [deftest testing]]
    [webchange.editor-v2.graph-builder.graph :refer [get-diagram-graph]]
    [webchange.editor-v2.graph-builder.graph.utils :refer [compare-results]]
    [webchange.editor-v2.graph-builder.graph.data.concept-sample :as concept]
    [webchange.editor-v2.graph-builder.graph.data.see-saw.source :as source]
    [webchange.editor-v2.graph-builder.graph.data.see-saw.dialog--finish :as finish]
    [webchange.editor-v2.graph-builder.graph.data.see-saw.dialog--introduce-concept :as introduce-concept]
    [webchange.editor-v2.graph-builder.graph.data.see-saw.dialog--move-to-start :as move-to-start]
    [webchange.editor-v2.graph-builder.graph.data.see-saw.dialog--try-another :as try-another]
    [webchange.editor-v2.graph-builder.graph.data.see-saw.dialog--welcome :as welcome]
    [webchange.editor-v2.graph-builder.graph.data.see-saw.scene-translation :as full-scene]))

(deftest test-get-diagram-graph--see-saw
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

  (deftest test-get-dialog-graph--see-saw--finish
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :mari-finish))
                     finish/data))

  (deftest test-get-dialog-graph--see-saw--try-another
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :try-another))
                     try-another/data))

  (deftest test-get-dialog-graph--see-saw--welcome
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :mari-welcome-audio))
                     welcome/data))

  (deftest test-get-dialog-graph--see-saw--move-to-start
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :mari-move-to-start))
                     move-to-start/data))

  (deftest test-get-dialog-graph--see-saw--introduce-concept
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :introduce-concept))
                     introduce-concept/data)))
