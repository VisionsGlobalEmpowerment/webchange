(ns webchange.editor-v2.graph-builder.graph.cinema-video-test
  (:require
    [cljs.test :refer [deftest testing]]
    [webchange.editor-v2.graph-builder.graph :refer [get-diagram-graph]]
    [webchange.editor-v2.graph-builder.graph.utils :refer [compare-results]]
    [webchange.editor-v2.graph-builder.graph.data.concept-sample :as concept]
    [webchange.editor-v2.graph-builder.graph.data.cinema-video.source :as source]
    [webchange.editor-v2.graph-builder.graph.data.cinema-video.dialog--finish :as finish]
    [webchange.editor-v2.graph-builder.graph.data.cinema-video.dialog--lets-watch :as lets-watch]
    [webchange.editor-v2.graph-builder.graph.data.cinema-video.dialog--touch-to-play :as touch-to-play]
    [webchange.editor-v2.graph-builder.graph.data.cinema-video.dialog--welcome :as welcome]
    [webchange.editor-v2.graph-builder.graph.data.cinema-video.scene-translation :as full-scene]))

(deftest test-get-diagram-graph--cinema-video
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

  (deftest test-get-dialog-graph--cinema-video--finish
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :play-video-finish))
                     finish/data))

  (deftest test-get-dialog-graph--cinema-video--lets-watch
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :vaca-voice-lets-watch))
                     lets-watch/data))

  (deftest test-get-dialog-graph--cinema-video--touch-to-play
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :mari-voice-touch))
                     touch-to-play/data))

  (deftest test-get-dialog-graph--cinema-video--welcome
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :vaca-voice-wonderful))
                     welcome/data)))
