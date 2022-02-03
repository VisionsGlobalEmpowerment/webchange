(ns webchange.templates.library.name-writing
  (:require
    [webchange.templates.core :as core]
    [webchange.templates.utils.common :as common]
    [webchange.templates.utils.dialog :as dialog]))

(def m {:id          35
        :name        "name writing"
        :tags        ["letter formation" "sight words"]
        :description "name writing"
        :options     {:type {:label   "Type"
                             :type    "lookup"
                             :options [{:name "First name" :value "first-name"}
                                       {:name "First letter only" :value "firts-letter"}]}}})

(def t {:assets        [{:url "/raw/img/library/painting-tablet/background.jpg", :type "image"}
                        {:url "/raw/img/ui/back_button_01.png", :type "image"}
                        {:url "/raw/img/library/painting-tablet/brush.png", :size 10, :type "image"}
                        {:url "/raw/img/library/painting-tablet/felt-tip.png", :size 10, :type "image"}
                        {:url "/raw/img/library/painting-tablet/pencil.png", :size 10, :type "image"}
                        {:url "/raw/img/library/painting-tablet/eraser.png", :size 10, :type "image"}
                        {:url "/raw/img/ui/star_03.png", :size 10, :type "image"}]
        :objects       {:background              {:type "background", :scene-name "background", :src "/raw/img/library/painting-tablet/background.jpg"},
                        :next-button             {:type       "group"
                                                  :x          1766
                                                  :y          28
                                                  :width      96 :height 96
                                                  :transition "next-button"
                                                  :children   ["next-background"
                                                               "next-button-mark"]}
                        :next-background      {:type          "rectangle"
                                               :x             0
                                               :y             0
                                               :transition    "next-background"
                                               :width         96
                                               :height        96
                                               :border-radius 48
                                               :fill          0xFF5C00}
                        :next-button-mark {:type    "svg-path"
                                           :x       20
                                           :y       25
                                           :width   128
                                           :height  128
                                           :fill    "#FFFFFF",
                                           :actions {:click {:id "finish-activity", :on "click", :type "action"}},
                                           :data    "M 9.29193 13.1343L0 22.3134L22.6633 45L59 9.47761L49.1793 0L22.6633 26.194L9.29193 13.1343"}
                        :outline                 {:type           "text",
                                                  :x              960,
                                                  :y              150,
                                                  :align          "center",
                                                  :fill           "#ffffff",
                                                  :font-family    "Lexend Deca",
                                                  :font-size      300,
                                                  :text           "",
                                                  :vertical-align "middle"
                                                  :shadow-offset  {:x 0, :y 0},
                                                  :shadow-color   "#1a1a1a",
                                                  :shadow-blur    5,
                                                  :shadow-opacity 0.5}

                        :text-tracing-pattern
                        {:type "text-tracing-pattern"
                         :text " "
                         :y    400}

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
                         :y          900
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
                         :actions    {:click {:on "click" :type "action" :id "dialog-tap-instructions"}}},
                        }
        :scene-objects [["background"
                         "outline"
                         "text-tracing-pattern"
                         "practice-canvas"
                         "painting-toolset"
                         "colors-palette"
                         "next-button"
                         "mari"]],
        :actions       {:finish-activity             {:type "sequence-data"
                                                      :data [{:type "action" :id "remove-timeout-timer"}
                                                             {:type "finish-activity"}]}
                        :init-text                   {:type     "set-variable"
                                                      :var-name "text",
                                                      :from-var [{:var-name "current-user" :action-property "var-value" :var-property "first-name"}]}
                        :init-tracing-text           {:type     "set-variable"
                                                      :var-name "tracing-text",
                                                      :from-var [{:var-name "current-user" :action-property "var-value" :var-property "first-name"}]}
                        :dialog-tap-instructions     (dialog/default "Tap instructions")
                        :start                       {:type "sequence-data"
                                                      :data [{:type "start-activity"}
                                                             {:type "copy-current-user-to-variable" :var-name "current-user"}
                                                             {:type "action" :id "init-text"}
                                                             {:type "action" :id "init-tracing-text"}
                                                             {:type      "set-attribute",
                                                              :target    "text-tracing-pattern",
                                                              :from-var  [{:var-name "tracing-text" :action-property "attr-value"}],
                                                              :attr-name "text"}
                                                             {:type      "set-attribute"
                                                              :target    "outline"
                                                              :from-var  [{:var-name "text" :action-property "attr-value"}]
                                                              :attr-name "text"}
                                                             {:type "action" :id "introduction-dialog"}
                                                             {:type "action" :id "timeout-timer"}]}

                        :timeout-timer               {:type     "set-interval"
                                                      :id       "incorrect-answer-checker"
                                                      :interval 15000
                                                      :action   "timeout-instructions-dialog"}
                        :remove-timeout-timer        {:type "remove-interval"
                                                      :id   "incorrect-answer-checker"}

                        :timeout-instructions-dialog (dialog/default "timeout instructions")
                        :correct-answer-dialog       (dialog/default "correct answer")
                        :introduction-dialog         (-> (dialog/default "introduction")
                                                         (assoc :available-activities []))


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
                        }
        :triggers      {:start {:on "start" :action "start"}}
        :metadata      {:tracks    [{:title "Tools"
                                     :nodes [{:type      "dialog"
                                              :action-id :dialog-tool-brush}
                                             {:type      "dialog"
                                              :action-id :dialog-tool-felt-tip}
                                             {:type      "dialog"
                                              :action-id :dialog-tool-pencil}
                                             {:type      "dialog"
                                              :action-id :dialog-tool-eraser}]}
                                    {:title "Colors"
                                     :nodes [{:type      "dialog"
                                              :action-id :dialog-color-4487611}
                                             {:type      "dialog"
                                              :action-id :dialog-color-9616714}
                                             {:type      "dialog"
                                              :action-id :dialog-color-15569322}
                                             {:type      "dialog"
                                              :action-id :dialog-color-16631089}
                                             {:type      "dialog"
                                              :action-id :dialog-color-65793}
                                             ]}
                                    {:title "Dialogues"
                                     :nodes [{:type      "dialog"
                                              :action-id :introduction-dialog}
                                             {:type      "dialog"
                                              :action-id :dialog-tap-instructions}
                                             {:type      "dialog"
                                              :action-id :correct-answer-dialog}
                                             {:type      "dialog"
                                              :action-id :timeout-instructions-dialog}]}
                                    ]
                        }})

(def first-letter-actions
  {:init-tracing-text {:type "sequence-data"
                       :data [{:type      "string-operation",
                               :options   [0, 1],
                               :var-name  "first-letter",
                               :operation "subs",
                               :from-var  [{:var-name "current-user" :action-property "string" :var-property "first-name"}]}
                              {:type      "string-operation",
                               :options   7,
                               :var-name  "tracing-text",
                               :operation "right-pad",
                               :from-var  [{:var-name "first-letter" :action-property "string"}]}]}})

(defn- config-text
  [template type]
  (case type
    "first-letter" (update template :actions merge first-letter-actions)
    template))

(defn f
  [args]
  (-> (common/init-metadata m t args)
      (common/add-highlight "next-button" "Highlight next button")
      (common/add-highlight "painting-toolset" "Highlight tools")
      (common/add-highlight "colors-palette" "Highlight colors")
      (common/add-highlight "outline" "Highlight text")
      (config-text (:type args))))

(core/register-template
  m f)
