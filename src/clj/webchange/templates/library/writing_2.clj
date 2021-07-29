(ns webchange.templates.library.writing-2
  (:require
    [webchange.templates.core :as core]
    [webchange.templates.utils.common :as common]
    [webchange.templates.utils.dialog :as dialog]))

(def m {:id          42
        :name        "Writing 2"
        :tags        ["letter formation" "sight words"]
        :description "Writing 2"
        :version     2
        :options     {:text {:label "Text" :type "string"}}})

(def t {:assets        [{:url "/raw/img/library/painting-tablet/background.jpg", :type "image"}
                        {:url "/raw/img/ui/checkmark.png", :type "image"}
                        {:url "/raw/img/library/painting-tablet/brush.png", :size 10, :type "image"}
                        {:url "/raw/img/library/painting-tablet/felt-tip.png", :size 10, :type "image"}
                        {:url "/raw/img/library/painting-tablet/pencil.png", :size 10, :type "image"}
                        {:url "/raw/img/library/painting-tablet/eraser.png", :size 10, :type "image"}
                        {:url "/raw/img/ui/star_03.png", :size 10, :type "image"}]
        :objects       {:background {:type "background", :scene-name "background", :src "/raw/img/library/painting-tablet/background.jpg"},
                        :next-button {:type    "image"
                                      :x       1706 :y 132
                                      :actions {:click {:id "finish-activity", :on "click", :type "action"}}
                                      :filters [{:name "brightness" :value 0}
                                                {:name "glow" :outer-strength 0 :color 0xffd700}]
                                      :src     "/raw/img/ui/checkmark.png"},
                        :outline    {:type           "text",
                                     :x              960,
                                     :y              150,
                                     :transition     "outline",
                                     :align          "center",
                                     :fill           "#ffffff",
                                     :font-family    "Lexend Deca",
                                     :font-size      300,
                                     :text           "",
                                     :vertical-align "middle"
                                     :shadow-offset  {:x 0, :y 0},
                                     :shadow-color   "#1a1a1a",
                                     :shadow-blur    5,
                                     :shadow-opacity 0.5
                                     :filter         "brighten"}

                        :text-tracing-pattern
                                    {:type "text-tracing-pattern"
                                     :text ""
                                     :y    400}

                        :practice-canvas
                                    {:type   "painting-area"
                                     :tool   "felt-tip"
                                     :color  "#4479bb"
                                     :change {:on "click" :type "action" :id "timeout-timer"}}
                        :painting-toolset
                                    {:type       "painting-toolset"
                                     :x          -100
                                     :transition "painting-toolset"
                                     :actions    {:change {:on "change" :type "action" :id "set-current-tool" :pick-event-param "tool"}}}
                        :colors-palette
                                    {:type       "colors-palette",
                                     :y          100
                                     :width      150
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
                                     :actions    {:click {:on "click" :type "action" :id "dialog-tap-instructions"}}},},
        :scene-objects [["background"
                         "outline"
                         "text-tracing-pattern"
                         "practice-canvas"
                         "painting-toolset"
                         "colors-palette"
                         "next-button"
                         "mari"]],
        :actions       {:finish-activity         {:type "sequence-data"
                                                  :data [{:type "action" :id "remove-timeout-timer"}
                                                         {:type "finish-activity"}]}

                        :start                   {:type "sequence-data"
                                                  :data [{:type "start-activity"}
                                                         {:type "action" :id "introduction-dialog"}
                                                         {:type "action" :id "timeout-timer"}]}

                        :timeout-timer           {:type     "set-interval"
                                                  :id       "incorrect-answer-checker"
                                                  :interval 15000
                                                  :action   "incorrect-answer-dialog"}
                        :remove-timeout-timer    {:type "remove-interval"
                                                  :id   "incorrect-answer-checker"}

                        :incorrect-answer-dialog (dialog/default "incorrect answer")
                        :correct-answer-dialog   (dialog/default "correct answer")
                        :introduction-dialog     (-> (dialog/default "introduction")
                                                     (assoc :available-activities ["highlight-tools" "highlight-colors" "highlight-next" "highlight-text"]))

                        :highlight-tools         {:type               "transition"
                                                  :transition-id      "painting-toolset"
                                                  :return-immediately true
                                                  :from               {:brightness 0},
                                                  :to                 {:brightness 0.35 :yoyo true :duration 0.5}
                                                  :kill-after         3000}
                        :highlight-colors        {:type               "transition"
                                                  :transition-id      "colors-palette"
                                                  :return-immediately true
                                                  :from               {:brightness 0},
                                                  :to                 {:brightness 0.35 :yoyo true :duration 0.5}
                                                  :kill-after         3000}
                        :highlight-next          {:type               "transition"
                                                  :transition-id      "next-button"
                                                  :return-immediately true
                                                  :from               {:brightness 0},
                                                  :to                 {:brightness 0.35 :yoyo true :duration 0.5}
                                                  :kill-after         3000}
                        :highlight-text          {:type               "transition"
                                                  :transition-id      "outline"
                                                  :return-immediately true
                                                  :from               {:brightness 0},
                                                  :to                 {:brightness 0.35 :yoyo true :duration 0.5}
                                                  :kill-after         3000}
                        :set-current-tool        {:type "sequence-data"
                                                  :data [{:type        "set-attribute",
                                                          :target      "practice-canvas"
                                                          :attr-name   "tool"
                                                          :from-params [{:param-property "tool", :action-property "attr-value"}]}
                                                         {:type        "action"
                                                          :from-params [{:param-property "tool", :action-property "id" :template "dialog-tool-%"}]}]}
                        :set-current-color       {:type "sequence-data"
                                                  :data [{:type        "set-attribute",
                                                          :target      "practice-canvas"
                                                          :attr-name   "color"
                                                          :from-params [{:param-property "color", :action-property "attr-value"}]}
                                                         {:type        "action"
                                                          :from-params [{:param-property "color", :action-property "id" :template "dialog-color-%"}]}]}
                        :dialog-tool-brush       (dialog/default "tool brush")
                        :dialog-tool-felt-tip    (dialog/default "tool felt-tip")
                        :dialog-tool-pencil      (dialog/default "tool pencil")
                        :dialog-tool-eraser      (dialog/default "tool eraser")
                        :dialog-color-4487611    (dialog/default "color blue")
                        :dialog-color-9616714    (dialog/default "color green")
                        :dialog-color-15569322   (dialog/default "color pink")
                        :dialog-color-16631089   (dialog/default "color yellow")
                        :dialog-color-65793      (dialog/default "color black")
                        }
        :triggers      {:start {:on "start" :action "start"}}
        :metadata      {}})

(defn- config-text
  [template text]
  (-> template
      (assoc-in [:objects :outline :text] text)
      (assoc-in [:objects :text-tracing-pattern :text] text)))

(defn f
  [args]
  (-> (common/init-metadata m t args)
      (config-text (:text args))))

(core/register-template
  m f)

