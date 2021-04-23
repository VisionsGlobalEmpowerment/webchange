(ns webchange.templates.library.recording-studio
  (:require
    [webchange.templates.core :as core]))

(def m {:id             23
        :name           "recording studio"
        :tags           ["Direct Instruction - Animated Instructor"]
        :description    "Users move a character around a race track filled with answer images.  Before time runs out, users must steer the character to as many correct answer options as possible while avoiding incorrect answer images."
        :lesson-sets    ["concepts-recording"]
        :preview        "/images/templates/previews/recording_studio.png"
        :props          {:game-changer? true}
        :options        {:demo-image {:type        "image"
                                      :label       "Prompt Image"
                                      :description "What visual prompt do you want to show on the screen?"}
                         :background {:type        "image"
                                      :collection  "backgrounds"
                                      :label       "Background"
                                      :description "Select the background for the activity"}
                         :character  {:type        "characters"
                                      :label       "Character"
                                      :description "What character do you want to include in this activity?"
                                      :max         1}}
        :options-groups [{:title   "Select Background"
                          :options ["background"]}
                         {:title   "Add Content"
                          :options ["character" "demo-image"]}]
        :fields         [{:name "image-src"
                          :type "image"}]})

(def t {:assets        [{:url "/raw/clipart/recording_studio/recording_studio_background.png" :type "image"}
                        {:url "/raw/clipart/recording_studio/recording_studio_decoration.png" :type "image"}
                        {:url "/raw/clipart/recording_studio/recording_studio_surface.png" :type "image"}
                        {:url "/raw/clipart/recording_studio/mic.png" :type "image"}
                        {:url "/raw/clipart/recording_studio/green-circle.png" :type "image"}
                        {:url "/raw/audio/l2/a11/L2_A11_Mari.m4a" :type "audio"}
                        {:url "/raw/audio/l2/mari-chants.m4a" :size 5 :type "audio" :alias "mari chants"}]
        :objects       {:background              {:type       "layered-background"
                                                  :background {:src "/raw/clipart/recording_studio/recording_studio_background.png"}
                                                  :decoration {:src "/raw/clipart/recording_studio/recording_studio_decoration.png"}
                                                  :surface    {:src "/raw/clipart/recording_studio/recording_studio_surface.png"}}
                        :concept-image           {:type       "image"
                                                  :x          965
                                                  :y          270
                                                  :origin     {:type "center-center"}
                                                  :scale-x    10
                                                  :scale-y    10
                                                  :max-width  400
                                                  :max-height 300
                                                  :min-height 300
                                                  :src        ""
                                                  :visible    false
                                                  :states     {:hidden  {:visible false}
                                                               :visible {:visible true}}}
                        :mic                     {:type    "image"
                                                  :x       960
                                                  :y       750
                                                  :origin  {:type "center-center"}
                                                  :width   360
                                                  :height  656
                                                  :src     "/raw/clipart/recording_studio/mic.png"
                                                  :visible true
                                                  }
                        :mari                    {:type       "animation"
                                                  :x          1300
                                                  :y          620
                                                  :width      473
                                                  :height     511
                                                  :scene-name "mari"
                                                  :transition "mari"
                                                  :anim       "idle"
                                                  :loop       true
                                                  :name       "mari"
                                                  :editable?  true
                                                  :scale-x    0.5
                                                  :scale-y    0.5
                                                  :speed      0.35
                                                  :start      true
                                                  :states     {:left {:scale-x 1} :right {:scale-x -1}}
                                                  :actions    {:click {:id "mari-click" :on "click" :type "action"}}}
                        :record-button           {:type     "group"
                                                  :x        896 :y 513
                                                  :width    128 :height 128
                                                  :children ["record-button-back" "record-button-icon" "record-button-icon-int"]
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
                        :record-button-icon-int  {:type          "rectangle"
                                                  :transition    "record-button-icon-int"
                                                  :x             40 :y 40
                                                  :width         48 :height 48
                                                  :border-radius 32
                                                  :fill          0xFFFFFF}
                        :approve-group           {:type     "group"
                                                  :x        1776
                                                  :y        48
                                                  :width    96 :height 96
                                                  :visible  false
                                                  :states   {:hidden  {:visible false}
                                                             :visible {:visible true}}
                                                  :children ["approve-background"
                                                             "approve-playback-button"
                                                             ]}
                        :approve-background      {:type          "rectangle"
                                                  :x             0
                                                  :y             0
                                                  :width         96
                                                  :height        96
                                                  :border-radius 48
                                                  :fill          0xFF5C00}
                        :playback-group          {:type     "group"
                                                  :x        896 :y 667
                                                  :width    128 :height 128
                                                  :visible  false
                                                  :states   {:hidden  {:visible false}
                                                             :visible {:visible true}}
                                                  :children ["playback-background"
                                                             "green"
                                                             "run-playback-button"
                                                             "stop-playback-button"]}
                        :playback-background     {:type          "rectangle"
                                                  :x             0 :y 0
                                                  :width         128 :height 128
                                                  :border-radius 24
                                                  :fill          0xFFFFFF}
                        :green                   {:type    "image"
                                                  :x       16
                                                  :y       16
                                                  :width   96
                                                  :height  96
                                                  :src     "/raw/clipart/recording_studio/green-circle.png"
                                                  :visible true
                                                  }
                        :run-playback-button     {:type    "svg-path"
                                                  :x       52
                                                  :y       42
                                                  :states  {:hidden  {:visible false}
                                                            :visible {:visible true}}
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
                                                  :states        {:hidden {:visible false} :visible {:visible true}}
                                                  :actions       {:click {:id "stop-playback-click" :on "click" :type "action"}}
                                                  :fill          0xFFFFFF}
                        :approve-playback-button {:type    "svg-path"
                                                  :x       20
                                                  :y       25
                                                  :width   128
                                                  :height  128
                                                  :fill    "#FFFFFF",
                                                  :actions {:click {:id "approve-playback-click" :on "click" :type "action"}}
                                                  :data    "M 9.29193 13.1343L0 22.3134L22.6633 45L59 9.47761L49.1793 0L22.6633 26.194L9.29193 13.1343"}}
        :scene-objects [["background"]
                        ["concept-image" "mic" "mari"]
                        ["record-button" "playback-group" "approve-group"]]
        :actions       {:introduction                       {:type "sequence"
                                                             :data ["introduction-welcome"
                                                                    "introduction-main"
                                                                    "introduction-how-to-get-help"
                                                                    "move-mari"
                                                                    "introduction-lets-get-started"
                                                                    ]}
                        :introduction-main                  {:type "sequence"
                                                             :data ["introduction-how-to-start-record"
                                                                    "introduction-when-to-begin-talking"
                                                                    "introduction-how-to-stop"
                                                                    "introduction-how-to-replay"
                                                                    ]}
                        :introduction-welcome               {:type               "sequence-data"
                                                             :editor-type        "dialog"
                                                             :concept-var        "current-concept"
                                                             :data               [{:type "sequence-data"
                                                                                   :data [{:type "empty" :duration 0}
                                                                                          {:type        "animation-sequence"
                                                                                           :phrase-text "New action"
                                                                                           :audio       nil}]}]
                                                             :phrase             "introduction-welcome"
                                                             :phrase-description "Welcome to the Recording Studio"}
                        :introduction-how-to-start-record   {:type               "sequence-data"
                                                             :editor-type        "dialog"
                                                             :concept-var        "current-concept"
                                                             :skippable          true
                                                             :data               [{:type "sequence-data"
                                                                                   :data [{:type "empty" :duration 0}
                                                                                          {:type        "animation-sequence"
                                                                                           :phrase-text "New action"
                                                                                           :audio       nil}]}]
                                                             :phrase             "introduction-how-to-start-record"
                                                             :phrase-description "How to start record"}
                        :introduction-when-to-begin-talking {:type               "sequence-data"
                                                             :editor-type        "dialog"
                                                             :concept-var        "current-concept"
                                                             :skippable          true
                                                             :data               [{:type "sequence-data"
                                                                                   :data [{:type "empty" :duration 0}
                                                                                          {:type        "animation-sequence"
                                                                                           :phrase-text "New action"
                                                                                           :audio       nil}]}]
                                                             :phrase             "When to begin talking"
                                                             :phrase-description "How to stop recording"}
                        :introduction-how-to-stop           {:type               "sequence-data"
                                                             :editor-type        "dialog"
                                                             :concept-var        "current-concept"
                                                             :skippable          true
                                                             :data               [{:type "sequence-data"
                                                                                   :data [{:type "empty" :duration 0}
                                                                                          {:type        "animation-sequence"
                                                                                           :phrase-text "New action"
                                                                                           :audio       nil}]}]
                                                             :phrase             "introduction-how-to-stop"
                                                             :phrase-description "How to stop"}
                        :introduction-how-to-replay         {:type               "sequence-data"
                                                             :editor-type        "dialog"
                                                             :concept-var        "current-concept"
                                                             :skippable          true
                                                             :data               [{:type "sequence-data"
                                                                                   :data [{:type "empty" :duration 0}
                                                                                          {:type        "animation-sequence"
                                                                                           :phrase-text "New action"
                                                                                           :audio       nil}]}]
                                                             :phrase             "introduction-how-to-replay"
                                                             :phrase-description "How to replay"}
                        :introduction-how-to-get-help       {:type               "sequence-data"
                                                             :editor-type        "dialog"
                                                             :concept-var        "current-concept"
                                                             :data               [{:type "sequence-data"
                                                                                   :data [{:type "empty" :duration 0}
                                                                                          {:type        "animation-sequence"
                                                                                           :phrase-text "New action"
                                                                                           :audio       nil}]}]
                                                             :phrase             "introduction-how-to-get-help"
                                                             :phrase-description "How to get help"}
                        :move-mari                          {:type "sequence-data"
                                                             :data [
                                                                    {:type          "transition"
                                                                     :transition-id "mari"
                                                                     :to            {:x 450 :y 300 :duration 2}}
                                                                    {:type   "state"
                                                                     :id     "right"
                                                                     :target "mari"}
                                                                    ]
                                                             }
                        :move-mari-back                     {:type "sequence-data"
                                                             :data [
                                                                    {:type          "transition"
                                                                     :transition-id "mari"
                                                                     :to            {:x 1300
                                                                                     :y 620 :duration 2}}
                                                                    {:type   "state"
                                                                     :id     "left"
                                                                     :target "mari"}
                                                                    ]
                                                             }
                        :introduction-lets-get-started      {:type               "sequence-data"
                                                             :editor-type        "dialog"
                                                             :concept-var        "current-concept"
                                                             :data               [{:type "sequence-data"
                                                                                   :data [{:type "empty" :duration 0}
                                                                                          {:type        "animation-sequence"
                                                                                           :phrase-text "New action"
                                                                                           :audio       nil}]}]
                                                             :phrase             "introduction-lets-get-started"
                                                             :phrase-description "Let's get started"}

                        :demonstration                      {:type "sequence"
                                                             :data ["demonstration-show-demo-image"
                                                                    "demonstration-today-task"
                                                                    "demonstration-what-to-tell"
                                                                    "delay"
                                                                    "demonstration-example"
                                                                    "move-mari-back"
                                                                    ]}
                        :delay                              {:type "empty" :duration 2000}
                        :demonstration-show-demo-image      {:type "sequence-data"
                                                             :data [{:type "action"
                                                                     :id   "demonstration-set-demo-image-src"}
                                                                    {:type "state" :target "concept-image" :id "visible"}]}

                        :demonstration-set-demo-image-src   {:type       "set-attribute"
                                                             :target     "concept-image"
                                                             :attr-name  "src"
                                                             :attr-value "***"}

                        :demonstration-today-task           {:type               "sequence-data"
                                                             :editor-type        "dialog"
                                                             :concept-var        "current-concept"
                                                             :data               [{:type "sequence-data"
                                                                                   :data [{:type "empty" :duration 0}
                                                                                          {:type        "animation-sequence"
                                                                                           :phrase-text "New action"
                                                                                           :audio       nil}]}]
                                                             :phrase             "demonstration-today-task"
                                                             :phrase-description "Today task"}

                        :demonstration-what-to-tell         {:type               "sequence-data"
                                                             :editor-type        "dialog"
                                                             :concept-var        "current-concept"
                                                             :data               [{:type "sequence-data"
                                                                                   :data [{:type "empty" :duration 0}
                                                                                          {:type        "animation-sequence"
                                                                                           :phrase-text "New action"
                                                                                           :audio       nil}]}]
                                                             :phrase             "demonstration-what-to-tell"
                                                             :phrase-description "What to tell"}

                        :demonstration-example              {:type               "sequence-data"
                                                             :editor-type        "dialog"
                                                             :concept-var        "current-concept"
                                                             :skippable          true
                                                             :data               [{:type "sequence-data"
                                                                                   :data [{:type "empty" :duration 0}
                                                                                          {:type        "animation-sequence"
                                                                                           :phrase-text "New action"
                                                                                           :audio       nil}]}]
                                                             :phrase             "demonstration-example"
                                                             :phrase-description "Example"}


                        :init-vars                          {:type "parallel"
                                                             :data [{:type "set-variable" :var-name "record-button-state" :var-value "record"}
                                                                    {:type "set-variable" :var-name "help-available" :var-value false}]}

                        :next-concept                       {:type "sequence-data"
                                                             :data [{:type "action" :id "hide-image"}
                                                                    {:type "action" :id "hide-playback-group"}
                                                                    {:type "action" :id "renew-concept"}
                                                                    {:type "action" :id "show-image"}
                                                                    {:type "action" :id "prompt"}]}

                        :prompt                             {:type               "sequence-data"
                                                             :editor-type        "dialog"
                                                             :concept-var        "current-concept"
                                                             :data               [{:type "sequence-data"
                                                                                   :data [{:type "empty" :duration 0}
                                                                                          {:type        "animation-sequence"
                                                                                           :phrase-text "New action"
                                                                                           :audio       nil}]}]
                                                             :phrase             "prompt"
                                                             :phrase-description "Concept prompt"}

                        :renew-concept                      {:type        "lesson-var-provider"
                                                             :from        "concepts-recording"
                                                             :provider-id "concepts"
                                                             :variables   ["current-concept"]
                                                             :on-end      "finish"}

                        :hide-image                         {:type "state" :target "concept-image" :id "hidden"}
                        :show-image                         {:type "sequence-data"
                                                             :data [{:type      "set-attribute"
                                                                     :target    "concept-image"
                                                                     :from-var  [{:var-name "current-concept" :var-property "image-src" :action-property "attr-value"}]
                                                                     :attr-name "src"}
                                                                    {:type "state" :target "concept-image" :id "visible"}]}

                        :show-button-record                 {:type "parallel"
                                                             :data [
                                                                    {:type          "transition"
                                                                     :transition-id "record-button-icon"
                                                                     :to            {:border-radius 48 :duration 0.2}}
                                                                    {:type          "transition"
                                                                     :transition-id "record-button-icon-int"
                                                                     :to            {:border-radius 32 :duration 0.2}}
                                                                    ]}
                        :show-button-stop                   {:type "parallel"
                                                             :data [
                                                                    {:type          "transition"
                                                                     :transition-id "record-button-icon"
                                                                     :to            {:border-radius 24 :duration 0.2}}
                                                                    {:type          "transition"
                                                                     :transition-id "record-button-icon-int"
                                                                     :to            {:border-radius 16 :duration 0.2}}
                                                                    ]}
                        :mari-click                         {:type     "test-var-scalar"
                                                             :var-name "help-available"
                                                             :value    true
                                                             :success  {:type "sequence-data"
                                                                        :data [{:type "set-variable" :var-name "help-available" :var-value false}
                                                                               {:type "action" :id "introduction-main"}
                                                                               {:type "set-variable" :var-name "help-available" :var-value true}]}
                                                             :fail     {:type "empty" :duration 10}}
                        :enable-help                        {:type "set-variable" :var-name "help-available" :var-value true}
                        :record-button-click                {:type     "test-var-scalar"
                                                             :var-name "record-button-state"
                                                             :value    "stop"
                                                             :success  {:type "parallel"
                                                                        :data [{:type "action" :id "show-button-record"}
                                                                               {:type "set-variable" :var-name "record-button-state" :var-value "record"}
                                                                               {:type "sequence-data"
                                                                                :data [{:type "stop-audio-recording" :var-name "recording-studio-audio"}
                                                                                       {:type     "set-progress"
                                                                                        :var-name "recording-studio"
                                                                                        :from-var [{:var-name        "recording-studio-audio"
                                                                                                    :action-property "var-value"}]}
                                                                                       {:type "action" :id "show-playback-group"}]}]}
                                                             :fail     {:type "sequence-data"
                                                                        :data [{:type "set-variable" :var-name "record-button-state" :var-value "stop"}
                                                                               {:type "start-audio-recording"}
                                                                               {:type "action" :id "show-button-stop"}]}}

                        :show-playback-group                {:type "parallel"
                                                             :data [{:type "state" :target "approve-group" :id "visible"}
                                                                    {:type "state" :target "playback-group" :id "visible"}]}

                        :hide-playback-group                {:type "parallel"
                                                             :data [{:type "state" :target "approve-group" :id "hidden"}
                                                                    {:type "state" :target "playback-group" :id "hidden"}]}


                        :run-record-playing                 {:type "parallel"
                                                             :data [{:type "state" :target "run-playback-button" :id "hidden"}
                                                                    {:type "state" :target "stop-playback-button" :id "visible"}
                                                                    {:type     "audio"
                                                                     :tags     ["recorded-audio-flow"]
                                                                     :from-var [{:var-name        "recording-studio-audio"
                                                                                 :action-property "id"}]}]}
                        :stop-record-playing                {:type "parallel"
                                                             :data [{:type "remove-flows" :flow-tag "recorded-audio-flow"}
                                                                    {:type "state" :target "run-playback-button" :id "visible"}
                                                                    {:type "state" :target "stop-playback-button" :id "hidden"}]}
                        :run-playback-click                 {:type "sequence-data"
                                                             :data [{:type "action" :id "run-record-playing"}
                                                                    {:type "action" :id "stop-record-playing"}]}
                        :stop-playback-click                {:type "action"
                                                             :id   "stop-record-playing"}

                        :approve-playback-click             {:type "action" :id "next-concept"}

                        :start-scene                        {:type "sequence"
                                                             :data ["start-activity"
                                                                    "init-vars"
                                                                    "introduction"
                                                                    "demonstration"
                                                                    "enable-help"
                                                                    "next-concept"]}
                        :finish                             {:type "sequence-data"
                                                             :data [{:type "action" :id "finish-activity"}
                                                                    {:type "scene-exit" :exit-point "back"}]}
                        :exit                               {:type "sequence-data"
                                                             :data [{:type "action" :id "stop-activity"}
                                                                    {:type "scene-exit" :exit-point "back"}]}

                        :start-activity                     {:type "start-activity" :id "recording-studio"}
                        :stop-activity                      {:type "stop-activity" :id "recording-studio"}
                        :finish-activity                    {:type "finish-activity" :id "recording-studio"}}
        :triggers      {:stop  {:on "back" :action "stop-activity"}
                        :start {:on "start" :action "start-scene"}}
        :metadata      {:autostart true
                        :resources []
                        :tracks    [{:title "Introduction"
                                     :nodes [{:type      "dialog"
                                              :action-id :introduction-welcome}
                                             {:type      "dialog"
                                              :action-id :introduction-how-to-start-record}
                                             {:type      "dialog"
                                              :action-id :introduction-when-to-begin-talking}
                                             {:type      "dialog"
                                              :action-id :introduction-how-to-stop}
                                             {:type      "dialog"
                                              :action-id :introduction-how-to-replay}
                                             {:type      "dialog"
                                              :action-id :introduction-how-to-get-help}
                                             {:type      "dialog"
                                              :action-id :introduction-lets-get-started}]}
                                    {:title "Demonstration"
                                     :nodes [{:type      "dialog"
                                              :action-id :demonstration-today-task}
                                             {:type      "dialog"
                                              :action-id :demonstration-what-to-tell}
                                             {:type      "dialog"
                                              :action-id :demonstration-example}]}
                                    {:title "Concept List"
                                     :nodes [{:type      "dialog"
                                              :action-id :prompt}]}]}})

(defn f
  [t args]
  (let [demo-image (get-in args [:demo-image :src])]
    (-> t
        (assoc-in [:actions :demonstration-set-demo-image-src :attr-value] demo-image)
        (update-in [:metadata :resources] conj demo-image))))

(core/register-template
  m
  (partial f t))
