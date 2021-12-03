(ns webchange.templates.library.categorize.round-0)

(def template-round-0 {:assets        [{:url "/raw/img/categorize/01.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize/02.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize/03.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize/yellow_table.png", :size 1, :type "image"}
                                       {:url "/raw/img/categorize/blue_table.png", :size 1, :type "image"}
                                       {:url "/raw/img/categorize/red_table.png", :size 1, :type "image"}
                                       {:url "/raw/img/categorize/question.png", :size 1, :type "image"}],
                       :objects       {:layered-background {:type       "layered-background",
                                                            :background {:src "/raw/img/categorize/01.png"},
                                                            :decoration {:src "/raw/img/categorize/03.png"},
                                                            :surface    {:src "/raw/img/categorize/02.png"}}
                                       :yellow-table       {:type  "image",
                                                            :x     745,
                                                            :y     773,
                                                            :scale 0.8,
                                                            :src   "/raw/img/categorize/yellow_table.png"},
                                       :blue-table         {:type  "image",
                                                            :x     330,
                                                            :y     667,
                                                            :scale 0.8,
                                                            :src   "/raw/img/categorize/blue_table.png"}
                                       :red-table          {:type  "image",
                                                            :x     1160,
                                                            :y     652,
                                                            :scale 0.8,
                                                            :src   "/raw/img/categorize/red_table.png"},
                                       :librarian        {:type   "animation",
                                                          :x      380,
                                                          :y      1000,
                                                          :width  351,
                                                          :height 717,
                                                          :anim   "idle",
                                                          :name   "senoravaca",
                                                          :skin   "lion",
                                                          :speed  0.3,
                                                          :editable? {:select true
                                                                      :show-in-tree? true}
                                                          :start  true}},
                       :scene-objects [["layered-background"]
                                       ["yellow-table" "blue-table" "red-table"]
                                       ["librarian"]]
                       :actions       {:init-activity {:type "sequence-data",
                                                       :data [{:type "start-activity"}
                                                              {:type "action" :id "voiceover"}
                                                              {:type "action" :id "next-round"}]}
                                       :voiceover     {:type               "sequence-data",
                                                       :editor-type        "dialog",
                                                       :data               [{:type "sequence-data"
                                                                             :data [{:type "empty" :duration 0}
                                                                                    {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                       :phrase             "Introduce Game",
                                                       :phrase-description "Introduce Game"}
                                       :stop-activity {:type "stop-activity"},
                                       :finish-activity {:type "finish-activity"}
                                       :next-round    {:type "action" :id "finish-scene"}
                                       :finish-scene  {:type "sequence",
                                                       :data ["finish-activity"]}}

                       :triggers
                       {:back  {:on "back", :action "stop-activity"},
                        :start {:on "start", :action "init-activity"}},
                       :metadata      {:autostart   true
                                       :tracks      [{:title "Welcome"
                                                      :nodes [{:type      "dialog"
                                                               :action-id :voiceover}]}]}})
