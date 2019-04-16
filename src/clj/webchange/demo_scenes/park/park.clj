(ns webchange.demo-scenes.park.park)

(def park-scene {:assets [{:url "/raw/img/park/background.jpg", :size 10, :type "image"}],

                :objects {:background {:type "background", :src "/raw/img/park/background.jpg"},
                          :vera       {:type "animation" :x 1392 :y 972 :name "vera" :anim "idle" :speed 0.3
                                       :width 1800 :height 2558 :scale {:x 0.17 :y 0.17} :start true}

                          :see-saw {:type "transparent" :x 113 :y 766 :width 600 :height 250
                                 :actions {:click {:type "scene", :scene-id "see-saw", :on "click"}}}
                          :swings {:type "transparent" :x 1551 :y 364 :width 250 :height 350
                                    :actions {:click {:type "scene", :scene-id "swings", :on "click"}}}
                          :sandbox {:type "transparent" :x 734 :y 719 :width 500 :height 350
                                    :actions {:click {:type "scene", :scene-id "sandbox", :on "click"}}}
                          },

                :scene-objects [["background" "see-saw" "swings" "sandbox"] ["vera"]],
                :actions {:start-background-music {:type "audio" :id "background" :loop true}},
                :audio {:background "/raw/audio/background/POL-daily-special-short.mp3"}
                :triggers      {:music {:on "start" :action "start-background-music"}}
                :metadata      {:autostart true
                                :prev "map"}})
