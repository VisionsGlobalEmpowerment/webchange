(ns webchange.templates.library.categorize-synonyms.round-0)

(def template-round-0 {:assets        [{:url "/raw/img/categorize/01.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize/02.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize/03.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize/yellow_table.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize/blue_table.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize/red_table.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize/blue_crayons.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize/question.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize/red_crayons.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize/yellow_crayons.png", :size 10, :type "image"}],
                       :objects       {:layered-background {:type       "layered-background",
                                                            :background {:src "/raw/img/categorize/01.png"},
                                                            :decoration {:src "/raw/img/categorize/03.png"},
                                                            :surface    {:src "/raw/img/categorize/02.png"}
                                                            }
                                       :yellow-table
                                                           {:type       "image",
                                                            :x          745,
                                                            :y          773,
                                                            :width      428,
                                                            :height     549,
                                                            :scale      0.8,
                                                            :src        "/raw/img/categorize/yellow_table.png",
                                                            :transition "yellow-table",
                                                            :states     {:highlighted {:highlight true} :not-highlighted {:highlight false}},
                                                            },
                                       :blue-table         {:type       "image",
                                                            :x          330,
                                                            :y          667,
                                                            :width      428,
                                                            :height     549,
                                                            :scale      0.8,
                                                            :transition "blue-table",
                                                            :src        "/raw/img/categorize/blue_table.png",
                                                            :states     {:highlighted {:highlight true} :not-highlighted {:highlight false}},
                                                            }
                                       :red-table          {:type       "image",
                                                            :x          1160,
                                                            :y          652,
                                                            :width      428,
                                                            :height     549,
                                                            :scale      0.8,
                                                            :transition "red-table",
                                                            :src        "/raw/img/categorize/red_table.png",
                                                            :states     {:highlighted {:highlight true} :not-highlighted {:highlight false}},
                                                            },
                                       :senora-vaca
                                                           {:type   "animation",
                                                            :x      380,
                                                            :y      1000,
                                                            :width  351,
                                                            :height 717,
                                                            :anim   "idle",
                                                            :name   "senoravaca",
                                                            :skin   "vaca",
                                                            :speed  0.3,
                                                            :start  true},
                                       },
                       :scene-objects [["layered-background"]
                                       ["yellow-table" "blue-table" "red-table"]
                                       ["senora-vaca"]
                                       ],
                       :actions       {

                                       :init-activity    {:type "sequence",
                                                          :data ["voiceover" "next-round"],
                                                          }
                                       :voiceover {:type               "sequence-data",
                                                          :editor-type        "dialog",
                                                          :data               [{:type "sequence-data"
                                                                                :data [{:type "empty" :duration 0}
                                                                                       {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                          :phrase             "Introduce Game",
                                                          :phrase-description "Introduce Game"}
                                       :stop-activity    {:type "stop-activity", :id "categorize"},
                                       :next-round {:type "action" :id "finish-scene"}
                                       :finish-scene    {:type "sequence",
                                                          :data ["stop-activity"]}
                                       },

                       :triggers
                                      {:back  {:on "back", :action "stop-activity"},
                                       :start {:on "start", :action "init-activity"}},
                       :metadata      {:autostart   true
                                       :last-insert "technical-question-placeholder"
                                       :tracks      [{:title "Welcome"
                                                      :nodes [{:type      "dialog"
                                                               :action-id :voiceover}]}]},
                       })