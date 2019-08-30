;; ToDo:
;; - set correct task sound

(ns webchange.demo-scenes.park.pinata)

(def pinata-scene
  {:assets        [{:url "/raw/img/park/pinata/background.jpg" :type "image"}
                   {:url "/raw/audio/l2/a11/L2_A11_Mari.m4a" :type "audio"}
                   {:url "/raw/audio/l1/a4/Mari_Level1_Activity4.m4a" :type "audio"} ; <<-- REMOVE when `current-concept-letter` is defined for concepts
                   ]
   :objects       {:background {:type "background"
                                :src  "/raw/img/park/pinata/background.jpg"}
                   :mari       {:type       "animation"
                                :scene-name "mari"
                                :name       "mari"
                                :transition "mari"
                                :anim       "idle"
                                :start      true
                                :loop       true
                                :speed      0.35
                                :x          1100
                                :y          420
                                :width      473
                                :height     511
                                :scale-y    0.5
                                :scale-x    0.5}
                   :pinata     {:type       "animation"
                                :scene-name "pinata"
                                :name       "pinata"
                                :transition "pinata"
                                :anim       "idle"
                                :start      true
                                :loop       true
                                :speed      0.35
                                :x          925
                                :y          555
                                :width      678
                                :height     899
                                :scale-x    1
                                :scale-y    1}
                   :letter1    {:type           "text"
                                :text           ""
                                :x              850
                                :y              760
                                :width          150
                                :height         150
                                :scale-x        1
                                :scale-y        1
                                :align          "center"
                                :vertical-align "bottom"
                                :font-family    "Lexend Deca"
                                :font-size      140
                                :fill           "white"}
                   :letter2    {:type           "text"
                                :text           ""
                                :x              1050
                                :y              760
                                :width          150
                                :height         150
                                :scale-x        1
                                :scale-y        1
                                :align          "center"
                                :vertical-align "bottom"
                                :font-family    "Lexend Deca"
                                :font-size      140
                                :fill           "white"}
                   :letter3    {:type           "text"
                                :text           ""
                                :x              1250
                                :y              760
                                :width          150
                                :height         150
                                :scale-x        1
                                :scale-y        1
                                :align          "center"
                                :vertical-align "bottom"
                                :font-family    "Lexend Deca"
                                :font-size      140
                                :fill           "white"}
                   :box1       {:type       "transparent"
                                :scene-name "box1"
                                :name       "boxes"
                                :skin       "qwestion"
                                :transition "box1"
                                :anim       "idle2"
                                :start      true
                                :loop       true
                                :speed      0.3
                                :x          205
                                :y          930
                                :width      671
                                :height     633
                                :scale-x    0.25
                                :scale-y    0.25
                                :draggable  {:var-name "drag-box-1"}
                                :actions    {:drag-end {:type   "action"
                                                        :id     "check-box-drop"
                                                        :params {:target "box1"}
                                                        :on     "drag-end"}}}
                   :box2       {:type       "transparent"
                                :scene-name "box2"
                                :name       "boxes"
                                :skin       "qwestion"
                                :transition "box2"
                                :anim       "idle2"
                                :start      true
                                :loop       true
                                :speed      0.3
                                :x          410
                                :y          985
                                :width      671
                                :height     633
                                :scale-x    0.25
                                :scale-y    0.25
                                :draggable  {:var-name "drag-box-2"}
                                :actions    {:drag-end {:type   "action"
                                                        :id     "check-box-drop"
                                                        :params {:target "box2"}
                                                        :on     "drag-end"}}}
                   :box3       {:type       "transparent"
                                :scene-name "box3"
                                :name       "boxes"
                                :skin       "qwestion"
                                :transition "box3"
                                :anim       "idle2"
                                :start      true
                                :loop       true
                                :speed      0.3
                                :x          610
                                :y          955
                                :width      671
                                :height     633
                                :scale-x    0.25
                                :scale-y    0.25
                                :draggable  {:var-name "drag-box-3"}
                                :actions    {:drag-end {:type   "action"
                                                        :id     "check-box-drop"
                                                        :params {:target "box3"}
                                                        :on     "drag-end"}}}
                   :box1-ph    {:type       "transparent"
                                :scene-name "box1-ph"
                                :transition "box1-ph"
                                :x          880
                                :y          930
                                :width      40
                                :height     40
                                :scale-x    1
                                :scale-y    1}
                   :box2-ph    {:type       "transparent"
                                :scene-name "box2-ph"
                                :transition "box2-ph"
                                :x          1110
                                :y          930
                                :width      40
                                :height     40
                                :scale-x    1
                                :scale-y    1}
                   :box3-ph    {:type       "transparent"
                                :scene-name "box3-ph"
                                :transition "box3-ph"
                                :x          1340
                                :y          930
                                :width      40
                                :height     40
                                :scale-x    1
                                :scale-y    1}}
   :scene-objects [["background"]
                   ["pinata" "mari"]
                   ["letter1" "letter2" "letter3"]
                   ["box1-ph" "box2-ph" "box3-ph"]
                   ["box1" "box2" "box3"]]
   :actions       {:start                     {:type "sequence"
                                               :data ["start-activity"
                                                      "clear-instruction"
                                                      "init-vars"
                                                      ;"mari-voice-welcome"
                                                      "next-round"]}

                   :stop                      {:type "sequence" :data ["stop-activity"]}

                   :start-activity            {:type "start-activity" :id "pinata"}

                   :stop-activity             {:type "stop-activity" :id "pinata"}

                   :clear-instruction         {:type "remove-flows" :flow-tag "instruction"}

                   :init-vars                 {:type "parallel"
                                               :data [{:type "set-variable" :var-name "current-slot-number" :var-value 0}]}

                   :next-task                 {:type "sequence-data"
                                               :data [{:type "counter" :counter-action "increase" :counter-id "current-slot-number"}
                                                      {:type "action" :id "set-next-current-slot"}
                                                      {:type "action" :id "mari-says-task"}]}

                   :set-next-current-slot     {:type "sequence-data"
                                               :data [
                                                      {:type     "test-var-scalar"
                                                       :var-name "current-slot-number"
                                                       :value    1
                                                       :success  {:type "set-variable" :var-name "current-slot" :from-var [{:action-property "var-value" :var-name "slot1"}]}
                                                       :fail     {:type     "test-var-scalar"
                                                                  :var-name "current-slot-number"
                                                                  :value    2
                                                                  :success  {:type "set-variable" :var-name "current-slot" :from-var [{:action-property "var-value" :var-name "slot2"}]}
                                                                  :fail     {:type     "test-var-scalar"
                                                                             :var-name "current-slot-number"
                                                                             :value    3
                                                                             :success  {:type "set-variable" :var-name "current-slot" :from-var [{:action-property "var-value" :var-name "slot3"}]}
                                                                             :fail     "next-round"}}}]}

                   :next-round                {:type "sequence-data"
                                               :data [{:type "set-variable" :var-name "current-slot-number" :var-value 0}
                                                      {:type "action" :id "renew-current-concepts"}
                                                      {:type "action" :id "get-new-boxes"}
                                                      {:type "action" :id "set-concepts-data"}
                                                      {:type "action" :id "next-task"}]}

                   ;; ---

                   :renew-current-concepts    {:type "sequence-data"
                                               :data [{:type        "lesson-var-provider"
                                                       :provider-id "words-set"
                                                       :variables   ["concept-1" "concept-2" "concept-3"]
                                                       :shuffled    false
                                                       :from        "concepts"
                                                       :on-end      "finish-activity"}
                                                      {:type      "vars-var-provider"
                                                       :variables ["box1" "box2" "box3"]
                                                       :from      ["concept-1" "concept-2" "concept-3"]
                                                       :shuffled  true}
                                                      {:type      "vars-var-provider"
                                                       :variables ["letter1" "letter2" "letter3"]
                                                       :from      ["concept-1" "concept-2" "concept-3"]
                                                       :shuffled  true}
                                                      {:type      "vars-var-provider"
                                                       :variables ["slot1" "slot2" "slot3"]
                                                       :from      ["concept-1" "concept-2" "concept-3"]
                                                       :shuffled  true}]}

                   :set-concepts-data         {:type "parallel"
                                               :data [{:type     "set-skin" :target "box1"
                                                       :from-var [{:var-name "box1" :action-property "skin" :var-property "skin"}]}
                                                      {:type     "set-skin" :target "box2"
                                                       :from-var [{:var-name "box2" :action-property "skin" :var-property "skin"}]}
                                                      {:type     "set-skin" :target "box3"
                                                       :from-var [{:var-name "box3" :action-property "skin" :var-property "skin"}]}
                                                      {:type     "set-attribute" :target "letter1" :attr-name "text"
                                                       :from-var [{:var-name "letter1" :action-property "attr-value" :var-property "letter"}]}
                                                      {:type     "set-attribute" :target "letter2" :attr-name "text"
                                                       :from-var [{:var-name "letter2" :action-property "attr-value" :var-property "letter"}]}
                                                      {:type     "set-attribute" :target "letter3" :attr-name "text"
                                                       :from-var [{:var-name "letter3" :action-property "attr-value" :var-property "letter"}]}]}

                   :finish-activity           {:type "sequence-data"
                                               :data [{:type "action" :id "mari-hits-pinata-toward"}
                                                      {:type "parallel"
                                                       :data [{:type "action" :id "mari-hits-pinata-backward"}
                                                              {:type "action" :id "pinata-fall-down"}
                                                              {:type "empty" :duration 2000}
                                                              {:type "action" :id "stop"}]}]}

                   ;; ---

                   :mari-says-task            {:type "sequence-data"
                                               :data [{:type     "action"
                                                       :from-var [{:var-name     "current-slot"
                                                                   :var-property "sandbox-this-is-letter-action"}]}  ; <<-- REMOVE when `current-concept-letter` is defined for concepts
                                                      ;{:type "action" :id "mari-voice-act-1"}
                                                      ;{:type "action" :id "current-concept-letter"}
                                                      ;{:type "empty" :duration 700}
                                                      ;{:type "action" :id "mari-voice-act-2"}
                                                      ;{:type "action" :id "current-concept-letter"}
                                                      ;{:type "action" :id "mari-voice-act-3"}
                                                      ]}

                   :current-concept-letter    {:type        "parallel"
                                               :description "a"
                                               :data        [{:type "audio" :id "/raw/audio/l2/a11/L2_A11_Mari.m4a" :start 20.516 :duration 0.592}
                                                             {:type "animation-sequence" :target "mari" :track 1 :offset 20.516
                                                              :data [{:start 20.516 :end 21.108 :anim "talk"}]}]}

                   ;; ---

                   :check-box-drop            {:type "sequence-data"
                                               :data [{:type        "set-variable" :var-name "current-box"
                                                       :from-params [{:action-property "var-value" :param-property "target"}]}
                                                      {:type         "test-transitions-collide"
                                                       :from-params  [{:action-property "transition-1" :param-property "target"}]
                                                       :transition-2 "box1-ph"
                                                       :success      "check-place-1"
                                                       :fail         "empty"}
                                                      {:type         "test-transitions-collide"
                                                       :from-params  [{:action-property "transition-1" :param-property "target"}]
                                                       :transition-2 "box2-ph"
                                                       :success      "check-place-2"
                                                       :fail         "empty"}
                                                      {:type         "test-transitions-collide"
                                                       :from-params  [{:action-property "transition-1" :param-property "target"}]
                                                       :transition-2 "box3-ph"
                                                       :success      "check-place-3"
                                                       :fail         "empty"}]}

                   :check-place-1             {:type     "test-var-scalar"
                                               :from-var [{:action-property "var-name" :var-name "current-box"}
                                                          {:action-property "value" :var-name "letter1"}]
                                               :success  "check-current-slot"
                                               :fail     "empty"}

                   :check-place-2             {:type     "test-var-scalar"
                                               :from-var [{:action-property "var-name" :var-name "current-box"}
                                                          {:action-property "value" :var-name "letter2"}]
                                               :success  "check-current-slot"
                                               :fail     "empty"}

                   :check-place-3             {:type     "test-var-scalar"
                                               :from-var [{:action-property "var-name" :var-name "current-box"}
                                                          {:action-property "value" :var-name "letter3"}]
                                               :success  "check-current-slot"
                                               :fail     "empty"}

                   :check-current-slot        {:type     "test-var-scalar"
                                               :from-var [{:action-property "var-name" :var-name "current-box"}
                                                          {:action-property "value" :var-name "current-slot"}]
                                               :success  "next-task"
                                               :fail     "empty"}

                   :empty                     {:type "empty" :duration 100}

                   ;; ---

                   :get-new-boxes             {:type "sequence-data"
                                               :data [{:type "action" :id "mari-hits-pinata-toward"}
                                                      {:type "parallel"
                                                       :data [{:type "action" :id "mari-hits-pinata-backward"}
                                                              {:type "action" :id "pinata-hit"}
                                                              {:type "action" :id "boxes-jump-out"}]}]}

                   :pinata-hit                {:type "sequence-data"
                                               :data [{:type "animation" :target "pinata" :id "hit"}
                                                      {:type "add-animation" :target "pinata" :id "idle" :loop true}]}

                   :pinata-fall-down          {:type "sequence-data"
                                               :data [{:type "animation" :target "pinata" :id "empty"}
                                                      {:type "add-animation" :target "pinata" :id "empty_idle" :loop true}]}


                   :mari-hits-pinata-toward   {:type "sequence-data"
                                               :data [{:type "transition" :transition-id "mari"
                                                       :to   {:bezier [{:x 691 :y 636} {:x 513 :y 570}] :duration 1.3}}]}

                   :mari-hits-pinata-backward {:type "sequence-data"
                                               :data [{:type "transition" :transition-id "mari"
                                                       :to   {:bezier [{:x 691 :y 500} {:x 1100 :y 420}] :duration 2 :ease [1 1]}}]}

                   :boxes-jump-out            {:type "sequence-data"
                                               :data [{:type "action" :id "boxes-reset-position"}
                                                      {:type "action" :id "boxes-show-up"}
                                                      {:type "action" :id "boxes-move-to-position"}]}

                   :boxes-reset-position      {:type "parallel"
                                               :data [{:type "set-attribute" :target "box1" :attr-name "x" :attr-value 420}
                                                      {:type "set-attribute" :target "box1" :attr-name "y" :attr-value 595}
                                                      {:type "set-attribute" :target "box2" :attr-name "x" :attr-value 420}
                                                      {:type "set-attribute" :target "box2" :attr-name "y" :attr-value 595}
                                                      {:type "set-attribute" :target "box3" :attr-name "x" :attr-value 420}
                                                      {:type "set-attribute" :target "box3" :attr-name "y" :attr-value 595}]}

                   :boxes-show-up             {:type "parallel"
                                               :data [{:type "sequence-data"
                                                       :data [{:type "set-attribute" :target "box1" :attr-name "type" :attr-value "animation"}
                                                              {:type "empty" :duration 100}
                                                              {:type "animation" :target "box1" :id "come2"}
                                                              {:type "add-animation" :target "box1" :id "idle2" :loop true}]}
                                                      {:type "sequence-data"
                                                       :data [{:type "set-attribute" :target "box2" :attr-name "type" :attr-value "animation"}
                                                              {:type "empty" :duration 100}
                                                              {:type "animation" :target "box2" :id "come2"}
                                                              {:type "add-animation" :target "box2" :id "idle2" :loop true}]}
                                                      {:type "sequence-data"
                                                       :data [{:type "set-attribute" :target "box3" :attr-name "type" :attr-value "animation"}
                                                              {:type "empty" :duration 100}
                                                              {:type "animation" :target "box3" :id "come2"}
                                                              {:type "add-animation" :target "box3" :id "idle2" :loop true}]}]}

                   :boxes-move-to-position    {:type "parallel"
                                               :data [{:type "transition" :transition-id "box1"
                                                       :to   {:bezier [{:x 256 :y 640} {:x 205 :y 930}] :duration 0.7}}
                                                      {:type "transition" :transition-id "box2"
                                                       :to   {:bezier [{:x 395 :y 758} {:x 410 :y 985}] :duration 0.7}}
                                                      {:type "transition" :transition-id "box3"
                                                       :to   {:bezier [{:x 570 :y 680} {:x 610 :y 955}] :duration 0.7}}]}

                   :boxes-disappear           {:type "parallel"
                                               :data [{:type "set-skin" :target "box1" :skin "qwestion"}
                                                      {:type "set-skin" :target "box2" :skin "qwestion"}
                                                      {:type "set-skin" :target "box3" :skin "qwestion"}
                                                      {:type "set-attribute" :target "box1" :attr-name "type" :attr-value "transparent"}
                                                      {:type "set-attribute" :target "box2" :attr-name "type" :attr-value "transparent"}
                                                      {:type "set-attribute" :target "box3" :attr-name "type" :attr-value "transparent"}]}

                   ;; ---

                   :mari-voice-welcome        {:type        "parallel"
                                               :description "Wiii, vamos a tener una fiesta con las letras!
                                                            Cuando le pegue a la pinata, las fotos caeran.
                                                            Empareja las letras sobre el piso a la foto correcta."
                                               :data        [{:type "audio" :id "/raw/audio/l2/a11/L2_A11_Mari.m4a" :start 0.677 :duration 15.949}
                                                             {:type "animation-sequence" :target "mari" :track 1 :offset 0.677
                                                              :data [{:start 1.329 :end 6.210 :anim "talk"}
                                                                     {:start 6.790 :end 10.633 :anim "talk"}
                                                                     {:start 11.623 :end 14.402 :anim "talk"}
                                                                     {:start 14.620 :end 16.287 :anim "talk"}]}]}

                   :mari-voice-act-1          {:type        "parallel"
                                               :description "Esta es la letra"
                                               :data        [{:type "audio" :id "/raw/audio/l2/a11/L2_A11_Mari.m4a" :start 18.776 :duration 1.716}
                                                             {:type "animation-sequence" :target "mari" :track 1 :offset 18.776
                                                              :data [{:start 18.776 :end 20.492 :anim "talk"}]}]}

                   :mari-voice-act-2          {:type        "parallel"
                                               :description "Coloca la foto que empieza con la letra"
                                               :data        [{:type "audio" :id "/raw/audio/l2/a11/L2_A11_Mari.m4a" :start 21.737 :duration 3.009}
                                                             {:type "animation-sequence" :target "mari" :track 1 :offset 21.737
                                                              :data [{:start 21.737 :end 24.745 :anim "talk"}]}]}

                   :mari-voice-act-3          {:type        "parallel"
                                               :description "sobre el piso"
                                               :data        [{:type "audio" :id "/raw/audio/l2/a11/L2_A11_Mari.m4a" :start 25.313 :duration 1.100}
                                                             {:type "animation-sequence" :target "mari" :track 1 :offset 25.313
                                                              :data [{:start 25.313 :end 26.509 :anim "talk"}]}]}}

   :triggers      {:start {:on "start" :action "start"}
                   :stop  {:on "back" :action "stop"}}
   :metadata      {:autostart true
                   :prev      "park"}})
