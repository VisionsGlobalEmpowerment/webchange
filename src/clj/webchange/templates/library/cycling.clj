(ns webchange.templates.library.cycling
  (:require
    [webchange.templates.core :as core]))

(def m {:id          2
        :name        "cycling"
        :tags        ["Independent Practice"]
        :description "Some description of cycling mechanics and covered skills"
        :lesson-sets ["concepts"]
        :fields [{:name "image-src",
                  :type "image"}
                 {:name "word-image-1",
                  :type "image"}
                 {:name "word-image-2",
                  :type "image"}
                 {:name "word-image-3",
                  :type "image"}
                 {:name "word-image-4",
                  :type "image"}]
        :options     {:rounds {:label   "Number of rounds"
                               :type    "lookup"
                               :options [{:name "1" :value 1}
                                         {:name "2" :value 2}
                                         {:name "3" :value 3}]}
                      }})

(def t {:assets        [{:url "/raw/img/stadium/cycling/cycle_race_bg_01.jpg", :size 10, :type "image"}
                        {:url "/raw/img/stadium/cycling/cycle_race_bg_02.jpg", :size 10, :type "image"}
                        {:url "/raw/img/stadium/cycling/cycle_race_bg_03.jpg", :size 10, :type "image"}]
        :objects       {:background {:type  "carousel" :x 0 :y 0 :width 1920 :height 1080,
                                     :first "/raw/img/stadium/cycling/cycle_race_bg_01.jpg"
                                     :last  "/raw/img/stadium/cycling/cycle_race_bg_03.jpg"
                                     :next  "/raw/img/stadium/cycling/cycle_race_bg_02.jpg"}
                        :vera       {:type       "animation",
                                     :x          500,
                                     :y          721,
                                     :width      727,
                                     :height     1091,
                                     :scale      {:x 0.4, :y 0.4},
                                     :scene-name "vera",
                                     :transition "vera",
                                     :anim       "bike_ride",
                                     :meshes     true,
                                     :name       "vera-90",
                                     :skin       "default",
                                     :speed      0.3,
                                     :start      true}
                        :box1
                                    {:type        "animation",
                                     :x           2000,
                                     :y           683,
                                     :width       671,
                                     :height      633,
                                     :scale       {:x -0.25, :y 0.25},
                                     :scene-name  "box1",
                                     :transition  "box1",
                                     :actions     {:click {:id "pick-box-1", :on "click", :type "action" :unique-tag "box"}},
                                     :anim        "idle2",
                                     :anim-offset {:x 0, :y -300},
                                     :loop        true,
                                     :name        "boxes",
                                     :skin        "qwestion",
                                     :speed       0.3,
                                     :start       true,
                                     :states      {:init {:visible false}, :reset {:visible true}}},
                        :box2
                                    {:type        "animation",
                                     :x           2200,
                                     :y           789,
                                     :width       671,
                                     :height      633,
                                     :scale       {:x -0.25, :y 0.25},
                                     :scene-name  "box2",
                                     :transition  "box2",
                                     :actions     {:click {:id "pick-box-2", :on "click", :type "action" :unique-tag "box"}},
                                     :anim        "idle2",
                                     :anim-offset {:x 0, :y -300},
                                     :loop        true,
                                     :name        "boxes",
                                     :skin        "qwestion",
                                     :speed       0.3,
                                     :start       true,
                                     :states      {:init {:visible false}, :reset {:visible true}}},
                        :box3
                                    {:type        "animation",
                                     :x           2400,
                                     :y           932,
                                     :width       671,
                                     :height      633,
                                     :scale       {:x -0.25, :y 0.25},
                                     :scene-name  "box3",
                                     :transition  "box3",
                                     :actions     {:click {:id "pick-box-3", :on "click", :type "action" :unique-tag "box"}},
                                     :anim        "idle2",
                                     :anim-offset {:x 0, :y -300},
                                     :loop        true,
                                     :name        "boxes",
                                     :skin        "qwestion",
                                     :speed       0.3,
                                     :start       true,
                                     :states      {:init {:visible false}, :reset {:visible true}}},
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
                                     :start       true},}
        :scene-objects [["background"] ["box1" "box2" "box3" "vera" "mari"]],
        :actions       {:dialog-1-before-start   {:type               "sequence-data",
                                                  :editor-type        "dialog",
                                                  :concept-var        "current-concept",
                                                  :data               [{:type "sequence-data"
                                                                        :data [{:type "empty" :duration 0}
                                                                               {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                  :phrase             "before-boxes",
                                                  :phrase-description "Before start",
                                                  :dialog-track       "1 Intro"}
                        :dialog-2-boxes          {:type               "sequence-data",
                                                  :editor-type        "dialog",
                                                  :concept-var        "current-word",
                                                  :data               [{:type "sequence-data"
                                                                        :data [{:type "empty" :duration 0}
                                                                               {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                  :phrase             "after-boxes",
                                                  :phrase-description "After boxes appear",
                                                  :dialog-track       "2 Concept"}
                        :dialog-3-pick-correct   {:type               "sequence-data",
                                                  :editor-type        "dialog",
                                                  :concept-var        "current-word",
                                                  :data               [{:type "sequence-data"
                                                                        :data [{:type "empty" :duration 0}
                                                                               {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                  :phrase             "pick-correct",
                                                  :phrase-description "Pick correct",
                                                  :dialog-track       "3 Correct"}
                        :dialog-4-pick-wrong     {:type               "sequence-data",
                                                  :editor-type        "dialog",
                                                  :concept-var        "current-word",
                                                  :data               [{:type "sequence-data"
                                                                        :data [{:type "empty" :duration 0}
                                                                               {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                  :phrase             "pick-wrong",
                                                  :phrase-description "Pick wrong",
                                                  :dialog-track       "4 Wrong"}
                        :provide-lesson-concepts {:type        "lesson-var-provider",
                                                  :from        "concepts",
                                                  :limit       3,
                                                  :provider-id "words-set",
                                                  :repeat      4,
                                                  :shuffled    true,
                                                  :variables   nil}
                        :provide-current-concept {:from        nil,
                                                  :type        "vars-var-provider",
                                                  :on-end      "finish-activity",
                                                  :shuffled    false,
                                                  :variables   ["current-concept"],
                                                  :provider-id "current-concept"}
                        :intro
                                                 {:type "sequence-data",
                                                  :data
                                                        [{:type "start-activity", :id "cycling"}
                                                         {:type "parallel",
                                                          :data
                                                                [{:type "set-variable", :var-name "slot1", :var-value "box1"}
                                                                 {:type "set-variable", :var-name "slot2", :var-value "box2"}
                                                                 {:type "set-variable", :var-name "slot3", :var-value "box3"}]}
                                                         {:type "set-variable", :var-name "current-line", :var-value "box2"},
                                                         {:type "action" :id "provide-lesson-concepts"}
                                                         {:type "action" :id "dialog-1-before-start"}
                                                         {:type "action" :id "renew-current-concept"}
                                                         {:type "action" :id "dialog-2-boxes"}]}

                        :renew-current-concept   {:type "sequence-data",
                                                  :data
                                                        [{:id "init", :type "state", :target "box1"}
                                                         {:id "init", :type "state", :target "box2"}
                                                         {:id "init", :type "state", :target "box3"}
                                                         {:id "wait-for-box-animations", :type "action"}
                                                         {:data
                                                                [{:to {:x 2000, :duration 0.1}, :type "transition", :transition-id "box1"}
                                                                 {:to {:x 2200, :duration 0.1}, :type "transition", :transition-id "box2"}
                                                                 {:to {:x 2400, :duration 0.1}, :type "transition", :transition-id "box3"}],
                                                          :type "parallel"}
                                                         {:id "reset", :type "state", :target "box1"}
                                                         {:id "reset", :type "state", :target "box2"}
                                                         {:id "reset", :type "state", :target "box3"}
                                                         {:id "wait-for-box-animations", :type "action"}

                                                         {:type "action" :id "provide-current-concept"}
                                                         {:data
                                                                [{:type     "set-variable",
                                                                  :from-var [{:var-name "current-concept", :var-property "word-image-1", :action-property "var-value"}],
                                                                  :var-name "word1"}
                                                                 {:type     "set-variable",
                                                                  :from-var [{:var-name "current-concept", :var-property "word-image-2", :action-property "var-value"}],
                                                                  :var-name "word2"}
                                                                 {:type     "set-variable",
                                                                  :from-var [{:var-name "current-concept", :var-property "word-image-3", :action-property "var-value"}],
                                                                  :var-name "word3"}
                                                                 {:type     "set-variable",
                                                                  :from-var [{:var-name "current-concept", :var-property "word-image-4", :action-property "var-value"}],
                                                                  :var-name "word4"}],
                                                          :type "parallel"}
                                                         {:from      ["word1" "word2" "word3" "word4"],
                                                          :type      "vars-var-provider",
                                                          :shuffled  true,
                                                          :variables ["box1" "box2" "box3"]}
                                                         {:from ["slot1" "slot2" "slot3"], :type "vars-var-provider", :shuffled true, :variables ["current-target"]}
                                                         {:type "set-variable",
                                                          :from-var
                                                                [{:var-name "current-concept", :var-property "image-src", :action-property "var-value"}
                                                                 {:var-name "current-target", :action-property "var-name"}]}

                                                         {:data
                                                                [{:type       "set-slot",
                                                                  :target     "box1",
                                                                  :from-var   [{:var-name "box1", :action-property "image"}],
                                                                  :slot-name  "box1",
                                                                  :attachment {:x 40, :scale-x -4, :scale-y 4}}
                                                                 {:type       "set-slot",
                                                                  :target     "box2",
                                                                  :from-var   [{:var-name "box2", :action-property "image"}],
                                                                  :slot-name  "box1",
                                                                  :attachment {:x 40, :scale-x -4, :scale-y 4}}
                                                                 {:type       "set-slot",
                                                                  :target     "box3",
                                                                  :from-var   [{:var-name "box3", :action-property "image"}],
                                                                  :slot-name  "box1",
                                                                  :attachment {:x 40, :scale-x -4, :scale-y 4}}],
                                                          :type "parallel"}
                                                         {:data
                                                                [{:to {:x 1300, :duration 2}, :type "transition", :transition-id "box1"}
                                                                 {:to {:x 1500, :duration 2}, :type "transition", :transition-id "box2"}
                                                                 {:to {:x 1700, :duration 2}, :type "transition", :transition-id "box3"}],
                                                          :type "parallel"}]}

                        :pick-box-1
                                                 {:type "sequence-data",
                                                  :data
                                                        [{:type "set-variable", :var-name "current-line-pos", :var-value {:y 621, :duration 0.5}}
                                                         {:type "set-variable", :var-name "current-line-jump", :var-value {:y 421, :duration 1}}
                                                         {:type "set-variable", :var-name "jump-wait", :var-value 500}
                                                         {:fail     {:id "bike_turnup", :type "animation", :target "vera"}
                                                          :type     "test-value",
                                                          :value1   "box1",
                                                          :success  {:type "empty" :duration 100}
                                                          :from-var [{:var-name "current-line", :action-property "value2"}]}
                                                         {:id "bike_ride", :loop true, :type "add-animation", :target "vera"}
                                                         {:type "set-variable", :var-name "current-line", :var-value "box1"}
                                                         {:type "transition", :from-var [{:var-name "current-line-pos", :action-property "to"}], :transition-id "vera"}
                                                         {:fail     "pick-wrong",
                                                          :type     "test-value",
                                                          :value1   "box1",
                                                          :success  "pick-correct",
                                                          :from-var [{:var-name "current-target", :action-property "value2"}]}]},
                        :pick-box-2
                                                 {:type "sequence-data",
                                                  :data
                                                        [{:type "set-variable", :var-name "current-line-pos", :var-value {:y 721, :duration 0.5}}
                                                         {:type "set-variable", :var-name "current-line-jump", :var-value {:y 521, :duration 1}}
                                                         {:type "set-variable", :var-name "jump-wait", :var-value 800}
                                                         {:type "case",
                                                          :options
                                                                {:box1     {:id "bike_turndown", :type "animation", :target "vera"},
                                                                 :box2     {:type "empty" :duration 100}
                                                                 :box3     {:id "bike_turnup", :type "animation", :target "vera"},
                                                                 :from-var [{:var-name "current-line", :action-property "value"}]}}
                                                         {:id "bike_ride", :loop true, :type "add-animation", :target "vera"}
                                                         {:type "set-variable", :var-name "current-line", :var-value "box2"}
                                                         {:type "transition", :from-var [{:var-name "current-line-pos", :action-property "to"}], :transition-id "vera"}
                                                         {:fail     "pick-wrong",
                                                          :type     "test-value",
                                                          :value1   "box2",
                                                          :success  "pick-correct",
                                                          :from-var [{:var-name "current-target", :action-property "value2"}]}]},
                        :pick-box-3
                                                 {:type "sequence-data",
                                                  :data
                                                        [{:type "set-variable", :var-name "current-line-pos", :var-value {:y 821, :duration 0.5}}
                                                         {:type "set-variable", :var-name "current-line-jump", :var-value {:y 621, :duration 1}}
                                                         {:type "set-variable", :var-name "jump-wait", :var-value 1200}
                                                         {:fail     {:id "bike_turndown", :type "animation", :target "vera"},
                                                          :type     "test-value",
                                                          :value1   "box3",
                                                          :success  {:type "empty" :duration 100},
                                                          :from-var [{:var-name "current-line", :action-property "value2"}]}
                                                         {:id "bike_ride", :loop true, :type "add-animation", :target "vera"}
                                                         {:type "set-variable", :var-name "current-line", :var-value "box3"}
                                                         {:type "transition", :from-var [{:var-name "current-line-pos", :action-property "to"}], :transition-id "vera"}
                                                         {:fail     "pick-wrong",
                                                          :type     "test-value",
                                                          :value1   "box3",
                                                          :success  "pick-correct",
                                                          :from-var [{:var-name "current-target", :action-property "value2"}]}]},
                        :pick-correct
                                                 {:type "sequence-data",
                                                  :data
                                                        [{:data
                                                                [{:id "dialog-3-pick-correct", :type "action"}
                                                                 {:to {:x -700, :duration 4}, :type "transition", :transition-id "box1"}
                                                                 {:to {:x -500, :duration 4}, :type "transition", :transition-id "box2"}
                                                                 {:to {:x -300, :duration 4}, :type "transition", :transition-id "box3"}
                                                                 {:data
                                                                        [{:type "empty", :from-var [{:var-name "jump-wait", :action-property "duration"}]}
                                                                         {:id "bike_jump", :type "animation", :target "vera"}
                                                                         {:type "transition", :from-var [{:var-name "current-line-jump", :action-property "to"}], :transition-id "vera"}
                                                                         {:id "bike_ride", :loop true, :type "add-animation", :target "vera"}
                                                                         {:type "transition", :from-var [{:var-name "current-line-pos", :action-property "to"}], :transition-id "vera"}],
                                                                  :type "sequence-data"}],
                                                          :type "parallel"}
                                                         {:id "renew-current-concept", :type "action"}
                                                         {:type "remove-flow-tag" :tag "box"}
                                                         {:id "dialog-2-boxes", :type "action"}]},
                        :pick-wrong              {:type "sequence-data", :data [{:id "dialog-4-pick-wrong", :type "action"}
                                                                                {:type "remove-flow-tag" :tag "box"}
                                                                                {:id "dialog-2-boxes", :type "action"}]}}
        :triggers
                       {:start {:on "start", :action "intro"}}})

(defn- config-rounds
  [template n]
  (let [items (->> (range 1 (inc n))
                   (map #(str "item-" %))
                   (into []))]
    (-> template
        (assoc-in [:objects :provide-lesson-concepts :variables] items)
        (assoc-in [:objects :provide-current-concept :from] items))))

(defn f
  [t args]
  (-> t
      (config-rounds (:rounds args))))

(core/register-template
  (:id m)
  m
  (partial f t))

