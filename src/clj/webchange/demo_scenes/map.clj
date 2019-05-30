(ns webchange.demo-scenes.map)

(def map-scene {:assets [{:url "/raw/img/map/background.jpg", :size 10, :type "image"}
                         {:url "/raw/img/map/casa_01.png", :size 1, :type "image"}
                         {:url "/raw/img/map/casa_02.png", :size 1, :type "image"}
                         {:url "/raw/img/map/feria_01.png", :size 1, :type "image"}
                         {:url "/raw/img/map/feria_02.png", :size 1, :type "image"}
                         {:url "/raw/img/map/feria_03.png", :size 1, :type "image"}
                         {:url "/raw/img/map/feria_locked.png", :size 1, :type "image"}
                         {:url "/raw/img/map/Biblioteca_Enabled.png", :size 1, :type "image"}
                         {:url "/raw/img/map/Estadio_Enabled.png", :size 1, :type "image"}
                         {:url "/raw/img/map/Parque_Enabled.png", :size 1, :type "image"}],

                :objects {:background {:type "background", :src "/raw/img/map/background.jpg"},
                          :vera {:type "animation" :scene-name "vera-go" :name "vera-45" :anim "go_front" :speed 0.5 :start false
                                 :x 1070, :y 665 :width 758 :height 1130 :scale {:x 0.15 :y 0.15} :skin "vera"
                                 :transition "vera-transition"
                                 :states {:init-home {:x 1000 :y 620}
                                          :init-feria {:x 590 :y 960}
                                          :init-park {:x 1325 :y 960}
                                          :init-library {:x 299 :y 576}
                                          :init-stadium {:x 1488 :y 399}}}
                          :home {:type "image" :x 731 :y 340 :width 433 :height 380
                                 :src    "/raw/img/map/casa_01.png",
                                 :states {:default {:type "image", :src "/raw/img/map/casa_01.png"},
                                          :hover   {:type "image", :src "/raw/img/map/casa_02.png"}},
                                 :actions {:click {:type "action", :id "move-to" :params {:scene-id "home"}, :on "click"}}}

                          :park {:type "image" :x 1337 :y 756 :width 346 :height 246 :src "/raw/img/map/Parque_Enabled.png"
                                 :actions {:click {:type "action", :id "move-to" :params {:scene-id "park"}, :on "click"}}}
                          :library {:type "image" :x 91 :y 250 :width 293 :height 373 :src "/raw/img/map/Biblioteca_Enabled.png"
                                    :actions {:click {:type "action", :id "move-to" :params {:scene-id "library"}, :on "click"}}}
                          :stadium {:type "image" :x 1481 :y 169 :width 234 :height 228 :src "/raw/img/map/Estadio_Enabled.png"
                                    :actions {:click {:type "action", :id "move-to" :params {:scene-id "stadium"}, :on "click"}}}
                          :feria {:type "image" :width 531 :height 455 :x 216 :y 671
                                  :scale {:x 0.68 :y 0.68}
                                  :src    "/raw/img/map/feria_locked.png"}},

                :scene-objects [["background" "home" "feria" "park" "library" "stadium"] ["vera"]],
                :actions
                 {:move-to {:type "test-value"
                            :from-params [{:action-property "value1" :param-property "scene-id"}]
                            :from-var [{:var-name "last-location" :action-property "value2"}]
                            :success "open-scene"
                            :fail "navigate-to-scene"}

                  :open-scene {:type "scene"
                               :from-params [{:action-property "scene-id" :param-property "scene-id"}]}

                  :navigate-to-scene {:type "sequence-data"
                                      :data [{:type "action"
                                              :from-var [{:template "move-from-%" :action-property "id" :var-name "last-location"}]}
                                             {:type "action"
                                              :from-params [{:template "move-to-%" :action-property "id" :param-property "scene-id"}]}
                                             {:type "set-progress" :var-name "last-location"
                                              :from-params [{:action-property "var-value" :param-property "scene-id"}]}
                                             {:type "scene"
                                              :from-params [{:action-property "scene-id" :param-property "scene-id"}]}]}

                  :move-from-home {:type "sequence-data"
                                   :data [{:type "start-animation" :target "vera-go"}
                                          {:type "animation-props" :target "vera-go" :props {:scaleX -1}}
                                          {:type "transition" :transition-id "vera-transition" :to {:x 1070 :y 665}}]}

                  :move-to-home {:type "sequence-data"
                                 :data [{:type "start-animation" :target "vera-go"}
                                        {:type "animation-props" :target "vera-go" :props {:scaleX 1}}
                                        {:type "transition" :transition-id "vera-transition" :to {:x 1000, :y 620}}]}

                  :move-from-feria {:type "sequence-data"
                                   :data [{:type "start-animation" :target "vera-go"}
                                          {:type "transition" :transition-id "vera-transition" :to {:x 875 :y 938}}
                                          {:type "transition" :transition-id "vera-transition" :to {:x 895 :y 851}}
                                          {:type "transition" :transition-id "vera-transition" :to {:x 975 :y 771}}
                                          {:type "transition" :transition-id "vera-transition" :to {:x 940, :y 725}}
                                          {:type "transition" :transition-id "vera-transition" :to {:x 1070 :y 665}}]}

                  :move-to-feria {:type "sequence-data",
                                  :data [{:type "start-animation" :target "vera-go"}
                                         {:type "transition" :transition-id "vera-transition" :to {:x 940, :y 725}}
                                         {:type "transition" :transition-id "vera-transition" :to {:x 975 :y 771}}
                                         {:type "transition" :transition-id "vera-transition" :to {:x 895 :y 851}}
                                         {:type "transition" :transition-id "vera-transition" :to {:x 875 :y 938}}
                                         {:type "transition" :transition-id "vera-transition" :to {:x 590 :y 960}}]}

                  :move-from-park {:type "sequence-data"
                                   :data [{:type "start-animation" :target "vera-go"}
                                          {:type "transition" :transition-id "vera-transition" :to {:x 915, :y 938}}
                                          {:type "transition" :transition-id "vera-transition" :to {:x 895, :y 851}}
                                          {:type "animation-props" :target "vera-go" :props {:scaleX -1}}
                                          {:type "transition" :transition-id "vera-transition" :to {:x 975 :y 771}}
                                          {:type "animation-props" :target "vera-go" :props {:scaleX 1}}
                                          {:type "transition" :transition-id "vera-transition" :to {:x 940, :y 725}}
                                          {:type "animation-props" :target "vera-go" :props {:scaleX -1}}
                                          {:type "transition" :transition-id "vera-transition" :to {:x 1070 :y 665}}]}

                  :move-to-park {:type "sequence-data"
                                 :data [{:type "start-animation" :target "vera-go"}
                                        {:type "animation-props" :target "vera-go" :props {:scaleX 1}}
                                        {:type "transition" :transition-id "vera-transition" :to {:x 940, :y 725}}
                                        {:type "animation-props" :target "vera-go" :props {:scaleX -1}}
                                        {:type "transition" :transition-id "vera-transition" :to {:x 975 :y 771}}
                                        {:type "animation-props" :target "vera-go" :props {:scaleX 1}}
                                        {:type "transition" :transition-id "vera-transition" :to {:x 895, :y 851}}
                                        {:type "animation-props" :target "vera-go" :props {:scaleX -1}}
                                        {:type "transition" :transition-id "vera-transition" :to {:x 915, :y 938}}
                                        {:type "transition" :transition-id "vera-transition" :to {:x 1325, :y 960}}]}

                  :move-from-library {:type "sequence-data"
                                      :data [{:type "start-animation" :target "vera-go"}
                                             {:type "transition" :transition-id "vera-transition" :to {:x 541, :y 644}}
                                             {:type "transition" :transition-id "vera-transition" :to {:x 691, :y 829}}
                                             {:type "transition" :transition-id "vera-transition" :to {:x 908, :y 829}}
                                             {:type "transition" :transition-id "vera-transition" :to {:x 975 :y 771}}
                                             {:type "transition" :transition-id "vera-transition" :to {:x 940, :y 725}}
                                             {:type "transition" :transition-id "vera-transition" :to {:x 1070 :y 665}}]}

                  :move-to-library {:type "sequence-data"
                                 :data [{:type "start-animation" :target "vera-go"}
                                        {:type "transition" :transition-id "vera-transition" :to {:x 940, :y 725}}
                                        {:type "animation-props" :target "vera-go" :props {:scaleX -1}}
                                        {:type "transition" :transition-id "vera-transition" :to {:x 975 :y 771}}
                                        {:type "animation-props" :target "vera-go" :props {:scaleX 1}}
                                        {:type "transition" :transition-id "vera-transition" :to {:x 908, :y 829}}
                                        {:type "transition" :transition-id "vera-transition" :to {:x 691, :y 829}}
                                        {:type "transition" :transition-id "vera-transition" :to {:x 541, :y 644}}
                                        {:type "transition" :transition-id "vera-transition" :to {:x 299, :y 576}}]}

                  :move-from-stadium {:type "sequence-data"
                                      :data [{:type "start-animation" :target "vera-go"}
                                             {:type "transition" :transition-id "vera-transition" :to {:x 1298, :y 450}}
                                             {:type "transition" :transition-id "vera-transition" :to {:x 1169, :y 454}}
                                             {:type "transition" :transition-id "vera-transition" :to {:x 1054 :y 531}}
                                             {:type "transition" :transition-id "vera-transition" :to {:x 1149, :y 583}}
                                             {:type "transition" :transition-id "vera-transition" :to {:x 1070 :y 665}}]}

                  :move-to-stadium {:type "sequence-data"
                                    :data [{:type "start-animation" :target "vera-go"}
                                           {:type "animation-props" :target "vera-go" :props {:scaleX -1}}
                                           {:type "transition" :transition-id "vera-transition" :to {:x 1149, :y 583}}
                                           {:type "animation-props" :target "vera-go" :props {:scaleX 1}}
                                           {:type "transition" :transition-id "vera-transition" :to {:x 1054 :y 531}}
                                           {:type "animation-props" :target "vera-go" :props {:scaleX -1}}
                                           {:type "transition" :transition-id "vera-transition" :to {:x 1169, :y 454}}
                                           {:type "transition" :transition-id "vera-transition" :to {:x 1298, :y 450}}
                                           {:type "transition" :transition-id "vera-transition" :to {:x 1488, :y 399}}]}

                  :move-from-volleyball {:type "sequence-data"
                                      :data [{:type "start-animation" :target "vera-go"}
                                             {:type "transition" :transition-id "vera-transition" :to {:x 1298, :y 450}}
                                             {:type "transition" :transition-id "vera-transition" :to {:x 1169, :y 454}}
                                             {:type "transition" :transition-id "vera-transition" :to {:x 1054 :y 531}}
                                             {:type "transition" :transition-id "vera-transition" :to {:x 1149, :y 583}}
                                             {:type "transition" :transition-id "vera-transition" :to {:x 1070 :y 665}}]}

                  :move-to-volleyball {:type "sequence-data"
                                    :data [{:type "start-animation" :target "vera-go"}
                                           {:type "animation-props" :target "vera-go" :props {:scaleX -1}}
                                           {:type "transition" :transition-id "vera-transition" :to {:x 1149, :y 583}}
                                           {:type "animation-props" :target "vera-go" :props {:scaleX 1}}
                                           {:type "transition" :transition-id "vera-transition" :to {:x 1054 :y 531}}
                                           {:type "animation-props" :target "vera-go" :props {:scaleX -1}}
                                           {:type "transition" :transition-id "vera-transition" :to {:x 1169, :y 454}}
                                           {:type "transition" :transition-id "vera-transition" :to {:x 1298, :y 450}}
                                           {:type "transition" :transition-id "vera-transition" :to {:x 1488, :y 399}}]}

                  :init {:type "sequence-data"
                         :data [{:type "set-variable" :var-name "last-location"
                                 :from-progress [{:action-property "var-value" :progress-property "last-location"}]}
                                {:type "case"
                                 :from-var [{:var-name "last-location" :action-property "value"}]
                                 :options {:home {:type "state" :target "vera" :id "init-home"}
                                           :feria {:type "state" :target "vera" :id "init-feria"}
                                           :park {:type "state" :target "vera" :id "init-park"}
                                           :library {:type "state" :target "vera" :id "init-library"}
                                           :stadium {:type "state" :target "vera" :id "init-stadium"}
                                           :volleyball {:type "state" :target "vera" :id "init-stadium"}}}]}

                  :start-background-music {:type "audio" :id "background" :loop true}},
                :audio {:background "/raw/audio/background/POL-daily-special-short.mp3"}
                :triggers      {:music {:on "start" :action "start-background-music"}
                                :init {:on "start" :action "init"}}
                :metadata      {:autostart true}})
