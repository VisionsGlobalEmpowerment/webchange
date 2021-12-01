(ns webchange.templates.library.sandbox-digging
  (:require
    [webchange.templates.core :as core]
    [webchange.templates.utils.dialog :as dialog]))

(def m {:id          47
        :name        "Sandbox digging"
        :tags        ["Sandbox"]
        :description "Some description of sandbox mechanics and covered skills"
        :options     {:image1-1 {:label   "Round 1 image 1"
                                 :type    "image"
                                 :options {:max-width  150
                                           :max-height 150
                                           :min-height 50
                                           :min-width  50}}
                      :image1-2 {:label   "Round 1 image 2"
                                 :type    "image"
                                 :options {:max-width  150
                                           :max-height 150
                                           :min-height 50
                                           :min-width  50}}
                      :image1-3 {:label   "Round 1 image 3"
                                 :type    "image"
                                 :options {:max-width  150
                                           :max-height 150
                                           :min-height 50
                                           :min-width  50}}
                      :image2-1 {:label   "Round 2 image 1"
                                 :type    "image"
                                 :options {:max-width  150
                                           :max-height 150
                                           :min-height 50
                                           :min-width  50}}
                      :image2-2 {:label   "Round 2 image 2"
                                 :type    "image"
                                 :options {:max-width  150
                                           :max-height 150
                                           :min-height 50
                                           :min-width  50}}
                      :image2-3 {:label   "Round 2 image 3"
                                 :type    "image"
                                 :options {:max-width  150
                                           :max-height 150
                                           :min-height 50
                                           :min-width  50}}}})

(def piles-counter {:state-1 3
                    :state-2 7
                    :state-3 11
                    :state-4 15
                    :state-5 20
                    :state-6 25
                    :state-7 31})

(def t {:assets        [{:url "/raw/img/park/sandbox-castle/background.png", :size 10, :type "image"}
                        {:url "/raw/img/park/sandbox-castle/surface.png", :size 10, :type "image"}
                        {:url "/raw/img/park/sandbox-castle/decoration.png", :size 10, :type "image"}
                        {:url "/raw/img/park/sandbox-castle/sandbox_face.png", :size 1 :type "image"}
                        {:url "/raw/img/park/sandbox-castle/sandbox.png", :size 1, :type "image"}
                        {:url "/raw/img/park/sandbox-castle/shadow.png", :size 1, :type "image"}
                        {:url "/raw/img/park/sandbox-castle/window.png", :size 1, :type "image"}
                        {:url "/raw/img/park/sandbox-castle/sand_01.png", :size 1, :type "image"}
                        {:url "/raw/img/park/sandbox-castle/sand_02.png", :size 1, :type "image"}
                        {:url "/raw/img/park/sandbox-castle/sand_03.png", :size 1, :type "image"}
                        {:url "/raw/img/park/sandbox-castle/sand_04.png", :size 1, :type "image"}
                        {:url "/raw/img/park/sandbox-castle/sand_05.png", :size 1, :type "image"}
                        {:url "/raw/img/park/sandbox-castle/sand_06.png", :size 1, :type "image"}
                        {:url "/raw/img/park/sandbox-castle/castle.png", :size 1, :type "image"}
                        {:url "/raw/img/onset-and-rime/cloud.png", :size 1, :type "image"}],
        :objects
                       {:background    {:type       "layered-background"
                                        :background {:src "/raw/img/park/sandbox-castle/background.png"}
                                        :decoration {:src "/raw/img/park/sandbox-castle/surface.png"}
                                        :surface    {:src "/raw/img/park/sandbox-castle/decoration.png"}},
                        :mari          {:type       "animation"
                                        :x          1600
                                        :y          520
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
                                        :states     {:left {:scale-x 1} :right {:scale-x -1}}}
                        :sand1-trigger {:type      "transparent"
                                        :x         -100
                                        :y         -100
                                        :width     200
                                        :height    200
                                        :actions   {:dig {:id "dig1" :on "pointerover" :type "action"}}}
                        :box1-group    {:type     "group"
                                        :x        492
                                        :y        720
                                        :children ["box1" "sand1" "sand1-trigger"]}
                        :box2-group    {:type     "group"
                                        :x        764,
                                        :y        685,
                                        :children ["box2" "sand2" "sand2-trigger"]}
                        :box3-group    {:type     "group"
                                        :x        1079,
                                        :y        727,
                                        :children ["box3" "sand3" "sand3-trigger"]}
                        :sand1         {:type     "group"
                                        :filters  [{:name "brightness" :value 0}
                                                   {:name "glow" :outer-strength 0 :color 0xffd700}]
                                        :children ["sand1-1" "sand1-2" "sand1-3" "sand1-4" "sand1-5" "sand1-6"]}
                        :sand1-1       {:type   "image"
                                        :y      10
                                        :src    "/raw/img/park/sandbox-castle/sand_01.png"
                                        :origin {:type "center-center"}}
                        :sand1-2       {:type   "image"
                                        :src    "/raw/img/park/sandbox-castle/sand_02.png"
                                        :origin {:type "center-center"}}
                        :sand1-3       {:type   "image"
                                        :src    "/raw/img/park/sandbox-castle/sand_03.png"
                                        :origin {:type "center-center"}}
                        :sand1-4       {:type   "image"
                                        :src    "/raw/img/park/sandbox-castle/sand_04.png"
                                        :origin {:type "center-center"}}
                        :sand1-5       {:type   "image"
                                        :src    "/raw/img/park/sandbox-castle/sand_05.png"
                                        :origin {:type "center-center"}}
                        :sand1-6       {:type   "image"
                                        :src    "/raw/img/park/sandbox-castle/sand_06.png"
                                        :origin {:type "center-center"}}
                        :box1
                                       {:type        "animation",
                                        :width       771,
                                        :height      1033,
                                        :y           60
                                        :scale       {:x 0.25, :y 0.25},
                                        :scene-name  "box1",
                                        :anim        "idle2",
                                        :anim-offset {:x 0, :y -300},
                                        :loop        true,
                                        :name        "boxes",
                                        :skin        "qwestion",
                                        :speed       0.3,
                                        :start       false,
                                        :filters     [{:name "brightness" :value 0}
                                                      {:name "glow" :outer-strength 0 :color 0xffd700}]
                                        :states      {:init-position {:x 304, :y 826}}},

                        :sand2-trigger {:type      "transparent"
                                        :x         -100
                                        :y         -100
                                        :width     200
                                        :height    200
                                        :actions   {:dig {:id "dig2" :on "pointerover" :type "action"}}}

                        :sand2         {:type     "group"
                                        :filters  [{:name "brightness" :value 0}
                                                   {:name "glow" :outer-strength 0 :color 0xffd700}]
                                        :children ["sand2-1" "sand2-2" "sand2-3" "sand2-4" "sand2-5" "sand2-6"]}
                        :sand2-1       {:type   "image"
                                        :y      10
                                        :src    "/raw/img/park/sandbox-castle/sand_01.png"
                                        :origin {:type "center-center"}}
                        :sand2-2       {:type   "image"
                                        :src    "/raw/img/park/sandbox-castle/sand_02.png"
                                        :origin {:type "center-center"}}
                        :sand2-3       {:type   "image"
                                        :src    "/raw/img/park/sandbox-castle/sand_03.png"
                                        :origin {:type "center-center"}}
                        :sand2-4       {:type   "image"
                                        :src    "/raw/img/park/sandbox-castle/sand_04.png"
                                        :origin {:type "center-center"}}
                        :sand2-5       {:type   "image"
                                        :src    "/raw/img/park/sandbox-castle/sand_05.png"
                                        :origin {:type "center-center"}}
                        :sand2-6       {:type   "image"
                                        :src    "/raw/img/park/sandbox-castle/sand_06.png"
                                        :origin {:type "center-center"}}
                        :box2
                                       {:type        "animation",
                                        :width       771,
                                        :height      1033,
                                        :y           60
                                        :scale       {:x 0.25, :y 0.25},
                                        :scene-name  "box2",
                                        :transition  "box2",
                                        :anim        "idle2",
                                        :anim-offset {:x 0, :y -300},
                                        :loop        true,
                                        :name        "boxes",
                                        :skin        "qwestion",
                                        :speed       0.3,
                                        :start       false,
                                        :filters     [{:name "brightness" :value 0}
                                                      {:name "glow" :outer-strength 0 :color 0xffd700}]
                                        :states      {:init-position {:x 401, :y 696}}}

                        :sand3-trigger {:type      "transparent"
                                        :x         -100
                                        :y         -100
                                        :width     200
                                        :height    200
                                        :actions   {:dig {:id "dig3" :on "pointerover" :type "action"}}}

                        :sand3         {:type     "group"
                                        :filters  [{:name "brightness" :value 0}
                                                   {:name "glow" :outer-strength 0 :color 0xffd700}]
                                        :children ["sand3-1" "sand3-2" "sand3-3" "sand3-4" "sand3-5" "sand3-6"]}
                        :sand3-1       {:type   "image"
                                        :y      10
                                        :src    "/raw/img/park/sandbox-castle/sand_01.png"
                                        :origin {:type "center-center"}}
                        :sand3-2       {:type   "image"
                                        :src    "/raw/img/park/sandbox-castle/sand_02.png"
                                        :origin {:type "center-center"}}
                        :sand3-3       {:type   "image"
                                        :src    "/raw/img/park/sandbox-castle/sand_03.png"
                                        :origin {:type "center-center"}}
                        :sand3-4       {:type   "image"
                                        :src    "/raw/img/park/sandbox-castle/sand_04.png"
                                        :origin {:type "center-center"}}
                        :sand3-5       {:type   "image"
                                        :src    "/raw/img/park/sandbox-castle/sand_05.png"
                                        :origin {:type "center-center"}}
                        :sand3-6       {:type   "image"
                                        :src    "/raw/img/park/sandbox-castle/sand_06.png"
                                        :origin {:type "center-center"}}

                        :box3
                                       {:type        "animation",
                                        :width       771,
                                        :height      1033,
                                        :y           60
                                        :scale       {:x 0.25, :y 0.25},
                                        :scene-name  "box3",
                                        :anim        "idle2",
                                        :anim-offset {:x 0, :y -300},
                                        :loop        true,
                                        :name        "boxes",
                                        :skin        "qwestion",
                                        :speed       0.3,
                                        :start       false,
                                        :filters     [{:name "brightness" :value 0}
                                                      {:name "glow" :outer-strength 0 :color 0xffd700}]
                                        :states      {:init-position {:x 600, :y 674}}}

                        :sandbox       {:type "image",
                                        :x    176,
                                        :y    636,
                                        :src  "/raw/img/park/sandbox-castle/sandbox.png"}
                        :skirting      {:type "image",
                                        :x    192,
                                        :y    667,
                                        :src  "/raw/img/park/sandbox-castle/sandbox_face.png"}
                        :shadow        {:type "image",
                                        :x    100,
                                        :y    741,
                                        :src  "/raw/img/park/sandbox-castle/shadow.png"}},
        :scene-objects [["background"] ["shadow" "skirting" "sandbox" "box3-group" "box2-group" "box1-group" "mari"]],
        :actions
                       {:dig1                {:type "sequence-data"
                                              :data [{:type           "counter"
                                                      :counter-action "increase"
                                                      :counter-id     "sand1"}
                                                     {:type "action" :id "fx-dialog" :unique-tag "sound-fx" :return-immediately true}
                                                     {:type       "test-var-inequality",
                                                      :unique-tag "dig"
                                                      :var-name   "sand1",
                                                      :inequality ">="
                                                      :from-var   [{:var-name "sand1-next-state-value", :action-property "value"}
                                                                   {:template "sand1-state%" :var-name "sand1-next-state", :action-property "success"}]}]}
                        :sand1-state2        {:type "sequence-data"
                                              :data [{:type "set-variable", :var-name "sand1-next-state", :var-value 3}
                                                     {:type "set-variable", :var-name "sand1-next-state-value", :var-value (:state-2 piles-counter)}
                                                     {:type "set-attribute", :target "sand1-6", :attr-name "visible" :attr-value false}
                                                     {:type     "action" :unique-tag "word" :return-immediately true
                                                      :from-var [{:template "word1-round%-dialog" :var-name "round-counter", :action-property "id"}]}]}
                        :sand1-state3        {:type "sequence-data"
                                              :data [{:type "set-variable", :var-name "sand1-next-state", :var-value 4}
                                                     {:type "set-variable", :var-name "sand1-next-state-value", :var-value (:state-3 piles-counter)}
                                                     {:type "set-attribute", :target "sand1-5", :attr-name "visible" :attr-value false}
                                                     {:type     "action" :unique-tag "word" :return-immediately true
                                                      :from-var [{:template "word1-round%-dialog" :var-name "round-counter", :action-property "id"}]}]}
                        :sand1-state4        {:type "sequence-data"
                                              :data [{:type "set-variable", :var-name "sand1-next-state", :var-value 5}
                                                     {:type "set-variable", :var-name "sand1-next-state-value", :var-value (:state-4 piles-counter)}
                                                     {:type "set-attribute", :target "sand1-4", :attr-name "visible" :attr-value false}
                                                     {:type     "action" :unique-tag "word" :return-immediately true
                                                      :from-var [{:template "word1-round%-dialog" :var-name "round-counter", :action-property "id"}]}]}
                        :sand1-state5        {:type "sequence-data"
                                              :data [{:type "set-variable", :var-name "sand1-next-state", :var-value 6}
                                                     {:type "set-variable", :var-name "sand1-next-state-value", :var-value (:state-5 piles-counter)}
                                                     {:type "set-attribute", :target "sand1-3", :attr-name "visible" :attr-value false}
                                                     {:type     "action" :unique-tag "word" :return-immediately true
                                                      :from-var [{:template "word1-round%-dialog" :var-name "round-counter", :action-property "id"}]}]}
                        :sand1-state6        {:type "sequence-data"
                                              :data [{:type "set-variable", :var-name "sand1-next-state", :var-value 7}
                                                     {:type "set-variable", :var-name "sand1-next-state-value", :var-value (:state-6 piles-counter)}
                                                     {:type "set-attribute", :target "sand1-2", :attr-name "visible" :attr-value false}
                                                     {:type     "action" :unique-tag "word" :return-immediately true
                                                      :from-var [{:template "word1-round%-dialog" :var-name "round-counter", :action-property "id"}]}]}
                        :sand1-state7        {:type "sequence-data"
                                              :data [{:type "set-variable", :var-name "sand1-next-state", :var-value 8}
                                                     {:type "set-variable", :var-name "sand1-next-state-value", :var-value (:state-7 piles-counter)}
                                                     {:type "set-attribute", :target "sand1-1", :attr-name "visible" :attr-value false}
                                                     {:type     "action"
                                                      :from-var [{:template "fact1-round%-dialog" :var-name "round-counter", :action-property "id"}]}
                                                     {:type "set-variable" :var-name "sand1-finished" :var-value true}
                                                     {:type "action" :id "check-round"}]}
                        :sand1-state8        {:type "empty" :duration 1}

                        :dig2                {:type "sequence-data"
                                              :data [{:type           "counter"
                                                      :counter-action "increase"
                                                      :counter-id     "sand2"}
                                                     {:type "action" :id "fx-dialog" :unique-tag "sound-fx" :return-immediately true}
                                                     {:type       "test-var-inequality",
                                                      :unique-tag "dig"
                                                      :var-name   "sand2",
                                                      :inequality ">="
                                                      :from-var   [{:var-name "sand2-next-state-value", :action-property "value"}
                                                                   {:template "sand2-state%" :var-name "sand2-next-state", :action-property "success"}]}]}
                        :sand2-state2        {:type "sequence-data"
                                              :data [{:type "set-variable", :var-name "sand2-next-state", :var-value 3}
                                                     {:type "set-variable", :var-name "sand2-next-state-value", :var-value (:state-2 piles-counter)}
                                                     {:type "set-attribute", :target "sand2-6", :attr-name "visible" :attr-value false}
                                                     {:type     "action" :unique-tag "word" :return-immediately true
                                                      :from-var [{:template "word2-round%-dialog" :var-name "round-counter", :action-property "id"}]}]}
                        :sand2-state3        {:type "sequence-data"
                                              :data [{:type "set-variable", :var-name "sand2-next-state", :var-value 4}
                                                     {:type "set-variable", :var-name "sand2-next-state-value", :var-value (:state-3 piles-counter)}
                                                     {:type "set-attribute", :target "sand2-5", :attr-name "visible" :attr-value false}
                                                     {:type     "action" :unique-tag "word" :return-immediately true
                                                      :from-var [{:template "word2-round%-dialog" :var-name "round-counter", :action-property "id"}]}]}
                        :sand2-state4        {:type "sequence-data"
                                              :data [{:type "set-variable", :var-name "sand2-next-state", :var-value 5}
                                                     {:type "set-variable", :var-name "sand2-next-state-value", :var-value (:state-4 piles-counter)}
                                                     {:type "set-attribute", :target "sand2-4", :attr-name "visible" :attr-value false}
                                                     {:type     "action" :unique-tag "word" :return-immediately true
                                                      :from-var [{:template "word2-round%-dialog" :var-name "round-counter", :action-property "id"}]}]}
                        :sand2-state5        {:type "sequence-data"
                                              :data [{:type "set-variable", :var-name "sand2-next-state", :var-value 6}
                                                     {:type "set-variable", :var-name "sand2-next-state-value", :var-value (:state-5 piles-counter)}
                                                     {:type "set-attribute", :target "sand2-3", :attr-name "visible" :attr-value false}
                                                     {:type     "action" :unique-tag "word" :return-immediately true
                                                      :from-var [{:template "word2-round%-dialog" :var-name "round-counter", :action-property "id"}]}]}
                        :sand2-state6        {:type "sequence-data"
                                              :data [{:type "set-variable", :var-name "sand2-next-state", :var-value 7}
                                                     {:type "set-variable", :var-name "sand2-next-state-value", :var-value (:state-6 piles-counter)}
                                                     {:type "set-attribute", :target "sand2-2", :attr-name "visible" :attr-value false}
                                                     {:type     "action" :unique-tag "word" :return-immediately true
                                                      :from-var [{:template "word2-round%-dialog" :var-name "round-counter", :action-property "id"}]}]}
                        :sand2-state7        {:type "sequence-data"
                                              :data [{:type "set-variable", :var-name "sand2-next-state", :var-value 8}
                                                     {:type "set-variable", :var-name "sand2-next-state-value", :var-value (:state-7 piles-counter)}
                                                     {:type "set-attribute", :target "sand2-1", :attr-name "visible" :attr-value false}
                                                     {:type     "action"
                                                      :from-var [{:template "fact2-round%-dialog" :var-name "round-counter", :action-property "id"}]}
                                                     {:type "set-variable" :var-name "sand2-finished" :var-value true}
                                                     {:type "action" :id "check-round"}]}
                        :sand2-state8        {:type "empty" :duration 1}


                        :dig3                {:type "sequence-data"
                                              :data [{:type           "counter"
                                                      :counter-action "increase"
                                                      :counter-id     "sand3"}
                                                     {:type "action" :id "fx-dialog" :unique-tag "sound-fx" :return-immediately true}
                                                     {:type       "test-var-inequality",
                                                      :unique-tag "dig"
                                                      :var-name   "sand3",
                                                      :inequality ">="
                                                      :from-var   [{:var-name "sand3-next-state-value", :action-property "value"}
                                                                   {:template "sand3-state%" :var-name "sand3-next-state", :action-property "success"}]}]}
                        :sand3-state2        {:type "sequence-data"
                                              :data [{:type "set-variable", :var-name "sand3-next-state", :var-value 3}
                                                     {:type "set-variable", :var-name "sand3-next-state-value", :var-value (:state-2 piles-counter)}
                                                     {:type "set-attribute", :target "sand3-6", :attr-name "visible" :attr-value false}
                                                     {:type     "action" :unique-tag "word" :return-immediately true
                                                      :from-var [{:template "word3-round%-dialog" :var-name "round-counter", :action-property "id"}]}]}
                        :sand3-state3        {:type "sequence-data"
                                              :data [{:type "set-variable", :var-name "sand3-next-state", :var-value 4}
                                                     {:type "set-variable", :var-name "sand3-next-state-value", :var-value (:state-3 piles-counter)}
                                                     {:type "set-attribute", :target "sand3-5", :attr-name "visible" :attr-value false}
                                                     {:type     "action" :unique-tag "word" :return-immediately true
                                                      :from-var [{:template "word3-round%-dialog" :var-name "round-counter", :action-property "id"}]}]}
                        :sand3-state4        {:type "sequence-data"
                                              :data [{:type "set-variable", :var-name "sand3-next-state", :var-value 5}
                                                     {:type "set-variable", :var-name "sand3-next-state-value", :var-value (:state-4 piles-counter)}
                                                     {:type "set-attribute", :target "sand3-4", :attr-name "visible" :attr-value false}
                                                     {:type     "action" :unique-tag "word" :return-immediately true
                                                      :from-var [{:template "word3-round%-dialog" :var-name "round-counter", :action-property "id"}]}]}
                        :sand3-state5        {:type "sequence-data"
                                              :data [{:type "set-variable", :var-name "sand3-next-state", :var-value 6}
                                                     {:type "set-variable", :var-name "sand3-next-state-value", :var-value (:state-5 piles-counter)}
                                                     {:type "set-attribute", :target "sand3-3", :attr-name "visible" :attr-value false}
                                                     {:type     "action" :unique-tag "word" :return-immediately true
                                                      :from-var [{:template "word3-round%-dialog" :var-name "round-counter", :action-property "id"}]}]}
                        :sand3-state6        {:type "sequence-data"
                                              :data [{:type "set-variable", :var-name "sand3-next-state", :var-value 7}
                                                     {:type "set-variable", :var-name "sand3-next-state-value", :var-value (:state-6 piles-counter)}
                                                     {:type "set-attribute", :target "sand3-2", :attr-name "visible" :attr-value false}
                                                     {:type     "action" :unique-tag "word" :return-immediately true
                                                      :from-var [{:template "word3-round%-dialog" :var-name "round-counter", :action-property "id"}]}]}
                        :sand3-state7        {:type "sequence-data"
                                              :data [{:type "set-variable", :var-name "sand3-next-state", :var-value 8}
                                                     {:type "set-variable", :var-name "sand3-next-state-value", :var-value (:state-7 piles-counter)}
                                                     {:type "set-attribute", :target "sand3-1", :attr-name "visible" :attr-value false}
                                                     {:type     "action"
                                                      :from-var [{:template "fact3-round%-dialog" :var-name "round-counter", :action-property "id"}]}
                                                     {:type "set-variable" :var-name "sand3-finished" :var-value true}
                                                     {:type "action" :id "check-round"}]}

                        :sand3-state8        {:type "empty" :duration 1}

                        :check-round         {:type       "test-var-list",
                                              :success    "next-round",
                                              :values     [true true true],
                                              :var-names  ["sand1-finished" "sand2-finished" "sand3-finished"]
                                              :unique-tag "round"}
                        :finish-activity     {:type "sequence-data",
                                              :data [{:type "action" :id "finish-activity-dialog"}
                                                     {:type "finish-activity"}]},
                        :start               {:type "sequence-data"
                                              :data [{:type "start-activity"}
                                                     {:type "set-variable", :var-name "round-counter", :var-value 0}
                                                     {:type "set-variable", :var-name "sand1-next-state", :var-value 8}
                                                     {:type "set-variable", :var-name "sand2-next-state", :var-value 8}
                                                     {:type "set-variable", :var-name "sand3-next-state", :var-value 8}
                                                     {:type "action" :id "intro-dialog"}
                                                     {:type "action" :id "new-round"}]}

                        :next-round          {:type     "test-value"
                                              :value1   2
                                              :success  "finish-activity"
                                              :fail     "new-round"
                                              :from-var [{:var-name "round-counter", :action-property "value2"}]}
                        :new-round           {:type "sequence-data"
                                              :data [{:type "parallel"
                                                      :data [{:type "counter" :counter-action "increase" :counter-id "round-counter"}
                                                             {:type "set-variable", :var-name "sand1", :var-value 0}
                                                             {:type "set-variable", :var-name "sand1-finished", :var-value false}
                                                             {:type "set-variable", :var-name "sand1-next-state", :var-value 2}
                                                             {:type "set-variable", :var-name "sand1-next-state-value", :var-value (:state-1 piles-counter)}

                                                             {:type "set-variable", :var-name "sand2", :var-value 0}
                                                             {:type "set-variable", :var-name "sand2-finished", :var-value false}
                                                             {:type "set-variable", :var-name "sand2-next-state", :var-value 2}
                                                             {:type "set-variable", :var-name "sand2-next-state-value", :var-value (:state-1 piles-counter)}

                                                             {:type "set-variable", :var-name "sand3", :var-value 0}
                                                             {:type "set-variable", :var-name "sand3-finished", :var-value false}
                                                             {:type "set-variable", :var-name "sand3-next-state", :var-value 2}
                                                             {:type "set-variable", :var-name "sand3-next-state-value", :var-value (:state-1 piles-counter)}

                                                             {:type "set-attribute", :target "sand1-1", :attr-name "visible" :attr-value true}
                                                             {:type "set-attribute", :target "sand1-2", :attr-name "visible" :attr-value true}
                                                             {:type "set-attribute", :target "sand1-3", :attr-name "visible" :attr-value true}
                                                             {:type "set-attribute", :target "sand1-4", :attr-name "visible" :attr-value true}
                                                             {:type "set-attribute", :target "sand1-5", :attr-name "visible" :attr-value true}
                                                             {:type "set-attribute", :target "sand1-6", :attr-name "visible" :attr-value true}

                                                             {:type "set-attribute", :target "sand2-1", :attr-name "visible" :attr-value true}
                                                             {:type "set-attribute", :target "sand2-2", :attr-name "visible" :attr-value true}
                                                             {:type "set-attribute", :target "sand2-3", :attr-name "visible" :attr-value true}
                                                             {:type "set-attribute", :target "sand2-4", :attr-name "visible" :attr-value true}
                                                             {:type "set-attribute", :target "sand2-5", :attr-name "visible" :attr-value true}
                                                             {:type "set-attribute", :target "sand2-6", :attr-name "visible" :attr-value true}

                                                             {:type "set-attribute", :target "sand3-1", :attr-name "visible" :attr-value true}
                                                             {:type "set-attribute", :target "sand3-2", :attr-name "visible" :attr-value true}
                                                             {:type "set-attribute", :target "sand3-3", :attr-name "visible" :attr-value true}
                                                             {:type "set-attribute", :target "sand3-4", :attr-name "visible" :attr-value true}
                                                             {:type "set-attribute", :target "sand3-5", :attr-name "visible" :attr-value true}
                                                             {:type "set-attribute", :target "sand3-6", :attr-name "visible" :attr-value true}]}
                                                     {:type     "action"
                                                      :from-var [{:template "set-images-round%" :var-name "round-counter", :action-property "id"}]}
                                                     {:type     "action"
                                                      :from-var [{:template "round%-started-dialog" :var-name "round-counter", :action-property "id"}]}]}
                        :stop-activity       {:type "stop-activity"},
                        :set-images-round1   {:type "parallel"
                                              :data [{:type "action" :id "set-image1-1"}
                                                     {:type "action" :id "set-image1-2"}
                                                     {:type "action" :id "set-image1-3"}]}
                        :set-images-round2   {:type "parallel"
                                              :data [{:type "action" :id "set-image2-1"}
                                                     {:type "action" :id "set-image2-2"}
                                                     {:type "action" :id "set-image2-3"}]}

                        :set-image1-1        {:type       "set-slot",
                                              :image      ""
                                              :target     "box1"
                                              :attachment {:x 40, :scale-x 4, :scale-y 4},
                                              :slot-name  "box1"}
                        :set-image1-2        {:type       "set-slot",
                                              :image      ""
                                              :target     "box2"
                                              :attachment {:x 40, :scale-x 4, :scale-y 4},
                                              :slot-name  "box1"}
                        :set-image1-3        {:type       "set-slot",
                                              :image      ""
                                              :target     "box3"
                                              :attachment {:x 40, :scale-x 4, :scale-y 4},
                                              :slot-name  "box1"}
                        :set-image2-1        {:type       "set-slot",
                                              :image      ""
                                              :target     "box1"
                                              :attachment {:x 40, :scale-x 4, :scale-y 4},
                                              :slot-name  "box1"}
                        :set-image2-2        {:type       "set-slot",
                                              :image      ""
                                              :target     "box2"
                                              :attachment {:x 40, :scale-x 4, :scale-y 4},
                                              :slot-name  "box1"}
                        :set-image2-3        {:type       "set-slot",
                                              :image      ""
                                              :target     "box3"
                                              :attachment {:x 40, :scale-x 4, :scale-y 4},
                                              :slot-name  "box1"}
                        :highlight-sand      {:type "parallel"
                                              :data [{:type               "transition"
                                                      :transition-id      "sand1"
                                                      :return-immediately true
                                                      :from               {:brightness 0 :glow 0}
                                                      :to                 {:brightness 1 :glow 10 :yoyo true :duration 0.5 :repeat 5}}
                                                     {:type               "transition"
                                                      :transition-id      "sand2"
                                                      :return-immediately true
                                                      :from               {:brightness 0 :glow 0}
                                                      :to                 {:brightness 1 :glow 10 :yoyo true :duration 0.5 :repeat 5}}
                                                     {:type               "transition"
                                                      :transition-id      "sand3"
                                                      :return-immediately true
                                                      :from               {:brightness 0 :glow 0}
                                                      :to                 {:brightness 1 :glow 10 :yoyo true :duration 0.5 :repeat 5}}]}
                        :intro-dialog        (dialog/default "Intro")
                        :round1-started-dialog
                                             (dialog/default "Start Round 1")
                        :round2-started-dialog
                                             (dialog/default "Start Round 2")
                        :fx-dialog           (-> (dialog/default "Sound FX")
                                                 (assoc :tags ["fx"]))
                        :finish-activity-dialog
                                             (dialog/default "Finish activity")
                        :fact1-round1-dialog (dialog/default "Round 1 Fact 1")
                        :fact2-round1-dialog (dialog/default "Round 1 Fact 2")
                        :fact3-round1-dialog (dialog/default "Round 1 Fact 3")

                        :fact1-round2-dialog (dialog/default "Round 2 Fact 1")
                        :fact2-round2-dialog (dialog/default "Round 2 Fact 2")
                        :fact3-round2-dialog (dialog/default "Round 2 Fact 3")

                        :word1-round1-dialog (dialog/default "Round 1 Word 1")
                        :word2-round1-dialog (dialog/default "Round 1 Word 2")
                        :word3-round1-dialog (dialog/default "Round 1 Word 3")

                        :word1-round2-dialog (dialog/default "Round 2 Word 1")
                        :word2-round2-dialog (dialog/default "Round 2 Word 2")
                        :word3-round2-dialog (dialog/default "Round 2 Word 3")},
        :triggers      {:back {:on "back", :action "stop-activity"}, :start {:on "start", :action "start"}},
        :metadata      {:prev              "park", :autostart true
                        :tracks            [{:title "Intro"
                                             :nodes [{:type      "dialog"
                                                      :action-id :intro-dialog}
                                                     {:type "prompt"
                                                      :text "Guide gives instruction. New round begins"}
                                                     {:type      "dialog"
                                                      :action-id :finish-activity-dialog}]}
                                            {:title "Round 1"
                                             :nodes [{:type      "dialog"
                                                      :action-id :word1-round1-dialog}
                                                     {:type      "dialog"
                                                      :action-id :word2-round1-dialog}
                                                     {:type      "dialog"
                                                      :action-id :word3-round1-dialog}
                                                     {:type      "dialog"
                                                      :action-id :fact1-round1-dialog}
                                                     {:type      "dialog"
                                                      :action-id :fact2-round1-dialog}
                                                     {:type      "dialog"
                                                      :action-id :fact3-round1-dialog}]}
                                            {:title "Round 2"
                                             :nodes [{:type      "dialog"
                                                      :action-id :word1-round2-dialog}
                                                     {:type      "dialog"
                                                      :action-id :word2-round2-dialog}
                                                     {:type      "dialog"
                                                      :action-id :word3-round2-dialog}
                                                     {:type      "dialog"
                                                      :action-id :fact1-round2-dialog}
                                                     {:type      "dialog"
                                                      :action-id :fact2-round2-dialog}
                                                     {:type      "dialog"
                                                      :action-id :fact3-round2-dialog}]}]
                        :available-actions [{:action "highlight-sand"
                                             :name   "Highlight sand"}]}})

(defn add-images
  [activity-data args]
  (-> activity-data
      (assoc-in [:actions :set-image1-1 :image] (get-in args [:image1-1 :src]))
      (assoc-in [:actions :set-image1-2 :image] (get-in args [:image1-2 :src]))
      (assoc-in [:actions :set-image1-3 :image] (get-in args [:image1-3 :src]))
      (assoc-in [:actions :set-image2-1 :image] (get-in args [:image2-1 :src]))
      (assoc-in [:actions :set-image2-2 :image] (get-in args [:image2-2 :src]))
      (assoc-in [:actions :set-image2-3 :image] (get-in args [:image2-3 :src]))

      (update :assets concat [{:url (get-in args [:image1-1 :src]), :size 1, :type "image"}
                              {:url (get-in args [:image1-2 :src]), :size 1, :type "image"}
                              {:url (get-in args [:image1-3 :src]), :size 1, :type "image"}
                              {:url (get-in args [:image2-1 :src]), :size 1, :type "image"}
                              {:url (get-in args [:image2-2 :src]), :size 1, :type "image"}
                              {:url (get-in args [:image2-3 :src]), :size 1, :type "image"}])))

(defn create
  [args]
  (-> t
      (add-images args)))

(core/register-template
  m
  create)
