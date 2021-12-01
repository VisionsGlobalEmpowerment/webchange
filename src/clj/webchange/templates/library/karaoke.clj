(ns webchange.templates.library.karaoke
  (:require
    [webchange.templates.core :as core]
    [webchange.templates.utils.dialog :as dialog]
    [webchange.templates.utils.common :as common]))

(def m {:id          36
        :name        "Karaoke"
        :tags        ["listening comprehension" "rhyming" "express ideas verbally" "Guided Practice"]
        :description "Karaoke"
        :options     {:video        {:label "Video File"
                                     :type  "video"}
                      :video-ranges {:label       "Video Range"
                                     :type        "video-ranges"
                                     :video-param "video"
                                     :max         10}}
        :actions     {:config-video {:title         "Config video"
                                     :default-props "config-video"
                                     :options       {:video        {:label "Video File"
                                                                    :type  "video"}
                                                     :video-ranges {:label       "Video Range"
                                                                    :type        "video-ranges"
                                                                    :video-param "video"
                                                                    :max         10}}}}})

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
                                                  :speed      0.15
                                                  :start      true
                                                  :actions    {:click {:id "mari-click" :on "click" :type "action"}}}
                        :video                   {:type       "video"
                                                  :transition "video"
                                                  :x          633
                                                  :y          83
                                                  :width      649
                                                  :height     379
                                                  :visible    false
                                                  :editable?  {:select        true
                                                               :show-in-tree? true}}
                        :play-video-button       {:type      "image"
                                                  :src       "/raw/clipart/karaoke/play_button.png"
                                                  :x         900
                                                  :y         210
                                                  :width     128
                                                  :height    128
                                                  :editable? true
                                                  :actions   {:click {:id "on-play-video-button-clicked" :on "click" :type "action"}}}

                        :mic                     {:type       "image"
                                                  :src        "/raw/clipart/karaoke/mic.png"
                                                  :transition "mic"
                                                  :x          770
                                                  :y          485
                                                  :visible    false}
                        :record-button           {:type       "group"
                                                  :x          896 :y 513
                                                  :scene-name "record-button"
                                                  :width      128 :height 128
                                                  :visible    false
                                                  :children   ["record-button-back" "record-button-icon" "record-button-icon-int"]
                                                  :actions    {:click {:id "record-button-click" :on "click" :type "action" :unique-tag "intro"}}}
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
                        :record-button-icon-int  {:type          "rectangle"
                                                  :transition    "record-button-icon-int"
                                                  :x             40 :y 40
                                                  :width         48 :height 48
                                                  :border-radius 32
                                                  :fill          0xFFFFFF}

                        :approve-group           {:type     "group"
                                                  :x        1706
                                                  :y        132
                                                  :width    96 :height 96
                                                  :visible  false
                                                  :children ["approve-background"
                                                             "approve-playback-button"]}
                        :approve-background      {:type          "rectangle"
                                                  :x             0
                                                  :y             0
                                                  :transition    "approve-background"
                                                  :width         96
                                                  :height        96
                                                  :border-radius 48
                                                  :fill          0xFF5C00}
                        :playback-group          {:type     "group"
                                                  :x        896 :y 667
                                                  :width    128 :height 128
                                                  :visible  false
                                                  :children ["playback-background"
                                                             "green"
                                                             "run-playback-button"
                                                             "stop-playback-button"]}
                        :playback-background     {:type          "rectangle"
                                                  :x             0 :y 0
                                                  :width         128 :height 128
                                                  :border-radius 24
                                                  :fill          0xFFFFFF}
                        :green                   {:type          "rectangle"
                                                  :x             16 :y 16
                                                  :width         96 :height 96
                                                  :border-radius 48
                                                  :fill          0x10BC2B}
                        :run-playback-button     {:type    "svg-path"
                                                  :x       52
                                                  :y       42
                                                  :fill    "#FFFFFF",
                                                  :actions {:click {:id "run-playback-click" :on "click" :type "action"}}
                                                  :data    "M34.2834 17.57L6.05798 0.63479C3.39189 -0.964869 0 0.955583 0 4.06476V37.9352C0 41.0444 3.39189 42.9649 6.05798 41.3652L34.2834 24.43C36.8727 22.8764 36.8727 19.1236 34.2834 17.57"}
                        :stop-playback-button    {:type          "rectangle"
                                                  :x             45
                                                  :y             45
                                                  :width         40
                                                  :height        40
                                                  :scene-name    "stop-playback-button"
                                                  :visible       false
                                                  :border-radius 10
                                                  :actions       {:click {:id "stop-playback-click" :on "click" :type "action"}}
                                                  :fill          0xFFFFFF}
                        :approve-playback-button {:type    "svg-path"
                                                  :x       20
                                                  :y       25
                                                  :width   128
                                                  :height  128
                                                  :fill    "#FFFFFF",
                                                  :actions {:click {:id "approve-playback-click" :on "click" :type "action" :unique-tag "intro"}}
                                                  :data    "M 9.29193 13.1343L0 22.3134L22.6633 45L59 9.47761L49.1793 0L22.6633 26.194L9.29193 13.1343"}}
        :scene-objects [["background"]
                        ["mic" "record-button" "playback-group" "approve-group"]
                        ["video" "play-video-button"]
                        ["mari"]]
        :actions       {:start-scene                          {:type "sequence"
                                                               :data ["start-activity"
                                                                      "init-video-src"
                                                                      "run-round-1"]}
                        :start-activity                       {:type "start-activity"}
                        :init-video-src                       {:type "set-variable" :var-name "video-src" :var-value "-"}

                        ;; Common

                        :show-video                           {:type "set-attribute" :target "video" :attr-name "visible" :attr-value true}
                        :hide-video                           {:type "set-attribute" :target "video" :attr-name "visible" :attr-value false}
                        :show-play-video-button               {:type "set-attribute" :target "play-video-button" :attr-name "visible" :attr-value true}
                        :hide-play-video-button               {:type "set-attribute" :target "play-video-button" :attr-name "visible" :attr-value false}
                        :show-playback                        {:type "parallel"
                                                               :data [{:type "set-attribute" :target "approve-group" :attr-name "visible" :attr-value true}
                                                                      {:type "set-attribute" :target "playback-group" :attr-name "visible" :attr-value true}]}
                        :hide-playback                        {:type "parallel"
                                                               :data [{:type "set-attribute" :target "approve-group" :attr-name "visible" :attr-value false}
                                                                      {:type "set-attribute" :target "playback-group" :attr-name "visible" :attr-value false}]}
                        :play-video-seq                       {:type "sequence"
                                                               :data ["show-video"
                                                                      "play-video"
                                        ;"hide-video"
                                                                      ]}

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
                                                                      "highlight-play-video-button"
                                                                      "start-timeout-play"]}
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
                                                               :data ["remove-timeout"
                                                                      "hide-play-video-button"
                                                                      "play-video-seq"
                                                                      "delay-2"
                                                                      "run-round-2"]}
                        :delay-2                              {:type "empty" :duration 0}

                        ;; Round 2

                        :run-round-2                          {:type "sequence"
                                                               :data ["set-current-round-2"
                                                                      "show-play-video-button"
                                                                      "dialog-sing-along"
                                                                      "highlight-play-video-button"
                                                                      "start-timeout-play"]}

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
                                                               :data ["remove-timeout"
                                                                      "hide-play-video-button"
                                                                      "play-video-seq"
                                                                      "delay-2"
                                                                      "run-round-3-intro"]}

                        ;; Round 3 Intro
                        :show-mic                             {:type "set-attribute" :target "mic" :attr-name "visible" :attr-value true}
                        :run-round-3-intro                    {:type "sequence"
                                                               :data ["show-mic"
                                                                      "dialog-round-3-intro"
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
                                                               :data [{:type "set-attribute" :target "record-button" :attr-name "visible" :attr-value true}
                                                                      {:type "set-attribute" :target "playback-group" :attr-name "visible" :attr-value true}]}

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

                        :activate-record-button               {:type "parallel"
                                                               :data [{:type          "transition"
                                                                       :transition-id "record-button-icon"
                                                                       :to            {:border-radius 24 :duration 0.2
                                                                                       :fill          0x0000FF}}
                                                                      {:type          "transition"
                                                                       :transition-id "record-button-icon-int"
                                                                       :to            {:border-radius 16 :duration 0.2}}]}
                        :deactivate-record-button             {:type "parallel"
                                                               :data [{:type          "transition"
                                                                       :transition-id "record-button-icon"
                                                                       :to            {:border-radius 48 :duration 0.2
                                                                                       :fill          0xED1C24}}
                                                                      {:type          "transition"
                                                                       :transition-id "record-button-icon-int"
                                                                       :to            {:border-radius 32 :duration 0.2}}]}

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
                                                                                         {:type "action" :id "show-playback"}
                                                                                         {:type "action" :id "stop-recording-dialog"}]}]}
                                                               :fail     {:type "sequence-data"
                                                                          :data [{:type "action" :id "remove-timeout"}
                                                                                 {:type "set-variable" :var-name "record-button-state" :var-value "stop"}
                                                                                 {:type "start-audio-recording"}
                                                                                 {:type "action" :id "hide-playback"}
                                                                                 {:type "action" :id "activate-record-button"}]}}
                        :run-record-playing                   {:type "parallel"
                                                               :data [{:type "set-attribute" :target "run-playback-button" :attr-name "visible" :attr-value false}
                                                                      {:type "set-attribute" :target "stop-playback-button" :attr-name "visible" :attr-value true}
                                                                      {:type     "audio"
                                                                       :tags     ["recorded-audio-flow"]
                                                                       :from-var [{:var-name        "karaoke-recorded-song"
                                                                                   :action-property "id"}]}]}
                        :stop-record-playing                  {:type "parallel"
                                                               :data [{:type "remove-flows" :flow-tag "recorded-audio-flow"}
                                                                      {:type "set-attribute" :target "run-playback-button" :attr-name "visible" :attr-value true}
                                                                      {:type "set-attribute" :target "stop-playback-button" :attr-name "visible" :attr-value false}]}
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

                        :finish                               {:type "sequence-data"
                                                               :data [{:type "action" :id "finish-dialog"}
                                                                      {:type "finish-activity"}]}

                        :stop-scene                           {:type "sequence-data"
                                                               :data [{:type "stop-activity"}]}

                        :start-timeout-play                   {:type "sequence-data"
                                                               :data [{:type "set-variable" :var-name "timeout-instructions-action" :var-value "timeout-play-dialog"}
                                                                      {:type "action" :id "timeout-timer"}]}
                        :start-timeout-record                 {:type "sequence-data"
                                                               :data [{:type "set-variable" :var-name "timeout-instructions-action" :var-value "timeout-record-dialog"}
                                                                      {:type "action" :id "timeout-timer"}]}
                        :remove-timeout                       {:type "remove-interval"
                                                               :id   "timeout-timer"}
                        :timeout-timer                        {:type     "set-interval"
                                                               :id       "timeout-timer"
                                                               :interval 25000
                                                               :action   "timeout"}
                        :timeout                              {:type       "action"
                                                               :unique-tag "instructions"
                                                               :from-var   [{:var-name        "timeout-instructions-action"
                                                                             :action-property "id"}]}
                        :empty                                {:type "empty" :duration 100}

                        :stop-recording-dialog                (dialog/default "Stop recording")
                        :timeout-play-dialog                  (dialog/default "Timeout play")
                        :timeout-record-dialog                (dialog/default "Timeout record")
                        :finish-dialog                        (dialog/default "Finish")
                        }
        :triggers      {:stop  {:on "back" :action "stop-scene"}
                        :start {:on "start" :action "start-scene"}}
        :metadata      {:autostart         true
                        :tracks            [{:title "Round 1"
                                             :nodes [{:type      "dialog"
                                                      :action-id :dialog-are-you-ready}
                                                     {:type      "dialog"
                                                      :action-id :timeout-play-dialog}]}
                                            {:title "Round 2"
                                             :nodes [{:type      "dialog"
                                                      :action-id :dialog-sing-along}
                                                     {:type      "dialog"
                                                      :action-id :timeout-play-dialog}]}
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
                                                      :action-id :dialog-finish-intro}
                                                     {:type      "dialog"
                                                      :action-id :stop-recording-dialog}
                                                     {:type      "dialog"
                                                      :action-id :timeout-record-dialog}
                                                     {:type      "dialog"
                                                      :action-id :finish-dialog}]}]
                        :available-actions [{:action "activate-record-button"
                                             :name   "Activate record button"}
                                            {:action "deactivate-record-button"
                                             :name   "Deactivate record button"}]}})

(defn- config-video-file
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
          {:type "sequence-data"
           :tags ["user-interactions-blocked"]
           :data [{:type     "play-video"
                   :target   "video"
                   :start    from
                   :end      to
                   :from-var [{:var-name "video-src" :action-property "src"}]}]}
          {:type "action" :id "highlight-record-button"}
          {:type "action" :id "start-timeout-record"}]})

(defn- config-round-3
  [template {:keys [video-ranges]}]
  (let [actions (->> video-ranges
                     (map-indexed vector)
                     (map (fn [[idx range]]
                            (let [action-name (get-round-3-action-name idx)]
                              [(keyword action-name) (get-round-3-action-data idx range {:last?         (= idx (dec (count video-ranges)))
                                                                                         :finish-action "finish"})]))))]
    (-> template
        (update-in [:actions] merge (into {} actions)))))

(defn- config-video
  [activity-data args]
  (-> activity-data
      (config-video-file args)
      (config-round-3 args)
      (assoc-in [:metadata :saved-props :config-video] args)))

(defn- create-activity
  [args]
  (-> t
      (config-video args)
      (update-in [:actions :run-round-3-play :data] conj {:type "action" :id (get-round-3-action-name 0)})
      (common/add-highlight "play-video-button" "Highligh play video")
      (common/add-highlight "record-button" "Highligh record button")
      (common/add-highlight "playback-group" "Highligh play button")
      (common/add-highlight "approve-group" "Highligh approve button")
      (assoc-in [:metadata :actions] (:actions m))))

(defn- update-activity
  [old-data {:keys [action-name] :as args}]
  (case action-name
    "config-video" (config-video old-data args)))

(core/register-template
  m
  create-activity
  update-activity)
