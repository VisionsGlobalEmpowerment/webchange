(ns webchange.templates.library.recording-studio.rhyme
  (:require
    [webchange.templates.common-actions :refer [update-activity]]
    [webchange.templates.library.recording-studio.generate-actions :refer [add-control-actions
                                                                           add-backward-compatibility-action]]
    [webchange.templates.library.recording-studio.generate-buttons :refer [add-start-play-button
                                                                           add-stop-play-button
                                                                           add-start-record-button
                                                                           add-stop-record-button
                                                                           add-approve-button]]
    [webchange.templates.library.recording-studio.layout :refer [layout-params]]
    [webchange.templates.utils.dialog :as dialog]
    [webchange.templates.core :as core]))

(defn change-song-options [title]
  {:title title
   :options {:thumbnail {:type        "image"
                         :label       "Thumbnail image"
                         :description "Pick thumbnail for the video"}
             :video     {:label "Pick the video"
                         :type  "video"}}})

(def m {:id             48
        :name           "Recording Studio (Repeat Rhyme)"
        :tags           ["Direct Instruction - Animated Instructor"]
        :description    "Recording Studio (Repeat Rhyme)"
        :options        {:thumbnail-1 {:type        "image"
                                       :label       "Thumbnail image"
                                       :description "Pick thumbnail for the first video"}
                         :video-1     {:label "Pick the first video"
                                       :type  "video"}
                         :thumbnail-2 {:type        "image"
                                       :label       "Thumbnail image"
                                       :description "Pick thumbnail for the second video"}
                         :video-2    {:label "Pick the second video"
                                      :type  "video"}
                         :thumbnail-3 {:type        "image"
                                       :label       "Thumbnail image"
                                       :description "Pick thumbnail for the third video"}
                         :video-3    {:label "Pick the third video"
                                      :type  "video"}}
        :actions        {:change-song-1 (change-song-options "Change Song 1")
                         :change-song-2 (change-song-options "Change Song 2")
                         :change-song-3 (change-song-options "Change Song 3")}})

(def template (-> {:assets        []
                   :objects       (-> {:background    {:type   "rectangle"
                                                       :x      0
                                                       :y      0
                                                       :width  1920
                                                       :height 1080
                                                       :fill   0x163760}
                                       :screen        (merge {:type          "rectangle"
                                                              :border-radius 32
                                                              :fill          0xB9D8E8}
                                                             (select-keys (:screen layout-params)
                                                                          [:x :y :width :height]))
                                       :video         (merge {:type "video"}
                                                             (select-keys (:screen layout-params)
                                                                          [:x :y :width :height]))
                                       :thumbnail-1  {:type "image"
                                                      :x 470
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
                                                      :x 1450
                                                      :y 400
                                                      :origin {:type "center-center"}
                                                      :scale-x 10
                                                      :scale-y 10
                                                      :max-width 400
                                                      :max-height 300
                                                      :min-height 300
                                                      :visible false
                                                      :actions {:click {:id "thumbnail-3-selected" :on "click" :type "action"}}}
                                       :sound-bar     (merge {:type       "sound-bar"
                                                              :transition "sound-bar"
                                                              :visible    false}
                                                             (select-keys (:sound-bar layout-params)
                                                                          [:x :y :width :height]))
                                       :timer-group   {:type     "group"
                                                       :x        0
                                                       :y        0
                                                       :visible  false
                                                       :overlay? true
                                                       :children ["timer-screen" "timer"]}
                                       :timer         (merge {:type              "timer"
                                                              :transition        "timer"
                                                              :show-minutes      false
                                                              :show-progress     true
                                                              :show-leading-zero false
                                                              :time              3
                                                              :font-size         124
                                                              :thickness         12
                                                              :font-weight       "normal"
                                                              :font-family       "Roboto"
                                                              :progress-color    0xff9000
                                                              :color             0x010101
                                                              :actions           {:end {:on "end" :type "action" :id "recording-countdown-ended"}}}
                                                             (select-keys (:timer layout-params)
                                                                          [:x :y :size]))
                                       :timer-screen  (merge {:type          "rectangle"
                                                              :transition    "timer-screen"
                                                              :border-radius 32
                                                              :fill          0xCCCCCC
                                                              :opacity       0.4}
                                                             (select-keys (:screen layout-params)
                                                                          [:x :y :width :height]))}
                                      (add-approve-button "approve-button" {:on-click "approve-button-click-handler"})
                                      (add-start-play-button "start-play-button" {:on-click "start-play-button-click-handler"})
                                      (add-stop-play-button "stop-play-button" {:on-click "stop-play-button-click-handler"})
                                      (add-start-record-button "start-record-button" {:on-click "start-record-button-click-handler"})
                                      (add-stop-record-button "stop-record-button" {:on-click "stop-record-button-click-handler"}))

                   :scene-objects [["background" "screen" "sound-bar"]
                                   ["timer-group" "video"]
                                   ["thumbnail-1" "thumbnail-2" "thumbnail-3"]
                                   ["approve-button" "start-play-button" "stop-play-button" "start-record-button" "stop-record-button"]]
                   :actions       {:main                              {:type "sequence-data",
                                                                       :data []}

                                   :finish                            {:type "sequence-data"
                                                                       :data [{:type "action" :id "remove-timeout-timer"}
                                                                              {:type "finish-activity"}]}

                                   :thumbnail-1-selected {:type "sequence-data"
                                                          :data [{:type "action" :id "hide-thumbnails"}
                                                                 {:type "copy-variable" :var-name "video-src" :from "video-src-1"}
                                                                 {:type "finish-flows" :tag "show-choices"}]}

                                   :thumbnail-2-selected {:type "sequence-data"
                                                          :data [{:type "action" :id "hide-thumbnails"}
                                                                 {:type "copy-variable" :var-name "video-src" :from "video-src-2"}
                                                                 {:type "finish-flows" :tag "show-choices"}]}

                                   :thumbnail-3-selected {:type "sequence-data"
                                                          :data [{:type "action" :id "hide-thumbnails"}
                                                                 {:type "copy-variable" :var-name "video-src" :from "video-src-3"}
                                                                 {:type "finish-flows" :tag "show-choices"}]}

                                   :show-thumbnails {:type "sequence-data"
                                                     :data [{:type "vars-var-provider" :from ["x1" "x2" "x3"] :shuffled true
                                                             :variables ["thumbnail-1-x" "thumbnail-2-x" "thumbnail-3-x"]}

                                                            {:type "set-attribute" :target "thumbnail-1" :attr-name "x"
                                                             :from-var [{:var-name "thumbnail-1-x" :action-property "attr-value"}]}
                                                            {:type "set-attribute" :target "thumbnail-1" :attr-name "visible" :attr-value true}

                                                            {:type "set-attribute" :target "thumbnail-2" :attr-name "x"
                                                             :from-var [{:var-name "thumbnail-2-x" :action-property "attr-value"}]}
                                                            {:type "set-attribute" :target "thumbnail-2" :attr-name "visible" :attr-value true}

                                                            {:type "set-attribute" :target "thumbnail-3" :attr-name "x"
                                                             :from-var [{:var-name "thumbnail-3-x" :action-property "attr-value"}]}
                                                            {:type "set-attribute" :target "thumbnail-3" :attr-name "visible" :attr-value true}]}

                                   :hide-thumbnails {:type "sequence-data"
                                                     :data [{:type "set-attribute" :target "thumbnail-1" :attr-name "visible" :attr-value false}
                                                            {:type "set-attribute" :target "thumbnail-2" :attr-name "visible" :attr-value false}
                                                            {:type "set-attribute" :target "thumbnail-3" :attr-name "visible" :attr-value false}]}

                                   :approve-button-click-handler      {:type "finish-flows" :tag "ask-recording"}

                                   :stop-activity                     {:type "sequence-data"
                                                                       :data [{:type "action" :id "remove-timeout-timer"}
                                                                              {:type "stop-activity" :id "recording-studio"}]}


                                   ;; UI actions

                                   ; Generated actions:
                                   ; - show-*
                                   ; - hide-*
                                   ; - highlight-*
                                   ; for
                                   ; - approve-button
                                   ; - start-play-button
                                   ; - stop-play-button
                                   ; - start-record-button
                                   ; - stop-record-button
                                   ; - sound-bar

                                   :activate-sound-bar                {:type "component-action" :target "sound-bar" :action "activate"}
                                   :deactivate-sound-bar              {:type "component-action" :target "sound-bar" :action "deactivate"}

                                   :plug-in-sound-bar                 {:type "sequence" :data ["show-sound-bar" "activate-sound-bar"]}
                                   :plug-out-sound-bar                {:type "sequence" :data ["deactivate-sound-bar" "hide-sound-bar"]}

                                   ;; Click handlers

                                   :start-play-button-click-handler   {:type "test-var-scalar"
                                                                       :var-name "record"
                                                                       :value    "true"
                                                                       :success {:type "sequence-data"
                                                                                 :data [{:id "hide-start-play-button" :type "action"}
                                                                                        {:id "hide-start-record-button" :type "action"}
                                                                                        {:id "hide-approve-button" :type "action"}
                                                                                        {:id "show-stop-play-button" :type "action"}

                                                                                        {:id "start-playing" :type "action"}]}
                                                                       :fail {:type "sequence-data"
                                                                              :data [{:id "hide-start-play-button" :type "action"}
                                                                                     {:id "start-playing" :type "action"}]}}

                                   :stop-play-button-click-handler    {:type "sequence-data"
                                                                       :data [{:id "hide-stop-play-button" :type "action"}
                                                                              {:id "show-start-play-button" :type "action"}
                                                                              {:id "show-start-record-button" :type "action"}
                                                                              {:id "show-approve-button" :type "action"}

                                                                              {:id "stop-playing" :type "action"}]}

                                   :start-record-button-click-handler {:type "sequence-data"
                                                                       :data [{:id "plug-in-sound-bar" :type "action"}
                                                                              {:id "hide-start-play-button" :type "action"}
                                                                              {:id "hide-start-record-button" :type "action"}
                                                                              {:id "hide-approve-button" :type "action"}

                                                                              {:id "start-recording-countdown" :type "action"}]}

                                   :stop-record-button-click-handler  {:type "sequence-data"
                                                                       :data [{:id "plug-out-sound-bar" :type "action"}
                                                                              {:id "hide-stop-record-button" :type "action"}
                                                                              {:id "show-start-record-button" :type "action"}
                                                                              {:id "show-start-play-button" :type "action"}
                                                                              {:id "show-approve-button" :type "action"}

                                                                              {:id "stop-recording" :type "action"}]}

                                   ;; Recording Timer

                                   :start-recording-countdown         {:type "sequence-data"
                                                                       :data [{:type "set-attribute" :target "timer-group" :attr-name "visible" :attr-value true}
                                                                              {:type "timer-start" :target "timer"}]}

                                   :reset-timer                       {:type "sequence-data"
                                                                       :data [{:type "set-attribute" :target "timer-group" :attr-name "visible" :attr-value false}
                                                                              {:type "timer-reset" :target "timer"}]}

                                   :recording-countdown-ended         {:type "sequence-data"
                                                                       :data [{:type "action" :id "reset-timer"}
                                                                              {:type "action" :id "plug-in-sound-bar"}
                                                                              {:type "action" :id "show-stop-record-button"}
                                                                              {:type "action" :id "start-recording"}]}

                                   ;; Record audio actions

                                   :start-video                       {:type "test-var-scalar"
                                                                       :var-name "played-once"
                                                                       :value    "true"
                                                                       :success {:type "reset-video" :target "video"
                                                                                 :from-var [{:var-name "video-src" :action-property "src"}]}
                                                                       :fail {:type "sequence-data"
                                                                              :data [{:type "set-variable" :var-name "played-once" :var-value "true"}
                                                                                     {:type "play-video" :target "video"
                                                                                      :from-var [{:var-name "video-src" :action-property "src"}]}]}}

                                   :start-playing                     {:type "test-var-scalar"
                                                                       :var-name "record"
                                                                       :value    "true"
                                                                       :success {:type "sequence-data"
                                                                                 :data [{:type "action" :id "remove-timeout-timer"}
                                                                                        {:type "action" :id "start-playback-dialog"}
                                                                                        {:type "parallel"
                                                                                         :data [{:type     "audio"
                                                                                                 :tags     ["recorded-audio-flow"]
                                                                                                 :from-var [{:var-name        "recording-studio-audio"
                                                                                                             :action-property "id"}]}
                                                                                                {:type "action" :id "start-video"}]}
                                                                                        {:type "action" :id "stop-play-button-click-handler"}
                                                                                        {:type "remove-flows" :flow-tag "recorded-audio-flow"}]}
                                                                       :fail {:type "sequence-data"
                                                                              :data [{:type "action" :id "remove-timeout-timer"}
                                                                                     {:type "action" :id "start-playback-dialog-2"}
                                                                                     {:type "action" :id "start-video"}
                                                                                     {:type "action" :id "stop-play-button-click-handler"}]}}

                                   :stop-playing                      {:type "test-var-scalar"
                                                                       :var-name "record"
                                                                       :value    "true"
                                                                       :success {:type "sequence-data"
                                                                                 :data [{:type "action" :id "timeout-timer"}
                                                                                        {:type "action" :id "stop-playback-dialog"}
                                                                                        {:type "parallel"
                                                                                         :data [{:type "remove-flows" :flow-tag "recorded-audio-flow"}
                                                                                                {:type "stop-video" :target "video"}]}]}
                                                                       :fail {:type "sequence-data"
                                                                              :data [{:type "action" :id "timeout-timer"}
                                                                                     {:type "action" :id "stop-playback-dialog-2"}
                                                                                     {:type "parallel"
                                                                                      :data [{:type "finish-flows" :tag "just-play"}
                                                                                             {:type "stop-video" :target "video"}]}]}}

                                   :start-recording                   {:type "sequence-data"
                                                                       :data [{:type "action" :id "start-recording-dialog"}
                                                                              {:type "action" :id "timeout-timer"}
                                                                              {:type "parallel"
                                                                               :data [{:type "start-audio-recording"}
                                                                                      {:type "action" :id "start-video"}]}
                                                                              {:type "action" :id "stop-record-button-click-handler"}]}

                                   :stop-recording                    {:type "sequence-data"
                                                                       :data [{:type "stop-audio-recording" :var-name "recording-studio-audio"}
                                                                              {:type     "set-progress"
                                                                               :var-name "recording-studio"
                                                                               :from-var [{:var-name        "recording-studio-audio"
                                                                                           :action-property "var-value"}]}
                                                                              {:type "action" :id "remove-timeout-timer"}
                                                                              {:type "parallel"
                                                                               :data [{:type "action" :id "stop-recording-dialog"}
                                                                                      {:type "stop-video" :target "video"}]}]}
                                   ;; Timeout

                                   :timeout-timer                     {:type     "set-interval"
                                                                       :id       "incorrect-answer-checker"
                                                                       :interval 25000
                                                                       :action   "timeout"}

                                   :timeout                           {:type       "action"
                                                                       :unique-tag "instructions"
                                                                       :from-var   [{:var-name        "timeout-instructions-action"
                                                                                     :action-property "id"}]}

                                   :remove-timeout-timer              {:type "remove-interval"
                                                                       :id   "incorrect-answer-checker"}

                                   :empty                             {:type "empty" :duration 100}

                                   ;; Dialogs

                                   :intro-dialog                      (dialog/default "Main")
                                   :start-recording-dialog            (dialog/default "Start recording")
                                   :stop-recording-dialog             (dialog/default "Stop recording")
                                   :start-playback-dialog             (dialog/default "Start playback")
                                   :stop-playback-dialog              (dialog/default "Stop playback")
                                   :start-playback-dialog-2           (dialog/default "Start playback")
                                   :stop-playback-dialog-2            (dialog/default "Stop playback")

                                   ;; Available actions

                                   :show-button-record                {:type "parallel"
                                                                       :data [{:type "action" :id "hide-stop-record-button"}
                                                                              {:type "action" :id "show-start-record-button"}]}

                                   :show-button-stop                  {:type "parallel"
                                                                       :data [{:type "action" :id "hide-start-record-button"}
                                                                              {:type "action" :id "show-stop-record-button"}]}

                                   :ask-recording                     {:type                "sequence-data"
                                                                       :workflow-user-input true
                                                                       :tags                ["ask-recording"]
                                                                       :data                [{:type "set-variable" :var-name "record" :var-value "true"}
                                                                                             {:type "action" :id "hide-approve-button"}
                                                                                             {:type "action" :id "hide-start-play-button"}
                                                                                             {:type "action" :id "hide-stop-play-button"}
                                                                                             {:type "action" :id "hide-stop-record-button"}
                                                                                             {:type "action" :id "show-start-record-button"}]}

                                   :show-choices                     {:type                "sequence-data"
                                                                      :workflow-user-input true
                                                                      :tags                ["show-choices"]
                                                                      :data                [{:type "action" :id "show-thumbnails"}]}

                                   :just-play                        {:type "sequence-data"
                                                                      :workflow-user-input true
                                                                      :tags                ["just-play"]
                                                                      :data [{:type "set-variable" :var-name "record" :var-value "false"}
                                                                             {:type "action" :id "show-start-play-button"}
                                                                             {:type "action" :id "highlight-start-play-button"}]}}

                   :triggers      {:stop  {:on "back" :action "stop-activity"}
                                   :start {:on "start" :action "main"}}
                   :metadata      {:autostart         true
                                   :resources         []
                                   :guide-settings    {:show-guide true
                                                       :character  "mari"}
                                   :tracks            [{:id    "main"
                                                        :title "Main Track"
                                                        :nodes [{:type      "dialog"
                                                                 :action-id "intro-dialog"}]}
                                                       {:id    "playing"
                                                        :title "Playing Dialogs"
                                                        :nodes [{:type "prompt"
                                                                 :text "Plays when the user taps the playback button:"}
                                                                {:type      "dialog"
                                                                 :action-id "start-playback-dialog-2"}
                                                                {:type "prompt"
                                                                 :text "Plays when the user stops the playback:"}
                                                                {:type      "dialog"
                                                                 :action-id "stop-playback-dialog-2"}]}
                                                       {:id    "recording"
                                                        :title "Ask Recording Dialogs"
                                                        :nodes [{:type "prompt"
                                                                 :text "Plays when the user taps the record button:"}
                                                                {:type      "dialog"
                                                                 :action-id "start-recording-dialog"}
                                                                {:type "prompt"
                                                                 :text "Automatically plays when the user taps the record button to end their recording:"}
                                                                {:type      "dialog"
                                                                 :action-id "stop-recording-dialog"}
                                                                {:type "prompt"
                                                                 :text "Plays when the user taps the playback button:"}
                                                                {:type      "dialog"
                                                                 :action-id "start-playback-dialog"}
                                                                {:type "prompt"
                                                                 :text "Plays when the user stops the playback:"}
                                                                {:type      "dialog"
                                                                 :action-id "stop-playback-dialog"}]}]
                                   :available-actions [{:action "activate-sound-bar"
                                                        :name   "Turn on sound bar"}
                                                       {:action "deactivate-sound-bar"
                                                        :name   "Turn off sound bar"}
                                                       {:action "show-choices"
                                                        :name   "Show video choices"}
                                                       {:action "ask-recording"
                                                        :name   "Ask user to record"}
                                                       {:action "just-play"
                                                        :name   "Play without recording"}]}}
                  (add-control-actions "approve-button")
                  (add-control-actions "start-play-button")
                  (add-control-actions "stop-play-button")
                  (add-control-actions "start-record-button")
                  (add-control-actions "stop-record-button")
                  (add-control-actions "sound-bar")
                  (add-backward-compatibility-action "highlight-playback-button" "start-play-button" "highlight")
                  (add-backward-compatibility-action "highlight-record-button" "start-record-button" "highlight")
                  (add-backward-compatibility-action "show-button-record" "start-record-button" "show")
                  (add-backward-compatibility-action "show-button-stop" "stop-record-button" "show")))

(defn create [args]
  (let [thumbnail-1 (get-in args [:thumbnail-1 :src])
        video-1 (:video-1 args)
        thumbnail-2 (get-in args [:thumbnail-2 :src])
        video-2 (:video-2 args)
        thumbnail-3 (get-in args [:thumbnail-3 :src])
        video-3 (:video-3 args)
        main-actions [{:type "start-activity"}
                      {:type "set-attribute" :target "thumbnail-1" :attr-name "src" :attr-value thumbnail-1}
                      {:type "set-variable" :var-name "video-src-1" :var-value video-1}
                      {:type "set-attribute" :target "thumbnail-2" :attr-name "src" :attr-value thumbnail-2}
                      {:type "set-variable" :var-name "video-src-2" :var-value video-2}
                      {:type "set-attribute" :target "thumbnail-3" :attr-name "src" :attr-value thumbnail-3}
                      {:type "set-variable" :var-name "video-src-3" :var-value video-3}
                      {:type "set-variable" :var-name "x1" :var-value 470}
                      {:type "set-variable" :var-name "x2" :var-value 960}
                      {:type "set-variable" :var-name "x3" :var-value 1450}
                      {:type "action" :id "intro-dialog"}
                      {:type "action" :id "finish"}]]
    (-> template
        (assoc-in [:metadata :actions] (:actions m))
        (update :assets (fn [assets]
                          (vec (concat [{:url thumbnail-1 :type "image"}
                                        {:url video-1 :type "video"}
                                        {:url thumbnail-2 :type "image"}
                                        {:url video-2 :type "video"}
                                        {:url thumbnail-3 :type "image"}
                                        {:url video-3 :type "video"}]
                                       assets))))
        (assoc-in [:actions :main :data] main-actions))))

(defn update-template [activity-data args]
  (let [{:keys [action-name thumbnail video]} args
        thumbnail (:src thumbnail)
        n (read-string (str (last action-name)))
        index (* 2 (dec n))]
    (-> activity-data
        (assoc-in [:assets index] {:url thumbnail :type "image"})
        (assoc-in [:assets (inc index)] {:url video :type "video"})

        (assoc-in [:actions :main :data (+ index 1)]
                  {:type "set-attribute"
                   :target (str "thumbnail-" n)
                   :attr-name "src"
                   :attr-value thumbnail})
        (assoc-in [:actions :main :data (+ index 2)]
                  {:type "set-variable"
                   :var-name (str "video-src-" n)
                   :var-value video}))))

(core/register-template
  m
  create
  update-template)
