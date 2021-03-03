(ns webchange.templates.library.name-writing
  (:require
    [webchange.templates.core :as core]
    [webchange.templates.utils.common :as common]))

(def m {:id          35
        :name        "name writing"
        :tags        ["letter formation" "sight words"]
        :description "name writing"})

(def t {:assets        [{:url "/raw/img/casa/background_casa.png", :size 10, :type "image"}
                        {:url "/raw/img/casa/decoration_casa.png", :size 10, :type "image"}
                        {:url "/raw/img/casa/surface_casa.png", :size 10, :type "image"}
                        {:url "/raw/img/ui/star_03.png", :size 10, :type "image"}]
        :objects       {:layered-background {:type       "layered-background"
                                             :background {:src "/raw/img/casa/background_casa.png"}
                                             :decoration {:src "/raw/img/casa/decoration_casa.png"}
                                             :surface    {:src "/raw/img/casa/surface_casa.png"}}
                        :next-button        {:y          100,
                                             :x          1000,
                                             :type       "image",
                                             :src        "/raw/img/ui/star_03.png",
                                             :scene-name "next-button"
                                             :actions    {:click {:on "click" :type "action" :id "next-round"}}
                                             },
                        :text               {:type           "text",
                                             :x              1000,
                                             :y              420,
                                             :transition     "text",
                                             :align          "center",
                                             :fill           "black",
                                             :font-family    "Lexend Deca",
                                             :font-size      200,
                                             :text           "",
                                             :vertical-align "middle"
                                             },
                        :outline            {:type           "text",
                                             :x              1000,
                                             :y              720,
                                             :transition     "outline",
                                             :align          "center",
                                             :fill           "#ffffff",
                                             :font-family    "Lexend Deca",
                                             :font-size      200,
                                             :text           "",
                                             :vertical-align "middle"
                                             :shadow-offset  {:x 0, :y 0},
                                             :shadow-color   "#1a1a1a",
                                             :shadow-blur    5,
                                             :shadow-opacity 0.5,
                                             }

                        :painting-area
                                            {:type       "painting-area",
                                             :x          0,
                                             :y          620,
                                             :width      1920,
                                             :height     300,
                                             :tool       "pencil"
                                             :color      "#4479bb"
                                             :scene-name "painting-area",
                                             :var-name   "painting-tablet-image"
                                             :actions    {:click {:on "click" :type "action" :id "reset-checker"}}
                                             },},
        :scene-objects [["layered-background" "text" "outline" "painting-area" "next-button"]],
        :actions       {
                        :next-round                      {:type "sequence-data"
                                                          :data [
                                                                 {:type "upload-screenshot"}
                                                                 {:type "action" :id "reset-checker"}
                                                                 {:type "action" :id "correct-answer-dialog"}
                                                                 {:type     "action"
                                                                  :from-var [{:var-name "next-round" :action-property "id"}]}
                                                                 ]}
                        :round-2                         {:type "sequence-data"
                                                          :data [
                                                                 {:type "upload-screenshot"}
                                                                 {:type "clear-painting-area", :target "painting-area"}
                                                                 {:type "action" :id "update-outline-round-2"}
                                                                 {:type "set-variable" :var-name "next-round" :var-value "finish-scene"}]}
                        :finish-scene                    {:type "sequence-data"
                                                          :data [
                                                                 {:type "action" :id "remove-incorrect-answer-checker"}
                                                                 {:type "stop-activity" :id "name-writing"}
                                                                 ]}

                        :copy-current-user               {:type     "copy-current-user-to-variable"
                                                          :var-name "current-user"}
                        :update-text                     {:type     "set-text"
                                                          :target   "text"
                                                          :from-var [{:var-name "current-user" :action-property "text" :var-property "first-name"}]}
                        :update-outline                  {:type     "set-text"
                                                          :target   "outline"
                                                          :from-var [{:var-name "current-user" :action-property "text" :var-property "first-name"}]}

                        :update-outline-round-2          {:type     "set-text"
                                                          :target   "outline"
                                                          :from-var [{:var-name "first-letter" :action-property "text"}]}

                        :init-first-letter               {:type      "string-operation",
                                                          :options   [0, 1],
                                                          :var-name  "first-letter",
                                                          :operation "subs",
                                                          :from-var  [{:var-name "current-user" :action-property "string" :var-property "first-name"}]}

                        :start                           {:type "sequence-data"
                                                          :data [{:type "action" :id "copy-current-user"}
                                                                 {:type "action" :id "introduction-dialog"}
                                                                 {:type "action" :id "update-text"}
                                                                 {:type "action" :id "update-outline"}
                                                                 {:type "action" :id "init-first-letter"}
                                                                 {:type "action" :id "start-incorrect-answer-checker"}
                                                                 {:type "set-variable" :var-name "next-round" :var-value "round-2"}
                                                                 ]}
                        :reset-checker                   {:type "sequence-data"
                                                          :data [
                                                                 {:type "action" :id "remove-incorrect-answer-checker"}
                                                                 {:type "action" :id "start-incorrect-answer-checker"}]
                                                          }
                        :start-incorrect-answer-checker  {:type     "set-interval"
                                                          :id       "incorrect-answer-checker"
                                                          :interval 15000
                                                          :action   "incorrect-answer-dialog"}
                        :remove-incorrect-answer-checker {:type "remove-interval"
                                                          :id   "incorrect-answer-checker"}

                        :incorrect-answer-dialog         {:type               "sequence-data",
                                                          :editor-type        "dialog",
                                                          :data               [{:type "sequence-data"
                                                                                :data [{:type "empty" :duration 0}
                                                                                       {:type  "animation-sequence", :phrase-text "New action",
                                                                                        :audio nil}]}],
                                                          :phrase             "incorrect answer dialog",
                                                          :phrase-description "Incorrect answer"
                                                          }
                        :correct-answer-dialog         {:type               "sequence-data",
                                                        :editor-type        "dialog",
                                                        :data               [{:type "sequence-data"
                                                                              :data [{:type "empty" :duration 0}
                                                                                     {:type  "animation-sequence", :phrase-text "New action",
                                                                                      :audio nil}]}],
                                                        :phrase             "correct answer dialog",
                                                        :phrase-description "Correct answer"
                                                        }
                        :introduction-dialog             {:type               "sequence-data",
                                                          :editor-type        "dialog",
                                                          :data               [{:type "sequence-data"
                                                                                :data [{:type "empty" :duration 0}
                                                                                       {:type  "animation-sequence", :phrase-text "New action",
                                                                                        :audio nil}]}],
                                                          :phrase             "introduction",
                                                          :phrase-description "introduction"
                                                          }
                        }
        :triggers      {:start {:on "start" :action "start"}}
        :metadata      {}
        }
  )

(defn f
  [args]
  (common/init-metadata m t args))

(core/register-template
  m f)

