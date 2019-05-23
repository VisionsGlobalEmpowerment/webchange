(ns webchange.demo-scenes.library.book)

(def book-scene {:assets [{:url "/raw/img/library/book/background.jpg", :size 10, :type "image"}
                          {:url "/raw/img/library/book/background2.jpg", :size 10, :type "image"}

                          {:url "/raw/img/elements/squirrel.png" :size 1 :type "image"}
                          {:url "/raw/img/elements/bee.png" :size 1 :type "image"}
                          {:url "/raw/img/elements/tree.png" :size 1 :type "image"}
                          {:url "/raw/img/elements/bear.png" :size 1 :type "image"}
                          {:url "/raw/img/elements/ear.png" :size 1 :type "image"}
                          {:url "/raw/img/elements/eyes.png" :size 1 :type "image"}
                          {:url "/raw/img/elements/magnet.png" :size 1 :type "image"}
                          {:url "/raw/img/elements/iguana.png" :size 1 :type "image"}
                          {:url "/raw/img/elements/fire.png" :size 1 :type "image"}


                          {:url "/raw/audio/l1/a6/L1_A6_Lion Intro.m4a", :size 2, :type "audio" :alias "librarian books intro"}
                          {:url "/raw/audio/l1/a6/L1_A6_Lion_words.m4a", :size 2, :type "audio" :alias "librarian books words"}
                          {:url "/raw/audio/l1/a6/L1_A6_Vera.m4a", :size 2, :type "audio" :alias "vera books intro"}
                          ],

                :objects {:background
                          {:type "background", :src "/raw/img/library/book/background2.jpg"
                           :states {:closed {:src "/raw/img/library/book/background2.jpg"}
                                    :open {:src "/raw/img/library/book/background.jpg"}}},
                          :book
                          {:type "animation" :x 928 :y 1048 :name "book" :anim "close_idle" :speed 0.3
                           :scene-name "book" :anim-offset {:x 0 :y -480}
                           :width 1439 :height 960 :scale-x 1 :scale-y 1 :start false}

                          :title-text
                          {:type "text"  :x 929 :y 164 :width 680 :height 800
                           :align "center" :vertical-align "middle"
                           :font-family "Liberation Sans" :font-size 90
                           :shadow-color "#1a1a1a" :shadow-offset {:x 5 :y 5} :shadow-blur 5 :shadow-opacity 0.5
                           :fill "white" :text "El libro de mis primeras palabras"
                           :chunks [{:start 0 :end 2}
                                    {:start 3 :end 5}
                                    {:start 5 :end 8}
                                    {:start 9 :end 11}
                                    {:start 12 :end 15}
                                    {:start 16 :end 19}
                                    {:start 19 :end 21}
                                    {:start 21 :end 24}
                                    {:start 25 :end 27}
                                    {:start 27 :end 29}
                                    {:start 29 :end 33}]
                           :states {:visible {:type "text"}
                                    :hidden {:type "transparent"}}}

                          :content
                          {:type "transparent" :x 209 :y 89 :children ["letter" "image" "image2" "image3"]
                           :transition "content"
                           :states {:visible {:type "group" :opacity 0} :hidden {:type "transparent"}}}

                          :letter
                          {:type "text" :x 220 :y 150 :width 200 :height 200
                           :font-family "Comic Sans MS" :font-size 180
                           :shadow-color "#1a1a1a" :shadow-offset {:x 5 :y 5} :shadow-blur 5 :shadow-opacity 0.5
                           :states {:default {}}}

                          :image
                          {:type "image" :x 220 :y 570 :width 100 :height 100 :scale-x 2 :scale-y 2 :states {:default {}}
                           :transition "image"}

                          :image2
                          {:type "image" :x 980 :y 150 :width 100 :height 100 :scale-x 2 :scale-y 2 :states {:default {}}
                           :transition "image2"}

                          :image3
                          {:type "image" :x 980 :y 570 :width 100 :height 100 :scale-x 2 :scale-y 2 :states {:default {}}
                           :transition "image3"}

                          :next-page-arrow
                          {:type "transparent" :x 1444 :y 91 :width 200 :height 200
                           :actions {:click {:type "action" :id "next-page" :on "click"}}}
                          },

                :scene-objects [["background"] ["book"] ["title-text" "content" "next-page-arrow"]],

                :actions
                 {:start-activity
                  {:type "sequence"
                   :data ["clear-instruction"
                          "renew-words"
                          "init-next-page"
                          "read-title"
                          ]}

                  :clear-instruction
                  {:type "remove-flows" :flow-tag "instruction"}

                  :renew-words
                  {:type "lesson-var-provider"
                   :provider-id "words-set"
                   :variables ["item-1" "item-2" "item-3"]
                   :shuffled false
                   :from      "concepts"}

                  :renew-current-concept
                  {:type "sequence-data"
                   :data [{:type "vars-var-provider"
                           :provider-id "current-word"
                           :variables ["current-word"]
                           :from ["item-1" "item-2" "item-3"]
                           :shuffled false
                           :on-end "finish-activity"}
                          {:type "action" :id "set-content-var"}]}

                  :set-content-var
                  {:type "action" :from-var [{:template "set-content-%" :var-name "current-word" :action-property "id" :var-property "concept-name"}]}

                  :set-content-ardilla
                  {:type "parallel"
                   :data [{:type "state" :target "letter" :id "default" :params {:text "a"}}
                          {:type "state" :target "image" :id "default"
                           :params {:src "/raw/img/elements/squirrel.png" :filter "grayscale"}}
                          {:type "state" :target "image2" :id "default"
                           :params {:src "/raw/img/elements/bee.png" :filter "grayscale"}}
                          {:type "state" :target "image3" :id "default"
                           :params {:src "/raw/img/elements/tree.png" :filter "grayscale"}}]}

                  :set-content-oso
                  {:type "parallel"
                   :data [{:type "state" :target "letter" :id "default" :params {:text "o"}}
                          {:type "state" :target "image" :id "default"
                           :params {:src "/raw/img/elements/bear.png" :filter "grayscale"}}
                          {:type "state" :target "image2" :id "default"
                           :params {:src "/raw/img/elements/ear.png" :filter "grayscale"}}
                          {:type "state" :target "image3" :id "default"
                           :params {:src "/raw/img/elements/eyes.png" :filter "grayscale"}}]}

                  :set-content-iman
                  {:type "parallel"
                   :data [{:type "state" :target "letter" :id "default" :params {:text "i"}}
                          {:type "state" :target "image" :id "default"
                           :params {:src "/raw/img/elements/magnet.png" :filter "grayscale"}}
                          {:type "state" :target "image2" :id "default"
                           :params {:src "/raw/img/elements/iguana.png" :filter "grayscale"}}
                          {:type "state" :target "image3" :id "default"
                           :params {:src "/raw/img/elements/fire.png" :filter "grayscale"}}]}

                  :init-next-page
                  {:type "set-variable" :var-name "next-page-action" :var-value "open-book"}

                  :open-book
                  {:type "sequence-data"
                   :data [{:type "state" :target "title-text" :id "hidden"}
                          {:type "start-animation" :target "book"}
                          {:type "animation" :target "book" :id "open_book"}
                          {:type "state" :target "background" :id "open"}
                          {:type "add-animation" :target "book" :id "idle"}
                          {:type "action" :id "renew-current-concept"}
                          {:type "state" :target "content" :id "visible"}
                          {:type "empty" :duration 3000}
                          {:type "transition" :transition-id "content" :to {:opacity 1 :duration 0.5}}
                          {:type "set-variable" :var-name "next-page-action" :var-value "open-next-page"}
                          {:type "action" :id "read-page"}]}

                  :open-next-page
                  {:type "sequence-data"
                   :data [{:type "transition" :transition-id "content" :to {:opacity 0 :duration 0.5}}
                          {:type "state" :target "content" :id "hidden"}
                          {:type "action" :id "renew-current-concept"}
                          {:type "animation" :target "book" :id "page" :loop false}
                          {:type "state" :target "content" :id "visible"}
                          {:type "empty" :duration 1500}
                          {:type "transition" :transition-id "content" :to {:opacity 1 :duration 0.5}}
                          {:type "action" :id "read-page"}]}

                  :read-page
                  {:type "action" :from-var [{:template "read-page-%" :var-name "current-word" :action-property "id" :var-property "concept-name"}]}

                  :read-page-ardilla
                  {:type "parallel"
                   :data [{:type "audio",
                           :id "/raw/audio/l1/a6/L1_A6_Lion Intro.m4a",
                           :start 36.055,
                           :duration 7.427}
                          {:type "sequence-data"
                           :data [{:type "empty" :duration 2000}
                                  {:type "state" :target "image" :id "default" :params {:filter ""}}]}
                          {:type "sequence-data"
                           :data [{:type "empty" :duration 4000}
                                  {:type "state" :target "image2" :id "default" :params {:filter ""}}]}
                          {:type "sequence-data"
                           :data [{:type "empty" :duration 6000}
                                  {:type "state" :target "image3" :id "default" :params {:filter ""}}]}]}

                  :read-page-oso
                  {:type "parallel"
                   :data [{:type "audio",
                           :id "/raw/audio/l1/a6/L1_A6_Lion Intro.m4a",
                           :start 44.788,
                           :duration 7.441}
                          {:type "sequence-data"
                           :data [{:type "empty" :duration 2000}
                                  {:type "state" :target "image" :id "default" :params {:filter ""}}]}
                          {:type "sequence-data"
                           :data [{:type "empty" :duration 4000}
                                  {:type "state" :target "image2" :id "default" :params {:filter ""}}]}
                          {:type "sequence-data"
                           :data [{:type "empty" :duration 6000}
                                  {:type "state" :target "image3" :id "default" :params {:filter ""}}]}]}

                  :read-page-iman
                  {:type "parallel"
                   :data [{:type "audio",
                           :id "/raw/audio/l1/a6/L1_A6_Lion Intro.m4a",
                           :start 53.735,
                           :duration 8.081}
                          {:type "sequence-data"
                           :data [{:type "empty" :duration 2000}
                                  {:type "state" :target "image" :id "default" :params {:filter ""}}]}
                          {:type "sequence-data"
                           :data [{:type "empty" :duration 4000}
                                  {:type "state" :target "image2" :id "default" :params {:filter ""}}]}
                          {:type "sequence-data"
                           :data [{:type "empty" :duration 6000}
                                  {:type "state" :target "image3" :id "default" :params {:filter ""}}]}]}

                  :read-title
                  {:type "sequence-data"
                   :data [{:type "audio",
                           :id "/raw/audio/l1/a6/L1_A6_Lion Intro.m4a",
                           :start 18.121,
                           :duration 1.346}
                          {:type "text-animation",
                           :audio "/raw/audio/l1/a6/L1_A6_Lion Intro.m4a",
                           :start 20.134,
                           :duration 1.987,
                           :animation "bounce",
                           :target "title-text",
                           :data
                           [{:start 20.241, :end 20.294, :duration 0.053, :at 20.241, :chunk 0}
                            {:start 20.427, :end 20.467, :duration 0.04, :at 20.427, :chunk 1}
                            {:start 20.561, :end 20.587, :duration 0.026, :at 20.561, :chunk 2}
                            {:start 20.614, :end 20.667, :duration 0.053, :at 20.614, :chunk 3}
                            {:start 20.734, :end 20.787, :duration 0.053, :at 20.734, :chunk 4}
                            {:start 21.054, :end 21.121, :duration 0.067, :at 21.054, :chunk 5}
                            {:start 21.161, :end 21.227, :duration 0.066, :at 21.161, :chunk 6}
                            {:start 21.267, :end 21.307, :duration 0.04, :at 21.267, :chunk 7}
                            {:start 21.468, :end 21.548, :duration 0.08, :at 21.468, :chunk 8}
                            {:start 21.588, :end 21.641, :duration 0.053, :at 21.588, :chunk 9}
                            {:start 21.681, :end 21.801, :duration 0.12, :at 21.681, :chunk 10}
                            ]}
                          {:type "audio",
                           :id "/raw/audio/l1/a6/L1_A6_Lion Intro.m4a",
                           :start 22.414,
                           :duration 1.907}
                          {:type "text-animation",
                           :audio "/raw/audio/l1/a6/L1_A6_Lion Intro.m4a",
                           :start 24.641,
                           :duration 1.813,
                           :animation "bounce",
                           :target "title-text",
                           :data
                           [{:start 24.694, :end 24.734, :duration 0.04, :at 24.694, :chunk 0}
                            {:start 24.748, :end 24.774, :duration 0.026, :at 24.748, :chunk 1}
                            {:start 24.801, :end 24.854, :duration 0.053, :at 24.801, :chunk 2}
                            {:start 24.868, :end 24.934, :duration 0.066, :at 24.868, :chunk 3}
                            {:start 24.961, :end 25.041, :duration 0.08, :at 24.961, :chunk 4}
                            {:start 25.148, :end 25.228, :duration 0.08, :at 25.148, :chunk 5}
                            {:start 25.534, :end 25.574, :duration 0.04, :at 25.534, :chunk 6}
                            {:start 25.601, :end 25.668, :duration 0.067, :at 25.601, :chunk 7}
                            {:start 25.721, :end 25.748, :duration 0.027, :at 25.721, :chunk 8}
                            {:start 25.921, :end 25.961, :duration 0.04, :at 25.921, :chunk 9}
                            {:start 26.094, :end 26.281, :duration 0.187, :at 26.094, :chunk 10}
                            ]}
                          {:type "audio", :id "/raw/audio/l1/a6/L1_A6_Vera.m4a", :start 5.497, :duration 2.201}
                          {:type "audio", :id "/raw/audio/l1/a6/L1_A6_Lion Intro.m4a", :start 27.734, :duration 1.92}
                          {:type "audio", :id "/raw/audio/l1/a6/L1_A6_Lion Intro.m4a", :start 31.028, :duration 2.347}]}

                  :next-page
                  {:type "action" :from-var [{:var-name "next-page-action" :action-property "id"}]}

                  :finish-activity {:type "sequence-data"
                                    :data [{:type "finish-activity" :id "book"}
                                           {:type "scene" :scene-id "library"}]}

                  :start-background-music
                  {:type "audio" :id "background" :loop true}},
                 :audio
                 {:background "/raw/audio/background/POL-daily-special-short.mp3"}
                 :triggers
                 {:start {:on "start" :action "start-activity"}}
                 :metadata
                 {:autostart true
                  :prev "map"}})
