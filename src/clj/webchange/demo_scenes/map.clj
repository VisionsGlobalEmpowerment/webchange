(ns webchange.demo-scenes.map)

(def map-scene {:assets
                               [{:url  "/raw/audio/background/POL-daily-special-short.mp3" :size 10 :type "audio"}
                                {:url  "/raw/audio/effects/NFF-fruit-collected.mp3" :size 1 :type "audio"}
                                {:url "/raw/audio/effects/NFF-glitter.mp3", :size 1, :type "audio"}
                                {:url  "/raw/audio/effects/NFF-robo-elastic.mp3" :size 1 :type "audio"}
                                {:url  "/raw/audio/effects/NFF-rusted-thing.mp3" :size 1 :type "audio"}
                                {:url "/raw/audio/effects/NFF-zing.mp3", :size 1, :type "audio"}
                                {:url "/raw/img/map/background.png", :size 10, :type "image"}
                                {:url "/raw/img/map/casa_01.png", :size 1, :type "image"}
                                {:url "/raw/img/map/casa_02.png", :size 1, :type "image"}
                                {:url "/raw/img/map/feria_01.png", :size 1, :type "image"}
                                {:url "/raw/img/map/feria_02.png", :size 1, :type "image"}
                                {:url "/raw/img/map/feria_03.png", :size 1, :type "image"}
                                {:url "/raw/img/map/feria_locked.png", :size 1, :type "image"}
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

                                {:url "/raw/anim/vera-go/skeleton.atlas", :size 1, :type "anim-text"}
                                {:url "/raw/anim/vera-go/skeleton.json", :size 1, :type "anim-text"}
                                {:url "/raw/anim/vera-go/skeleton.png", :size 1, :type "anim-texture"}
                                {:url "/raw/anim/vera-go/skeleton2.png", :size 1, :type "anim-texture"}],
                :objects
                               {:background {:type "background", :src "/raw/img/map/background.png"},
                                :vera {:type "animation" :name "vera-go" :anim "go_front" :speed 0.5 :start false
                                       :x 1070, :y 665 :scale {:x 0.15 :y 0.15} :width 752 :height 1175
                                       :transition "vera-transition"}
                                :home
                                            {:type "image" :x 731 :y 340 :width 433 :height 380
                                             :src    "/raw/img/map/casa_01.png",
                                             :states
                                             {:default {:type "image", :src "/raw/img/map/casa_01.png"},
                                              :hover   {:type "image", :src "/raw/img/map/casa_02.png"}},
                                             :actions
                                             {:mouseover
                                                     {:type "state", :target "home", :id "hover", :on "mouseover"},
                                              :mouseout
                                                     {:type "state", :target "home", :id "default", :on "mouseout"},
                                              :click {:type "action", :id "move-to-home", :on "click"}}},
                                :feria
                                            {:type "image" :x 235 :y 683 :width 319 :height 280
                                             :src    "/raw/img/map/feria_01.png",
                                             :states
                                             {:default {:type "image", :src "/raw/img/map/feria_01.png"},
                                              :hover   {:type "image", :src "/raw/img/map/feria_02.png"}},
                                             :actions
                                             {:mouseover
                                                     {:type "state", :target "feria", :id "hover", :on "mouseover"},
                                              :mouseout
                                                     {:type "state", :target "feria", :id "default", :on "mouseout"},
                                              :click {:type "action", :id "move-to-feria", :on "click"}}}},
                :scene-objects [["background" "home" "feria"] ["vera"]],
                :actions
                               {:start-movement {:type "start-animation" :target "vera-go"}
                                :move-to-feria-transition-1
                                                {:type          "transition",
                                                 :transition-id "vera-transition",
                                                 :to            {:x 940, :y 725}},
                                :open-home  {:type "scene", :scene-id "home"},
                                :move-to-feria-transition-4
                                                {:type          "transition",
                                                 :transition-id "vera-transition",
                                                 :to            {:x 875, :y 938}},
                                :move-to-home-transition
                                                {:type          "transition",
                                                 :transition-id "vera-transition",
                                                 :to            {:x 1000, :y 620}},
                                :open-feria {:type "scene", :scene-id "feria"},
                                :move-to-feria-transition-2
                                                {:type          "transition",
                                                 :transition-id "vera-transition",
                                                 :to            {:x 975 :y 771}},
                                :move-to-feria-transition-3
                                                {:type          "transition",
                                                 :transition-id "vera-transition",
                                                 :to            {:x 895, :y 851}},
                                :move-to-home
                                                {:type "sequence", :data ["start-movement" "move-to-home-transition" "open-home"]},
                                :move-to-feria-transition-5
                                                {:type          "transition",
                                                 :transition-id "vera-transition",
                                                 :to            {:x 590, :y 960}},
                                :move-to-feria
                                                {:type "sequence",
                                                 :data ["start-movement"
                                                        "move-to-feria-transition-1"
                                                        "move-to-feria-transition-2"
                                                        "move-to-feria-transition-3"
                                                        "move-to-feria-transition-4"
                                                        "move-to-feria-transition-5"
                                                        "open-feria"]}
                                :start-background-music {:type "audio" :id "background" :loop true}},
                :audio {:background "/raw/audio/background/POL-daily-special-short.mp3"}
                :triggers      {:music {:on "start" :action "start-background-music"}}
                :metadata      {:autostart true}})
