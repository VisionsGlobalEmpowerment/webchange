(ns webchange.editor-v2.activity-dialogs.stub-scene-data)

(def scene-data {:assets        []
                 :actions       {:introduce-big-small {:data                          [{:data [{:type "empty", :duration 0} {:type "hide-guide"}],
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
                                                       :phrase-description-translated "Introduction"}
                                 :describe-writing    {:data                          [{:data [{:type "empty", :duration 1000}
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
                                                       :phrase-description-translated "Describe letter shape"}
                                 :dialog-main         {:data               [{:data [{:type "empty", :duration 0}
                                                                                    {:id "question-2", :type "action"}],
                                                                             :type "sequence-data"}
                                                                            {:data [{:type "empty", :duration 0}
                                                                                    {:phrase-text        "¡Buena pregunta, Vera! ¡Yo me siento feliz cuando veo que mis estudiantes se ayudan entre ellos!",
                                                                                     :start              198.18,
                                                                                     :type               "animation-sequence",
                                                                                     :duration           8.909999999999997,
                                                                                     :region-text        "good question berra i feel happy when i see my students helping others",
                                                                                     :audio              "/upload/YEXSQZDYZUTCSDZP.mp3",
                                                                                     :target             "teacher",
                                                                                     :end                207.09,
                                                                                     :phrase-placeholder "Enter phrase text",
                                                                                     :data               [{:end 198.66, :anim "talk", :start 198.18}
                                                                                                          {:end 199.5, :anim "talk", :start 198.66}
                                                                                                          {:end 200.13, :anim "talk", :start 199.753184}
                                                                                                          {:end 200.97, :anim "talk", :start 200.64}
                                                                                                          {:end 201.21, :anim "talk", :start 201}
                                                                                                          {:end 201.81, :anim "talk", :start 201.21}
                                                                                                          {:end 202.38, :anim "talk", :start 201.87}
                                                                                                          {:end 203.25, :anim "talk", :start 202.8}
                                                                                                          {:end 203.61, :anim "talk", :start 203.25}
                                                                                                          {:end 203.79, :anim "talk", :start 203.64}
                                                                                                          {:end 204.03, :anim "talk", :start 203.79}
                                                                                                          {:end 205.05, :anim "talk", :start 204.03}
                                                                                                          {:end 205.44, :anim "talk", :start 205.2}
                                                                                                          {:end 206.04, :anim "talk", :start 205.44}
                                                                                                          {:end 206.52, :anim "talk", :start 206.04022}
                                                                                                          {:end 207.09, :anim "talk", :start 206.52}]}],
                                                                             :type "sequence-data"}
                                                                            {:data {:1 {:audio "/upload/GBNRFLRVPZSGFXJX.mp3"}}}],
                                                       :type               "sequence-data",
                                                       :phrase             "Main",
                                                       :editor-type        "dialog",
                                                       :phrase-description "Main"}},
                 :objects       {},
                 :metadata      {},
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
