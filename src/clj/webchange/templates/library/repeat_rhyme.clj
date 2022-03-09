(ns webchange.templates.library.repeat-rhyme
  (:require
    [webchange.templates.utils.dialog :as dialog]
    [webchange.templates.core :as core]))

(def m {:id          48
        :name        "Repeat Rhyme"
        :tags        ["rhyming"]
        :description "Watch Video and sing along"
        :fields      []
        :options     {:thumbnail-1 {:type        "image"
                                    :label       "thumbnail Image"
                                    :description "Pick thumbnail for the first video"}
                      :thumbnail-2 {:type        "image"
                                    :label       "thumbnail Image"
                                    :description "Pick thumbnail for the second video"}
                      :thumbnail-3 {:type        "image"
                                    :label       "thumbnail Image"
                                    :description "Pick thumbnail for the third video"}
                      :video-1     {:label "Pick the first video"
                                    :type  "video"}
                      :video-2    {:label "Pick the second video"
                                   :type  "video"}
                      :video-3    {:label "Pick the third video"
                                   :type  "video"}}})

(def template
  {:assets
   [{:url "/raw/clipart/recording_studio/recording_studio_background.png" :type "image"}
    {:url "/raw/clipart/recording_studio/recording_studio_decoration.png" :type "image"}
    {:url "/raw/clipart/recording_studio/recording_studio_surface.png" :type "image"}
    {:url "/raw/clipart/karaoke/play_button.png" :type "image"}
    {:url "/raw/clipart/karaoke/stop_button.png" :type "image"}
    {:url "/raw/clipart/karaoke/record_button.png" :type "image"}
    {:url "/raw/clipart/karaoke/checkmark.png" :type "image"}]
   :objects {:layered-background
             {:type       "layered-background"
              :background {:src "/raw/clipart/recording_studio/recording_studio_background.png"}
              :decoration {:src "/raw/clipart/recording_studio/recording_studio_decoration.png"}
              :surface    {:src "/raw/clipart/recording_studio/recording_studio_surface.png"}}

             :blue-background {:type "rectangle"
                               :x 0
                               :y 0
                               :width 1920
                               :height 1080
                               :fill 0xb1d9ea
                               :visible false}

             :thumbnail-1  {:type "image"
                            :x 320
                            :y 400
                            :origin {:type "center-center"}
                            :scale-x 10
                            :scale-y 10
                            :max-width 400
                            :max-height 300
                            :min-height 300
                            :visible false
                            :actions {:click {:id "thumbnail-1-selected" :on "click" :type "action"}}}

             :thumbnail-2  {:type "image"
                            :x 960
                            :y 400
                            :origin {:type "center-center"}
                            :scale-x 10
                            :scale-y 10
                            :max-width 400
                            :max-height 300
                            :min-height 300
                            :visible false
                            :actions {:click {:id "thumbnail-2-selected" :on "click" :type "action"}}}

             :thumbnail-3  {:type "image"
                            :x 1600
                            :y 400
                            :origin {:type "center-center"}
                            :scale-x 10
                            :scale-y 10
                            :max-width 400
                            :max-height 300
                            :min-height 300
                            :visible false
                            :actions {:click {:id "thumbnail-3-selected" :on "click" :type "action"}}}

             :video {:type "video"
                     :x 633
                     :y 83
                     :width 649
                     :height 379}

             :play-button {:type "image"
                           :x 960
                           :y 540
                           :scale-x 2
                           :scale-y 2
                           :origin {:type "center-center"}
                           :src "/raw/clipart/karaoke/play_button.png"
                           :visible false
                           :filters    [{:name "brightness" :value 0}
                                        {:name "glow" :outer-strength 0 :color 0xffd700}]
                           :actions {:click {:id "play-pressed" :on "click" :type "action"}}}

             :play-recording-button {:type "image"
                                     :x 1400
                                     :y 540
                                     :scale-x 2
                                     :scale-y 2
                                     :origin {:type "center-center"}
                                     :src "/raw/clipart/karaoke/play_button.png"
                                     :visible false
                                     :actions {:click {:id "play-recording" :on "click" :type "action"}}}

             :checkmark-button {:type "image"
                                :x 1700
                                :y 540
                                :scale-x 2
                                :scale-y 2
                                :origin {:type "center-center"}
                                :src "/raw/clipart/karaoke/checkmark.png"
                                :visible false
                                :actions {:click {:id "done" :on "click" :type "action"}}}

             :sound-bar {:type "sound-bar"
                         :transition "sound-bar"
                         :x 560
                         :y 650
                         :width 800
                         :height 400
                         :visible false}}

   :scene-objects [["layered-background"]
                   ["blue-background" "video" "thumbnail-1" "thumbnail-2" "thumbnail-3"]
                   ["play-button" "play-recording-button" "checkmark-button" "sound-bar"]]

   :actions {:thumbnail-1-selected {:type "sequence-data"
                                    :data [{:type "action" :id "hide-thumbnails"}
                                           {:type "action" :id "show-play-button"}
                                           {:type "copy-variable" :var-name "video-src" :from "video-src-1"}
                                           {:type "action" :id "watch-for-rhymes-dialog"}]}

             :thumbnail-2-selected {:type "sequence-data"
                                    :data [{:type "action" :id "hide-thumbnails"}
                                           {:type "action" :id "show-play-button"}
                                           {:type "copy-variable" :var-name "video-src" :from "video-src-2"}
                                           {:type "action" :id "watch-for-rhymes-dialog"}]}

             :thumbnail-3-selected {:type "sequence-data"
                                    :data [{:type "action" :id "hide-thumbnails"}
                                           {:type "action" :id "show-play-button"}
                                           {:type "copy-variable" :var-name "video-src" :from "video-src-3"}
                                           {:type "action" :id "watch-for-rhymes-dialog"}]}

             :hide-thumbnails {:type "sequence-data"
                               :data [{:type "set-attribute" :target "thumbnail-1" :attr-name "visible" :attr-value false}
                                      {:type "set-attribute" :target "thumbnail-2" :attr-name "visible" :attr-value false}
                                      {:type "set-attribute" :target "thumbnail-3" :attr-name "visible" :attr-value false}]}

             :hide-play-button {:type "sequence-data"
                                :data [{:type "set-attribute" :target "blue-background" :attr-name "visible" :attr-value false}
                                       {:type "set-attribute" :target "play-button" :attr-name "visible" :attr-value false}
                                       {:type "set-attribute" :target "play-recording-button" :attr-name "visible" :attr-value false}
                                       {:type "set-attribute" :target "checkmark-button" :attr-name "visible" :attr-value false}
                                       {:type "action" :id "remove-timeout-timer"}]}

             :show-play-button {:type "sequence-data"
                                :data [{:type "set-attribute" :target "blue-background" :attr-name "visible" :attr-value true}
                                       {:type "set-attribute" :target "play-button" :attr-name "visible" :attr-value true}
                                       {:type "transition"
                                        :transition-id "play-button"
                                        :return-immediately true
                                        :from {:brightness 0 :glow 0}
                                        :to {:brightness 1 :glow 10 :yoyo true :duration 0.5 :repeat 5}}]}

             :play-pressed {:type "test-var-scalar"
                            :var-name "first-show"
                            :value    "true"
                            :success {:type "sequence-data"
                                      :data [{:type "action" :id "hide-play-button"}
                                             {:type "set-attribute" :target "play-button" :attr-name "src"
                                              :attr-value "/raw/clipart/karaoke/record_button.png"}
                                             {:type "set-attribute" :target "video" :attr-name "visible" :attr-value true}
                                             {:type "play-video" :target "video" :from-var [{:var-name "video-src" :action-property "src"}]}
                                             {:type "action" :id "show-play-button"}
                                             {:type "set-attribute" :target "video" :attr-name "visible" :attr-value false}
                                             {:type "set-variable" :var-name "first-show" :var-value "false"}
                                             {:type "action" :id "sing-along-dialog"}
                                             ]}
                            :fail {:type "sequence-data"
                                   :data [{:type "action" :id "hide-play-button"}
                                          {:type "set-attribute" :target "play-recording-button" :attr-name "visible" :attr-value false}
                                          {:type "set-attribute" :target "checkmark-button" :attr-name "visible" :attr-value false}
                                          {:type "action" :id "remove-timeout-timer"}
                                          {:type "set-attribute" :target "video" :attr-name "visible" :attr-value true}
                                          {:type "start-audio-recording"}
                                          {:type "action" :id "activate-sound-bar"}
                                          {:type "reset-video" :target "video" :from-var [{:var-name "video-src" :action-property "src"}]}
                                          {:type "action" :id "show-play-button"}
                                          {:type "action" :id "deactivate-sound-bar"}
                                          {:type "set-attribute" :target "play-recording-button" :attr-name "visible" :attr-value true}
                                          {:type "action" :id "timeout-timer"}
                                          {:type "set-attribute" :target "video" :attr-name "visible" :attr-value false}
                                          {:type "stop-audio-recording" :var-name "recording-studio-audio"}
                                          {:type "action" :id "hear-recording-dialog"}]}}

             :activate-sound-bar {:type "sequence-data"
                                  :data [{:type "set-attribute" :target "sound-bar" :attr-name "visible" :attr-value true}
                                         {:type "component-action" :target "sound-bar" :action "activate"}]}

             :deactivate-sound-bar {:type "sequence-data"
                                    :data [{:type "set-attribute" :target "sound-bar" :attr-name "visible" :attr-value false}
                                           {:type "component-action" :target "sound-bar" :action "deactivate"}]}

             :show-record-play-button {:type "sequence-data"
                                       :data [{:type "set-variable" :var-name "stopped" :var-value "true"}
                                              {:type "set-attribute" :target "play-recording-button" :attr-name "src"
                                               :attr-value "/raw/clipart/karaoke/play_button.png"}]}

             :play-recording {:type "test-var-scalar"
                              :var-name "stopped"
                              :value    "true"
                              :success {:type "sequence-data"
                                        :data [{:type "action" :id "remove-timeout-timer"}
                                               {:type "set-variable" :var-name "stopped" :var-value "false"}
                                               {:type "set-attribute" :target "play-recording-button" :attr-name "src"
                                                :attr-value "/raw/clipart/karaoke/stop_button.png"}
                                               {:type     "audio"
                                                :tags     ["recorded-audio-flow"]
                                                :from-var [{:var-name        "recording-studio-audio"
                                                            :action-property "id"}]}
                                               {:type "action" :id "show-record-play-button"}
                                               {:type "action" :id "timeout-timer"}]}
                              :fail {:type "sequence-data"
                                     :data [{:type "action" :id "show-record-play-button"}
                                            {:type "remove-flows" :flow-tag "recorded-audio-flow"}
                                            {:type "action" :id "timeout-timer"}]}}

             :remove-timeout-timer {:type "remove-interval" :id "try-again-timeout"}

             :timeout-timer {:type "set-interval" :id "try-again-timeout" :interval 5000 :action "timeout"}

             :timeout {:type "sequence-data"
                       :data [{:type "set-attribute" :target "checkmark-button" :attr-name "visible" :attr-value true}
                              {:type "action" :id "try-again-dialog"}]}

             :done {:type "action" :id "stop-activity"}

             :start-activity {:type "sequence-data"
                              :data [{:type "start-activity"}
                                     {:type "action" :id "intro-dialog"}
                                     {:type "set-variable" :var-name "first-show" :var-value "true"}
                                     {:type "set-variable" :var-name "stopped" :var-value "true"}
                                     {:type "set-attribute" :target "blue-background" :attr-name "visible" :attr-value true}
                                     {:type "set-attribute" :target "thumbnail-1" :attr-name "visible" :attr-value true}
                                     {:type "set-attribute" :target "thumbnail-2" :attr-name "visible" :attr-value true}
                                     {:type "set-attribute" :target "thumbnail-3" :attr-name "visible" :attr-value true}
                                     {:type "action" :id "pick-song-dialog"}
                                     ]}

             :stop-activity {:type "finish-activity"}

             :intro-dialog (dialog/default "Intro")
             :pick-song-dialog (dialog/default "Pick Song")
             :watch-for-rhymes-dialog (dialog/default "Watch for rhymes")
             :sing-along-dialog (dialog/default "Sing along")
             :hear-recording-dialog (dialog/default "Hear redorcing")
             :try-again-dialog (dialog/default "Try again")}

   :triggers {:back  {:on "back" :action "stop-activity"}
              :start {:on "start" :action "start-activity"}}

   :metadata {:autostart true
              :tracks [{:id "main"
                        :title "Main Track"
                        :nodes [{:type      "dialog"
                                 :action-id "intro-dialog"}
                                {:type      "dialog"
                                 :action-id "pick-song-dialog"}
                                {:type      "dialog"
                                 :action-id "watch-for-rhymes-dialog"}
                                {:type      "dialog"
                                 :action-id "sing-along-dialog"}
                                {:type      "dialog"
                                 :action-id "hear-recording-dialog"}
                                {:type      "dialog"
                                 :action-id "try-again-dialog"}]}]}})

(defn create-template [args]
  (let [[[thumbnail-1 video-1]
         [thumbnail-2 video-2]
         [thumbnail-3 video-3]]
        (shuffle [[(get-in args [:thumbnail-1 :src]) (:video-1 args)]
                  [(get-in args [:thumbnail-2 :src]) (:video-2 args)]
                  [(get-in args [:thumbnail-3 :src]) (:video-3 args)]])]
    (-> template
        (update :assets conj {:url thumbnail-1 :type "image"})
        (update-in [:actions :start-activity :data] concat
                   [{:type       "set-attribute"
                     :target     "thumbnail-1"
                     :attr-name  "src"
                     :attr-value thumbnail-1}])

        (update :assets conj {:url thumbnail-2 :type "image"})
        (update-in [:actions :start-activity :data] concat
                   [{:type       "set-attribute"
                     :target     "thumbnail-2"
                     :attr-name  "src"
                     :attr-value thumbnail-2}])

        (update :assets conj {:url thumbnail-3 :type "image"})
        (update-in [:actions :start-activity :data] concat
                   [{:type       "set-attribute"
                     :target     "thumbnail-3"
                     :attr-name  "src"
                     :attr-value thumbnail-3}])

        (update :assets conj {:url video-1 :type "video"})
        (update-in [:actions :start-activity :data] concat
                   [{:type "set-variable" :var-name "video-src-1" :var-value video-1}])

        (update :assets conj {:url video-2 :type "video"})
        (update-in [:actions :start-activity :data] concat
                   [{:type "set-variable" :var-name "video-src-2" :var-value video-2}])

        (update :assets conj {:url video-3 :type "video"})
        (update-in [:actions :start-activity :data] concat
                   [{:type "set-variable" :var-name "video-src-3" :var-value video-3}]))))

(defn update-template [data args]
  data)

(core/register-template
  m
  create-template
  update-template)
