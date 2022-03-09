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
  (let [take-half #(-> % (/ 2) (Math/ceil))

        canvas {:width  1920
                :height 1080}

        screen-margin-x 208
        screen-margin-y 48
        screen {:x         screen-margin-x
                :y         screen-margin-y
                :width     (- (:width canvas) (* 2 screen-margin-x))
                :height    768
                :padding-x 200
                :padding-y 100}

        timer-size 600
        timer {:x    (+ (:x screen)
                        (take-half (:width screen))
                        (- (take-half timer-size)))
               :y    (+ (:y screen)
                        (take-half (:height screen))
                        (- (take-half timer-size)))
               :size timer-size}

        right-margin {:x     (+ (:x screen) (:width screen))
                      :width screen-margin-x}
        bottom-margin {:y      (+ (:y screen) (:height screen))
                       :height (- (:height canvas)
                                  (:height screen)
                                  (:y screen))}

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
                        :height approve-button-size}

        button-size 168
        button-border 16
        button-half-size (take-half button-size)
        button-small-size (-> button-size (* 0.8) (Math/ceil))
        button-half-small-size (take-half button-small-size)

        record-buttons-center {:x (-> (+ (:x screen)
                                         (take-half (:width screen))))
                               :y (+ (:y bottom-margin)
                                     (-> (:height bottom-margin) (take-half)))}

        play-back-buttons-center {:x (- (:x record-buttons-center)
                                        button-size
                                        64)
                                  :y (:y record-buttons-center)}

        sound-bar-height 115
        sound-bar {:x      screen-margin-x
                   :y      (+ (:y bottom-margin)
                              (take-half (- (:height bottom-margin)
                                            sound-bar-height)))
                   :width  (- (:x play-back-buttons-center)
                              button-half-size
                              80
                              screen-margin-x)
                   :height sound-bar-height}]

    {:approve-button      approve-button
     :concept-image       concept-image
     :start-play-button   {:x            (-> (:x play-back-buttons-center) (- button-half-size))
                           :y            (-> (:y play-back-buttons-center) (- button-half-size))
                           :width        button-size
                           :height       button-size
                           :border-width button-border}
     :stop-play-button    {:x            (-> (:x play-back-buttons-center) (- button-half-small-size))
                           :y            (-> (:y play-back-buttons-center) (- button-half-small-size))
                           :width        button-small-size
                           :height       button-small-size
                           :border-width button-border}
     :start-record-button {:x            (-> (:x record-buttons-center) (- button-half-size))
                           :y            (-> (:y record-buttons-center) (- button-half-size))
                           :width        button-size
                           :height       button-size
                           :border-width button-border}
     :stop-record-button  {:x            (-> (:x record-buttons-center) (- button-half-small-size))
                           :y            (-> (:y record-buttons-center) (- button-half-small-size))
                           :width        button-small-size
                           :height       button-small-size
                           :border-width button-border}
     :screen              screen
     :sound-bar           sound-bar
     :timer               timer}))

(def layout-params (get-layout-params))

(defn- with-highlight-filters
  [object-data]
  (assoc object-data :filters [{:name "brightness" :value 0}
                               {:name "glow" :outer-strength 0 :color 0xffd700}]))

(defn- add-start-play-button
  [objects-data name {:keys [on-click]}]
  (let [{:keys [x y width border-width]} (:start-play-button layout-params)

        bg-1-size width
        bg-2-size (- width (* 2 border-width))
        icon-size 68

        background-1-name (-> name (str "-background-1"))
        background-2-name (-> name (str "-background-2"))
        icon-name (-> name (str "-icon"))]
    (merge objects-data
           {(keyword name)              (-> {:type       "group"
                                             :children   [background-1-name background-2-name icon-name]
                                             :transition name
                                             :x          x
                                             :y          y
                                             :visible    false
                                             :actions    {:click {:id on-click :on "click" :type "action"}}}
                                            (with-highlight-filters))
            (keyword background-1-name) {:type          "rectangle"
                                         :x             0
                                         :y             0
                                         :width         bg-1-size
                                         :height        bg-1-size
                                         :border-radius (/ bg-1-size 2)
                                         :fill          0xFFFFFF}
            (keyword background-2-name) {:type          "rectangle"
                                         :x             border-width
                                         :y             border-width
                                         :width         bg-2-size
                                         :height        bg-2-size
                                         :border-radius (/ bg-2-size 2)
                                         :fill          0x10BC2B}
            (keyword icon-name)         {:type   "svg-path"
                                         :x      (-> (- bg-1-size icon-size) (/ 2) (+ 15))
                                         :y      (/ (- bg-1-size icon-size) 2)
                                         :width  icon-size
                                         :height icon-size
                                         :fill   "#FFFFFF"
                                         :data   "M50.95 27.57L6.05798 0.63479C3.39189 -0.964869 0 0.955585 0 4.06476V57.9352C0 61.0444 3.39189 62.9649 6.05798 61.3652L50.95 34.43C53.5394 32.8764 53.5394 29.1236 50.95 27.57Z"}})))

(defn- add-stop-play-button
  [objects-data name {:keys [on-click]}]
  (let [{:keys [x y width border-width]} (:stop-play-button layout-params)
        bg-1-size width
        bg-2-size (- width (* 2 border-width))

        background-1-name (-> name (str "-background-1"))
        background-2-name (-> name (str "-background-2"))]
    (merge objects-data
           {(keyword name)              {:type       "group"
                                         :children   [background-1-name background-2-name]
                                         :transition name
                                         :x          x
                                         :y          y
                                         :visible    false
                                         :actions    {:click {:id on-click :on "click" :type "action"}}}
            (keyword background-1-name) {:type          "rectangle"
                                         :x             0
                                         :y             0
                                         :width         bg-1-size
                                         :height        bg-1-size
                                         :border-radius (* border-width 1.5)
                                         :fill          0xFFFFFF}
            (keyword background-2-name) {:type          "rectangle"
                                         :x             (/ (- bg-1-size bg-2-size) 2)
                                         :y             (/ (- bg-1-size bg-2-size) 2)
                                         :width         bg-2-size
                                         :height        bg-2-size
                                         :border-radius border-width
                                         :fill          0x10BC2B}})))

(defn- add-start-record-button
  [objects-data name {:keys [on-click]}]
  (let [{:keys [x y width border-width]} (:start-record-button layout-params)
        bg-1-size width
        bg-2-size (- width (* 2 border-width))

        background-1-name (-> name (str "-background-1"))
        background-2-name (-> name (str "-background-2"))]
    (merge objects-data
           {(keyword name)              (-> {:type       "group"
                                             :children   [background-1-name background-2-name]
                                             :transition name
                                             :x          x
                                             :y          y
                                             :visible    false
                                             :actions    {:click {:id on-click :on "click" :type "action"}}}
                                            (with-highlight-filters))
            (keyword background-1-name) {:type          "rectangle"
                                         :x             0
                                         :y             0
                                         :width         bg-1-size
                                         :height        bg-1-size
                                         :border-radius (/ bg-1-size 2)
                                         :fill          0xFFFFFF}
            (keyword background-2-name) {:type          "rectangle"
                                         :x             border-width
                                         :y             border-width
                                         :width         bg-2-size
                                         :height        bg-2-size
                                         :border-radius (/ bg-2-size 2)
                                         :fill          0xED1C24}})))

(defn- add-stop-record-button
  [objects-data name {:keys [on-click]}]
  (let [{:keys [x y width border-width]} (:stop-record-button layout-params)
        bg-1-size width
        bg-2-size (- width (* 2 border-width))

        background-1-name (-> name (str "-background-1"))
        background-2-name (-> name (str "-background-2"))]
    (merge objects-data
           {(keyword name)              {:type       "group"
                                         :children   [background-1-name background-2-name]
                                         :transition name
                                         :x          x
                                         :y          y
                                         :visible    false
                                         :actions    {:click {:id on-click :on "click" :type "action"}}}
            (keyword background-1-name) {:type          "rectangle"
                                         :x             0
                                         :y             0
                                         :width         bg-1-size
                                         :height        bg-1-size
                                         :border-radius (* border-width 1.5)
                                         :fill          0xFFFFFF}
            (keyword background-2-name) {:type          "rectangle"
                                         :x             (/ (- bg-1-size bg-2-size) 2)
                                         :y             (/ (- bg-1-size bg-2-size) 2)
                                         :width         bg-2-size
                                         :height        bg-2-size
                                         :border-radius border-width
                                         :fill          0xED1C24}})))

(defn- add-approve-button
  [objects-data name {:keys [on-click]}]
  (merge objects-data
         {(keyword name)           (merge {:type     "group"
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
          :approve-playback-button {:type    "svg-path"
                                    :x       20
                                    :y       25
                                    :width   128
                                    :height  128
                                    :fill    "#FFFFFF",
                                    :actions {:click {:id on-click :on "click" :type "action" :unique-tag "intro"}}
                                    :data    "M 9.29193 13.1343L0 22.3134L22.6633 45L59 9.47761L49.1793 0L22.6633 26.194L9.29193 13.1343"}}))

(defn- highlight-action
  [transition-id]
  {:type               "transition"
   :transition-id      transition-id
   :return-immediately true
   :from               {:brightness 0 :glow 0}
   :to                 {:brightness 1 :glow 10 :yoyo true :duration 0.5 :repeat 5}})

(def template {:assets        []
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
                                   :concept-image (merge {:type       "image"
                                                          :image-size "contain"
                                                          :src        ""
                                                          :visible    false}
                                                         (select-keys (:concept-image layout-params)
                                                                      [:x :y :width :height]))
                                   :sound-bar     (merge {:type       "sound-bar"
                                                          :transition "sound-bar"
                                                          :visible    false}
                                                         (select-keys (:sound-bar layout-params)
                                                                      [:x :y :width :height]))
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
                                                          :visible           false
                                                          :actions           {:end {:on "end" :type "action" :id "recording-countdown-ended"}}}
                                                         (select-keys (:timer layout-params)
                                                                      [:x :y :size]))
                                   :timer-screen  (merge {:type          "rectangle"
                                                          :transition    "timer-screen"
                                                          :border-radius 32
                                                          :fill          0xCCCCCC
                                                          :opacity       0.4
                                                          :visible       false}
                                                         (select-keys (:screen layout-params)
                                                                      [:x :y :width :height]))}
                                  (add-approve-button "approve-group" {:on-click "approve-button-click-handler"})
                                  (add-start-play-button "start-play-button" {:on-click "start-play-button-click-handler"})
                                  (add-stop-play-button "stop-play-button" {:on-click "stop-play-button-click-handler"})
                                  (add-start-record-button "start-record-button" {:on-click "start-record-button-click-handler"})
                                  (add-stop-record-button "stop-record-button" {:on-click "stop-record-button-click-handler"}))
               :scene-objects [["background" "screen" "sound-bar"]
                               ["concept-image" "timer-screen" "timer"]
                               ["approve-group" "start-play-button" "stop-play-button" "start-record-button" "stop-record-button"]]
               :actions       {:script                            {:type   "workflow"
                                                                   :data   [{:type "start-activity"}
                                                                            {:type "parallel"
                                                                             :data [{:type "set-variable" :var-name "tap-instructions-action" :var-value "empty"}
                                                                                    {:type "set-variable" :var-name "timeout-instructions-action" :var-value "empty"}]}
                                                                            {:type "action" :id "intro-dialog"}
                                                                            {:type "action" :id "reset-controls"}]
                                                                   :on-end "finish"}

                               :finish                            {:type "sequence-data"
                                                                   :data [{:type "action" :id "remove-timeout-timer"}
                                                                          {:type "action" :id "finish-dialog"}
                                                                          {:type "finish-activity"}]}

                               :set-demo-image-src                {:type "set-attribute" :target "concept-image" :attr-name "src" :attr-value ""}

                               :approve-button-click-handler      {:type "action" :id "script"}

                               :stop-activity                     {:type "sequence-data"
                                                                   :data [{:type "action" :id "remove-timeout-timer"}
                                                                          {:type "stop-activity" :id "recording-studio"}]}

                               ;; UI actions

                               :activate-sound-bar                {:type "sequence-data"
                                                                   :data [{:type "set-attribute" :target "sound-bar" :attr-name "visible" :attr-value true}
                                                                          {:type "component-action" :target "sound-bar" :action "activate"}]}
                               :deactivate-sound-bar              {:type "sequence-data"
                                                                   :data [{:type "set-attribute" :target "sound-bar" :attr-name "visible" :attr-value false}
                                                                          {:type "component-action" :target "sound-bar" :action "deactivate"}]}
                               :show-start-play-button            {:type "set-attribute" :target "start-play-button" :attr-name "visible" :attr-value true}
                               :hide-start-play-button            {:type "set-attribute" :target "start-play-button" :attr-name "visible" :attr-value false}
                               :show-stop-play-button             {:type "set-attribute" :target "stop-play-button" :attr-name "visible" :attr-value true}
                               :hide-stop-play-button             {:type "set-attribute" :target "stop-play-button" :attr-name "visible" :attr-value false}
                               :show-start-record-button          {:type "set-attribute" :target "start-record-button" :attr-name "visible" :attr-value true}
                               :hide-start-record-button          {:type "set-attribute" :target "start-record-button" :attr-name "visible" :attr-value false}
                               :show-stop-record-button           {:type "set-attribute" :target "stop-record-button" :attr-name "visible" :attr-value true}
                               :hide-stop-record-button           {:type "set-attribute" :target "stop-record-button" :attr-name "visible" :attr-value false}
                               :show-approve-button               {:type "set-attribute" :target "approve-group" :attr-name "visible" :attr-value true}
                               :hide-approve-button               {:type "set-attribute" :target "approve-group" :attr-name "visible" :attr-value false}

                               :highlight-record-button           (highlight-action "start-record-button")
                               :highlight-playback-button         (highlight-action "start-play-button")
                               :highlight-approve-button          (highlight-action "approve-group")

                               :reset-controls                    {:type "sequence-data"
                                                                   :data [{:id "hide-approve-button" :type "action"}
                                                                          {:id "hide-start-play-button" :type "action"}]}

                               ;; Click handlers

                               :start-play-button-click-handler   {:type "sequence-data"
                                                                   :data [{:id "activate-sound-bar" :type "action"}
                                                                          {:id "hide-start-play-button" :type "action"}
                                                                          {:id "hide-start-record-button" :type "action"}
                                                                          {:id "hide-approve-button" :type "action"}
                                                                          {:id "show-stop-play-button" :type "action"}

                                                                          {:id "start-playing" :type "action"}]}

                               :stop-play-button-click-handler    {:type "sequence-data"
                                                                   :data [{:id "deactivate-sound-bar" :type "action"}
                                                                          {:id "hide-stop-play-button" :type "action"}
                                                                          {:id "show-start-play-button" :type "action"}
                                                                          {:id "show-start-record-button" :type "action"}
                                                                          {:id "show-approve-button" :type "action"}

                                                                          {:id "stop-playing" :type "action"}]}

                               :start-record-button-click-handler {:type "sequence-data"
                                                                   :data [{:id "hide-start-play-button" :type "action"}
                                                                          {:id "hide-start-record-button" :type "action"}
                                                                          {:id "hide-approve-button" :type "action"}

                                                                          {:id "start-recording-countdown" :type "action"}]}

                               :stop-record-button-click-handler  {:type "sequence-data"
                                                                   :data [{:id "deactivate-sound-bar" :type "action"}
                                                                          {:id "hide-stop-record-button" :type "action"}
                                                                          {:id "show-start-record-button" :type "action"}
                                                                          {:id "show-start-play-button" :type "action"}
                                                                          {:id "show-approve-button" :type "action"}

                                                                          {:id "stop-recording" :type "action"}]}

                               ;; Recording Timer

                               :start-recording-countdown         {:type "sequence-data"
                                                                   :data [{:type "set-attribute" :target "timer" :attr-name "visible" :attr-value true}
                                                                          {:type "set-attribute" :target "timer-screen" :attr-name "visible" :attr-value true}
                                                                          {:type "timer-start" :target "timer"}]}

                               :reset-timer                       {:type "sequence-data"
                                                                   :data [{:type "set-attribute" :target "timer" :attr-name "visible" :attr-value false}
                                                                          {:type "set-attribute" :target "timer-screen" :attr-name "visible" :attr-value false}
                                                                          {:type "timer-reset" :target "timer"}]}

                               :recording-countdown-ended         {:type "sequence-data"
                                                                   :data [{:type "action" :id "reset-timer"}
                                                                          {:type "action" :id "activate-sound-bar"}
                                                                          {:type "action" :id "show-stop-record-button"}
                                                                          {:type "action" :id "start-recording"}]}

                               ;; Record audio actions

                               :start-playing                     {:type "sequence-data"
                                                                   :data [{:type "action" :id "remove-timeout-timer"}
                                                                          {:type "action" :id "start-playback-dialog"}
                                                                          {:type     "audio"
                                                                           :tags     ["recorded-audio-flow"]
                                                                           :from-var [{:var-name        "recording-studio-audio"
                                                                                       :action-property "id"}]}
                                                                          {:type "remove-flows" :flow-tag "recorded-audio-flow"}]}

                               :stop-playing                      {:type "sequence-data"
                                                                   :data [{:type "action" :id "timeout-timer"}
                                                                          {:type "action" :id "stop-playback-dialog"}
                                                                          {:type "remove-flows" :flow-tag "recorded-audio-flow"}]}

                               :start-recording                   {:type "sequence-data"
                                                                   :data [{:type "action" :id "start-recording-dialog"}
                                                                          {:type "action" :id "timeout-timer"}
                                                                          {:type "start-audio-recording"}]}

                               :stop-recording                    {:type "sequence-data"
                                                                   :data [{:type "stop-audio-recording" :var-name "recording-studio-audio"}
                                                                          {:type     "set-progress"
                                                                           :var-name "recording-studio"
                                                                           :from-var [{:var-name        "recording-studio-audio"
                                                                                       :action-property "var-value"}]}
                                                                          {:type "action" :id "remove-timeout-timer"}
                                                                          {:type "action" :id "stop-recording-dialog"}]}

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

                               ;; Dialogs

                               :intro-dialog                      (-> (dialog/default "Intro")
                                                                      (assoc :unique-tag "intro"))
                               :start-recording-dialog            (dialog/default "Start recording")
                               :stop-recording-dialog             (dialog/default "Stop recording")
                               :start-playback-dialog             (dialog/default "Start playback")
                               :stop-playback-dialog              (dialog/default "Stop playback")
                               :finish-dialog                     (dialog/default "Finish")

                               ;; Available actions

                               :show-button-record                {:type "parallel"
                                                                   :data [{:type "action" :id "hide-stop-record-button"}
                                                                          {:type "action" :id "show-start-record-button"}]}

                               :show-button-stop                  {:type "parallel"
                                                                   :data [{:type "action" :id "hide-start-record-button"}
                                                                          {:type "action" :id "show-stop-record-button"}]}}

               :triggers      {:stop  {:on "back" :action "stop-activity"}
                               :start {:on "start" :action "script"}}
               :metadata      {:autostart         true
                               :resources         []
                               :guide-settings    {:show-guide true
                                                   :character  "mari"}
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
                                                    :name   "Show record"}
                                                   {:action "highlight-playback-button"
                                                    :name   "Highlight play"}
                                                   {:action "highlight-approve-button"
                                                    :name   "Highlight approve"}
                                                   {:action "show-button-stop"
                                                    :name   "Show stop"}]}})

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
