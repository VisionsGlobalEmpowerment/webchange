(ns webchange.demo-scenes.park.park)

(def park-scene {:assets [{:url "/raw/img/park/main/background.jpg", :size 10, :type "image"}
                          {:url "/raw/img/park/main/HideAndSeek_Enabled.png", :size 1, :type "image"}
                          {:url "/raw/img/park/main/Pinata_Enabled.png", :size 1, :type "image"}
                          {:url "/raw/img/park/main/Sandbox_Enabled.png", :size 1, :type "image"}
                          {:url "/raw/img/park/main/SeeSaw_Enabled.png", :size 1, :type "image"}
                          {:url "/raw/img/park/main/Slide_Enabled.png", :size 1, :type "image"}
                          {:url "/raw/img/park/main/Swing_Enabled.png", :size 1, :type "image"}],

                :objects {:background {:type "background", :src "/raw/img/park/main/background.jpg"},
                          :vera       {:type "animation" :x 1382 :y 886 :name "vera" :anim "idle" :speed 0.3
                                       :width 1800 :height 2558 :scale {:x 0.17 :y 0.17} :start true}

                          :see-saw {:type "image" :x 124 :y 811 :width 605 :height 188 :src "/raw/img/park/main/SeeSaw_Enabled.png"
                                 :actions {:click {:type "scene", :scene-id "see-saw", :on "click"}}}
                          :swings {:type "image" :x 1571 :y 214 :width 191 :height 471 :src "/raw/img/park/main/Swing_Enabled.png"
                                    :actions {:click {:type "scene", :scene-id "swings", :on "click"}}}
                          :sandbox {:type "image" :x 782 :y 772 :width 499 :height 214 :src "/raw/img/park/main/Sandbox_Enabled.png"
                                    :actions {:click {:type "scene", :scene-id "sandbox", :on "click"}}}

                          :slide {:type "image" :x 36 :y 252 :width 661 :height 551 :src "/raw/img/park/main/Slide_Enabled.png"}
                          :pinata {:type "image" :x 562 :y 0 :width 338 :height 477 :src "/raw/img/park/main/Pinata_Enabled.png"}
                          :hide-n-seek {:type "image" :x 908 :y 422 :width 207 :height 288 :src "/raw/img/park/main/HideAndSeek_Enabled.png"}
                          },

                :scene-objects [["background" "see-saw" "swings" "sandbox" "slide" "pinata" "hide-n-seek"] ["vera"]],
                :actions {:start-background-music {:type "audio" :id "background" :loop true}},
                :audio {:background "/raw/audio/background/POL-daily-special-short.mp3"}
                :triggers      {:music {:on "start" :action "start-background-music"}}
                :metadata      {:autostart true
                                :prev "map"}})
