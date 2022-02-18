(ns webchange.editor-v2.activity-dialogs.stub-scene-data)

(def scene-data {:assets        [{:url "/raw/img/casa/background.jpg", :size 10, :type "image"}
                                 {:url "/raw/img/casa/painting_canvas.png", :size 10, :type "image"}
                                 {:url    "/upload/VEQRANHKKEMOIUFU.mp3",
                                  :date   1621979625768,
                                  :size   576,
                                  :type   "audio",
                                  :alias  "Letter introduction",
                                  :target nil}
                                 {:url    "/upload/TIEHZKZSWTWMWVHD.mp3",
                                  :date   1621980696869,
                                  :size   618,
                                  :type   "audio",
                                  :alias  "Letter Chants",
                                  :target "mari"}
                                 {:url    "/upload/WDIGPWORUJZTQGJS.mp3",
                                  :date   1620427029731,
                                  :size   303,
                                  :type   "audio",
                                  :alias  "Rachel's song chant #1",
                                  :target nil}
                                 {:url    "/upload/RNCWHTCTBCGIJWPO.mp3",
                                  :date   1620428438833,
                                  :size   155,
                                  :type   "audio",
                                  :alias  "Mari",
                                  :target nil}
                                 {:url "/upload/QLMVNPSNFGXZYHGX.mp3", :size 10, :type "audio"}
                                 {:url    "/upload/BRPWTKIKMOZPMOEV.mp3",
                                  :date   1622585202458,
                                  :size   299,
                                  :type   "audio",
                                  :alias  "Rachel Song Chant #2",
                                  :target "senora-vaca"}
                                 {:url    "/upload/ZDDEHHFBUHHZWJXZ.mp3",
                                  :date   1622583553548,
                                  :size   629,
                                  :type   "audio",
                                  :alias  "Letter Introduction #2",
                                  :target "senora-vaca"}
                                 {:url "/upload/DAERZKMHJTREXJAN.png", :size 1, :type "image"}
                                 {:url   "/upload/GRKUBPLXJBVKOFSI.mp3",
                                  :date  1643659565121,
                                  :lang  nil,
                                  :size  15,
                                  :type  "audio",
                                  :alias "Student-New instr."}
                                 {:url   "/upload/ORCWDXCAJHKZSVWE.mp3",
                                  :date  1643659640933,
                                  :lang  nil,
                                  :size  62,
                                  :type  "audio",
                                  :alias "Student letter names"}],
                 :skills        [],
                 :actions       {:introduce-big-small         {:data                          [{:data [{:type "empty", :duration 0} {:type "hide-guide"}],
                                                                                                :type "sequence-data"}
                                                                                               {:data [{:type "empty", :duration 1000}
                                                                                                       {:phrase-text "",
                                                                                                        :start       0.783,
                                                                                                        :type        "animation-sequence",
                                                                                                        :duration    3.477,
                                                                                                        :volume      "0.89",
                                                                                                        :audio       "/upload/VEQRANHKKEMOIUFU.mp3",
                                                                                                        :target      "senora-vaca",
                                                                                                        :end         4.26,
                                                                                                        :data        [{:end 2.0, :anim "talk", :start 1.18}
                                                                                                                      {:end 4.13, :anim "talk", :start 2.5}]}],
                                                                                                :type "sequence-data"}
                                                                                               {:data [{:type "empty", :duration 0}
                                                                                                       {:end         7.35,
                                                                                                        :data        [{:end 6.51, :anim "talk", :start 6.14}
                                                                                                                      {:end 7.31, :anim "talk", :start 6.7}],
                                                                                                        :type        "animation-sequence",
                                                                                                        :audio       "/upload/VEQRANHKKEMOIUFU.mp3",
                                                                                                        :start       6.12,
                                                                                                        :target      "senora-vaca",
                                                                                                        :duration    1.2299999999999995,
                                                                                                        :phrase-text "This is the letter"}],
                                                                                                :type "sequence-data"}
                                                                                               {:type     "action",
                                                                                                :from-var [{:var-name     "current-concept",
                                                                                                            :var-property "dialog-field-ef56a7df-3b1d-4abb-a43d-e06bad56bba5"}]}
                                                                                               {:data [{:type "empty", :duration 500}
                                                                                                       {:end         10.317,
                                                                                                        :data        [{:end 10.24, :anim "talk", :start 8.73}],
                                                                                                        :type        "animation-sequence",
                                                                                                        :audio       "/upload/VEQRANHKKEMOIUFU.mp3",
                                                                                                        :start       8.7,
                                                                                                        :target      "senora-vaca",
                                                                                                        :duration    1.617,
                                                                                                        :phrase-text "There are 2 ways to write it "}],
                                                                                                :type "sequence-data"}
                                                                                               {:data [{:data [{:type "empty", :duration 0}
                                                                                                               {:end         11.88,
                                                                                                                :data        [{:end 11.86, :anim "talk", :start 10.84}],
                                                                                                                :type        "animation-sequence",
                                                                                                                :audio       "/upload/VEQRANHKKEMOIUFU.mp3",
                                                                                                                :start       10.83,
                                                                                                                :target      "senora-vaca",
                                                                                                                :duration    1.353,
                                                                                                                :phrase-text "the lowercase"}],
                                                                                                        :type "sequence-data"}
                                                                                                       {:data [{:type "empty", :duration 0} {:id "glow-small", :type "action"}],
                                                                                                        :type "sequence-data"}],
                                                                                                :type "parallel"}
                                                                                               {:data [{:type "empty", :duration 0} {:id "stop-glow-small", :type "action"}],
                                                                                                :type "sequence-data"}
                                                                                               {:data [{:data [{:type "empty", :duration 500}
                                                                                                               {:end         13.53,
                                                                                                                :data        [{:end 13.42, :anim "talk", :start 12.41}],
                                                                                                                :type        "animation-sequence",
                                                                                                                :audio       "/upload/VEQRANHKKEMOIUFU.mp3",
                                                                                                                :start       12.39,
                                                                                                                :target      "senora-vaca",
                                                                                                                :duration    1.1399999999999988,
                                                                                                                :phrase-text "and the uppercase"}],
                                                                                                        :type "sequence-data"}
                                                                                                       {:data [{:type "empty", :duration 0} {:id "glow-big", :type "action"}],
                                                                                                        :type "sequence-data"}],
                                                                                                :type "parallel"}
                                                                                               {:data [{:type "empty", :duration 0} {:id "stop-glow-big", :type "action"}],
                                                                                                :type "sequence-data"}
                                                                                               {:data [{:type "empty", :duration 0}
                                                                                                       {:end         17.04,
                                                                                                        :data        [{:end 15.84, :anim "talk", :start 14.77}
                                                                                                                      {:end 17.0, :anim "talk", :start 16.44}],
                                                                                                        :type        "animation-sequence",
                                                                                                        :audio       "/upload/VEQRANHKKEMOIUFU.mp3",
                                                                                                        :start       14.76,
                                                                                                        :target      "senora-vaca",
                                                                                                        :duration    2.2799999999999994,
                                                                                                        :phrase-text "Let’s practice: say,"}],
                                                                                                :type "sequence-data"}
                                                                                               {:type     "action",
                                                                                                :from-var [{:var-name     "current-concept",
                                                                                                            :var-property "dialog-field-e0b429c2-cf25-4736-8160-75a9df533dac"}]}],
                                                               :type                          "sequence-data",
                                                               :phrase                        "intro",
                                                               :concept-var                   "current-concept",
                                                               :editor-type                   "dialog",
                                                               :phrase-description            "Introduce task",
                                                               :available-activities          ["glow-big"
                                                                                               "stop-glow-big"
                                                                                               "glow-small"
                                                                                               "stop-glow-small"
                                                                                               "highlight-big-letter"
                                                                                               "highlight-small-letter"],
                                                               :phrase-description-translated "Introduction"},
                                 :start-background-music      {:id "/upload/QLMVNPSNFGXZYHGX.mp3", :loop true, :type "audio", :volume "0.23"},
                                 :whole-word-stop-glow        {:id "stop-glow", :type "state", :target "word"},
                                 :whole-word-dialog           {:data                 [{:data [{:type "empty", :duration 0}
                                                                                              {:end         96.06,
                                                                                               :data        [{:end 95.82, :anim "talk", :start 95.11}],
                                                                                               :type        "animation-sequence",
                                                                                               :audio       "/upload/VEQRANHKKEMOIUFU.mp3",
                                                                                               :start       95.1,
                                                                                               :target      "senora-vaca",
                                                                                               :duration    0.960000000000008,
                                                                                               :phrase-text "Here is the word"}],
                                                                                       :type "sequence-data"}
                                                                                      {:type     "action",
                                                                                       :from-var [{:var-name     "current-concept",
                                                                                                   :var-property "dialog-field-1f82149c-500d-47f0-9c11-afc0d1c43dd6"}]}
                                                                                      {:data [{:type "empty", :duration 0} {:type "show-guide"}], :type "sequence-data"}
                                                                                      {:data [{:type "empty", :duration 500}
                                                                                              {:phrase-text "Tap the lowercase letter ",
                                                                                               :start       0.137,
                                                                                               :type        "animation-sequence",
                                                                                               :duration    2.1,
                                                                                               :region-text "tap the lower case letter",
                                                                                               :audio       "/upload/GRKUBPLXJBVKOFSI.mp3",
                                                                                               :target      "guide",
                                                                                               :end         2.237,
                                                                                               :data        [{:end 2.237, :anim "talk", :start 0.137}]}],
                                                                                       :type "sequence-data"}
                                                                                      {:type     "action",
                                                                                       :from-var [{:var-name     "current-concept",
                                                                                                   :var-property "dialog-field-984e0807-24e5-4e0a-9577-423049b0a0d1"}]}
                                                                                      {:data [{:type "empty", :duration 0}
                                                                                              {:phrase-text        "at the beginning of the word!",
                                                                                               :start              3.377,
                                                                                               :type               "animation-sequence",
                                                                                               :duration           2.07,
                                                                                               :region-text        "at the beginning of the word",
                                                                                               :audio              "/upload/GRKUBPLXJBVKOFSI.mp3",
                                                                                               :target             "guide",
                                                                                               :end                5.447,
                                                                                               :phrase-placeholder "Enter phrase text",
                                                                                               :data               [{:end 5.447, :anim "talk", :start 3.377}]}],
                                                                                       :type "sequence-data"}
                                                                                      {:data [{:type "empty", :duration 0} {:id "whole-word-glow", :type "action"}],
                                                                                       :type "sequence-data"}
                                                                                      {:data [{:type "empty", :duration 0} {:id "whole-word-stop-glow", :type "action"}],
                                                                                       :type "sequence-data"}],
                                                               :type                 "sequence-data",
                                                               :phrase               "whole-word-dialog",
                                                               :concept-var          "current-concept",
                                                               :editor-type          "dialog",
                                                               :phrase-description   "Whole word dialog",
                                                               :available-activities ["whole-word-glow" "whole-word-stop-glow"]},
                                 :mari-wand-hit               {:data [{:id "wand_hit", :type "animation", :track 2, :target "mari"}
                                                                      {:id "wand_idle", :loop true, :type "add-animation", :track 2, :target "mari"}],
                                                               :type "sequence-data"},
                                 :timeout-instructions        {:type     "action",
                                                               :from-var [{:var-name "timeout-instructions-action", :action-property "id"}]},
                                 :introduce-image             {:data [{:id "hidden", :type "state", :target "image"}
                                                                      {:to            {:x 1500, :y 575, :loop false, :duration 0.1},
                                                                       :type          "transition",
                                                                       :transition-id "image"}
                                                                      {:id "visible", :type "state", :target "image"}
                                                                      {:data [{:to            {:x 1146, :y 295, :loop false, :duration 2},
                                                                               :type          "transition",
                                                                               :transition-id "mari"}
                                                                              {:to            {:x 1025, :y 240, :loop false, :duration 2},
                                                                               :type          "transition",
                                                                               :transition-id "image"}],
                                                                       :type "parallel"}
                                                                      {:type "empty", :duration 1000}
                                                                      {:id "mari-init-wand", :type "action"}
                                                                      {:to            {:x 1600, :y 635, :loop false, :duration 1.5},
                                                                       :type          "transition",
                                                                       :transition-id "mari"}
                                                                      {:id "introduce-image-dialog", :type "action"}],
                                                               :type "sequence-data"},
                                 :dialog-timeout-instructions {:data               [{:data [{:type "empty", :duration 0} {:type "highlight-guide"}],
                                                                                     :type "sequence-data"}],
                                                               :type               "sequence-data",
                                                               :phrase             "Timeout instructions",
                                                               :editor-type        "dialog",
                                                               :phrase-description "Timeout instructions"},
                                 :correct-response-dialog     {:data               [{:type     "action",
                                                                                     :from-var [{:var-name     "current-concept",
                                                                                                 :var-property "dialog-field-f5ded6d4-5f95-4c0e-b396-97d1844b3841"}]}
                                                                                    {:data [{:type "empty", :duration 0} {:type "hide-guide"}],
                                                                                     :type "sequence-data"}
                                                                                    {:data [{:type "empty", :duration 500}
                                                                                            {:end         111.9,
                                                                                             :data        [{:end 107.42, :anim "talk", :start 105.88}
                                                                                                           {:end 111.79, :anim "talk", :start 107.93}],
                                                                                             :type        "animation-sequence",
                                                                                             :audio       "/upload/VEQRANHKKEMOIUFU.mp3",
                                                                                             :start       105.87,
                                                                                             :target      "senora-vaca",
                                                                                             :duration    6.03,
                                                                                             :phrase-text "We’ve learned a lot today! Here’s a song to help us remember everything we just learned"}],
                                                                                     :type "sequence-data"}
                                                                                    {:data [{:type "empty", :duration 0} {:type "mute-background-music"}],
                                                                                     :type "sequence-data"}
                                                                                    {:type     "action",
                                                                                     :from-var [{:var-name     "current-concept",
                                                                                                 :var-property "dialog-field-d2bc7f61-b886-4bf3-830e-bd2ad2c4b906"}]}
                                                                                    {:data [{:type "empty", :duration 500}
                                                                                            {:end         122.52,
                                                                                             :data        [{:end 122.49, :anim "talk", :start 120.32}],
                                                                                             :type        "animation-sequence",
                                                                                             :audio       "/upload/VEQRANHKKEMOIUFU.mp3",
                                                                                             :start       120.3,
                                                                                             :target      "senora-vaca",
                                                                                             :duration    2.219999999999999,
                                                                                             :phrase-text "Sing it with me 3 times."}],
                                                                                     :type "sequence-data"}
                                                                                    {:type     "action",
                                                                                     :from-var [{:var-name     "current-concept",
                                                                                                 :var-property "dialog-field-9dab4753-40ab-4321-85b5-34904c148fd0"}]}
                                                                                    {:type     "action",
                                                                                     :from-var [{:var-name     "current-concept",
                                                                                                 :var-property "dialog-field-aa4f11e0-c86c-4cd8-ac0f-36f9aa81d1f3"}]}
                                                                                    {:type     "action",
                                                                                     :from-var [{:var-name     "current-concept",
                                                                                                 :var-property "dialog-field-78767e44-80c5-4643-bf02-f853c6500369"}]}
                                                                                    {:data [{:type "empty", :duration 0}
                                                                                            {:end         95.422,
                                                                                             :data        [{:end 95.36, :anim "talk", :start 93.15}],
                                                                                             :type        "animation-sequence",
                                                                                             :audio       "/upload/RNCWHTCTBCGIJWPO.mp3",
                                                                                             :start       93.077,
                                                                                             :target      "mari",
                                                                                             :duration    2.345,
                                                                                             :phrase-text "I love learning letters with you!"}],
                                                                                     :type "sequence-data"}],
                                                               :type               "sequence-data",
                                                               :phrase             "Correct Response",
                                                               :concept-var        "current-concept",
                                                               :editor-type        "dialog",
                                                               :phrase-description "Correct Response and finish"},
                                 :stop-glow-small             {:id "stop-glow", :type "state", :target "letter-small"},
                                 :init-guide                  {:data [{:type      "set-variable",
                                                                       :var-name  "tap-instructions-action",
                                                                       :var-value "dialog-tap-instructions"}
                                                                      {:type      "set-variable",
                                                                       :var-name  "timeout-instructions-action",
                                                                       :var-value "dialog-timeout-instructions"}],
                                                               :type "parallel"},
                                 :writing-task                {:data [{:id "hidden", :type "state", :target "letter-small"}
                                                                      {:id "visible", :type "state", :target "letter-path"}
                                                                      {:data [{:type "path-animation", :state "play", :target "letter-path"}
                                                                              {:id "describe-writing", :type "action"}],
                                                                       :type "parallel"}],
                                                               :type "sequence-data"},
                                 :redraw-letter               {:data [{:type       "set-attribute",
                                                                       :target     "letter-path",
                                                                       :attr-name  "stroke",
                                                                       :attr-value "#5c54ef"}
                                                                      {:type "path-animation", :state "play", :target "letter-path"}],
                                                               :type "sequence-data"},
                                 :stop-activity               {:type "stop-activity"},
                                 :finish-activity             {:data [{:id "inactive-counter", :type "remove-interval"} {:type "finish-activity"}],
                                                               :type "sequence-data"},
                                 :init-state                  {:data [{:type      "set-attribute",
                                                                       :target    "letter-small",
                                                                       :from-var  [{:var-name        "current-concept",
                                                                                    :var-property    "letter",
                                                                                    :action-property "attr-value"}],
                                                                       :attr-name "text"}
                                                                      {:type      "set-attribute",
                                                                       :target    "letter-path",
                                                                       :from-var  [{:var-name        "current-concept",
                                                                                    :var-property    "letter",
                                                                                    :action-property "attr-value"}],
                                                                       :attr-name "path"}
                                                                      {:id "big", :type "state", :target "letter-big"}
                                                                      {:type      "set-attribute",
                                                                       :target    "letter-big",
                                                                       :from-var  [{:var-name        "current-concept",
                                                                                    :var-property    "letter-big",
                                                                                    :action-property "attr-value"}],
                                                                       :attr-name "text"}
                                                                      {:id "hidden", :type "state", :target "word"}
                                                                      {:type      "set-attribute",
                                                                       :target    "word",
                                                                       :from-var  [{:var-name        "current-concept",
                                                                                    :var-property    "concept-name",
                                                                                    :action-property "attr-value"}],
                                                                       :attr-name "text"}
                                                                      {:id "init-position", :type "state", :target "image"}
                                                                      {:type      "set-attribute",
                                                                       :target    "image",
                                                                       :from-var  [{:var-name        "current-concept",
                                                                                    :var-property    "image-src",
                                                                                    :action-property "attr-value"}],
                                                                       :attr-name "src"}],
                                                               :type "parallel"},
                                 :stop-glow-big               {:id "stop-glow", :type "state", :target "letter-big"},
                                 :dialog-wrong-answer         {:data               [{:data [{:type "empty", :duration 0}
                                                                                            {:type               "animation-sequence",
                                                                                             :audio              nil,
                                                                                             :phrase-text        "New action",
                                                                                             :phrase-placeholder "Enter phrase text"}],
                                                                                     :type "sequence-data"}],
                                                               :type               "sequence-data",
                                                               :phrase             "dialog-wrong-answer",
                                                               :concept-var        "current-concept",
                                                               :editor-type        "dialog",
                                                               :phrase-description "Dialog wrong answer"},
                                 :tap-instructions            {:type "action", :from-var [{:var-name "tap-instructions-action", :action-property "id"}]},
                                 :hide-uploaded-image-3       {:type       "set-attribute",
                                                               :target     "uploaded-image-3",
                                                               :attr-name  "visible",
                                                               :attr-value false},
                                 :renew-concept               {:from        "concepts-single",
                                                               :type        "lesson-var-provider",
                                                               :variables   ["current-concept"],
                                                               :provider-id "concepts"},
                                 :stop-timeout                {:id "inactive-counter", :type "remove-interval"},
                                 :continue-try                {:data ["start-timeout" "dialog-wrong-answer"], :type "sequence"},
                                 :highlight-small-letter      {:to                 {:yoyo true, :opacity 0.38, :duration 0.5},
                                                               :from               {:opacity 1},
                                                               :type               "transition",
                                                               :kill-after         3000,
                                                               :transition-id      "letter-small",
                                                               :return-immediately true},
                                 :word-click                  {:data       [{:id "stop-timeout", :type "action"}
                                                                            {:id "correct-response-dialog", :type "action"}
                                                                            {:id "finish-activity", :type "action"}],
                                                               :type       "sequence-data",
                                                               :unique-tag "instruction"},
                                 :glow-small                  {:id "glow", :type "state", :target "letter-small"},
                                 :start-timeout               {:id        "inactive-counter",
                                                               :type      "start-timeout-counter",
                                                               :action    "continue-try",
                                                               :interval  10000,
                                                               :autostart true},
                                 :whole-word-glow             {:id "glow", :type "state", :target "word"},
                                 :introduce-image-dialog      {:data               [{:data [{:type "empty", :duration 500}
                                                                                            {:end         6.63,
                                                                                             :data        [{:end 6.6, :anim "talk", :start 4.45}],
                                                                                             :type        "animation-sequence",
                                                                                             :audio       "/upload/TIEHZKZSWTWMWVHD.mp3",
                                                                                             :start       4.44,
                                                                                             :target      "senora-vaca",
                                                                                             :duration    2.1899999999999995,
                                                                                             :phrase-text "Listen to the first sound in the word"}],
                                                                                     :type "sequence-data"}
                                                                                    {:type     "action",
                                                                                     :from-var [{:var-name     "current-concept",
                                                                                                 :var-property "dialog-field-362f6c8b-4dd2-42fe-a724-ff035628a31d"}]}
                                                                                    {:type     "action",
                                                                                     :from-var [{:var-name     "current-concept",
                                                                                                 :var-property "dialog-field-94364d3d-6578-4f71-be86-72036b9d2ad1"}]}
                                                                                    {:type     "action",
                                                                                     :from-var [{:var-name     "current-concept",
                                                                                                 :var-property "dialog-field-5a412dc4-914f-48fe-b09e-db3cdaff3b28"}]}
                                                                                    {:data [{:type "empty", :duration 500}
                                                                                            {:end         18.54,
                                                                                             :data        [{:end 18.5, :anim "talk", :start 17.72}],
                                                                                             :type        "animation-sequence",
                                                                                             :audio       "/upload/TIEHZKZSWTWMWVHD.mp3",
                                                                                             :start       17.7,
                                                                                             :target      "senora-vaca",
                                                                                             :duration    0.8399999999999999,
                                                                                             :phrase-text "I hear the"}],
                                                                                     :type "sequence-data"}
                                                                                    {:type     "action",
                                                                                     :from-var [{:var-name     "current-concept",
                                                                                                 :var-property "dialog-field-cca7cc32-b8c7-4076-8eeb-06fca437dcfd"}]}
                                                                                    {:data [{:type "empty", :duration 500}
                                                                                            {:end         21.33,
                                                                                             :data        [{:end 21.26, :anim "talk", :start 19.89}],
                                                                                             :type        "animation-sequence",
                                                                                             :audio       "/upload/TIEHZKZSWTWMWVHD.mp3",
                                                                                             :start       19.71,
                                                                                             :target      "senora-vaca",
                                                                                             :duration    1.6199999999999974,
                                                                                             :phrase-text "sound at the beginning of"}],
                                                                                     :type "sequence-data"}
                                                                                    {:type     "action",
                                                                                     :from-var [{:var-name     "current-concept",
                                                                                                 :var-property "dialog-field-33540e04-8d51-4dcb-a3aa-3865baa0976e"}]}
                                                                                    {:data [{:type "empty", :duration 500}
                                                                                            {:end         23.64,
                                                                                             :data        [{:end 23.26, :anim "talk", :start 22.83}],
                                                                                             :type        "animation-sequence",
                                                                                             :audio       "/upload/TIEHZKZSWTWMWVHD.mp3",
                                                                                             :start       22.8,
                                                                                             :target      "senora-vaca",
                                                                                             :duration    0.8399999999999999,
                                                                                             :phrase-text "so the word"}],
                                                                                     :type "sequence-data"}
                                                                                    {:type     "action",
                                                                                     :from-var [{:var-name     "current-concept",
                                                                                                 :var-property "dialog-field-99e0364b-aca7-43f1-a74f-6ba73fdef98d"}]}
                                                                                    {:data [{:type "empty", :duration 500}
                                                                                            {:end         26.64,
                                                                                             :data        [{:end 25.77, :anim "talk", :start 24.89}
                                                                                                           {:end 26.63, :anim "talk", :start 26.01}],
                                                                                             :type        "animation-sequence",
                                                                                             :audio       "/upload/TIEHZKZSWTWMWVHD.mp3",
                                                                                             :start       24.87,
                                                                                             :target      "senora-vaca",
                                                                                             :duration    1.7699999999999996,
                                                                                             :phrase-text "must start with the letter"}],
                                                                                     :type "sequence-data"}
                                                                                    {:type     "action",
                                                                                     :from-var [{:var-name     "current-concept",
                                                                                                 :var-property "dialog-field-907be08c-546a-4528-b386-f333cae4b999"}]}],
                                                               :type               "sequence-data",
                                                               :phrase             "introduce-image-dialog",
                                                               :concept-var        "current-concept",
                                                               :editor-type        "dialog",
                                                               :phrase-description "Introduce image dialog"},
                                 :glow-big                    {:id "glow", :type "state", :target "letter-big"},
                                 :describe-writing            {:data                          [{:data [{:type "empty", :duration 1000}
                                                                                                       {:end         22.408,
                                                                                                        :data        [{:end 22.32, :anim "talk", :start 21.86}],
                                                                                                        :type        "animation-sequence",
                                                                                                        :audio       "/upload/VEQRANHKKEMOIUFU.mp3",
                                                                                                        :start       21.851,
                                                                                                        :target      "senora-vaca",
                                                                                                        :duration    0.557,
                                                                                                        :phrase-text "The letter "}],
                                                                                                :type "sequence-data"}
                                                                                               {:type     "action",
                                                                                                :from-var [{:var-name     "current-concept",
                                                                                                            :var-property "dialog-field-8d4ef5e3-6e37-4a69-988f-de8cf45903f9"}]}
                                                                                               {:data [{:type "empty", :duration 500}
                                                                                                       {:phrase-text            "goes ",
                                                                                                        :start                  34.634,
                                                                                                        :type                   "animation-sequence",
                                                                                                        :duration               0.58,
                                                                                                        :region-text            "goes",
                                                                                                        :audio                  "/upload/VEQRANHKKEMOIUFU.mp3",
                                                                                                        :target                 "senora-vaca",
                                                                                                        :end                    35.214,
                                                                                                        :phrase-text-translated "",
                                                                                                        :data                   [{:end 35.09, :anim "talk", :start 34.74}]}],
                                                                                                :type "sequence-data"}
                                                                                               {:type     "action",
                                                                                                :from-var [{:var-name     "current-concept",
                                                                                                            :var-property "dialog-field-136fa9c0-4bde-45a0-a7aa-f933ab1cbdeb"}]}
                                                                                               {:type     "action",
                                                                                                :from-var [{:var-name     "current-concept",
                                                                                                            :var-property "dialog-field-45d4b490-d167-4992-bf64-3487e8dbd862"}]}
                                                                                               {:data [{:type "empty", :duration 500}
                                                                                                       {:end         24.15,
                                                                                                        :data        [{:end 24.01, :anim "talk", :start 23.66}],
                                                                                                        :type        "animation-sequence",
                                                                                                        :audio       "/upload/VEQRANHKKEMOIUFU.mp3",
                                                                                                        :start       23.61,
                                                                                                        :target      "senora-vaca",
                                                                                                        :duration    0.5399999999999991,
                                                                                                        :phrase-text "goes"}],
                                                                                                :type "sequence-data"}
                                                                                               {:type     "action",
                                                                                                :from-var [{:var-name     "current-concept",
                                                                                                            :var-property "dialog-field-e51a7576-061f-4e94-bbd6-936fc7df466c"}]}
                                                                                               {:type     "action",
                                                                                                :from-var [{:var-name     "current-concept",
                                                                                                            :var-property "dialog-field-7dcb4949-4ae7-4947-bdb9-e744399aef73"}]}
                                                                                               {:data [{:type "empty", :duration 500}
                                                                                                       {:end         42.63,
                                                                                                        :data        [{:end 41.34, :anim "talk", :start 40.08}
                                                                                                                      {:end 42.56, :anim "talk", :start 41.82}],
                                                                                                        :type        "animation-sequence",
                                                                                                        :audio       "/upload/VEQRANHKKEMOIUFU.mp3",
                                                                                                        :start       39.93,
                                                                                                        :target      "senora-vaca",
                                                                                                        :duration    3.253,
                                                                                                        :phrase-text "has a special sound it says"}],
                                                                                                :type "sequence-data"}
                                                                                               {:type     "action",
                                                                                                :from-var [{:var-name     "current-concept",
                                                                                                            :var-property "dialog-field-fbc892e9-258e-4a27-be9e-eb4d7011d394"}]}
                                                                                               {:data [{:type "empty", :duration 500}
                                                                                                       {:end         45.66,
                                                                                                        :data        [{:end 45.65, :anim "talk", :start 44.71}],
                                                                                                        :type        "animation-sequence",
                                                                                                        :audio       "/upload/VEQRANHKKEMOIUFU.mp3",
                                                                                                        :start       44.623,
                                                                                                        :target      "senora-vaca",
                                                                                                        :duration    1.3,
                                                                                                        :phrase-text "Say the"}],
                                                                                                :type "sequence-data"}
                                                                                               {:type     "action",
                                                                                                :from-var [{:var-name     "current-concept",
                                                                                                            :var-property "dialog-field-2b1330ce-7745-4a8e-87cc-7e77398939b2"}]}
                                                                                               {:data [{:type "empty", :duration 500}
                                                                                                       {:end         47.537,
                                                                                                        :data        [{:end 47.5, :anim "talk", :start 46.53}],
                                                                                                        :type        "animation-sequence",
                                                                                                        :audio       "/upload/VEQRANHKKEMOIUFU.mp3",
                                                                                                        :start       46.41,
                                                                                                        :target      "senora-vaca",
                                                                                                        :duration    1.127,
                                                                                                        :phrase-text "sound with me"}],
                                                                                                :type "sequence-data"}
                                                                                               {:type     "action",
                                                                                                :from-var [{:var-name     "current-concept",
                                                                                                            :var-property "dialog-field-02c7dada-5129-47cd-9f16-31298a67dca9"}]}],
                                                               :type                          "sequence-data",
                                                               :phrase                        "describe-writing",
                                                               :concept-var                   "current-concept",
                                                               :editor-type                   "dialog",
                                                               :phrase-description            "Describe writing",
                                                               :available-activities          ["redraw-letter"],
                                                               :phrase-description-translated "Describe letter shape"},
                                 :whole-word                  {:data [{:id "visible", :type "state", :target "word"} {:id "whole-word-dialog", :type "action"}],
                                                               :type "sequence-data"},
                                 :show-uploaded-image-3       {:type       "set-attribute",
                                                               :target     "uploaded-image-3",
                                                               :attr-name  "visible",
                                                               :attr-value true},
                                 :dialog-tap-instructions     {:data               [{:data [{:type "empty", :duration 0}
                                                                                            {:phrase-text "Tap the lowercase letter",
                                                                                             :start       0.15,
                                                                                             :type        "animation-sequence",
                                                                                             :duration    2.1,
                                                                                             :region-text "tap the lower case letter",
                                                                                             :audio       "/upload/GRKUBPLXJBVKOFSI.mp3",
                                                                                             :target      "guide",
                                                                                             :end         2.25,
                                                                                             :data        [{:end 2.15, :anim "talk", :start 0.25}]}],
                                                                                     :type "sequence-data"}
                                                                                    {:type     "action",
                                                                                     :from-var [{:var-name     nil,
                                                                                                 :var-property "dialog-field-4963d2fe-4d6d-475b-963e-cb9914ce58e2"}]}
                                                                                    {:data [{:type "empty", :duration 0}
                                                                                            {:phrase-text        "at the beginning of the word",
                                                                                             :start              3.42,
                                                                                             :type               "animation-sequence",
                                                                                             :duration           2.0700000000000003,
                                                                                             :region-text        "at the beginning of the word",
                                                                                             :audio              "/upload/GRKUBPLXJBVKOFSI.mp3",
                                                                                             :target             "guide",
                                                                                             :end                5.49,
                                                                                             :phrase-placeholder "Enter phrase text",
                                                                                             :data               [{:end 3.63, :anim "talk", :start 3.42}
                                                                                                                  {:end 3.72, :anim "talk", :start 3.63}
                                                                                                                  {:end 4.47, :anim "talk", :start 3.72}
                                                                                                                  {:end 4.8, :anim "talk", :start 4.53}
                                                                                                                  {:end 4.92, :anim "talk", :start 4.8}
                                                                                                                  {:end 5.49, :anim "talk", :start 4.92}]}],
                                                                                     :type "sequence-data"}],
                                                               :type               "sequence-data",
                                                               :phrase             "Tap instructions",
                                                               :editor-type        "dialog",
                                                               :phrase-description "Tap instructions"},
                                 :start-activity              {:type "start-activity"},
                                 :start-scene                 {:data        ["start-activity"
                                                                             "renew-concept"
                                                                             "init-state"
                                                                             "introduce-big-small"
                                                                             "writing-task"
                                                                             "introduce-image"
                                                                             "whole-word"
                                                                             "start-timeout"],
                                                               :type        "sequence",
                                                               :unique-tag  "instruction",
                                                               :description "Initial action"},
                                 :mari-init-wand              {:id "wand_idle", :loop true, :type "add-animation", :track 2, :target "mari"},
                                 :highlight-big-letter        {:to                 {:yoyo true, :opacity 0.38, :duration 0.5},
                                                               :from               {:opacity 1},
                                                               :type               "transition",
                                                               :kill-after         3000,
                                                               :transition-id      "letter-big",
                                                               :return-immediately true}},
                 :objects       {:senora-vaca      {:y          1000,
                                                    :speed      0.3,
                                                    :name       "senoravaca",
                                                    :width      351,
                                                    :start      true,
                                                    :type       "animation",
                                                    :anim       "idle",
                                                    :x          380,
                                                    :scene-name "senora-vaca",
                                                    :skin       "vaca",
                                                    :height     717},
                                 :letter-small     {:y           260,
                                                    :align       "center",
                                                    :states      {:glow      {:permanent-pulsation {:speed 2}},
                                                                  :hidden    {:visible false},
                                                                  :visible   {:visible true},
                                                                  :stop-glow {:permanent-pulsation false}},
                                                    :font-size   120,
                                                    :transition  "letter-small",
                                                    :fill        "#ef545c",
                                                    :width       150,
                                                    :type        "text",
                                                    :x           885,
                                                    :font-family "Lexend Deca",
                                                    :height      130,
                                                    :text        ""},
                                 :background       {:type       "layered-background",
                                                    :surface    {:src "/raw/clipart/casa/casa_surface.png"},
                                                    :background {:src "/raw/clipart/casa/casa_background.png"},
                                                    :decoration {:src "/raw/clipart/casa/casa_decoration.png"}},
                                 :canvas           {:x       650,
                                                    :y       130,
                                                    :src     "/raw/img/casa/painting_canvas.png",
                                                    :type    "image",
                                                    :width   287,
                                                    :height  430,
                                                    :scale-x 2,
                                                    :scale-y 2},
                                 :letter-path      {:y            230,
                                                    :path         "",
                                                    :stroke       "#ef545c",
                                                    :animation    "stop",
                                                    :states       {:hidden {:visible false}, :visible {:visible true}},
                                                    :width        325,
                                                    :type         "animated-svg-path",
                                                    :duration     5000,
                                                    :stroke-width 15,
                                                    :scale-y      0.65,
                                                    :x            870,
                                                    :scale-x      0.65,
                                                    :visible      false,
                                                    :scene-name   "letter-path",
                                                    :line-cap     "round",
                                                    :height       300},
                                 :letter-big       {:y           260,
                                                    :align       "center",
                                                    :states      {:glow      {:permanent-pulsation {:speed 2}},
                                                                  :hidden    {:visible false},
                                                                  :visible   {:visible true},
                                                                  :stop-glow {:permanent-pulsation false}},
                                                    :font-size   120,
                                                    :transition  "letter-big",
                                                    :fill        "#ef545c",
                                                    :width       165,
                                                    :type        "text",
                                                    :x           720,
                                                    :scene-name  "letter-big",
                                                    :font-family "Lexend Deca",
                                                    :height      130,
                                                    :text        ""},
                                 :word             {:y              450,
                                                    :align          "center",
                                                    :vertical-align "bottom",
                                                    :states         {:glow      {:permanent-pulsation {:speed 2}},
                                                                     :hidden    {:visible false},
                                                                     :visible   {:visible true},
                                                                     :stop-glow {:permanent-pulsation false}},
                                                    :font-size      80,
                                                    :fill           "#fc8e51",
                                                    :width          470,
                                                    :type           "text",
                                                    :chunks         [{:end         1,
                                                                      :fill        "#ef545c",
                                                                      :start       0,
                                                                      :actions     {:click {:id "word-click", :on "click", :type "action", :unique-tag "click"}},
                                                                      :font-weight "bold"}
                                                                     {:end "last", :start 1}],
                                                    :x              720,
                                                    :font-family    "Lexend Deca",
                                                    :height         60,
                                                    :text           ""},
                                 :image            {:y          502,
                                                    :states     {:hidden {:visible false}, :visible {:visible true}, :init-position {:x 1805, :y 502}},
                                                    :transition "image",
                                                    :width      100,
                                                    :type       "image",
                                                    :src        "",
                                                    :scale-y    1.3,
                                                    :x          1805,
                                                    :scale-x    1.3,
                                                    :height     100},
                                 :mari             {:y          635,
                                                    :transition "mari",
                                                    :speed      0.35,
                                                    :name       "mari",
                                                    :width      473,
                                                    :start      true,
                                                    :type       "animation",
                                                    :anim       "idle",
                                                    :scale-y    0.5,
                                                    :x          1600,
                                                    :scale-x    0.5,
                                                    :height     511},
                                 :uploaded-image-3 {:y         540,
                                                    :scale     {:x 1, :y 1},
                                                    :editable? {:drag true, :select true, :show-in-tree? true},
                                                    :type      "image",
                                                    :src       "/upload/LBZPCIVJZITFMOKG.png",
                                                    :alias     "Preview image",
                                                    :origin    {:type "center-center"},
                                                    :x         960,
                                                    :visible   false,
                                                    :metadata  {:uploaded-image? true},
                                                    :links     [{:id "show-uploaded-image-3", :type "action"}
                                                                {:id "hide-uploaded-image-3", :type "action"}]}},
                 :metadata      {:template-name      "Letter intro",
                                 :tracks             [{:nodes [{:text "Introduce big and small letters", :type "prompt"}
                                                               {:type "dialog", :action-id "introduce-big-small"}
                                                               {:text "Describe writing task. Letter is written on desk", :type "prompt"}
                                                               {:type "dialog", :action-id "describe-writing"}
                                                               {:text "Image from concept is moved to the desk", :type "prompt"}
                                                               {:type "dialog", :action-id "introduce-image-dialog"}
                                                               {:text "Show word from concept", :type "prompt"}
                                                               {:type "dialog", :action-id "whole-word-dialog"}
                                                               {:text "When correct letter from word is clicked", :type "prompt"}
                                                               {:type "dialog", :action-id "correct-response-dialog"}
                                                               {:text "After inactivity timeout", :type "prompt"}
                                                               {:type "dialog", :action-id "dialog-wrong-answer"}],
                                                       :title "Dialogs"}],
                                 :history            {:created {:template-id 39},
                                                      :updated [{:data           {:background-music {:src "/upload/QLMVNPSNFGXZYHGX.mp3", :volume "0.23"}},
                                                                 :action         "background-music",
                                                                 :common-action? true}
                                                                {:data           {:character "vera", :show-guide false},
                                                                 :action         "set-guide-settings",
                                                                 :common-action? true}
                                                                {:data           {:name "Preview image", :image {:src "/upload/HPRWIZOOZJUDMEGY.png"}},
                                                                 :action         "add-image",
                                                                 :common-action? true}
                                                                {:data {:name "uploaded-image-1"}, :action "remove-image", :common-action? true}
                                                                {:data           {:name "Preview image", :image {:src "/upload/ORIQDGGFZWDFSDHP.png"}},
                                                                 :action         "add-image",
                                                                 :common-action? true}
                                                                {:data {:name "uploaded-image-2"}, :action "remove-image", :common-action? true}
                                                                {:data           {:name "Preview image", :image {:src "/upload/DAERZKMHJTREXJAN.png"}},
                                                                 :action         "add-image",
                                                                 :common-action? true}
                                                                {:data           {:character "vera", :show-guide true},
                                                                 :action         "set-guide-settings",
                                                                 :common-action? true}
                                                                {:data           {:character "vera", :show-guide false},
                                                                 :action         "set-guide-settings",
                                                                 :common-action? true}]},
                                 :template-version   nil,
                                 :uploaded-image-idx 3,
                                 :next-action-index  0,
                                 :lip-not-sync       false,
                                 :prev               "map",
                                 :guide-settings     {:character "vera", :show-guide false},
                                 :autostart          true,
                                 :unique-suffix      0,
                                 :template-id        39,
                                 :available-actions  [{:name   "Show Preview image",
                                                       :type   "image",
                                                       :links  [{:id "uploaded-image-3", :type "object"}],
                                                       :action "show-uploaded-image-3"}
                                                      {:name   "Hide Preview image",
                                                       :type   "image",
                                                       :links  [{:id "uploaded-image-3", :type "object"}],
                                                       :action "hide-uploaded-image-3"}]},
                 :triggers      {:back  {:on "back", :action "stop-activity"},
                                 :guide {:on "start", :action "init-guide"},
                                 :music {:on "start", :action "start-background-music"},
                                 :start {:on "start", :action "start-scene"}},
                 :variables     {:status nil},
                 :scene-objects [["background"]
                                 ["canvas"]
                                 ["letter-small" "letter-big" "word"]
                                 ["letter-path"]
                                 ["senora-vaca" "mari"]
                                 ["image" "uploaded-image-3"]]})
