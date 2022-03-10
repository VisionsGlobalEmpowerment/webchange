(ns webchange.templates.library.recording-studio.template
  (:require
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
                                      :optional?   false
                                      :label       "Demo Image"
                                      :description "What demo image you want to show on the screen?"}
                         :image      {:type        "image"
                                      :optional?   true
                                      :label       "Prompt Image"
                                      :description "What visual prompt do you want to show on the screen?"}}
        :options-groups [{:title   "Add Content"
                          :options ["demo-image" "image"]}]
        :actions        {:add-round {:title    "Add Round"
                                     :track-id "main"}}})

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
                                   ["concept-image" "timer-group"]
                                   ["approve-button" "start-play-button" "stop-play-button" "start-record-button" "stop-record-button"]]
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

                                   :reset-controls                    {:type "sequence-data"
                                                                       :data [{:type "set-attribute" :target "concept-image" :attr-name "src" :attr-value ""}
                                                                              {:id "hide-approve-button" :type "action"}
                                                                              {:id "hide-start-play-button" :type "action"}
                                                                              {:id "hide-start-record-button" :type "action"}]}

                                   ;; Click handlers

                                   :start-play-button-click-handler   {:type "sequence-data"
                                                                       :data [{:id "plug-in-sound-bar" :type "action"}
                                                                              {:id "hide-start-play-button" :type "action"}
                                                                              {:id "hide-start-record-button" :type "action"}
                                                                              {:id "hide-approve-button" :type "action"}
                                                                              {:id "show-stop-play-button" :type "action"}

                                                                              {:id "start-playing" :type "action"}]}

                                   :stop-play-button-click-handler    {:type "sequence-data"
                                                                       :data [{:id "plug-out-sound-bar" :type "action"}
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
                                   :available-actions [{:action "activate-sound-bar"
                                                        :name   "Turn on sound bar"}
                                                       {:action "deactivate-sound-bar"
                                                        :name   "Turn off sound bar"}]}}
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
                              :data (cond-> [{:type "action" :id "reset-controls"}]
                                            (some? image) (conj {:type       "set-attribute"
                                                                 :target     "concept-image"
                                                                 :attr-name  "src"
                                                                 :attr-value image})
                                            :always (concat [{:type "set-variable" :var-name "tap-instructions-action" :var-value round-tap}
                                                             {:type "set-variable" :var-name "timeout-instructions-action" :var-value round-timeout}
                                                             {:type "action" :id round-dialog}
                                                             {:type "action" :id "timeout-timer"}]))}
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
    (cond-> (-> activity-data
                (update :actions merge actions)
                (update-in [:actions :script :data] concat [{:type "action" :id round-action :workflow-user-input true}])
                (assoc-in [:metadata :tracks] tracks)
                (assoc-in [:metadata :next-round-id] next-round))
            (some? image) (update-in [:metadata :resources] conj image))))

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
                                                  {:type "action" :id "demo-dialog"}])
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
