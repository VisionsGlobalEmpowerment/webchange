(ns webchange.templates.library.karaoke
  (:require
    [webchange.templates.core :as core]))

(def m {:id          35
        :name        "Karaoke"
        :tags        ["listening comprehension" "rhyming" "express ideas verbally"]
        :description "Karaoke"
        :options     {:video       {:label "Video File"
                                    :type  "video"}
                      :video-range {:label       "Video Range"
                                    :type        "video-range"
                                    :video-param "video"}}})

(def t {:assets        [{:url "/raw/clipart/recording_studio/recording_studio_background.png" :type "image"}
                        {:url "/raw/clipart/recording_studio/recording_studio_decoration.png" :type "image"}
                        {:url "/raw/clipart/recording_studio/recording_studio_surface.png" :type "image"}
                        {:url "/raw/clipart/karaoke/mic.png" :type "image"}
                        {:url "/raw/clipart/karaoke/play_button.png" :type "image"}
                        {:url "/raw/clipart/karaoke/record_button.png" :type "image"}]
        :objects       {:background         {:type       "layered-background"
                                             :background {:src "/raw/clipart/recording_studio/recording_studio_background.png"}
                                             :decoration {:src "/raw/clipart/recording_studio/recording_studio_decoration.png"}
                                             :surface    {:src "/raw/clipart/recording_studio/recording_studio_surface.png"}}
                        :mari               {:type       "animation"
                                             :x          1600
                                             :y          900
                                             :width      473
                                             :height     511
                                             :scene-name "mari"
                                             :transition "mari"
                                             :anim       "idle"
                                             :loop       true
                                             :name       "mari"
                                             :scale-x    0.5
                                             :scale-y    0.5
                                             :speed      0.35
                                             :start      true
                                             :actions    {:click {:id "mari-click" :on "click" :type "action"}}}
                        :video-screen       {:type       "group"
                                             :x          342
                                             :y          111
                                             :transition "video-screen"
                                             :children   ["video-background" "video"]
                                             :visible    false
                                             :states     {:hidden {:visible false} :visible {:visible true}}}
                        :video-background   {:type   "rectangle"
                                             :x      0
                                             :y      0
                                             :width  1200
                                             :height 800
                                             :fill   0x202C6D}
                        :video              {:type   "video"
                                             :x      0
                                             :y      0
                                             :width  1200
                                             :height 800}
                        :play-video-button  {:type       "image"
                                             :src        "/raw/clipart/karaoke/play_button.png"
                                             :transition "play-video-button"
                                             :x          880
                                             :y          478
                                             :width      128
                                             :height     128
                                             :editable?  true
                                             :states     {:hidden {:visible false} :visible {:visible true}}
                                             :actions    {:click {:id "on-play-video-button-clicked" :on "click" :type "action"}}}

                        :mic                {:type       "image"
                                             :src        "/raw/clipart/karaoke/mic.png"
                                             :transition "mic"
                                             :x          765
                                             :y          435
                                             :width      360
                                             :height     656
                                             :visible    false}
                        :play-record-button {:type       "image"
                                             :src        "/raw/clipart/karaoke/play_button.png"
                                             :transition "play-record-button"
                                             :x          880
                                             :y          678
                                             :width      128
                                             :height     128
                                             :visible    false
                                             :actions    {:click {:id "handle-play-button-click" :on "click" :type "action"}}}
                        :record-button      {:type       "image"
                                             :src        "/raw/clipart/karaoke/record_button.png"
                                             :transition "record-button"
                                             :x          880
                                             :y          528
                                             :width      128
                                             :height     128
                                             :visible    false}
                        }
        :scene-objects [["background"]
                        ["mic" "play-record-button" "record-button"]
                        ["video-screen" "play-video-button"]
                        ["mari"]]
        :actions       {:start-scene                  {:type "sequence"
                                                       :data ["start-activity"
                                                              "run-round-1"]}
                        :start-activity               {:type "start-activity"}

                        ;; Round 1

                        :run-round-1                  {:type "sequence"
                                                       :data ["show-video-screen"
                                                              "show-play-video-button"
                                                              "dialog-are-you-ready"
                                                              "highlight-play-video-button"]}
                        :dialog-are-you-ready         {:type               "sequence-data"
                                                       :editor-type        "dialog"
                                                       :concept-var        "current-concept"
                                                       :data               [{:type "sequence-data"
                                                                             :data [{:type "empty" :duration 0}
                                                                                    {:type "animation-sequence" :phrase-text "New action" :audio nil}]}]
                                                       :phrase             "instructions"
                                                       :phrase-description "Instructions"
                                                       :dialog-track       "Round 1"}
                        :show-video-screen            {:type "state" :id "visible" :target "video-screen"}
                        :show-play-video-button       {:type "state" :id "visible" :target "play-video-button"}
                        :hide-play-video-button       {:type "state" :id "hidden" :target "play-video-button"}
                        :highlight-play-video-button  {:type      "set-attribute"
                                                       :target    "play-video-button"
                                                       :attr-name "highlight" :attr-value true}
                        :play-video                   {:type   "play-video"
                                                       :target "video"
                                                       :src    "---"}
                        :on-play-video-button-clicked {:type "sequence"
                                                       :data ["hide-play-video-button"
                                                              "play-video"
                                                              "run-round-2"]}

                        ;; Round 2

                        :run-round-2                  {:type "empty" :duration 200}

                        ;; Finish

                        :stop-scene                   {:type "sequence" :data ["stop-activity"]}
                        :stop-activity                {:type "stop-activity"}

                        :finish-activity              {:type "finish-activity"}}
        :triggers      {:stop  {:on "back" :action "stop-scene"}
                        :start {:on "start" :action "start-scene"}}
        :metadata      {:autostart true}})

(defn- add-video
  [template {:keys [video]}]
  (-> template
      (update :assets conj {:url video :type "video"})
      (assoc-in [:actions :play-video :src] video)))

(defn f
  [t args]
  (-> t
      (add-video args)))

(core/register-template
  m
  (partial f t))
