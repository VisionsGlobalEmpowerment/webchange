(ns webchange.templates.library.writing-1
  (:require
    [webchange.templates.core :as core]
    [webchange.templates.utils.dialog :as dialog]))

(def m {:id          41
        :name        "Writing 1"
        :tags        ["Guided Practice"]
        :description "Users practice expressing ideas through drawing and making attempts at\nwriting or mimicking print. In this activity, users are prompted to draw or write a response that\nidentifies the who or what of the story or text they just heard in the Interactive Read Aloud\nactivity and the main event(s) that occurred."})

(def t {:assets
                  [{:url "/raw/img/library/painting-tablet/background.jpg", :type "image"}
                   {:url "/raw/img/ui/back_button_01.png", :type "image"}
                   {:url "/raw/img/library/painting-tablet/brush.png", :size 10, :type "image"}
                   {:url "/raw/img/library/painting-tablet/felt-tip.png", :size 10, :type "image"}
                   {:url "/raw/img/library/painting-tablet/pencil.png", :size 10, :type "image"}
                   {:url "/raw/img/library/painting-tablet/eraser.png", :size 10, :type "image"}],
        :objects
                  {:background {:type "background", :scene-name "background", :src "/raw/img/library/painting-tablet/background.jpg"},

                   :practice-canvas
                               {:type    "painting-area"
                                :tool "felt-tip"
                                :color "#4479bb"
                                :change  {:on "click" :type "action" :id "timeout-timer"}}
                   :painting-toolset
                               {:type       "painting-toolset"
                                :transition "painting-toolset"
                                :actions    {:change {:on "change" :type "action" :id "set-current-tool" :pick-event-param "tool"}}}
                   :colors-palette
                               {:type       "colors-palette",
                                :transition "colors-palette"
                                :actions    {:change {:on "change" :type "action", :id "set-current-color" :pick-event-param "color"}}}
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
                                :actions    {:click {:on "click" :type "action" :id "dialog-tap-instructions"}}},
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
                                :src      "/raw/img/ui/back_button_01.png"}},
        :scene-objects
                  [["background"]
                   ["practice-canvas"
                    "painting-toolset"
                    "colors-palette"

                    "next-button"
                    "mari"]],
        :actions
                  {:start-scene                 {:type "sequence-data",
                                                 :data [{:type "start-activity"}
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

                   :timeout-timer               {:type     "set-interval",
                                                 :id       "instructions-timeout",
                                                 :action   "dialog-timeout-instructions",
                                                 :interval 15000}

                   :dialog-timeout-instructions (-> (dialog/default "Timeout instructions")
                                                    (assoc :available-activities ["show-example"]))
                   :dialog-tap-instructions     (-> (dialog/default "Tap instructions")
                                                    (assoc :available-activities ["show-example"]))},
        :triggers {:stop {:on "back", :action "stop-activity"}, :start {:on "start", :action "start-scene"}},
        :metadata {:prev   "library", :autostart true
                   :tracks [{:title "1 Instructions stage 1"
                             :nodes [{:type      "dialog"
                                      :action-id :dialog-instructions}
                                     {:type      "dialog"
                                      :action-id :dialog-timeout-instructions}
                                     {:type      "dialog"
                                      :action-id :dialog-tap-instructions}]}
                            {:title "2 Colors"
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
                            {:title "3 Tools"
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
