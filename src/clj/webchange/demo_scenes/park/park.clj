(ns webchange.demo-scenes.park.park)

(def park-scene {:assets
                         [{:url  "/raw/audio/background/POL-daily-special-short.mp3" :size 10 :type "audio"}
                          {:url  "/raw/audio/effects/NFF-fruit-collected.mp3" :size 1 :type "audio"}
                          {:url "/raw/audio/effects/NFF-glitter.mp3", :size 1, :type "audio"}
                          {:url  "/raw/audio/effects/NFF-robo-elastic.mp3" :size 1 :type "audio"}
                          {:url  "/raw/audio/effects/NFF-rusted-thing.mp3" :size 1 :type "audio"}
                          {:url "/raw/audio/effects/NFF-zing.mp3", :size 1, :type "audio"}
                          {:url "/raw/img/ui/back_button_01.png", :size 1, :type "image"}
                          {:url "/raw/img/ui/back_button_02.png", :size 1, :type "image"}
                          {:url "/raw/img/ui/close_button_01.png", :size 1, :type "image"}
                          {:url "/raw/img/ui/close_button_02.png", :size 1, :type "image"}
                          {:url "/raw/img/ui/play_button_01.png", :size 1, :type "image"}
                          {:url "/raw/img/ui/play_button_02.png", :size 1, :type "image"}
                          {:url "/raw/img/ui/reload_button_01.png", :size 1, :type "image"}
                          {:url "/raw/img/ui/reload_button_02.png", :size 1, :type "image"}
                          {:url "/raw/img/ui/settings_button_01.png", :size 1, :type "image"}
                          {:url "/raw/img/ui/settings_button_02.png", :size 1, :type "image"}
                          {:url "/raw/img/ui/star_01.png", :size 1, :type "image"}
                          {:url "/raw/img/ui/star_02.png", :size 1, :type "image"}
                          {:url "/raw/img/ui/star_03.png", :size 1, :type "image"}

                          {:url "/raw/img/park/background.jpg", :size 10, :type "image"}

                          {:url "/raw/anim/vera/skeleton.atlas", :size 1, :type "anim-text"}
                          {:url "/raw/anim/vera/skeleton.json", :size 1, :type "anim-text"}
                          {:url "/raw/anim/vera/skeleton.png", :size 1, :type "anim-texture"}
                          {:url "/raw/anim/vera/skeleton2.png", :size 1, :type "anim-texture"}
                          {:url "/raw/anim/vera/skeleton3.png", :size 1, :type "anim-texture"}],

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
