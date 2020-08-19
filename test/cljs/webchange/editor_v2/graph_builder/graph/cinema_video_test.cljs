(ns webchange.editor-v2.graph-builder.graph.cinema-video-test
  (:require
    [cljs.test :refer [deftest testing]]
    [webchange.editor-v2.graph-builder.graph :refer [get-diagram-graph]]
    [webchange.editor-v2.graph-builder.graph.utils :refer [compare-results]]
    [webchange.editor-v2.graph-builder.graph.data.concept-sample :as concept]
    [webchange.editor-v2.graph-builder.graph.data.cinema-video.source :as source]
    [webchange.editor-v2.graph-builder.graph.data.cinema-video.dialog--click-to-next :as click-to-next]
    [webchange.editor-v2.graph-builder.graph.data.cinema-video.dialog--click-to-replay :as click-to-replay]
    [webchange.editor-v2.graph-builder.graph.data.cinema-video.dialog--lets-watch :as lets-watch]
    [webchange.editor-v2.graph-builder.graph.data.cinema-video.dialog--touch-to-play :as touch-to-play]
    [webchange.editor-v2.graph-builder.graph.data.cinema-video.dialog--very-good :as very-good]
    [webchange.editor-v2.graph-builder.graph.data.cinema-video.dialog--welcome :as welcome]))

(let [diagram-mode :translation
      scene-data source/data
      params {:concept-data {:current-concept concept/data}}]

  (deftest test-get-dialog-graph--cinema-video--click-to-next
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :mari-voice-touch-again-1))
                     click-to-next/data))

  (deftest test-get-dialog-graph--cinema-video--click-to-replay
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :mari-voice-touch-again-2))
                     click-to-replay/data))

  (deftest test-get-dialog-graph--cinema-video--lets-watch
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :vaca-voice-lets-watch))
                     lets-watch/data))

  (deftest test-get-dialog-graph--cinema-video--touch-to-play
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :mari-voice-touch))
                     touch-to-play/data))

  (deftest test-get-dialog-graph--cinema-video--very-good
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :vaca-voice-very-good))
                     very-good/data))

  (deftest test-get-dialog-graph--cinema-video--welcome
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :vaca-voice-wonderful))
                     welcome/data)))
