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
        :options     {:text {:label "Text" :type "string"}}
        :actions     {:change-word {:title    "Change word"
                                    :options  {:word {:label   "Select Round"
                                                      :type    "string"
                                                      :placeholder "Place your word here"}
                                               }}}})

(def t {:assets        [{:url "/raw/img/library/painting-tablet/background.jpg", :type "image"}
                        {:url "/raw/img/ui/checkmark.png", :type "image"}
                        {:url "/raw/img/library/painting-tablet/brush.png", :size 10, :type "image"}
                        {:url "/raw/img/library/painting-tablet/felt-tip.png", :size 10, :type "image"}
                        {:url "/raw/img/library/painting-tablet/pencil.png", :size 10, :type "image"}
                        {:url "/raw/img/library/painting-tablet/eraser.png", :size 10, :type "image"}
                        {:url "/raw/img/ui/star_03.png", :size 10, :type "image"}]
        :objects       {:background {:type "background", :scene-name "background", :src "/raw/img/library/painting-tablet/background.jpg"}
                        :next-button {:type    "image"
                                      :x       1706 :y 132
                                      :actions {:click {:id "finish-activity", :on "click", :type "action"}}
                                      :filters [{:name "brightness" :value 0}
                                                {:name "glow" :outer-strength 0 :color 0xffd700}]
                                      :src     "/raw/img/ui/checkmark.png"},
                        :text-tracing-pattern
                        {:type "text-tracing-pattern"
                         :text ""
                         :x-offset 200
                         :spacing 50
                         :y    300}
                        :practice-canvas
                        {:type   "painting-area"
                         :tool   "felt-tip"
                         :color  "#4479bb"
                         :actions {:change {:on "click" :type "action" :id "timeout-timer"}}}
                        :painting-toolset
                        {:type       "painting-toolset"
                         :x          -100
                         :transition "painting-toolset"
                         :actions    {:change {:on "change" :type "action" :id "set-current-tool" :pick-event-param "tool"}}}
                        :colors-palette
                        {:type       "colors-palette",
                         :x          400
                         :height     150
                         :y          20
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
                         :editable?  {:select true :drag true :show-in-tree? true}
                         :actions    {:click {:on "click" :type "action" :id "dialog-tap-instructions"}}}
                        :rect {:type "rectangle"
                               :x 100
                               :y 100
                               :width 100
                               :height 100
                               :fill 0xff0000
                               :actions {:click {:id "my-action" :on "click" :type "action"}}
                               }

                        :letter {:type "svg-path"
                                 :x 300
                                 :y 100
                                 :dash [7 7]
                                 :data "M 10 10 H 90 V 90 H 10 L 10 10"
                                 :line-cap "round"
                                 :stroke "#ff0000"
                                 :stroke-width 4
                                 :filters [{:name "brightness" :value 0}
                                           {:name "glow" :outer-strength 0 :color 0xffd700}]
                               }
                        },
        :scene-objects [["background"
                         "text-tracing-pattern"
                         "practice-canvas"
                         "painting-toolset"
                         "colors-palette"
                         "next-button"
                         "mari"
                         "rect"
                         "letter"
                         ]],
        :actions       {:finish-activity         {:type "sequence-data"
                                                  :data [{:type "action" :id "remove-timeout-timer"}
                                                         {:type "action" :id "correct-answer-dialog"}
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
                        :my-action {:type               "transition"
                                    :transition-id      "letter"
                                    :return-immediately true
                                    :from               {:brightness 0 :glow 0}
                                    :to                 {:brightness 1 :glow 10 :yoyo true :duration 0.5 :repeat 5}}

                        :incorrect-answer-dialog (dialog/default "Timeout instrucitons")
                        :correct-answer-dialog   (dialog/default "Finish activity")
                        :introduction-dialog     (-> (dialog/default "introduction")
                                                     (assoc :available-activities []))
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
                        :dialog-tap-instructions    (dialog/default "Tap instructions")
                        }
        :triggers      {:start {:on "start" :action "start"}}
        :metadata      {:tracks            [{:title "1 Instructions"
                                             :nodes [{:type      "dialog"
                                                      :action-id :introduction-dialog}
                                                     {:type      "dialog"
                                                      :action-id :incorrect-answer-dialog}
                                                     {:type      "dialog"
                                                      :action-id :dialog-tap-instructions}
                                                     {:type      "dialog"
                                                      :action-id :correct-answer-dialog}]}
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

(defn- change-word
  [activity-data args]
  (assoc-in activity-data [:objects :text-tracing-pattern :text] (:word args)))

(defn update-activity
  [old-data args]
  (case (:action-name args)
    "change-word" (change-word old-data args)))

(defn- config-text
  [template text]
  (-> template
      (assoc-in [:objects :text-tracing-pattern :text] text)))

(defn f
  [args]
  (-> (common/init-metadata m t args)
      (common/add-highlight "next-button" "Highlight next button")
      (common/add-highlight "painting-toolset" "Highlight tools")
      (common/add-highlight "colors-palette" "Highlight colors")
      (config-text (:text args))))

(core/register-template m f update-activity)
