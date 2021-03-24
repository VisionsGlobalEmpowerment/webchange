(ns webchange.templates.library.writing-practice
  (:require
    [webchange.templates.core :as core]
    [webchange.templates.utils.dialog :as dialog]))

(def m {:id          40
        :name        "Writing practice"
        :tags        ["Guided Practice"]
        :lesson-sets ["concepts-single"]
        :fields      [{:name "image-src",
                       :type "image"}
                      {:name "letter-src",
                       :type "image"}
                      {:name "letter-path"
                       :type "string"}
                      {:name "letter"
                       :type "string"}]
        :description "Users will practice writing letters themselves."})

(def t {:assets
                  [{:url "/raw/img/library/painting-tablet/background.jpg", :type "image"}
                   {:url "/raw/img/ui/back_button_01.png", :type "image"}
                   {:url "/raw/img/library/painting-tablet/brush.png", :size 10, :type "image"}
                   {:url "/raw/img/library/painting-tablet/felt-tip.png", :size 10, :type "image"}
                   {:url "/raw/img/library/painting-tablet/pencil.png", :size 10, :type "image"}
                   {:url "/raw/img/library/painting-tablet/eraser.png", :size 10, :type "image"}
                   {:url "/raw/img/letters/a.png" :size 1 :type "image"}],
        :objects
                  {:background {:type "background", :scene-name "background", :src "/raw/img/library/painting-tablet/background.jpg"},

                   :letter-tutorial-image
                               {:type "image", :x 1065, :y 75, :width 651, :height 651, :rotation 0, :scale-x 0.45, :scale-y 0.45},
                   :letter-tutorial-path
                               {:type         "animated-svg-path",
                                :x            600,
                                :y            25,
                                :width        225,
                                :height       225,
                                :duration     5000,
                                :fill         "transparent",
                                :line-cap     "round",
                                :path         "",
                                :scale-x      1.8,
                                :scale-y      1.8,
                                :states       {:hidden {:visible false}, :visible {:visible true}},
                                :stroke       "#323232",
                                :stroke-width 10},
                   :letter-tutorial-trace
                               {:type         "svg-path",
                                :x            600,
                                :y            25,
                                :width        225,
                                :height       225,
                                :scene-name   "letter-tutorial-trace",
                                :dash         [7 7],
                                :data         "",
                                :line-cap     "round",
                                :rotation     0,
                                :scale-x      1.8,
                                :scale-y      1.8,
                                :states       {:hidden {:visible false}, :visible {:visible true}},
                                :stroke       "#898989",
                                :stroke-width 4},
                   :text-tracing-pattern
                               {:type        "text-tracing-pattern"
                                :traceable   true
                                :repeat-text 3
                                :text        " "
                                :actions     {:next-letter {:on "next-letter-activated" :type "action" :id "letter-finished-dialog"}
                                              :finish      {:on "finish" :type "action" :id "text-finished"}
                                              :click       {:on "click" :type "action" :id "timeout-timer"}}}

                   :practice-canvas
                               {:type    "painting-area"
                                :tool "felt-tip"
                                :color "#4479bb"
                                :visible false
                                :change  {:on "click" :type "action" :id "timeout-timer"}}
                   :painting-toolset
                               {:type       "painting-toolset"
                                :transition "painting-toolset"
                                :actions    {:change {:on "change" :type "action" :id "set-current-tool" :pick-event-param "tool"}}
                                :visible    false}
                   :colors-palette
                               {:type       "colors-palette",
                                :transition "colors-palette"
                                :actions    {:change {:on "change" :type "action", :id "set-current-color" :pick-event-param "color"}}
                                :visible    false}
                   :mari
                               {:type       "animation",
                                :x          1600,
                                :y          225,
                                :width      473,
                                :height     511,
                                :scene-name "mari",
                                :transition "mari",
                                :anim       "idle",
                                :name       "mari",
                                :scale-x    0.5,
                                :scale-y    0.5,
                                :speed      0.35,
                                :start      true
                                :actions    {:click {:on "click" :type "action" :id "tap-instructions"}}},
                   :next-button
                               {:type     "image",
                                :x        1800,
                                :y        915,
                                :width    97,
                                :height   99,
                                :actions  {:click {:id "finish-activity", :on "click", :type "action"}},
                                :rotation 0,
                                :scale-x  -1,
                                :scale-y  1,
                                :src      "/raw/img/ui/back_button_01.png"
                                :visible  false}},
        :scene-objects
                  [["background"]
                   ["letter-tutorial-trace"
                    "letter-tutorial-path"
                    "letter-tutorial-image"

                    "text-tracing-pattern"

                    "practice-canvas"
                    "painting-toolset"
                    "colors-palette"

                    "next-button"
                    "mari"]],
        :actions
                  {:start-scene                 {:type "sequence-data",
                                                 :data [{:type "start-activity"}
                                                        {:type "lesson-var-provider", :from "concepts-single", :provider-id "concepts", :variables ["current-concept"]}

                                                        {:type      "set-attribute",
                                                         :target    "letter-tutorial-trace",
                                                         :from-var  [{:var-name "current-concept", :action-property "attr-value" :var-property "letter-path"}],
                                                         :attr-name "data"}
                                                        {:type      "set-attribute",
                                                         :target    "letter-tutorial-path",
                                                         :from-var  [{:var-name "current-concept", :action-property "attr-value" :var-property "letter-path"}],
                                                         :attr-name "path"}
                                                        {:type      "set-attribute",
                                                         :target    "letter-tutorial-image",
                                                         :from-var  [{:var-name "current-concept", :action-property "attr-value", :var-property "letter-src"}],
                                                         :attr-name "src"}
                                                        {:type      "set-attribute",
                                                         :target    "text-tracing-pattern",
                                                         :from-var  [{:var-name "current-concept", :action-property "attr-value" :var-property "letter"}],
                                                         :attr-name "text"}
                                                        {:type "add-animation", :id "wand_idle", :target "mari", :track 2, :loop true},
                                                        {:type "action" :id "dialog-instructions"}
                                                        {:type "action" :id "timeout-timer"}]},
                   :stop-activity               {:type "sequence-data"
                                                 :data [{:type "remove-interval" :id "instructions-timeout"}
                                                        {:type "stop-activity"}]}
                   :finish-activity             {:type "sequence-data"
                                                 :data [{:type "remove-interval" :id "instructions-timeout"}
                                                        {:type "finish-activity"}]},
                   :dialog-instructions         (-> (dialog/default "Instructions")
                                                    (assoc :available-activities ["show-example"]))
                   :dialog-instructions-second-stage
                                                (-> (dialog/default "Instructions second stage")
                                                    (assoc :available-activities ["highlight-tools" "highlight-colors" "highlight-next"]))
                   :show-example                {:type "sequence-data"
                                                 :data [{:to {:x 1080, :y 360, :loop false, :duration 1.5}, :type "transition", :transition-id "mari"}
                                                        {:id "visible", :type "state", :target "letter-tutorial-path"}
                                                        {:data [{:type "path-animation", :state "play", :target "letter-tutorial-path"}
                                                                {:to            {:path "", :scale {:x 1.8, :y 1.8}, :origin {:x 780, :y 60}, :duration 5},
                                                                 :type          "transition",
                                                                 :from-var      [{:var-name "current-concept", :action-property "to.path" :var-property "letter-path"}],
                                                                 :transition-id "mari"}],
                                                         :type "parallel"}
                                                        {:to {:x 1490, :y 180, :loop false, :duration 1.5}, :type "transition", :transition-id "mari"}]}

                   :highlight-tools             {:type               "transition"
                                                 :transition-id      "painting-toolset"
                                                 :return-immediately true
                                                 :from               {:brightness 0},
                                                 :to                 {:brightness 0.35 :yoyo true :duration 0.5}
                                                 :kill-after         3000}
                   :highlight-colors            {:type               "transition"
                                                 :transition-id      "colors-palette"
                                                 :return-immediately true
                                                 :from               {:brightness 0},
                                                 :to                 {:brightness 0.35 :yoyo true :duration 0.5}
                                                 :kill-after         3000}
                   :highlight-next              {:type               "transition"
                                                 :transition-id      "next-button"
                                                 :return-immediately true
                                                 :from               {:brightness 0},
                                                 :to                 {:brightness 0.35 :yoyo true :duration 0.5}
                                                 :kill-after         3000}
                   :set-current-tool            {:type "sequence-data"
                                                 :data [{:type        "set-attribute",
                                                         :target      "practice-canvas"
                                                         :attr-name   "tool"
                                                         :from-params [{:param-property "tool", :action-property "attr-value"}]}
                                                        {:type        "action"
                                                         :from-params [{:param-property "tool", :action-property "id" :template "dialog-tool-%"}]}]}
                   :set-current-color           {:type "sequence-data"
                                                 :data [{:type        "set-attribute",
                                                         :target      "practice-canvas"
                                                         :attr-name   "color"
                                                         :from-params [{:param-property "color", :action-property "attr-value"}]}
                                                        {:type        "action"
                                                         :from-params [{:param-property "color", :action-property "id" :template "dialog-color-%"}]}]}
                   :dialog-tool-brush           (dialog/default "tool brush")
                   :dialog-tool-felt-tip        (dialog/default "tool felt-tip")
                   :dialog-tool-pencil          (dialog/default "tool pencil")
                   :dialog-tool-eraser          (dialog/default "tool eraser")
                   :dialog-color-4487611        (dialog/default "color blue")
                   :dialog-color-9616714        (dialog/default "color green")
                   :dialog-color-15569322       (dialog/default "color pink")
                   :dialog-color-16631089       (dialog/default "color yellow")
                   :dialog-color-65793          (dialog/default "color black")
                   :letter-finished-dialog      (dialog/default "letter finished")
                   :text-finished               {:type "sequence-data"
                                                 :data [{:type "remove-interval" :id "instructions-timeout"}
                                                        {:type "action" :id "letter-finished-dialog"}
                                                        {:type "action" :id "text-finished-dialog"}
                                                        {:type "set-attribute" :target "letter-tutorial-image" :attr-name "visible" :attr-value false}
                                                        {:type "set-attribute" :target "letter-tutorial-path" :attr-name "visible" :attr-value false}
                                                        {:type "set-attribute" :target "letter-tutorial-trace" :attr-name "visible" :attr-value false}
                                                        {:type "set-attribute" :target "text-tracing-pattern" :attr-name "visible" :attr-value false}

                                                        {:type "set-attribute" :target "practice-canvas" :attr-name "visible" :attr-value true}
                                                        {:type "set-attribute" :target "painting-toolset" :attr-name "visible" :attr-value true}
                                                        {:type "set-attribute" :target "colors-palette" :attr-name "visible" :attr-value true}

                                                        {:type "set-variable" :var-name "second-stage" :var-value true}
                                                        {:type "action" :id "dialog-instructions-second-stage"}
                                                        {:type "action" :id "timeout-timer"}]}
                   :text-finished-dialog        (dialog/default "text finished")
                   :timeout-timer               {:type     "set-interval",
                                                 :id       "instructions-timeout",
                                                 :action   "timeout-instructions",
                                                 :interval 15000}

                   :timeout-instructions        {:type     "test-value",
                                                 :value2   true,
                                                 :success  {:type "action" :id "dialog-timeout-instructions-second-stage"},
                                                 :fail     {:type "action" :id "dialog-timeout-instructions"},
                                                 :from-var [{:var-name "second-stage", :action-property "value1"}]}
                   :tap-instructions            {:type     "test-value",
                                                 :value2   true,
                                                 :success  {:type "action" :id "dialog-tap-instructions-second-stage"},
                                                 :fail     {:type "action" :id "dialog-tap-instructions"},
                                                 :from-var [{:var-name "second-stage", :action-property "value1"}]}

                   :dialog-timeout-instructions (-> (dialog/default "Timeout instructions")
                                                    (assoc :available-activities ["show-example"]))
                   :dialog-timeout-instructions-second-stage
                                                (-> (dialog/default "Timeout instructions second stage")
                                                    (assoc :available-activities ["highlight-tools" "highlight-colors" "highlight-next"]))

                   :dialog-tap-instructions     (-> (dialog/default "Tap instructions")
                                                    (assoc :available-activities ["show-example"]))
                   :dialog-tap-instructions-second-stage
                                                (-> (dialog/default "Tap instructions second stage")
                                                    (assoc :available-activities ["highlight-tools" "highlight-colors" "highlight-next"]))
                   },
        :triggers {:stop {:on "back", :action "stop-activity"}, :start {:on "start", :action "start-scene"}},
        :metadata {:prev   "library", :autostart true
                   :tracks [{:title "1 Instructions stage 1"
                             :nodes [{:type      "dialog"
                                      :action-id :dialog-instructions}
                                     {:type      "dialog"
                                      :action-id :dialog-timeout-instructions}
                                     {:type      "dialog"
                                      :action-id :dialog-tap-instructions}]}
                            {:title "2 Instructions stage 2"
                             :nodes [{:type      "dialog"
                                      :action-id :dialog-instructions-second-stage}
                                     {:type      "dialog"
                                      :action-id :dialog-timeout-instructions-second-stage}
                                     {:type      "dialog"
                                      :action-id :dialog-tap-instructions-second-stage}]}
                            {:title "3 Colors"
                             :nodes [{:type      "dialog"
                                      :action-id :dialog-color-4487611}
                                     {:type      "dialog"
                                      :action-id :dialog-color-9616714}
                                     {:type      "dialog"
                                      :action-id :dialog-color-15569322}
                                     {:type      "dialog"
                                      :action-id :dialog-color-16631089}
                                     {:type      "dialog"
                                      :action-id :dialog-color-65793}]}
                            {:title "4 Tools"
                             :nodes [{:type      "dialog"
                                      :action-id :dialog-tool-brush}
                                     {:type      "dialog"
                                      :action-id :dialog-tool-felt-tip}
                                     {:type      "dialog"
                                      :action-id :dialog-tool-pencil}
                                     {:type      "dialog"
                                      :action-id :dialog-tool-eraser}]}]}})

(defn f
  [t args]
  t)

(core/register-template
  m
  (partial f t))
