(ns webchange.templates.library.recording-studio-rounds
  (:require
    [webchange.templates.utils.dialog :as dialog]
    [webchange.templates.core :as core]))

(def m {:id             46
        :name           "Recording Studio (Rounds)"
        :tags           ["Direct Instruction - Animated Instructor"]
        :description    "Recording Studio (Rounds)"
        :props          {:game-changer?    true
                         :preview          "/images/templates/previews/recording_studio.png"
                         :preview-anim     ["/images/templates/previews/recording_studio_1.png"
                                            "/images/templates/previews/recording_studio_2.png"]
                         :preview-activity {:course-slug   "recording-studio-test-english-gvfoggox"
                                            :activity-slug "recording-apple"}}
        :options        {:demo-image {:type        "image"
                                      :optional?   true
                                      :label       "Demo Image"
                                      :description "What demo image you want to show on the screen?"}
                         :image      {:type        "image"
                                      :optional?   true
                                      :label       "Prompt Image"
                                      :description "What visual prompt do you want to show on the screen?"}}
        :options-groups [{:title   "Add Content"
                          :options ["demo-image" "image"]}]
        :actions        {:add-round {:title    "Add Round"
                                     :track-id "main"
                                     :options  {:image {:type        "image"
                                                        :label       "Prompt Image"
                                                        :description "What visual prompt do you want to show on the screen?"}}}}})

(defn- get-layout-params
  []
  (let [canvas {:width  1920
                :height 1080}

        screen-margin-x 208
        screen-margin-y 48
        screen {:x         screen-margin-x
                :y         screen-margin-y
                :width     (- (:width canvas) (* 2 screen-margin-x))
                :height    768
                :padding-x 200
                :padding-y 100}

        right-margin {:x     (+ (:x screen) (:width screen))
                      :width screen-margin-x}

        concept-image {:x      (+ (:x screen) (:padding-x screen))
                       :y      (+ (:y screen) (:padding-y screen))
                       :width  (- (:width screen) (* 2 (:padding-x screen)))
                       :height (- (:height screen) (* 2 (:padding-y screen)))}

        approve-button-size 96
        approve-button {:x      (+ (:x right-margin)
                                   (/ (:width right-margin) 2)
                                   (- (/ approve-button-size 2)))
                        :y      (:y screen)
                        :width  approve-button-size
                        :height approve-button-size}]

    {:approve-button approve-button
     :concept-image  concept-image
     :screen         screen}))

(def layout-params (get-layout-params))

(def template {:assets        [{:url "/raw/clipart/recording_studio/green-circle.png" :type "image"}]
               :objects       {:background              {:type   "rectangle"
                                                         :x      0
                                                         :y      0
                                                         :width  1920
                                                         :height 1080
                                                         :fill   0x163760}
                               :screen                  (merge {:type          "rectangle"
                                                                :border-radius 32
                                                                :fill          0xB9D8E8}
                                                               (select-keys (:screen layout-params)
                                                                            [:x :y :width :height]))
                               :concept-image           (merge {:type       "image"
                                                                :image-size "contain"
                                                                :src        ""
                                                                :visible    false}
                                                               (select-keys (:concept-image layout-params)
                                                                            [:x :y :width :height]))
                               :mari                    {:type       "animation"
                                                         :x          1600
                                                         :y          800
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
                               :record-button           {:type       "group"
                                                         :x          996 :y 879
                                                         :transition "record-button"
                                                         :scene-name "record-button"
                                                         :width      128 :height 128
                                                         :filters    [{:name "brightness" :value 0}
                                                                      {:name "glow" :outer-strength 0 :color 0xffd700}],
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
                               :approve-group           (merge {:type     "group"
                                                                :visible  false
                                                                :filters  [{:name "brightness" :value 0}
                                                                           {:name "glow" :outer-strength 0 :color 0xffd700}]
                                                                :children ["approve-background"
                                                                           "approve-playback-button"]}
                                                               (select-keys (:approve-button layout-params)
                                                                            [:x :y :width :height]))
                               :approve-background      {:type          "rectangle"
                                                         :x             0
                                                         :y             0
                                                         :transition    "approve-background"
                                                         :width         96
                                                         :height        96
                                                         :border-radius 48
                                                         :fill          0xFF5C00}
                               :playback-group          {:type     "group"
                                                         :x        796 :y 879
                                                         :width    128 :height 128
                                                         :filters  [{:name "brightness" :value 0}
                                                                    {:name "glow" :outer-strength 0 :color 0xffd700}]
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
                                                         :transition    "green"
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
               :scene-objects [["background" "screen"]
                               ["concept-image" "mari"]
                               ["record-button" "playback-group" "approve-group"]]
               :actions       {:move-mari                 {:type "sequence-data"
                                                           :data [{:type          "transition"
                                                                   :transition-id "mari"
                                                                   :to            {:x 450 :y 300 :duration 2}}
                                                                  {:type "state" :id "right" :target "mari"}]}
                               :move-mari-back            {:type "sequence-data"
                                                           :data [{:type          "transition"
                                                                   :transition-id "mari"
                                                                   :to            {:x 1300 :y 620 :duration 2}}
                                                                  {:type "state" :id "left" :target "mari"}]}

                               :show-button-record        {:type "parallel"
                                                           :data [{:type          "transition"
                                                                   :transition-id "record-button-icon"
                                                                   :to            {:border-radius 48 :duration 0.2
                                                                                   :fill          0xED1C24}}
                                                                  {:type          "transition"
                                                                   :transition-id "record-button-icon-int"
                                                                   :to            {:border-radius 32 :duration 0.2}}]}

                               :show-button-stop          {:type "parallel"
                                                           :data [{:type          "transition"
                                                                   :transition-id "record-button-icon"
                                                                   :to            {:border-radius 24 :duration 0.2
                                                                                   :fill          0x0000FF}}
                                                                  {:type          "transition"
                                                                   :transition-id "record-button-icon-int"
                                                                   :to            {:border-radius 16 :duration 0.2}}]}

                               :highlight-record-button   {:type               "transition"
                                                           :transition-id      "record-button"
                                                           :return-immediately true
                                                           :from               {:brightness 0 :glow 0}
                                                           :to                 {:brightness 1 :glow 10 :yoyo true :duration 0.5 :repeat 5}}


                               :highlight-playback-button {:type               "transition"
                                                           :transition-id      "playback-group"
                                                           :return-immediately true
                                                           :from               {:brightness 0 :glow 0}
                                                           :to                 {:brightness 1 :glow 10 :yoyo true :duration 0.5 :repeat 5}}

                               :highlight-approve-button  {:type               "transition"
                                                           :transition-id      "approve-group"
                                                           :return-immediately true
                                                           :from               {:brightness 0 :glow 0}
                                                           :to                 {:brightness 1 :glow 10 :yoyo true :duration 0.5 :repeat 5}}

                               :mari-click                {:type       "action"
                                                           :unique-tag "instructions"
                                                           :from-var   [{:var-name        "tap-instructions-action"
                                                                         :action-property "id"}]}

                               :record-button-click       {:type     "test-var-scalar"
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
                                                                                     {:type "action" :id "show-playback-group"}
                                                                                     {:type "action" :id "remove-timeout-timer"}
                                                                                     {:type "action" :id "stop-recording-dialog"}]}]}
                                                           :fail     {:type "sequence-data"
                                                                      :data [{:type "set-variable" :var-name "record-button-state" :var-value "stop"}
                                                                             {:type "action" :id "start-recording-dialog"}
                                                                             {:type "start-audio-recording"}
                                                                             {:type "action" :id "timeout-timer"}
                                                                             {:type "action" :id "show-button-stop"}]}}

                               :show-playback-group       {:type "parallel"
                                                           :data [{:type "set-attribute" :target "approve-group" :attr-name "visible" :attr-value true}
                                                                  {:type "set-attribute" :target "playback-group" :attr-name "visible" :attr-value true}]}

                               :hide-playback-group       {:type "parallel"
                                                           :data [{:type "set-attribute" :target "approve-group" :attr-name "visible" :attr-value false}
                                                                  {:type "set-attribute" :target "playback-group" :attr-name "visible" :attr-value false}]}

                               :run-record-playing        {:type "parallel"
                                                           :data [{:type "set-attribute" :target "run-playback-button" :attr-name "visible" :attr-value false}
                                                                  {:type "set-attribute" :target "stop-playback-button" :attr-name "visible" :attr-value true}
                                                                  {:type     "audio"
                                                                   :tags     ["recorded-audio-flow"]
                                                                   :from-var [{:var-name        "recording-studio-audio"
                                                                               :action-property "id"}]}]}
                               :stop-record-playing       {:type "parallel"
                                                           :data [{:type "remove-flows" :flow-tag "recorded-audio-flow"}
                                                                  {:type "set-attribute" :target "run-playback-button" :attr-name "visible" :attr-value true}
                                                                  {:type "set-attribute" :target "stop-playback-button" :attr-name "visible" :attr-value false}]}
                               :run-playback-click        {:type "sequence-data"
                                                           :data [{:type "action" :id "remove-timeout-timer"}
                                                                  {:type "action" :id "start-playback-dialog"}
                                                                  {:type "action" :id "run-record-playing"}
                                                                  {:type "action" :id "stop-record-playing"}]}
                               :stop-playback-click       {:type "sequence-data"
                                                           :data [{:type "action" :id "timeout-timer"}
                                                                  {:type "action" :id "stop-playback-dialog"}
                                                                  {:type "action" :id "stop-record-playing"}]}

                               :set-demo-image-src        {:type "set-attribute" :target "concept-image" :attr-name "src" :attr-value ""}
                               :script                    {:type   "workflow"
                                                           :data   [{:type "start-activity"}
                                                                    {:type "parallel"
                                                                     :data [{:type "set-variable" :var-name "record-button-state" :var-value "record"}
                                                                            {:type "set-variable" :var-name "tap-instructions-action" :var-value "empty"}
                                                                            {:type "set-variable" :var-name "timeout-instructions-action" :var-value "empty"}]}
                                                                    {:type "action" :id "intro-dialog"}
                                                                    {:type "action" :id "reset-controls"}]
                                                           :on-end "finish"}
                               :reset-controls            {:type "sequence-data"
                                                           :data [{:type "action" :id "hide-playback-group"}]}
                               :approve-playback-click    {:type "action" :id "script"}
                               :finish                    {:type "sequence-data"
                                                           :data [{:type "action" :id "remove-timeout-timer"}
                                                                  {:type "action" :id "finish-dialog"}
                                                                  {:type "finish-activity"}]}
                               :stop-activity             {:type "sequence-data"
                                                           :data [{:type "action" :id "remove-timeout-timer"}
                                                                  {:type "stop-activity" :id "recording-studio"}]}
                               :remove-timeout-timer      {:type "remove-interval"
                                                           :id   "incorrect-answer-checker"}
                               :timeout-timer             {:type     "set-interval"
                                                           :id       "incorrect-answer-checker"
                                                           :interval 25000
                                                           :action   "timeout"}
                               :timeout                   {:type       "action"
                                                           :unique-tag "instructions"
                                                           :from-var   [{:var-name        "timeout-instructions-action"
                                                                         :action-property "id"}]}
                               :empty                     {:type "empty" :duration 100}
                               :intro-dialog              (-> (dialog/default "Intro")
                                                              (assoc :unique-tag "intro"))
                               :start-recording-dialog    (dialog/default "Start recording")
                               :stop-recording-dialog     (dialog/default "Stop recording")
                               :start-playback-dialog     (dialog/default "Start playback")
                               :stop-playback-dialog      (dialog/default "Stop playback")
                               :finish-dialog             (dialog/default "Finish")}

               :triggers      {:stop  {:on "back" :action "stop-activity"}
                               :start {:on "start" :action "script"}}
               :metadata      {:autostart         true
                               :resources         []
                               :tracks            [{:id    "main"
                                                    :title "Main Track"
                                                    :nodes [{:type      "dialog"
                                                             :action-id "intro-dialog"}
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
                                                             :action-id "stop-playback-dialog"}
                                                            {:type "prompt"
                                                             :text "Plays after check mark button click:"}
                                                            {:type      "dialog"
                                                             :action-id "finish-dialog"}]}]
                               :available-actions [{:action "highlight-record-button"
                                                    :name   "Highlight record"}
                                                   {:action "show-button-record"
                                                    :name   "Show record (record button goes red)"}
                                                   {:action "highlight-playback-button"
                                                    :name   "Highlight play"}
                                                   {:action "highlight-approve-button"
                                                    :name   "Highligh approve"}
                                                   {:action "show-button-stop"
                                                    :name   "Show stop (record button goes blue)"}]}})

(defn- round-action-name [round-id]
  (str "round-" round-id))

(defn- round-dialog-name [round-id]
  (str "round-dialog-" round-id))

(defn- round-tap-name [round-id]
  (str "round-tap-dialog-" round-id))

(defn- round-timeout-name [round-id]
  (str "round-timeout-dialog-" round-id))

(defn- create-round-actions
  [round-id image]
  (let [round-action (round-action-name round-id)
        round-dialog (round-dialog-name round-id)
        round-tap (round-tap-name round-id)
        round-timeout (round-timeout-name round-id)]
    {(keyword round-action)  {:type "sequence-data"
                              :data [{:type       "set-attribute"
                                      :target     "concept-image"
                                      :attr-name  "src"
                                      :attr-value image}
                                     {:type "set-variable" :var-name "tap-instructions-action" :var-value round-tap}
                                     {:type "set-variable" :var-name "timeout-instructions-action" :var-value round-timeout}
                                     {:type "action" :id round-dialog}
                                     {:type "action" :id "timeout-timer"}]}
     (keyword round-dialog)  (dialog/default (str "Round " round-id))
     (keyword round-tap)     (dialog/default (str "Round " round-id " tap instructions"))
     (keyword round-timeout) (dialog/default (str "Round " round-id " timeout instructions"))}))

(defn- add-round
  [activity-data image]
  (let [next-round (-> activity-data
                       (get-in [:metadata :next-round-id])
                       (or 0)
                       (inc))
        round-action (round-action-name next-round)
        round-dialog (round-dialog-name next-round)
        round-tap (round-tap-name next-round)
        round-timeout (round-timeout-name next-round)
        actions (create-round-actions next-round image)
        main-track (-> activity-data
                       (get-in [:metadata :tracks])
                       (first)
                       (update :nodes concat [{:type "track" :track-id round-action}]))
        round-track {:id    round-action
                     :title (str "Round " next-round)
                     :nodes [{:type      "dialog"
                              :action-id round-dialog}
                             {:type      "dialog"
                              :action-id round-tap}
                             {:type      "dialog"
                              :action-id round-timeout}]}
        tracks (as-> activity-data x
                     (get-in x [:metadata :tracks])
                     (drop 1 x)
                     (concat [main-track] x [round-track]))]
    (-> activity-data
        (update :actions merge actions)
        (update-in [:actions :script :data] concat [{:type "action" :id round-action :workflow-user-input true}])
        (assoc-in [:metadata :tracks] tracks)
        (update-in [:metadata :resources] conj image)
        (assoc-in [:metadata :next-round-id] next-round))))

(defn- add-demo
  [activity-data image]
  (-> activity-data
      (assoc-in [:actions :set-demo-image-src :attr-value] image)
      (assoc-in [:actions :demo-dialog] (dialog/default "Demo"))
      (update-in [:actions :script :data] concat [{:type "action" :id "set-demo-image-src"}
                                                  {:type       "set-attribute"
                                                   :target     "concept-image"
                                                   :attr-name  "visible"
                                                   :attr-value true}
                                                  {:type "action" :id "demo-dialog"}
                                                  {:type "action" :id "reset-controls"}])
      (update-in [:metadata :tracks 0 :nodes] concat [{:type "prompt"
                                                       :text "Plays after the instructions and before round 1:"}
                                                      {:type      "dialog"
                                                       :action-id "demo-dialog"}])
      (update-in [:metadata :resources] conj image)))

(defn create
  [args]
  (let [demo-image (get-in args [:demo-image :src])
        image (get-in args [:image :src])]
    (cond-> template
            demo-image (add-demo demo-image)
            image (add-round image)
            :always (assoc-in [:metadata :actions] (:actions m)))))

(defn update-template
  [old-data {:keys [action-name image]}]
  (case action-name
    "add-round" (add-round old-data (:src image))))

(core/register-template
  m
  create
  update-template)
