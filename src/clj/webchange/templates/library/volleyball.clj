(ns webchange.templates.library.volleyball
  (:require
    [webchange.templates.core :as core]
    [webchange.templates.utils.dialog :as dialog]))

(def m {:id          9
        :name        "Volleyball"
        :tags        ["Independent Practice"]
        :description "Some description of volleyball mechanics and covered skills"
        :lesson-sets ["volleyball-concepts" "volleyball-incorrect"]
        :fields      [{:name "volleyball-image-1",
                       :type "image"}
                      {:name "volleyball-image-2",
                       :type "image"}
                      {:name "volleyball-image-3",
                       :type "image"}
                      {:name "volleyball-image-4",
                       :type "image"}
                      {:name "volleyball-image-5",
                       :type "image"}
                      {:name "volleyball-image-6",
                       :type "image"}]})


(def t {:assets
                       [{:url "/raw/img/stadium/volleyball/background.jpg", :size 10, :type "image"}
                        {:url "/raw/img/stadium/volleyball/ball.png", :size 1, :type "image"}],
        :objects
                       {:background {:type "background", :src "/raw/img/stadium/volleyball/background.jpg"},
                        :ball
                                    {:type      "image",
                                     :src       "/raw/img/stadium/volleyball/ball.png"
                                     :x         1130
                                     :y         908
                                     :width     128,
                                     :height    128,
                                     :scale-x   0.55,
                                     :scale-y   0.55,
                                     :visible   true
                                     :draggable true
                                     :actions   {:drag-end   {:id "stop-drag", :on "drag-end", :type "action"},
                                                 :drag-start {:id "start-drag", :on "drag-start", :type "action"}}}
                        :box1
                                    {:type        "animation",
                                     :x           1304,
                                     :y           831,
                                     :width       771,
                                     :height      1033,
                                     :scale       {:x 0.3, :y 0.3},
                                     :origin      {:type "center-center"},
                                     :scene-name  "box1",
                                     :actions     {:click {:id "click-box", :on "click", :type "action", :params {:player 1}}},
                                     :anim        "idle1",
                                     :anim-offset {:x 0, :y -300},
                                     :name        "boxes",
                                     :skin        "empty"},
                        :box2
                                    {:type        "animation",
                                     :x           1665,
                                     :y           666,
                                     :width       771,
                                     :height      1033,
                                     :scale       {:x 0.3, :y 0.3},
                                     :origin      {:type "center-center"},
                                     :scene-name  "box2",
                                     :actions     {:click {:id "click-box", :on "click", :type "action", :params {:player 2}}},
                                     :anim        "idle1",
                                     :anim-offset {:x 0, :y -300},
                                     :name        "boxes",
                                     :skin        "empty"},
                        :box3
                                    {:type        "animation",
                                     :x           409,
                                     :y           831,
                                     :width       771,
                                     :height      1033,
                                     :scale       {:x 0.3, :y 0.3},
                                     :origin      {:type "center-center"},
                                     :scene-name  "box3",
                                     :actions     {:click {:id "click-box", :on "click", :type "action", :params {:player 3}}},
                                     :anim        "idle1",
                                     :anim-offset {:x 0, :y -300},
                                     :name        "boxes",
                                     :skin        "empty"},
                        :box4
                                    {:type        "animation",
                                     :x           729,
                                     :y           666,
                                     :width       771,
                                     :height      1033,
                                     :scale       {:x 0.3, :y 0.3},
                                     :origin      {:type "center-center"},
                                     :scene-name  "box4",
                                     :actions     {:click {:id "click-box", :on "click", :type "action", :params {:player 4}}},
                                     :anim        "idle1",
                                     :anim-offset {:x 0, :y -300},
                                     :name        "boxes",
                                     :skin        "empty"},
                        :boy1
                                    {:type       "animation",
                                     :x          1556,
                                     :y          871,
                                     :width      758,
                                     :height     1130,
                                     :scale      {:x 0.25, :y 0.25},
                                     :scene-name "boy1",
                                     :anim       "idle",
                                     :name       "vera-45",
                                     :skin       "1_boy",
                                     :speed      0.8,
                                     :start      true},
                        :girl2
                                    {:type       "animation",
                                     :x          297,
                                     :y          1010,
                                     :width      758,
                                     :height     1130,
                                     :scale      {:x -0.25, :y 0.25},
                                     :scene-name "girl2",
                                     :anim       "idle",
                                     :name       "vera-45",
                                     :skin       "2_girl",
                                     :speed      0.8,
                                     :start      true},
                        :girl3
                                    {:type       "animation",
                                     :x          612,
                                     :y          871,
                                     :width      758,
                                     :height     1130,
                                     :scale      {:x -0.25, :y 0.25},
                                     :scene-name "girl3",
                                     :anim       "idle",
                                     :name       "vera-45",
                                     :skin       "3_girl",
                                     :speed      0.8,
                                     :start      true},
                        :mari
                                    {:type        "animation",
                                     :x           1265,
                                     :y           311,
                                     :width       473,
                                     :height      511,
                                     :scene-name  "mari",
                                     :transition  "mari",
                                     :anim        "idle",
                                     :anim-offset {:x 0, :y -150},
                                     :name        "mari",
                                     :scale-x     0.5,
                                     :scale-y     0.5,
                                     :speed       0.35,
                                     :start       true
                                     :actions    {:click {:on "click" :type "action" :id "tap-instructions-dialog"}}},
                        :vera
                                    {:type       "animation",
                                     :x          1182,
                                     :y          1010,
                                     :width      758,
                                     :height     1130,
                                     :scale      {:x 0.25, :y 0.25},
                                     :scene-name "vera",
                                     :anim       "volley_idle",
                                     :name       "vera-45",
                                     :skin       "vera",
                                     :speed      0.8,
                                     :start      true}},
        :scene-objects [["background"] ["vera" "boy1" "girl2" "girl3" "box1" "box2" "box3" "box4"] ["ball" "mari"]],
        :actions
                       {:start-drag                {:type "sequence-data"
                                                    :data [{:id "check-collide-2", :type "set-interval", :action "check-collide", :interval 100}]}
                        :stop-drag                 {:type "sequence-data",
                                                    :data [{:id "check-collide-2", :type "remove-interval"}
                                                           {:type     "test-value",
                                                            :fail     "box-selected",
                                                            :value1   nil
                                                            :from-var [{:var-name "player-selected", :action-property "value2"}]}]}
                        :box-selected              {:type "sequence-data"
                                                    :data [{:type     "test-value",
                                                            :success  "pick-correct"
                                                            :fail     "pick-wrong"
                                                            :from-var [{:var-name "current-target", :action-property "value1"}
                                                                       {:var-name "player-selected", :action-property "value2"}]}]}

                        :check-collide             {:type        "test-transitions-and-pointer-collide"
                                                    :success     "highlight"
                                                    :fail        "unhighlight"
                                                    :transitions ["box1" "box2" "box3" "box4"]}
                        :highlight                 {:type "sequence-data",
                                                    :data [{:type        "set-variable", :var-name "box-selected"
                                                            :from-params [{:param-property "transition", :action-property "var-value"}]}
                                                           {:type        "case",
                                                            :options     {:box1 {:type "set-variable", :var-name "player-selected" :var-value "player1"},
                                                                          :box2 {:type "set-variable", :var-name "player-selected" :var-value "player2"}
                                                                          :box3 {:type "set-variable", :var-name "player-selected" :var-value "player3"},
                                                                          :box4 {:type "set-variable", :var-name "player-selected" :var-value "player4"}}
                                                            :from-params [{:param-property "transition", :action-property "value"}]}
                                                           {:type        "set-attribute", :attr-value true :attr-name "highlight"
                                                            :from-params [{:param-property "transition", :action-property "target"}]}]},
                        :unhighlight
                                                   {:type "sequence-data",
                                                    :data [{:type        "test-var-scalar"
                                                            :success     {:type "set-variable", :var-name "player-selected" :var-value nil},
                                                            :var-name    "box-selected"
                                                            :from-params [{:param-property "transition", :action-property "value"}]}
                                                           {:type        "set-attribute", :attr-value false :attr-name "highlight"
                                                            :from-params [{:param-property "transition", :action-property "target"}]}]}

                        :animate-catch-player1
                                                   {:type "sequence-data",
                                                    :data
                                                          [{:id "volley_call", :type "animation", :target "vera"}
                                                           {:id "volley_idle", :loop true, :type "add-animation", :target "vera"}]},
                        :animate-catch-player2
                                                   {:type "sequence-data",
                                                    :data
                                                          [{:id "volley_call", :type "animation", :target "boy1"}
                                                           {:id "volley_idle", :loop true, :type "add-animation", :target "boy1"}]},
                        :animate-catch-player3
                                                   {:type "sequence-data",
                                                    :data
                                                          [{:id "volley_call", :type "animation", :target "girl2"}
                                                           {:id "volley_idle", :loop true, :type "add-animation", :target "girl2"}]},
                        :animate-catch-player4
                                                   {:type "sequence-data",
                                                    :data
                                                          [{:id "volley_call", :type "animation", :target "girl3"}
                                                           {:id "volley_idle", :loop true, :type "add-animation", :target "girl3"}]},
                        :animate-throw-player1
                                                   {:type "sequence-data",
                                                    :data
                                                          [{:id "volley_throw", :type "animation", :target "vera"}
                                                           {:id "idle", :loop true, :type "add-animation", :target "vera"}]},
                        :animate-throw-player2
                                                   {:type "sequence-data",
                                                    :data
                                                          [{:id "volley_throw", :type "animation", :target "boy1"}
                                                           {:id "idle", :loop true, :type "add-animation", :target "boy1"}]},
                        :animate-throw-player3
                                                   {:type "sequence-data",
                                                    :data
                                                          [{:id "volley_throw", :type "animation", :target "girl2"}
                                                           {:id "idle", :loop true, :type "add-animation", :target "girl2"}]},
                        :animate-throw-player4
                                                   {:type "sequence-data",
                                                    :data
                                                          [{:id "volley_throw", :type "animation", :target "girl3"}
                                                           {:id "idle", :loop true, :type "add-animation", :target "girl3"}]},
                        :click-box
                                                   {:type "sequence-data"
                                                    :data [{:type        "test-var-scalar",
                                                            :var-name    "current-target",
                                                            :success     {:type "copy-variable" :var-name "active-word" :from "current-concept"},
                                                            :fail        {:type "copy-variable" :var-name "active-word" :from "incorrect-concept"},
                                                            :from-params [{:param-property "player", :action-property "value" :template "player%"}]}
                                                           {:type        "copy-variable"
                                                            :var-name    "active-image-idx"
                                                            :from-params [{:param-property "player", :action-property "from" :template "image-idx-%"}]}
                                                           {:type     "action"
                                                            :from-var [{:var-name "active-image-idx", :action-property "id" :template "word-%-dialog"}]}]},
                        :finish-activity
                                                   {:type "sequence-data",
                                                    :data [{:type "action" :id "finish-activity-dialog"}
                                                           {:type "finish-activity"}]},
                        :init-first-player         {:type "set-variable", :var-name "current-player", :var-value "player1"},
                        :init-slots
                                                   {:type "parallel",
                                                    :data
                                                          [{:type "set-variable", :var-name "slot1", :var-value "player1"}
                                                           {:type "set-variable", :var-name "slot2", :var-value "player2"}
                                                           {:type "set-variable", :var-name "slot3", :var-value "player3"}
                                                           {:type "set-variable", :var-name "slot4", :var-value "player4"}]}
                        :init-rounds
                                                   {:type "sequence-data",
                                                    :data
                                                          [{:type      "set-variable-list"
                                                            :values    [1 2 3 4 5 6 7 8]
                                                            :var-names ["round-1" "round-2" "round-3" "round-4" "round-5" "round-6" "round-7" "round-8"]}
                                                           {:type "set-variable", :var-name "current-round", :var-value 0}]}

                        :pick-correct              {:type "sequence-data"
                                                    :data [{:type     "set-attribute", :attr-value false :attr-name "highlight"
                                                            :from-var [{:var-name "box-selected", :action-property "target"}]}
                                                           {:type "action" :id "return-ball"}
                                                           {:type "empty" :duration 400}
                                                           {:type     "pick-correct",
                                                            :activity "volleyball",
                                                            :from-var [{:var-name "current-concept", :var-property "concept-name", :action-property "concept-name"}]}
                                                           {:type "parallel",
                                                            :data
                                                                  [{:type "action" :id "correct-response-dialog"}
                                                                   {:id "throw", :type "action"}]}]}
                        :pick-wrong
                                                   {:type "sequence-data",
                                                    :data
                                                          [{:type     "set-attribute", :attr-value false :attr-name "highlight"
                                                            :from-var [{:var-name "box-selected", :action-property "target"}]}
                                                           {:type "action" :id "return-ball"}
                                                           {:type "action" :id "incorrect-response-dialog"}
                                                           {:type "copy-variable", :var-name "picked", :from-params [{:param-property "player", :action-property "from" :template "image-%"}]}
                                                           {:type     "pick-wrong",
                                                            :activity "volleyball",
                                                            :from-var
                                                                      [{:var-name "current-concept", :var-property "concept-name", :action-property "concept-name"}
                                                                       {:var-name "picked", :action-property "option"}]}]},
                        :return-ball               {:type     "case",
                                                    :options
                                                              {:player1 {:type "transition", :to {:x 1130, :y 908, :duration 0.4}, :transition-id "ball"}
                                                               :player2 {:type "transition", :to {:x 1507, :y 770, :duration 0.4}, :transition-id "ball"}
                                                               :player3 {:type "transition", :to {:x 281, :y 908, :duration 0.4}, :transition-id "ball"},
                                                               :player4 {:type "transition", :to {:x 595, :y 770, :duration 0.4}, :transition-id "ball"}}
                                                    :from-var [{:var-name "current-player", :action-property "value"}]}
                        :next-round                {:type    "test-value"
                                                    :success {:type "action" :id "finish-activity"}
                                                    :fail    {:type "sequence-data"
                                                              :data [{:type "action" :id "renew-current-concept"}
                                                                     {:type "action" :id "concept-dialog"}]}
                                                    :value2  8
                                                    :from-var
                                                             [{:var-name "current-round" :action-property "value1"}]}
                        :renew-current-concept
                                                   {:type "sequence-data",
                                                    :data
                                                          [{:type "counter", :counter-id "current-round", :counter-action "increase"}
                                                           {:type     "copy-variable",
                                                            :from-var [{:var-name "current-round", :action-property "from" :template "round-%"}],
                                                            :var-name "current-idx"}
                                                           {:type     "copy-variable",
                                                            :from-var [{:var-name "current-idx", :action-property "from" :template "item-%"}]
                                                            :var-name "current-concept"}
                                                           {:type     "copy-variable",
                                                            :from-var [{:var-name "current-idx", :action-property "from" :template "incorrect-%"}]
                                                            :var-name "incorrect-concept"}
                                                           {:type      "set-variable-list"
                                                            :values    [1 2 3 4 5 6]
                                                            :shuffled  true
                                                            :var-names ["image-idx-1" "image-idx-2" "image-idx-3" "image-idx-4" "image-idx-5" "image-idx-6"]}
                                                           {:from      ["slot1" "slot2" "slot3" "slot4"],
                                                            :type      "vars-var-provider",
                                                            :from-var  [{:var-name "current-player", :to-vector true, :action-property "exclude-values"}],
                                                            :shuffled  true,
                                                            :variables ["current-target"]}

                                                           {:type     "set-current-concept",
                                                            :from-var [{:var-name "current-concept", :var-property "concept-name", :action-property "value"}]}

                                                           {:type     "set-variable",
                                                            :var-name "image-1-property"
                                                            :from-var [{:var-name "image-idx-1", :action-property "var-value" :template "volleyball-image-%"}]}
                                                           {:type     "set-variable",
                                                            :var-name "image-2-property"
                                                            :from-var [{:var-name "image-idx-2", :action-property "var-value" :template "volleyball-image-%"}]}
                                                           {:type     "set-variable",
                                                            :var-name "image-3-property"
                                                            :from-var [{:var-name "image-idx-3", :action-property "var-value" :template "volleyball-image-%"}]}
                                                           {:type     "set-variable",
                                                            :var-name "image-4-property"
                                                            :from-var [{:var-name "image-idx-4", :action-property "var-value" :template "volleyball-image-%"}]}

                                                           {:type     "set-variable",
                                                            :var-name "image-1"
                                                            :from-var [{:var-name "incorrect-concept", :var-property-from-var "image-1-property", :action-property "var-value"}]}
                                                           {:type     "set-variable",
                                                            :var-name "image-2"
                                                            :from-var [{:var-name "incorrect-concept", :var-property-from-var "image-2-property", :action-property "var-value"}]}
                                                           {:type     "set-variable",
                                                            :var-name "image-3"
                                                            :from-var [{:var-name "incorrect-concept", :var-property-from-var "image-3-property", :action-property "var-value"}]}
                                                           {:type     "set-variable",
                                                            :var-name "image-4"
                                                            :from-var [{:var-name "incorrect-concept", :var-property-from-var "image-4-property", :action-property "var-value"}]}

                                                           {:type     "case",
                                                            :options
                                                                      {:player1 {:type     "set-variable",
                                                                                 :var-name "image-1"
                                                                                 :from-var [{:var-name "current-concept", :var-property-from-var "image-1-property", :action-property "var-value"}]},
                                                                       :player2 {:type     "set-variable",
                                                                                 :var-name "image-2"
                                                                                 :from-var [{:var-name "current-concept", :var-property-from-var "image-2-property", :action-property "var-value"}]}
                                                                       :player3 {:type     "set-variable",
                                                                                 :var-name "image-3"
                                                                                 :from-var [{:var-name "current-concept", :var-property-from-var "image-3-property", :action-property "var-value"}]},
                                                                       :player4 {:type     "set-variable",
                                                                                 :var-name "image-4"
                                                                                 :from-var [{:var-name "current-concept", :var-property-from-var "image-4-property", :action-property "var-value"}]}}
                                                            :from-var [{:var-name "current-target", :action-property "value"}]}

                                                           {:data
                                                                  [{:type       "set-slot",
                                                                    :target     "box1",
                                                                    :from-var   [{:var-name "image-1", :action-property "image"}],
                                                                    :slot-name  "box1",
                                                                    :attachment {:x 40, :scale-x 4, :scale-y 4}}
                                                                   {:type       "set-slot",
                                                                    :target     "box2",
                                                                    :from-var   [{:var-name "image-2", :action-property "image"}],
                                                                    :slot-name  "box1",
                                                                    :attachment {:x 40, :scale-x 4, :scale-y 4}}
                                                                   {:type       "set-slot",
                                                                    :target     "box3",
                                                                    :from-var   [{:var-name "image-3", :action-property "image"}],
                                                                    :slot-name  "box1",
                                                                    :attachment {:x 40, :scale-x 4, :scale-y 4}}
                                                                   {:type       "set-slot",
                                                                    :target     "box4",
                                                                    :from-var   [{:var-name "image-4", :action-property "image"}],
                                                                    :slot-name  "box1",
                                                                    :attachment {:x 40, :scale-x 4, :scale-y 4}}],
                                                            :type "parallel"}]},
                        :renew-words               {:type "sequence-data"
                                                    :data [{:type        "lesson-var-provider",
                                                            :from        "volleyball-concepts",
                                                            :limit       1,
                                                            :provider-id "words-set",
                                                            :repeat      4,
                                                            :variables   ["item-1" "item-2" "item-3" "item-4"]}
                                                           {:type        "lesson-var-provider",
                                                            :from        "volleyball-concepts",
                                                            :limit       1,
                                                            :provider-id "words-set",
                                                            :repeat      2,
                                                            :on-end      "renew-words-1"
                                                            :variables   ["item-5" "item-6"]}
                                                           {:type        "lesson-var-provider",
                                                            :from        "volleyball-concepts",
                                                            :provider-id "words-set",
                                                            :repeat      2,
                                                            :shuffled    true
                                                            :on-end      "renew-words-2"
                                                            :variables   ["item-7" "item-8"]}]}
                        :renew-words-1             {:type      "lesson-var-provider",
                                                    :from      "volleyball-concepts",
                                                    :repeat    4,
                                                    :variables ["item-5" "item-6" "item-7" "item-8"]}
                        :renew-words-2             {:type      "lesson-var-provider",
                                                    :from      "volleyball-concepts",
                                                    :repeat    2,
                                                    :variables ["item-7" "item-8"]}
                        :renew-incorrect-words     {:type "sequence-data"
                                                    :data [{:type      "lesson-var-provider",
                                                            :from      "volleyball-incorrect",
                                                            :shuffled  true
                                                            :variables ["incorrect-1"]
                                                            :from-var
                                                                       [{:var-key         "concept-name",
                                                                         :var-name        "item-1",
                                                                         :var-property    "concept-name",
                                                                         :action-property "exclude-property-values"}]}
                                                           {:type      "lesson-var-provider",
                                                            :from      "volleyball-incorrect",
                                                            :shuffled  true
                                                            :variables ["incorrect-2"]
                                                            :from-var
                                                                       [{:var-key         "concept-name",
                                                                         :var-name        "item-2",
                                                                         :var-property    "concept-name",
                                                                         :action-property "exclude-property-values"}]}
                                                           {:type      "lesson-var-provider",
                                                            :from      "volleyball-incorrect",
                                                            :shuffled  true
                                                            :variables ["incorrect-3"]
                                                            :from-var
                                                                       [{:var-key         "concept-name",
                                                                         :var-name        "item-3",
                                                                         :var-property    "concept-name",
                                                                         :action-property "exclude-property-values"}]}
                                                           {:type      "lesson-var-provider",
                                                            :from      "volleyball-incorrect",
                                                            :shuffled  true
                                                            :variables ["incorrect-4"]
                                                            :from-var
                                                                       [{:var-key         "concept-name",
                                                                         :var-name        "item-4",
                                                                         :var-property    "concept-name",
                                                                         :action-property "exclude-property-values"}]}
                                                           {:type      "lesson-var-provider",
                                                            :from      "volleyball-incorrect",
                                                            :shuffled  true
                                                            :variables ["incorrect-5"]
                                                            :from-var
                                                                       [{:var-key         "concept-name",
                                                                         :var-name        "item-5",
                                                                         :var-property    "concept-name",
                                                                         :action-property "exclude-property-values"}]}
                                                           {:type      "lesson-var-provider",
                                                            :from      "volleyball-incorrect",
                                                            :shuffled  true
                                                            :variables ["incorrect-6"]
                                                            :from-var
                                                                       [{:var-key         "concept-name",
                                                                         :var-name        "item-6",
                                                                         :var-property    "concept-name",
                                                                         :action-property "exclude-property-values"}]}
                                                           {:type      "lesson-var-provider",
                                                            :from      "volleyball-incorrect",
                                                            :shuffled  true
                                                            :variables ["incorrect-7"]
                                                            :from-var
                                                                       [{:var-key         "concept-name",
                                                                         :var-name        "item-7",
                                                                         :var-property    "concept-name",
                                                                         :action-property "exclude-property-values"}]}
                                                           {:type      "lesson-var-provider",
                                                            :from      "volleyball-incorrect",
                                                            :shuffled  true
                                                            :variables ["incorrect-8"]
                                                            :from-var
                                                                       [{:var-key         "concept-name",
                                                                         :var-name        "item-8",
                                                                         :var-property    "concept-name",
                                                                         :action-property "exclude-property-values"}]}]}

                        :start-activity            {:type "start-activity", :id "volleyball"},
                        :start-scene
                                                   {:type "sequence",
                                                    :data
                                                          ["start-activity"
                                                           "init-slots"
                                                           "init-first-player"
                                                           "init-rounds"
                                                           "renew-words"
                                                           "renew-incorrect-words"
                                                           "renew-current-concept"
                                                           "intro-dialog"
                                                           "concept-dialog"]},
                        :stop-activity             {:type "stop-activity", :id "volleyball"},
                        :throw
                                                   {:type "sequence-data",
                                                    :data
                                                          [{:type "action", :from-var [{:template "animate-throw-%", :var-name "current-player", :action-property "id"}]}
                                                           {:type "empty", :duration 200}
                                                           {:type "parallel"
                                                            :data [{:type "action", :from-var [{:template "throw-from-%", :var-name "current-player", :action-property "id"}]}
                                                                   {:type "sequence-data",
                                                                    :data [{:type "empty", :duration 900}
                                                                           {:type     "action",
                                                                            :from-var [{:template "animate-catch-%", :var-name "current-target", :action-property "id"}]}
                                                                           {:type "empty", :duration 800}]}],}
                                                           {:type     "set-variable",
                                                            :var-name "current-player",
                                                            :from-var [{:var-name "current-target", :action-property "var-value"}]}
                                                           {:type "action" :id "next-round"}]},
                        :throw-from-player1
                                                   {:type "action", :from-var [{:template "throw-player1-%", :var-name "current-target", :action-property "id"}]},
                        :throw-from-player2
                                                   {:type "action", :from-var [{:template "throw-player2-%", :var-name "current-target", :action-property "id"}]},
                        :throw-from-player3
                                                   {:type "action", :from-var [{:template "throw-player3-%", :var-name "current-target", :action-property "id"}]},
                        :throw-from-player4
                                                   {:type "action", :from-var [{:template "throw-player4-%", :var-name "current-target", :action-property "id"}]},
                        :throw-player1-player2
                                                   {:type          "transition",
                                                    :to            {:bezier [{:x 1316, :y 342} {:x 1390, :y 203} {:x 1507, :y 770}], :duration 1.4},
                                                    :transition-id "ball"},
                        :throw-player1-player3
                                                   {:type          "transition",
                                                    :to            {:bezier [{:x 825, :y 342} {:x 655, :y 342} {:x 281, :y 908}], :duration 1.4},
                                                    :transition-id "ball"},
                        :throw-player1-player4
                                                   {:type          "transition",
                                                    :to            {:bezier [{:x 951, :y 342} {:x 843, :y 203} {:x 595, :y 770}], :duration 1.4},
                                                    :transition-id "ball"},
                        :throw-player2-player1
                                                   {:type          "transition",
                                                    :to            {:bezier [{:x 1390, :y 203} {:x 1316, :y 342} {:x 1130, :y 908}], :duration 1.4},
                                                    :transition-id "ball"},
                        :throw-player2-player3
                                                   {:type          "transition",
                                                    :to            {:bezier [{:x 1050, :y 203} {:x 804, :y 342} {:x 281, :y 908}], :duration 1.4},
                                                    :transition-id "ball"},
                        :throw-player2-player4
                                                   {:type          "transition",
                                                    :to            {:bezier [{:x 1175, :y 203} {:x 993, :y 203} {:x 595, :y 770}], :duration 1.4},
                                                    :transition-id "ball"},
                        :throw-player3-player1
                                                   {:type          "transition",
                                                    :to            {:bezier [{:x 655, :y 342} {:x 825, :y 342} {:x 1130, :y 908}], :duration 1.4},
                                                    :transition-id "ball"},
                        :throw-player3-player2
                                                   {:type          "transition",
                                                    :to            {:bezier [{:x 804, :y 342} {:x 1050, :y 203} {:x 1507, :y 770}], :duration 1.4},
                                                    :transition-id "ball"},
                        :throw-player3-player4
                                                   {:type          "transition",
                                                    :to            {:bezier [{:x 440, :y 342} {:x 502, :y 203} {:x 595, :y 770}], :duration 1.4},
                                                    :transition-id "ball"},
                        :throw-player4-player1
                                                   {:type          "transition",
                                                    :to            {:bezier [{:x 843, :y 203} {:x 951, :y 342} {:x 1130, :y 908}], :duration 1.4},
                                                    :transition-id "ball"},
                        :throw-player4-player2
                                                   {:type          "transition",
                                                    :to            {:bezier [{:x 993, :y 203} {:x 1175, :y 203} {:x 1507, :y 770}], :duration 1.4},
                                                    :transition-id "ball"},
                        :throw-player4-player3
                                                   {:type          "transition",
                                                    :to            {:bezier [{:x 502, :y 203} {:x 440, :y 342} {:x 281, :y 908}], :duration 1.4},
                                                    :transition-id "ball"},
                        :wait-for-box-animations   {:type "empty", :duration 500}
                        :intro-dialog              (-> (dialog/default "Intro"))
                        :concept-dialog            (-> (dialog/default "Concept word")
                                                       (assoc :concept-var "current-concept"))
                        :tap-instructions-dialog   (-> (dialog/default "Tap instructions")
                                                       (assoc :concept-var "current-concept"))
                        :correct-response-dialog   (-> (dialog/default "Correct response")
                                                       (assoc :concept-var "current-concept"))
                        :incorrect-response-dialog (-> (dialog/default "Incorrect response")
                                                       (assoc :concept-var "current-concept"))
                        :word-1-dialog             (-> (dialog/default "Word 1")
                                                       (assoc :concept-var "active-word"))
                        :word-2-dialog             (-> (dialog/default "Word 2")
                                                       (assoc :concept-var "active-word"))
                        :word-3-dialog             (-> (dialog/default "Word 3")
                                                       (assoc :concept-var "active-word"))
                        :word-4-dialog             (-> (dialog/default "Word 4")
                                                       (assoc :concept-var "active-word"))
                        :word-5-dialog             (-> (dialog/default "Word 5")
                                                       (assoc :concept-var "active-word"))
                        :word-6-dialog             (-> (dialog/default "Word 6")
                                                       (assoc :concept-var "active-word"))
                        :finish-activity-dialog    (-> (dialog/default "Finish activity"))},
        :triggers      {:back {:on "back", :action "stop-activity"}, :start {:on "start", :action "start-scene"}},
        :metadata      {:prev   "stadium", :autostart true
                        :tracks [{:title "Main Script"
                                  :nodes [{:type      "dialog"
                                           :action-id :intro-dialog}
                                          {:type "prompt"
                                           :text "Guide gives instructions. New round begins"}
                                          {:type      "dialog"
                                           :action-id :concept-dialog}
                                          {:type "prompt"
                                           :text "Student drags the ball to correct image"}
                                          {:type      "dialog"
                                           :action-id :correct-response-dialog}
                                          {:type      "dialog"
                                           :action-id :incorrect-response-dialog}
                                          {:type      "dialog"
                                           :action-id :tap-instructions-dialog}
                                          {:type "prompt"
                                           :text "After the last round activity finishes with a dialog"}
                                          {:type      "dialog"
                                           :action-id :finish-activity-dialog}]}
                                 {:title "Words"
                                  :nodes [{:type      "dialog"
                                           :action-id :word-1-dialog}
                                          {:type      "dialog"
                                           :action-id :word-2-dialog}
                                          {:type      "dialog"
                                           :action-id :word-3-dialog}
                                          {:type      "dialog"
                                           :action-id :word-4-dialog}
                                          {:type      "dialog"
                                           :action-id :word-5-dialog}
                                          {:type      "dialog"
                                           :action-id :word-6-dialog}]}]}})

(defn f
  [t args]
  t)

(core/register-template
  m
  (partial f t))
