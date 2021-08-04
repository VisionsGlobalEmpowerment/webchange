(ns webchange.question.common.params)

(def template-size {:x      0
                    :y      0
                    :width  1920
                    :height 1080})

(def background-color 0xFFA301)
(def substrate-color 0xFFFFFF)

(def block-padding 60)

(def font-size 48)
(def font-size-task-text font-size)

(def voice-over-button-margin 30)

(def check-button-size 80)

(def voice-over {:size   80
                 :margin 30})

(def options {:gap 60})

(def option {:background-color 0xFFFFFF
             :border-width     2
             :border-radius    {:text  20
                                :image 20}
             :border-color     {:default 0x000000
                                :active  0x00c3ff}
             :padding          {:text 20}
             :text             {:height    100
                                :min-width 0
                                :max-width 1920}
             :mark-size        250})

(def check-button {:size 80})

(def options-gap 30)


(def option-font-size font-size)
(def option-padding 10)


