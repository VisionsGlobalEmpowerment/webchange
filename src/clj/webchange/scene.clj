(ns webchange.scene
  (:require [webchange.demo-scenes.cinema.cinema :refer [cinema-scene]]
            [webchange.demo-scenes.cinema.cinema_video :refer [cinema-video-scene]]
            [webchange.demo-scenes.home.home :refer [home-scene]]
            [webchange.demo-scenes.home.letter-intro :refer [letter-intro-scene]]
            [webchange.demo-scenes.library.painting-tablet :refer [painting-tablet-scene]]
            [webchange.demo-scenes.map :refer [map-scene]]
            [webchange.demo-scenes.park.see-saw :refer [see-saw-scene]]
            [webchange.demo-scenes.park.swings :refer [swings-scene]]
            [webchange.demo-scenes.park.sandbox :refer [sandbox-scene]]
            [webchange.demo-scenes.park.park :refer [park-scene]]
            [webchange.demo-scenes.park.park-poem :refer [park-poem-scene]]
            [webchange.demo-scenes.park.slide :refer [slide-scene]]
            [webchange.demo-scenes.stadium.stadium :refer [stadium-scene]]
            [webchange.demo-scenes.stadium.volleyball :refer [volleyball-scene]]
            [webchange.demo-scenes.stadium.cycling :refer [cycling-scene]]
            [webchange.demo-scenes.stadium.running :refer [running-scene]]
            [webchange.demo-scenes.park.hide-n-seek :refer [hide-n-seek-scene]]
            [webchange.demo-scenes.library.library :refer [library-scene]]
            [webchange.demo-scenes.library.book :refer [book-scene]]

            [clojure.tools.logging :as log]))

(def courses {"test" {"home"            home-scene
                      "letter-intro"    letter-intro-scene
                      "map"             map-scene
                      "see-saw"         see-saw-scene
                      "swings"          swings-scene
                      "sandbox"         sandbox-scene
                      "painting-tablet" painting-tablet-scene
                      "park"            park-scene
                      "park-poem"       park-poem-scene
                      "slide"           slide-scene
                      "volleyball"      volleyball-scene
                      "hide-n-seek"     hide-n-seek-scene
                      "library"         library-scene
                      "book"            book-scene
                      "cinema"          cinema-scene
                      "cinema-video"    cinema-video-scene
                      "cycling"         cycling-scene
                      "running"         running-scene
                      "stadium"         stadium-scene}})
(defn get-course
  [course-id]
  {:initial-scene    "map"
   :scene-list       {:home            {:name    "Casa"
                                        :preview "/images/dashboard/scene-preview/Casa_Room.jpg"
                                        :type    "non-scored"
                                        :outs    [{:name "map" :x 1457 :y 630}]}
                      :letter-intro    {:name    "Letter Introduction"
                                        :preview "/images/dashboard/scene-preview/Casa_Room.jpg"
                                        :type    "non-scored"
                                        :outs    [{:name "map" :x 1457 :y 330}]}
                      :map             {:name    "Map"
                                        :preview "/images/dashboard/scene-preview/Casa_Room.jpg"
                                        :type    "non-scored"
                                        :outs    [{:name "park" :x 1447 :y 860}
                                                  {:name "library" :x 181 :y 419}
                                                  {:name "stadium" :x 1581 :y 269}
                                                  {:name "home" :x 881 :y 490}]}
                      :park            {:name    "Park"
                                        :preview "/images/dashboard/scene-preview/Park_Main.jpg"
                                        :type    "non-scored"
                                        :outs    [{:name "map" :x 100 :y 100}
                                                  {:name "see-saw" :x 407 :y 860}
                                                  {:name "swings" :x 1637 :y 660}
                                                  {:name "sandbox" :x 937 :y 810}
                                                  {:name "hide-n-seek" :x 987 :y 670}]}
                      :see-saw         {:name    "Sea-saw"
                                        :preview "/images/dashboard/scene-preview/Park_See-Saw.jpg"
                                        :type    "non-scored"
                                        :outs    [{:name "park" :x 100 :y 100}]}
                      :park-poem       {:name    "Poem"
                                        :preview "/images/dashboard/scene-preview/Park_Main.jpg"
                                        :type    "non-scored"
                                        :outs    [{:name "park" :x 100 :y 100}]}
                      :slide           {:name    "Slide"
                                        :preview "/images/dashboard/scene-preview/Park_Slide.jpg"
                                        :type    "non-scored"
                                        :outs    [{:name "park" :x 100 :y 100}]}
                      :swings          {:name    "Swings"
                                        :preview "/images/dashboard/scene-preview/Park_Swing.jpg"
                                        :type    "non-scored"
                                        :outs    [{:name "park" :x 100 :y 100}]}
                      :sandbox         {:name    "Sandbox"
                                        :preview "/images/dashboard/scene-preview/Park_Sandbox.jpg"
                                        :type    "non-scored"
                                        :outs    [{:name "park" :x 100 :y 100}]}
                      :hide-n-seek     {:name    "Hide and Seek"
                                        :preview "/images/dashboard/scene-preview/Park_Hide-and-seek.jpg"
                                        :type    "assessment"
                                        :outs    [{:name "park" :x 100 :y 100}]}
                      :stadium         {:name    "Stadium"
                                        :preview "/images/dashboard/scene-preview/Stadium_Main.jpg"
                                        :type    "non-scored"
                                        :outs    [{:name "map" :x 100 :y 100}
                                                  {:name "volleyball" :x 857 :y 870}
                                                  {:name "cycling" :x 357 :y 870}]}
                      :volleyball      {:name    "Volleyball"
                                        :preview "/images/dashboard/scene-preview/Stadium_Volleyball.jpg"
                                        :type    "non-scored"
                                        :outs    [{:name "stadium" :x 100 :y 100}]}
                      :cinema          {:name    "Cinema"
                                        :preview "/images/dashboard/scene-preview/Cinema-Room.jpg"
                                        :type    "non-scored"
                                        :outs    [{:name "map" :x 100 :y 100}]}
                      :cinema-video    {:name    "Cinema Video"
                                        :preview "/images/dashboard/scene-preview/Cinema-Room.jpg"
                                        :type    "non-scored"
                                        :outs    [{:name "map" :x 100 :y 100}]}
                      :cycling         {:name    "Cycling"
                                        :preview "/images/dashboard/scene-preview/Stadium_Cycling-Race.jpg"
                                        :type    "non-scored"
                                        :outs    [{:name "stadium" :x 100 :y 100}]}
                      :running         {:name    "Running"
                                        :preview "/images/dashboard/scene-preview/Stadium_Running.jpg"
                                        :type    "non-scored"
                                        :outs    [{:name "stadium" :x 100 :y 100}]}
                      :library         {:name    "Library"
                                        :preview "/images/dashboard/scene-preview/Library_Room.jpg"
                                        :type    "non-scored"
                                        :outs    [{:name "map" :x 100 :y 100}
                                                  {:name "book" :x 1357 :y 750}
                                                  {:name "painting-tablet" :x 1057 :y 780}]}
                      :book            {:name    "Book"
                                        :preview "/images/dashboard/scene-preview/Library_Book.jpg"
                                        :type    "non-scored"
                                        :outs    [{:name "library" :x 100 :y 100}]}
                      :painting-tablet {:name    "Painting"
                                        :preview "/images/dashboard/scene-preview/Library_Drawing-Lesson.jpg"
                                        :type    "non-scored"
                                        :outs    [{:name "library" :x 100 :y 100}]}}

   :scenes           ["home" "map" "see-saw" "swings" "sandbox" "park" "stadium" "volleyball" "hide-n-seek" "library"
                      "book" "painting-tablet" "cinema" "cinema-video" "cycling" "running" "letter-intro" "park-poem"
                      "slide"]
   :lessons          [{:id 1 :lesson-sets {:concepts     "ls1"
                                           :assessment-1 "assessment1"}}
                      {:id 2 :lesson-sets {:concepts     "ls2"
                                           :assessment-1 "assessment1"}}
                      {:id 3 :lesson-sets {:concepts     "ls3"
                                           :assessment-1 "assessment1"}}
                      {:id 4 :lesson-sets {:concepts     "ls4"
                                           :assessment-1 "assessment2"}}
                      {:id 5 :lesson-sets {:concepts     "ls5"
                                           :assessment-1 "assessment2"}}
                      {:id 6 :lesson-sets {:concepts     "ls6"
                                           :assessment-1 "assessment2"}}
                      {:id 7 :lesson-sets {:concepts     "ls7"
                                           :assessment-1 "assessment2"}}
                      {:id 8 :lesson-sets {:concepts     "ls8"
                                           :assessment-1 "assessment3"}}
                      {:id 9 :lesson-sets {:concepts     "ls9"
                                           :assessment-1 "assessment3"}}
                      {:id 10 :lesson-sets {:concepts     "ls10"
                                            :assessment-1 "assessment3"}}
                      {:id 11 :lesson-sets {:concepts     "ls11"
                                            :assessment-1 "assessment3"}}]
   :workflow-actions [{:id 10 :type "init-progress"}
                      {:id 1 :type "set-activity" :activity "home" :activity-number 1 :lesson 1 :level 1 :time-expected 300}
                      {:id 2 :type "set-activity" :activity "see-saw" :activity-number 2 :lesson 1 :level 1 :time-expected 300}
                      {:id 3 :type "set-activity" :activity "swings" :activity-number 3 :lesson 1 :level 1 :time-expected 300}
                      {:id 4 :type "set-activity" :activity "sandbox" :activity-number 4 :lesson 1 :level 1 :time-expected 300}
                      {:id 5 :type "set-activity" :activity "volleyball" :activity-number 5 :lesson 1 :level 1 :time-expected 300 :scored true}
                      {:id 6 :type "set-activity" :activity "book" :activity-number 6 :lesson 1 :level 1 :time-expected 300}
                      {:id 7 :type "set-activity" :activity "cycling" :activity-number 7 :lesson 1 :level 1 :time-expected 300 :scored true}
                      {:id 8 :type "set-activity" :activity "painting-tablet" :activity-number 8 :lesson 1 :level 1 :time-expected 300}

                      {:id 19 :type "set-scene-activities" :scene-activities {:home            11
                                                                              :see-saw         12
                                                                              :swings          13
                                                                              :sandbox         14
                                                                              :volleyball      15
                                                                              :book            16
                                                                              :cycling         17
                                                                              :painting-tablet 18
                                                                              :hide-n-seek     29}}

                      {:id 11 :type "set-activity" :activity "home" :activity-number 9 :lesson 2 :level 1 :time-expected 300}
                      {:id 12 :type "set-activity" :activity "see-saw" :activity-number 10 :lesson 2 :level 1 :time-expected 300}
                      {:id 13 :type "set-activity" :activity "swings" :activity-number 11 :lesson 2 :level 1 :time-expected 300}
                      {:id 14 :type "set-activity" :activity "sandbox" :activity-number 12 :lesson 2 :level 1 :time-expected 300}
                      {:id 15 :type "set-activity" :activity "volleyball" :activity-number 13 :lesson 2 :level 1 :time-expected 300 :scored true}
                      {:id 16 :type "set-activity" :activity "book" :activity-number 14 :lesson 2 :level 1 :time-expected 300}
                      {:id 17 :type "set-activity" :activity "cycling" :activity-number 15 :lesson 2 :level 1 :time-expected 300 :scored true}
                      {:id 18 :type "set-activity" :activity "painting-tablet" :activity-number 16 :lesson 2 :level 1 :time-expected 300}

                      {:id 20 :type "set-scene-activities" :scene-activities {:home            21
                                                                              :see-saw         22
                                                                              :swings          23
                                                                              :sandbox         24
                                                                              :volleyball      25
                                                                              :book            26
                                                                              :cycling         27
                                                                              :painting-tablet 28
                                                                              :hide-n-seek     29}}

                      {:id 21 :type "set-activity" :activity "home" :activity-number 17 :lesson 3 :level 1 :time-expected 300}
                      {:id 22 :type "set-activity" :activity "see-saw" :activity-number 18 :lesson 3 :level 1 :time-expected 300}
                      {:id 23 :type "set-activity" :activity "swings" :activity-number 19 :lesson 3 :level 1 :time-expected 300}
                      {:id 24 :type "set-activity" :activity "sandbox" :activity-number 20 :lesson 3 :level 1 :time-expected 300}
                      {:id 25 :type "set-activity" :activity "volleyball" :activity-number 21 :lesson 3 :level 1 :time-expected 300 :scored true}
                      {:id 26 :type "set-activity" :activity "book" :activity-number 22 :lesson 3 :level 1 :time-expected 300}
                      {:id 27 :type "set-activity" :activity "cycling" :activity-number 23 :lesson 3 :level 1 :time-expected 300 :scored true}
                      {:id 28 :type "set-activity" :activity "painting-tablet" :activity-number 24 :lesson 3 :level 1 :time-expected 300}

                      {:id     29 :type "set-activity" :activity "hide-n-seek" :activity-number 25 :lesson 3 :level 1 :time-expected 300
                       :scored true :expected-score-percentage 90}

                      {:id 30 :type "set-scene-activities" :scene-activities {:home            31
                                                                              :see-saw         32
                                                                              :swings          33
                                                                              :sandbox         34
                                                                              :volleyball      35
                                                                              :book            36
                                                                              :cycling         37
                                                                              :painting-tablet 38
                                                                              :hide-n-seek     69}}

                      {:id 31 :type "set-activity" :activity "home" :activity-number 26 :lesson 4 :level 1 :time-expected 300}
                      {:id 32 :type "set-activity" :activity "see-saw" :activity-number 27 :lesson 4 :level 1 :time-expected 300}
                      {:id 33 :type "set-activity" :activity "swings" :activity-number 28 :lesson 4 :level 1 :time-expected 300}
                      {:id 34 :type "set-activity" :activity "sandbox" :activity-number 29 :lesson 4 :level 1 :time-expected 300}
                      {:id 35 :type "set-activity" :activity "volleyball" :activity-number 30 :lesson 4 :level 1 :time-expected 300 :scored true}
                      {:id 36 :type "set-activity" :activity "book" :activity-number 31 :lesson 4 :level 1 :time-expected 300}
                      {:id 37 :type "set-activity" :activity "cycling" :activity-number 32 :lesson 4 :level 1 :time-expected 300 :scored true}
                      {:id 38 :type "set-activity" :activity "painting-tablet" :activity-number 33 :lesson 4 :level 1 :time-expected 300}

                      {:id 40 :type "set-scene-activities" :scene-activities {:home            41
                                                                              :see-saw         42
                                                                              :swings          43
                                                                              :sandbox         44
                                                                              :volleyball      45
                                                                              :book            46
                                                                              :cycling         47
                                                                              :painting-tablet 48
                                                                              :hide-n-seek     69}}

                      {:id 41 :type "set-activity" :activity "home" :activity-number 34 :lesson 5 :level 1 :time-expected 300}
                      {:id 42 :type "set-activity" :activity "see-saw" :activity-number 35 :lesson 5 :level 1 :time-expected 300}
                      {:id 43 :type "set-activity" :activity "swings" :activity-number 36 :lesson 5 :level 1 :time-expected 300}
                      {:id 44 :type "set-activity" :activity "sandbox" :activity-number 37 :lesson 5 :level 1 :time-expected 300}
                      {:id 45 :type "set-activity" :activity "volleyball" :activity-number 38 :lesson 5 :level 1 :time-expected 300 :scored true}
                      {:id 46 :type "set-activity" :activity "book" :activity-number 39 :lesson 5 :level 1 :time-expected 300}
                      {:id 47 :type "set-activity" :activity "cycling" :activity-number 40 :lesson 5 :level 1 :time-expected 300 :scored true}
                      {:id 48 :type "set-activity" :activity "painting-tablet" :activity-number 41 :lesson 5 :level 1 :time-expected 300}

                      {:id 50 :type "set-scene-activities" :scene-activities {:home            51
                                                                              :see-saw         52
                                                                              :swings          53
                                                                              :sandbox         54
                                                                              :volleyball      55
                                                                              :book            56
                                                                              :cycling         57
                                                                              :painting-tablet 58
                                                                              :hide-n-seek     69}}

                      {:id 51 :type "set-activity" :activity "home" :activity-number 42 :lesson 6 :level 1 :time-expected 300}
                      {:id 52 :type "set-activity" :activity "see-saw" :activity-number 43 :lesson 6 :level 1 :time-expected 300}
                      {:id 53 :type "set-activity" :activity "swings" :activity-number 44 :lesson 6 :level 1 :time-expected 300}
                      {:id 54 :type "set-activity" :activity "sandbox" :activity-number 45 :lesson 6 :level 1 :time-expected 300}
                      {:id 55 :type "set-activity" :activity "volleyball" :activity-number 46 :lesson 6 :level 1 :time-expected 300 :scored true}
                      {:id 56 :type "set-activity" :activity "book" :activity-number 47 :lesson 6 :level 1 :time-expected 300}
                      {:id 57 :type "set-activity" :activity "cycling" :activity-number 48 :lesson 6 :level 1 :time-expected 300 :scored true}
                      {:id 58 :type "set-activity" :activity "painting-tablet" :activity-number 49 :lesson 6 :level 1 :time-expected 300}

                      {:id 30 :type "set-scene-activities" :scene-activities {:home            61
                                                                              :see-saw         62
                                                                              :swings          63
                                                                              :sandbox         64
                                                                              :volleyball      65
                                                                              :book            66
                                                                              :cycling         67
                                                                              :painting-tablet 68
                                                                              :hide-n-seek     69}}

                      {:id 61 :type "set-activity" :activity "home" :activity-number 50 :lesson 7 :level 1 :time-expected 300}
                      {:id 62 :type "set-activity" :activity "see-saw" :activity-number 51 :lesson 7 :level 1 :time-expected 300}
                      {:id 63 :type "set-activity" :activity "swings" :activity-number 52 :lesson 7 :level 1 :time-expected 300}
                      {:id 64 :type "set-activity" :activity "sandbox" :activity-number 53 :lesson 7 :level 1 :time-expected 300}
                      {:id 65 :type "set-activity" :activity "volleyball" :activity-number 54 :lesson 7 :level 1 :time-expected 300 :scored true}
                      {:id 66 :type "set-activity" :activity "book" :activity-number 55 :lesson 7 :level 1 :time-expected 300}
                      {:id 67 :type "set-activity" :activity "cycling" :activity-number 56 :lesson 7 :level 1 :time-expected 300 :scored true}
                      {:id 68 :type "set-activity" :activity "painting-tablet" :activity-number 57 :lesson 7 :level 1 :time-expected 300}

                      {:id     69 :type "set-activity" :activity "hide-n-seek" :activity-number 58 :lesson 7 :level 1 :time-expected 300
                       :scored true :expected-score-percentage 90}

                      {:id 70 :type "set-scene-activities" :scene-activities {:home            71
                                                                              :see-saw         72
                                                                              :swings          73
                                                                              :sandbox         74
                                                                              :volleyball      75
                                                                              :book            76
                                                                              :cycling         77
                                                                              :painting-tablet 78
                                                                              :hide-n-seek     109}}

                      {:id 71 :type "set-activity" :activity "home" :activity-number 59 :lesson 8 :level 1 :time-expected 300}
                      {:id 72 :type "set-activity" :activity "see-saw" :activity-number 60 :lesson 8 :level 1 :time-expected 300}
                      {:id 73 :type "set-activity" :activity "swings" :activity-number 61 :lesson 8 :level 1 :time-expected 300}
                      {:id 74 :type "set-activity" :activity "sandbox" :activity-number 62 :lesson 8 :level 1 :time-expected 300}
                      {:id 75 :type "set-activity" :activity "volleyball" :activity-number 63 :lesson 8 :level 1 :time-expected 300 :scored true}
                      {:id 76 :type "set-activity" :activity "book" :activity-number 64 :lesson 8 :level 1 :time-expected 300}
                      {:id 77 :type "set-activity" :activity "cycling" :activity-number 65 :lesson 8 :level 1 :time-expected 300 :scored true}
                      {:id 78 :type "set-activity" :activity "painting-tablet" :activity-number 66 :lesson 8 :level 1 :time-expected 300}

                      {:id 80 :type "set-scene-activities" :scene-activities {:home            81
                                                                              :see-saw         82
                                                                              :swings          83
                                                                              :sandbox         84
                                                                              :volleyball      85
                                                                              :book            86
                                                                              :cycling         87
                                                                              :painting-tablet 88
                                                                              :hide-n-seek     109}}

                      {:id 81 :type "set-activity" :activity "home" :activity-number 67 :lesson 9 :level 1 :time-expected 300}
                      {:id 82 :type "set-activity" :activity "see-saw" :activity-number 68 :lesson 9 :level 1 :time-expected 300}
                      {:id 83 :type "set-activity" :activity "swings" :activity-number 69 :lesson 9 :level 1 :time-expected 300}
                      {:id 84 :type "set-activity" :activity "sandbox" :activity-number 70 :lesson 9 :level 1 :time-expected 300}
                      {:id 85 :type "set-activity" :activity "volleyball" :activity-number 71 :lesson 9 :level 1 :time-expected 300 :scored true}
                      {:id 86 :type "set-activity" :activity "book" :activity-number 72 :lesson 9 :level 1 :time-expected 300}
                      {:id 87 :type "set-activity" :activity "cycling" :activity-number 73 :lesson 9 :level 1 :time-expected 300 :scored true}
                      {:id 88 :type "set-activity" :activity "painting-tablet" :activity-number 74 :lesson 9 :level 1 :time-expected 300}

                      {:id 90 :type "set-scene-activities" :scene-activities {:home            91
                                                                              :see-saw         92
                                                                              :swings          93
                                                                              :sandbox         94
                                                                              :volleyball      95
                                                                              :book            96
                                                                              :cycling         97
                                                                              :painting-tablet 98
                                                                              :hide-n-seek     109}}

                      {:id 91 :type "set-activity" :activity "home" :activity-number 75 :lesson 10 :level 1 :time-expected 300}
                      {:id 92 :type "set-activity" :activity "see-saw" :activity-number 76 :lesson 10 :level 1 :time-expected 300}
                      {:id 93 :type "set-activity" :activity "swings" :activity-number 77 :lesson 10 :level 1 :time-expected 300}
                      {:id 94 :type "set-activity" :activity "sandbox" :activity-number 78 :lesson 10 :level 1 :time-expected 300}
                      {:id 95 :type "set-activity" :activity "volleyball" :activity-number 79 :lesson 10 :level 1 :time-expected 300 :scored true}
                      {:id 96 :type "set-activity" :activity "book" :activity-number 80 :lesson 10 :level 1 :time-expected 300}
                      {:id 97 :type "set-activity" :activity "cycling" :activity-number 81 :lesson 10 :level 1 :time-expected 300 :scored true}
                      {:id 98 :type "set-activity" :activity "painting-tablet" :activity-number 82 :lesson 10 :level 1 :time-expected 300}

                      {:id 100 :type "set-scene-activities" :scene-activities {:home            101
                                                                               :see-saw         102
                                                                               :swings          103
                                                                               :sandbox         104
                                                                               :volleyball      105
                                                                               :book            106
                                                                               :cycling         107
                                                                               :painting-tablet 108
                                                                               :hide-n-seek     109}}

                      {:id 101 :type "set-activity" :activity "home" :activity-number 83 :lesson 11 :level 1 :time-expected 300}
                      {:id 102 :type "set-activity" :activity "see-saw" :activity-number 84 :lesson 11 :level 1 :time-expected 300}
                      {:id 103 :type "set-activity" :activity "swings" :activity-number 85 :lesson 11 :level 1 :time-expected 300}
                      {:id 104 :type "set-activity" :activity "sandbox" :activity-number 86 :lesson 11 :level 1 :time-expected 300}
                      {:id 105 :type "set-activity" :activity "volleyball" :activity-number 87 :lesson 11 :level 1 :time-expected 300 :scored true}
                      {:id 106 :type "set-activity" :activity "book" :activity-number 88 :lesson 11 :level 1 :time-expected 300}
                      {:id 107 :type "set-activity" :activity "cycling" :activity-number 89 :lesson 11 :level 1 :time-expected 300 :scored true}
                      {:id 108 :type "set-activity" :activity "painting-tablet" :activity-number 90 :lesson 11 :level 1 :time-expected 300}

                      {:id     109 :type "set-activity" :activity "hide-n-seek" :activity-number 91 :lesson 11 :level 1 :time-expected 300
                       :scored true :expected-score-percentage 90}

                      {:id 11 :type "set-activity" :activity "cinema" :activity-number 100 :lesson 1 :level 2}
                      {:id 12 :type "set-activity" :activity "cinema-video" :activity-number 101 :lesson 2 :level 2}
                      {:id 12 :type "set-activity" :activity "slide" :activity-number 102 :lesson 1 :level 1}
                      ]
   :default-progress {:current-activity          "home"
                      :variables                 {:last-location "home"}
                      :finished-workflow-actions #{}
                      :scene-activities          {:home            1
                                                  :see-saw         2
                                                  :swings          3
                                                  :sandbox         4
                                                  :volleyball      5
                                                  :book            6
                                                  :cycling         7
                                                  :painting-tablet 8
                                                  :hide-n-seek     29}}})

(defn get-scene
  [course-id scene-id]
  (get-in courses [course-id scene-id]))
