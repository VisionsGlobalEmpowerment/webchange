(ns webchange.templates.library.karaoke
  (:require
    [clojure.tools.logging :as log]
    [webchange.dev-templates :as templates]
    [webchange.templates.core :as core]))

(def m {:id          35
        :name        "Karaoke"
        :tags        ["listening comprehension" "rhyming" "express ideas verbally"]
        :description "Karaoke"
        :options     {:video        {:label "Video File"
                                     :type  "video"}
                      :video-ranges {:label       "Video Range"
                                     :type        "video-ranges"
                                     :video-param "video"
                                     :max         3}}})

(def t {:assets        [{:url "/raw/clipart/recording_studio/recording_studio_background.png" :type "image"}
                        {:url "/raw/clipart/recording_studio/recording_studio_decoration.png" :type "image"}
                        {:url "/raw/clipart/recording_studio/recording_studio_surface.png" :type "image"}
                        {:url "/raw/clipart/karaoke/mic.png" :type "image"}
                        {:url "/raw/clipart/karaoke/play_button.png" :type "image"}
                        {:url "/raw/clipart/karaoke/record_button.png" :type "image"}]
        :objects       {:background              {:type       "layered-background"
                                                  :background {:src "/raw/clipart/recording_studio/recording_studio_background.png"}
                                                  :decoration {:src "/raw/clipart/recording_studio/recording_studio_decoration.png"}
                                                  :surface    {:src "/raw/clipart/recording_studio/recording_studio_surface.png"}}
                        :mari                    {:type       "animation"
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
                        :video                   {:type       "video"
                                                  :transition "video"
                                                  :x          633
                                                  :y          83
                                                  :width      649
                                                  :height     379
                                                  :visible    false
                                                  :states     {:hidden {:visible false} :visible {:visible true}}}
                        :play-video-button       {:type       "image"
                                                  :src        "/raw/clipart/karaoke/play_button.png"
                                                  :transition "play-video-button"
                                                  :x          900
                                                  :y          210
                                                  :width      128
                                                  :height     128
                                                  :editable?  true
                                                  :states     {:hidden {:visible false} :visible {:visible true}}
                                                  :actions    {:click {:id "on-play-video-button-clicked" :on "click" :type "action"}}}

                        :mic                     {:type       "image"
                                                  :src        "/raw/clipart/karaoke/mic.png"
                                                  :transition "mic"
                                                  :x          770
                                                  :y          485
                                                  :width      360
                                                  :height     656}
                        :record-button           {:type     "group"
                                                  :x        886
                                                  :y        638
                                                  :width    128
                                                  :height   128
                                                  :visible  false
                                                  :states   {:hidden  {:visible false}
                                                             :visible {:visible true}}
                                                  :children ["record-button-back" "record-button-icon"]
                                                  :actions  {:click {:id "record-button-click" :on "click" :type "action"}}}
                        :record-button-back      {:type          "rectangle"
                                                  :x             0 :y 0
                                                  :width         128 :height 128
                                                  :border-radius 24
                                                  :fill          0xFFFFFF}
                        :record-button-icon      {:type          "rectangle"
                                                  :transition    "record-button-icon"
                                                  :x             16 :y 16
                                                  :width         96 :height 96
                                                  :border-radius 48
                                                  :fill          0xED1C24}

                        :playback-group          {:type     "group"
                                                  :x        300 :y 910
                                                  :width    256 :height 128
                                                  :visible  false
                                                  :states   {:hidden  {:visible false}
                                                             :visible {:visible true}}
                                                  :children ["playback-background"
                                                             "run-playback-button"
                                                             "stop-playback-button"
                                                             "approve-playback-button"]}
                        :playback-background     {:type          "rectangle"
                                                  :x             0 :y 0
                                                  :width         242 :height 128
                                                  :border-radius 24
                                                  :fill          0xFFFFFF}
                        :run-playback-button     {:type         "svg-path"
                                                  :x            16 :y 16
                                                  :states       {:hidden  {:visible false}
                                                                 :visible {:visible true}}
                                                  :stroke-width 2
                                                  :fill         true
                                                  :actions      {:click {:id "run-playback-click" :on "click" :type "action"}}
                                                  :data         "M 64 41.08 L 44 29.52 C 42.7844 28.8182 41.4056 28.4486 40.002 28.4482 C 38.5984 28.4478 37.2194 28.8168 36.0036 29.518 C 34.7876 30.2192 33.7776 31.2278 33.0748 32.4428 C 32.3722 33.6578 32.0014 35.0364 32 36.44 V 59.56 C 32.0014 60.963 32.3718 62.3408 33.0738 63.5554 C 33.776 64.77 34.785 65.7786 36 66.48 C 37.2162 67.1822 38.5958 67.5518 40 67.5518 C 41.4044 67.5518 42.7838 67.1822 44 66.48 L 64 54.92 C 65.2124 54.2168 66.2186 53.2074 66.9182 51.993 C 67.6176 50.7784 67.9858 49.4016 67.9858 48 C 67.9858 46.5984 67.6176 45.2216 66.9182 44.007 C 66.2186 42.7926 65.2124 41.7832 64 41.08 Z M 60 48 L 40 59.56 V 36.44 L 60 48 Z M 48 8 C 40.0888 8 32.3552 10.346 25.7772 14.7412 C 19.1992 19.1365 14.0723 25.3836 11.0448 32.6926 C 8.0173 40.0018 7.2252 48.0444 8.7686 55.8036 C 10.312 63.5628 14.1216 70.6902 19.7157 76.2842 C 25.3098 81.8784 32.4372 85.688 40.1964 87.2314 C 47.9556 88.7748 55.9984 87.9826 63.3074 84.9552 C 70.6164 81.9276 76.8636 76.8008 81.2588 70.2228 C 85.654 63.6448 88 55.9112 88 48 C 88 42.7472 86.9654 37.5456 84.9552 32.6926 C 82.945 27.8396 79.9986 23.43 76.2842 19.7157 C 72.57 16.0014 68.1604 13.055 63.3074 11.0448 C 58.4544 9.0346 53.2528 8 48 8 Z M 48 80 C 41.671 80 35.4842 78.1232 30.2218 74.607 C 24.9594 71.0908 20.8578 66.0932 18.4359 60.2458 C 16.0139 54.3986 15.3802 47.9646 16.6149 41.7572 C 17.8496 35.5498 20.8974 29.8478 25.3726 25.3726 C 29.8478 20.8974 35.5498 17.8496 41.7572 16.6149 C 47.9646 15.3801 54.3986 16.0139 60.2458 18.4359 C 66.0932 20.8578 71.0908 24.9594 74.607 30.2218 C 78.1232 35.4842 80 41.671 80 48 C 80 56.487 76.6286 64.6262 70.6274 70.6274 C 64.6262 76.6286 56.487 80 48 80 Z"}
                        :stop-playback-button    {:type         "svg-path"
                                                  :x            17 :y 17
                                                  :visible      false
                                                  :states       {:hidden  {:visible false}
                                                                 :visible {:visible true}}
                                                  :width        128 :height 128
                                                  :stroke-width 4
                                                  :fill         true
                                                  :actions      {:click {:id "stop-playback-click" :on "click" :type "action"}}
                                                  :data         "M 57.2811 34.8576 h -21.4272 c -0.513 0 -0.9342 0.4023 -0.9342 0.9234 v 21.6594 c 0 0.5103 0.4185 0.9288 0.9342 0.9288 h 21.4272 c 0.513 0 0.9288 -0.4158 0.9288 -0.9288 V 35.781 C 58.2072 35.2626 57.7941 34.8576 57.2811 34.8576 z M 46.5702 6 C 24.1683 6 6.0027 24.1602 6.0027 46.5675 S 24.1683 87.135 46.5702 87.135 c 22.4046 0 40.5621 -18.1602 40.5621 -40.5675 S 68.9748 6 46.5702 6 z M 46.5702 80.358 c -18.6624 0 -33.7932 -15.1308 -33.7932 -33.7905 c 0 -18.6678 15.1308 -33.7959 33.7932 -33.7959 c 18.6651 0 33.7878 15.1281 33.7878 33.7959 C 80.358 65.2272 65.2353 80.358 46.5702 80.358"}
                        :approve-playback-button {:type         "svg-path"
                                                  :x            135 :y 24
                                                  :width        128 :height 128
                                                  :stroke-width 4
                                                  :fill         true
                                                  :actions      {:click {:id "approve-playback-click" :on "click" :type "action"}}
                                                  :data         "M 50.88 27.16 L 33.72 44.36 L 27.12 37.76 C 26.7614 37.3412 26.3202 37.0012 25.8238 36.7612 C 25.3276 36.521 24.7872 36.386 24.2362 36.3648 C 23.6854 36.3436 23.136 36.4364 22.6228 36.6374 C 22.1094 36.8386 21.6432 37.1436 21.2534 37.5334 C 20.8636 37.9232 20.5586 38.3894 20.3574 38.9028 C 20.1564 39.416 20.0636 39.9654 20.0848 40.5162 C 20.1062 41.067 20.241 41.6076 20.4812 42.1038 C 20.7212 42.6002 21.0612 43.0414 21.48 43.4 L 30.88 52.84 C 31.2538 53.2108 31.697 53.504 32.1844 53.703 C 32.6718 53.9022 33.1936 54.003 33.72 54 C 34.7694 53.9956 35.775 53.579 36.52 52.84 L 56.52 32.84 C 56.895 32.4682 57.1926 32.0258 57.3956 31.5384 C 57.5986 31.0508 57.7032 30.528 57.7032 30 C 57.7032 29.472 57.5986 28.9492 57.3956 28.4616 C 57.1926 27.9742 56.895 27.5318 56.52 27.16 C 55.7706 26.415 54.7568 25.9968 53.7 25.9968 C 52.6432 25.9968 51.6294 26.415 50.88 27.16 Z M 40 0 C 32.0888 0 24.3552 2.346 17.7772 6.7412 C 11.1992 11.1365 6.0723 17.3836 3.0448 24.6926 C 0.0173 32.0018 -0.7748 40.0444 0.7686 47.8036 C 2.312 55.5628 6.1216 62.6902 11.7157 68.2842 C 17.3098 73.8784 24.4372 77.688 32.1964 79.2314 C 39.9556 80.7748 47.9984 79.9826 55.3074 76.9552 C 62.6164 73.9276 68.8636 68.8008 73.2588 62.2228 C 77.654 55.6448 80 47.9112 80 40 C 80 34.7472 78.9654 29.5456 76.9552 24.6926 C 74.945 19.8396 71.9986 15.4301 68.2842 11.7157 C 64.57 8.0014 60.1604 5.055 55.3074 3.0448 C 50.4544 1.0346 45.2528 0 40 0 Z M 40 72 C 33.671 72 27.4842 70.1232 22.2218 66.607 C 16.9594 63.0908 12.8579 58.0932 10.4359 52.2458 C 8.0139 46.3986 7.3802 39.9646 8.6149 33.7572 C 9.8496 27.5498 12.8973 21.8478 17.3726 17.3726 C 21.8478 12.8973 27.5498 9.8496 33.7572 8.6149 C 39.9646 7.3801 46.3986 8.0139 52.2458 10.4359 C 58.0932 12.8579 63.0908 16.9594 66.607 22.2218 C 70.1232 27.4842 72 33.671 72 40 C 72 48.487 68.6286 56.6262 62.6274 62.6274 C 56.6262 68.6286 48.487 72 40 72 Z"}}
        :scene-objects [["background"]
                        ["mic" "record-button" "playback-group"]
                        ["video" "play-video-button"]
                        ["mari"]]
        :actions       {:start-scene                          {:type "sequence"
                                                               :data ["start-activity"
                                                                      "init-video-src"
                                                                      "run-round-1"]}
                        :start-activity                       {:type "start-activity"}
                        :init-video-src                       {:type "set-variable" :var-name "video-src" :var-value "-"}

                        ;; Common

                        :show-video                           {:type "state" :id "visible" :target "video"}
                        :hide-video                           {:type "state" :id "hidden" :target "video"}
                        :show-play-video-button               {:type "state" :id "visible" :target "play-video-button"}
                        :hide-play-video-button               {:type "state" :id "hidden" :target "play-video-button"}
                        :show-playback                        {:type "state" :id "visible" :target "playback-group"}
                        :hide-playback                        {:type "state" :id "hidden" :target "playback-group"}
                        :highlight-play-video-button          {:type      "set-attribute"
                                                               :target    "play-video-button"
                                                               :attr-name "highlight" :attr-value true}
                        :unhighlight-play-video-button        {:type      "set-attribute"
                                                               :target    "play-video-button"
                                                               :attr-name "highlight" :attr-value false}

                        :play-video-seq                       {:type "sequence"
                                                               :data ["show-video"
                                                                      "play-video"
                                                                      "hide-video"]}

                        :play-video                           {:type     "play-video"
                                                               :target   "video"
                                                               :from-var [{:var-name "video-src" :action-property "src"}]}

                        :on-play-video-button-clicked         {:type     "case"
                                                               :from-var [{:var-name "current-round" :action-property "value"}]
                                                               :options  {:round1 {:type "action" :id "on-play-video-button-clicked-round-1"}
                                                                          :round2 {:type "action" :id "on-play-video-button-clicked-round-2"}}}

                        ;; Round 1

                        :run-round-1                          {:type "sequence"
                                                               :data ["set-current-round-1"
                                                                      "show-video"
                                                                      "show-play-video-button"
                                                                      "dialog-are-you-ready"
                                                                      "highlight-play-video-button"]}
                        :set-current-round-1                  {:type "set-variable" :var-name "current-round" :var-value "round1"}
                        :dialog-are-you-ready                 {:type               "sequence-data"
                                                               :editor-type        "dialog"
                                                               :concept-var        "current-concept"
                                                               :data               [{:type "sequence-data"
                                                                                     :data [{:type "empty" :duration 0}
                                                                                            {:type "animation-sequence" :phrase-text "New action" :audio nil}]}]
                                                               :phrase             "instructions"
                                                               :phrase-description "Instructions"}

                        :on-play-video-button-clicked-round-1 {:type "sequence"
                                                               :data ["hide-play-video-button"
                                                                      "unhighlight-play-video-button"
                                                                      "play-video-seq"
                                                                      "run-round-2"]}

                        ;; Round 2

                        :run-round-2                          {:type "sequence"
                                                               :data ["set-current-round-2"
                                                                      "show-play-video-button"
                                                                      "dialog-sing-along"
                                                                      "highlight-play-video-button"]}

                        :set-current-round-2                  {:type "set-variable" :var-name "current-round" :var-value "round2"}

                        :dialog-sing-along                    {:type               "sequence-data"
                                                               :editor-type        "dialog"
                                                               :concept-var        "current-concept"
                                                               :data               [{:type "sequence-data"
                                                                                     :data [{:type "empty" :duration 0}
                                                                                            {:type "animation-sequence" :phrase-text "New action" :audio nil}]}]
                                                               :phrase             "sing-along"
                                                               :phrase-description "Can't help but sing along"}

                        :on-play-video-button-clicked-round-2 {:type "sequence"
                                                               :data ["hide-play-video-button"
                                                                      "unhighlight-play-video-button"
                                                                      "play-video-seq"
                                                                      "run-round-3-intro"]}

                        ;; Round 3 Intro

                        :run-round-3-intro                    {:type "sequence"
                                                               :data ["dialog-round-3-intro"
                                                                      "get-record-buttons"
                                                                      "controls-intro"
                                                                      "dialog-finish-intro"
                                                                      "run-round-3-play"]}

                        :dialog-round-3-intro                 {:type               "sequence-data"
                                                               :editor-type        "dialog"
                                                               :concept-var        "current-concept"
                                                               :data               [{:type "sequence-data"
                                                                                     :data [{:type "empty" :duration 0}
                                                                                            {:type "animation-sequence" :phrase-text "New action" :audio nil}]}]
                                                               :phrase             "round-3-intro"
                                                               :phrase-description "Round 3 Intro"}

                        :get-record-buttons                   {:type "sequence-data"
                                                               :data [{:type "state" :id "visible" :target "record-button"}
                                                                      {:type "state" :id "visible" :target "playback-group"}]}

                        :controls-intro                       {:type "sequence"
                                                               :data ["dialog-how-to-record"
                                                                      "highlight-record-button"
                                                                      "dialog-when-to-sing-1"
                                                                      "activate-record-button"
                                                                      "dialog-when-to-sing-2"
                                                                      "dialog-how-to-stop"
                                                                      "deactivate-record-button"]}
                        :dialog-how-to-record                 {:type               "sequence-data"
                                                               :editor-type        "dialog"
                                                               :concept-var        "current-concept"
                                                               :data               [{:type "sequence-data"
                                                                                     :data [{:type "empty" :duration 0}
                                                                                            {:type "animation-sequence" :phrase-text "New action" :audio nil}]}]
                                                               :phrase             "how-to-record"
                                                               :phrase-description "How to start record"}
                        :dialog-when-to-sing-1                {:type               "sequence-data"
                                                               :editor-type        "dialog"
                                                               :concept-var        "current-concept"
                                                               :data               [{:type "sequence-data"
                                                                                     :data [{:type "empty" :duration 0}
                                                                                            {:type "animation-sequence" :phrase-text "New action" :audio nil}]}]
                                                               :phrase             "when-to-sing-1"
                                                               :phrase-description "How to know when singing. Part 1"}
                        :dialog-when-to-sing-2                {:type               "sequence-data"
                                                               :editor-type        "dialog"
                                                               :concept-var        "current-concept"
                                                               :data               [{:type "sequence-data"
                                                                                     :data [{:type "empty" :duration 0}
                                                                                            {:type "animation-sequence" :phrase-text "New action" :audio nil}]}]
                                                               :phrase             "when-to-sing-2"
                                                               :phrase-description "How to know when singing. Part 2"}
                        :dialog-how-to-stop                   {:type               "sequence-data"
                                                               :editor-type        "dialog"
                                                               :concept-var        "current-concept"
                                                               :data               [{:type "sequence-data"
                                                                                     :data [{:type "empty" :duration 0}
                                                                                            {:type "animation-sequence" :phrase-text "New action" :audio nil}]}]
                                                               :phrase             "how-to-stop"
                                                               :phrase-description "How to stop record"}

                        :highlight-record-button              {:type      "set-attribute"
                                                               :target    "record-button-icon"
                                                               :attr-name "highlight" :attr-value true}
                        :unhighlight-record-button            {:type      "set-attribute"
                                                               :target    "record-button-icon"
                                                               :attr-name "highlight" :attr-value false}
                        :activate-record-button               {:type "sequence-data"
                                                               :data [{:type          "transition"
                                                                       :transition-id "record-button-icon"
                                                                       :to            {:border-radius 24 :duration 0.2}}
                                                                      {:type      "set-attribute"
                                                                       :target    "record-button-icon"
                                                                       :attr-name "fill" :attr-value 0x0fbc2a}]}
                        :deactivate-record-button             {:type "sequence-data"
                                                               :data [{:type      "set-attribute"
                                                                       :target    "record-button-icon"
                                                                       :attr-name "fill" :attr-value 0xed1d24}
                                                                      {:type          "transition"
                                                                       :transition-id "record-button-icon"
                                                                       :to            {:border-radius 48 :duration 0.2}}]}

                        :dialog-finish-intro                  {:type               "sequence-data"
                                                               :editor-type        "dialog"
                                                               :concept-var        "current-concept"
                                                               :data               [{:type "sequence-data"
                                                                                     :data [{:type "empty" :duration 0}
                                                                                            {:type "animation-sequence" :phrase-text "New action" :audio nil}]}]
                                                               :phrase             "finish-intro"
                                                               :phrase-description "If can't remember words"}

                        ;; Round 3 Play

                        :record-button-click                  {:type     "test-var-scalar"
                                                               :var-name "record-button-state"
                                                               :value    "stop"
                                                               :success  {:type "parallel"
                                                                          :data [{:type "action" :id "deactivate-record-button"}
                                                                                 {:type "set-variable" :var-name "record-button-state" :var-value "record"}
                                                                                 {:type "sequence-data"
                                                                                  :data [{:type "stop-audio-recording" :var-name "karaoke-recorded-song"}
                                                                                         {:type     "set-progress"
                                                                                          :from-var [{:var-name        "progress-var"
                                                                                                      :action-property "var-name"}
                                                                                                     {:var-name        "karaoke-recorded-song"
                                                                                                      :action-property "var-value"}]}
                                                                                         {:type "action" :id "show-playback"}]}]}
                                                               :fail     {:type "sequence-data"
                                                                          :data [{:type "set-variable" :var-name "record-button-state" :var-value "stop"}
                                                                                 {:type "start-audio-recording"}
                                                                                 {:type "action" :id "hide-playback"}
                                                                                 {:type "action" :id "activate-record-button"}]}}
                        :run-record-playing                   {:type "parallel"
                                                               :data [{:type "state" :target "run-playback-button" :id "hidden"}
                                                                      {:type "state" :target "stop-playback-button" :id "visible"}
                                                                      {:type     "audio"
                                                                       :tags     ["recorded-audio-flow"]
                                                                       :from-var [{:var-name        "karaoke-recorded-song"
                                                                                   :action-property "id"}]}]}
                        :stop-record-playing                  {:type "parallel"
                                                               :data [{:type "remove-flows" :flow-tag "recorded-audio-flow"}
                                                                      {:type "state" :target "run-playback-button" :id "visible"}
                                                                      {:type "state" :target "stop-playback-button" :id "hidden"}]}
                        :run-playback-click                   {:type "sequence-data"
                                                               :data [{:type "action" :id "run-record-playing"}
                                                                      {:type "action" :id "stop-record-playing"}]}
                        :stop-playback-click                  {:type "action"
                                                               :id   "stop-record-playing"}
                        :approve-playback-click               {:type "sequence-data"
                                                               :data [{:type "action" :id "hide-playback"}
                                                                      {:type     "action"
                                                                       :from-var [{:var-name        "next-action"
                                                                                   :action-property "id"}]}]}

                        :run-round-3-play                     {:type "sequence-data"
                                                               :data [{:type "action" :id "show-video"}
                                                                      {:type "action" :id "hide-play-video-button"}
                                                                      {:type "action" :id "hide-playback"}]}

                        ;; Finish

                        :finish                               {:type "sequence"
                                                               :data ["finish-activity"]}

                        :stop-scene                           {:type "sequence" :data ["stop-activity"]}
                        :stop-activity                        {:type "stop-activity"}

                        :finish-activity                      {:type "finish-activity"}}
        :triggers      {:stop  {:on "back" :action "stop-scene"}
                        :start {:on "start" :action "start-scene"}}
        :metadata      {:autostart true
                        :tracks    [{:title "Round 1"
                                     :nodes [{:type      "dialog"
                                              :action-id :dialog-are-you-ready}]}
                                    {:title "Round 2"
                                     :nodes [{:type      "dialog"
                                              :action-id :dialog-sing-along}]}
                                    {:title "Round 3"
                                     :nodes [{:type      "dialog"
                                              :action-id :dialog-round-3-intro}
                                             {:type      "dialog"
                                              :action-id :dialog-how-to-record}
                                             {:type      "dialog"
                                              :action-id :dialog-when-to-sing-1}
                                             {:type      "dialog"
                                              :action-id :dialog-when-to-sing-2}
                                             {:type      "dialog"
                                              :action-id :dialog-how-to-stop}
                                             {:type      "dialog"
                                              :action-id :dialog-finish-intro}]}]}})

(defn- add-video
  [template {:keys [video]}]
  (-> template
      (update :assets conj {:url video :type "video"})
      (assoc-in [:actions :play-video :src] video)
      (assoc-in [:actions :init-video-src :var-value] video)))

(defn- get-round-3-action-name
  [idx]
  (str "run-video-part-" idx))

(defn- get-round-3-action-data
  [idx {:keys [from to]} {:keys [finish-action last?]}]
  {:type "sequence-data"
   :data [{:type      "set-variable"
           :var-name  "progress-var"
           :var-value (str "karaoke-part-" idx)}
          {:type      "set-variable"
           :var-name  "next-action"
           :var-value (if last? finish-action (get-round-3-action-name (inc idx)))}
          {:type     "play-video"
           :target   "video"
           :start    from
           :end      to
           :from-var [{:var-name "video-src" :action-property "src"}]}]})

(defn- add-round-3
  [template {:keys [video-ranges]}]
  (let [actions (->> video-ranges
                     (map-indexed vector)
                     (map (fn [[idx range]]
                            (let [action-name (get-round-3-action-name idx)]
                              [(keyword action-name) (get-round-3-action-data idx range {:last?         (= idx (dec (count video-ranges)))
                                                                                         :finish-action "finish"})]))))
        first-action (-> actions first first clojure.core/name)]
    (-> template
        (update-in [:actions :run-round-3-play :data] conj {:type "action" :id first-action})
        (update-in [:actions] merge (into {} actions)))))

(defn f
  [t args]
  (-> t
      (add-video args)
      (add-round-3 args)))

(core/register-template
  m
  (partial f t))

(comment
  (def course-slug "ee-qq-lfihnruo")
  (def scene-slug "ww")

  ;; update activity
  (let [dialog-action (templates/get-dialog-actions course-slug scene-slug)
        update-result (templates/update-activity course-slug scene-slug :actions dialog-action)]
    (get-in update-result [:data :assets])))
