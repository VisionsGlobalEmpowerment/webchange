(ns webchange.interpreter.defaults)

(def default-assets [{:url "/raw/audio/background/POL-daily-special-short.mp3" :size 10 :type "audio"}
                     {:url "/raw/audio/effects/NFF-fruit-collected.mp3" :size 1 :type "audio"}
                     {:url "/raw/audio/effects/NFF-glitter.mp3", :size 1, :type "audio"}
                     {:url "/raw/audio/effects/NFF-robo-elastic.mp3" :size 1 :type "audio"}
                     {:url "/raw/audio/effects/NFF-rusted-thing.mp3" :size 1 :type "audio"}
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
                     {:url "/raw/img/ui/settings/music.png", :size 1, :type "image"}
                     {:url "/raw/img/ui/settings/music_icon.png", :size 1, :type "image"}
                     {:url "/raw/img/ui/settings/sound_fx.png", :size 1, :type "image"}
                     {:url "/raw/img/ui/settings/sound_fx_icon.png", :size 1, :type "image"}
                     {:url "/raw/img/ui/settings/settings.png", :size 1, :type "image"}
                     {:url "/raw/img/ui/hand.png", :size 1, :type "image"}
                     {:url "/raw/img/bg.jpg", :size 1, :type "image"}
                     {:url "/raw/img/ui/logo.png", :size 1, :type "image"}

                     {:url "/raw/anim/senoravaca/skeleton.atlas", :size 1, :type "anim-text"}
                     {:url "/raw/anim/senoravaca/skeleton.json", :size 1, :type "anim-text"}
                     {:url "/raw/anim/senoravaca/skeleton.png", :size 1, :type "anim-texture"}
                     {:url "/raw/anim/senoravaca/skeleton2.png", :size 1, :type "anim-texture"}
                     {:url "/raw/anim/senoravaca/skeleton3.png", :size 1, :type "anim-texture"}
                     {:url "/raw/anim/senoravaca/skeleton4.png", :size 1, :type "anim-texture"}
                     {:url "/raw/anim/senoravaca/skeleton5.png", :size 1, :type "anim-texture"}
                     {:url "/raw/anim/senoravaca/skeleton6.png", :size 1, :type "anim-texture"}
                     {:url "/raw/anim/senoravaca/skeleton7.png", :size 1, :type "anim-texture"}
                     {:url "/raw/anim/senoravaca/skeleton8.png", :size 1, :type "anim-texture"}
                     {:url "/raw/anim/senoravaca/skeleton9.png", :size 1, :type "anim-texture"}
                     {:url "/raw/anim/senoravaca/skeleton10.png", :size 1, :type "anim-texture"}

                     {:url "/raw/anim/vera/skeleton.atlas", :size 1, :type "anim-text"}
                     {:url "/raw/anim/vera/skeleton.json", :size 1, :type "anim-text"}
                     {:url "/raw/anim/vera/skeleton.png", :size 1, :type "anim-texture"}
                     {:url "/raw/anim/vera/skeleton2.png", :size 1, :type "anim-texture"}
                     {:url "/raw/anim/vera/skeleton3.png", :size 1, :type "anim-texture"}

                     {:url "/raw/anim/vera-go/skeleton.atlas", :size 1, :type "anim-text"}
                     {:url "/raw/anim/vera-go/skeleton.json", :size 1, :type "anim-text"}
                     {:url "/raw/anim/vera-go/skeleton.png", :size 1, :type "anim-texture"}
                     {:url "/raw/anim/vera-go/skeleton2.png", :size 1, :type "anim-texture"}
                     {:url "/raw/anim/vera-go/skeleton3.png", :size 1, :type "anim-texture"}

                     {:url "/raw/anim/vera-45/skeleton.atlas", :size 1, :type "anim-text"}
                     {:url "/raw/anim/vera-45/skeleton.json", :size 1, :type "anim-text"}
                     {:url "/raw/anim/vera-45/skeleton.png", :size 1, :type "anim-texture"}
                     {:url "/raw/anim/vera-45/skeleton2.png", :size 1, :type "anim-texture"}
                     {:url "/raw/anim/vera-45/skeleton3.png", :size 1, :type "anim-texture"}
                     {:url "/raw/anim/vera-45/skeleton4.png", :size 1, :type "anim-texture"}
                     {:url "/raw/anim/vera-45/skeleton5.png", :size 1, :type "anim-texture"}
                     {:url "/raw/anim/vera-45/skeleton6.png", :size 1, :type "anim-texture"}
                     {:url "/raw/anim/vera-45/skeleton7.png", :size 1, :type "anim-texture"}

                     {:url "/raw/anim/vera-90/skeleton.atlas", :size 1, :type "anim-text"}
                     {:url "/raw/anim/vera-90/skeleton.json", :size 1, :type "anim-text"}
                     {:url "/raw/anim/vera-90/skeleton.png", :size 1, :type "anim-texture"}
                     {:url "/raw/anim/vera-90/skeleton2.png", :size 1, :type "anim-texture"}
                     {:url "/raw/anim/vera-90/skeleton3.png", :size 1, :type "anim-texture"}

                     {:url "/raw/anim/rock/skeleton.atlas", :size 1, :type "anim-text"}
                     {:url "/raw/anim/rock/skeleton.json", :size 1, :type "anim-text"}
                     {:url "/raw/anim/rock/skeleton.png", :size 1, :type "anim-texture"}
                     {:url "/raw/anim/rock/skeleton2.png", :size 1, :type "anim-texture"}

                     {:url "/raw/anim/mari/skeleton.atlas", :size 1, :type "anim-text"}
                     {:url "/raw/anim/mari/skeleton.json", :size 1, :type "anim-text"}
                     {:url "/raw/anim/mari/skeleton.png", :size 1, :type "anim-texture"}

                     {:url "/raw/anim/boxes/skeleton.atlas", :size 1, :type "anim-text"}
                     {:url "/raw/anim/boxes/skeleton.json", :size 1, :type "anim-text"}
                     {:url "/raw/anim/boxes/skeleton.png", :size 1, :type "anim-texture"}

                     {:url "/raw/anim/book/skeleton.atlas", :size 1, :type "anim-text"}
                     {:url "/raw/anim/book/skeleton.json", :size 1, :type "anim-text"}
                     {:url "/raw/anim/book/skeleton.png", :size 1, :type "anim-texture"}
                     {:url "/raw/anim/book/skeleton2.png", :size 1, :type "anim-texture"}])
