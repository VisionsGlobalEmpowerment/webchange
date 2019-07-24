(ns webchange.scene
  (:require [webchange.demo-scenes.cinema.cinema :refer [cinema-scene]]
            [webchange.demo-scenes.cinema.cinema_video :refer [cinema-video-scene]]
            [webchange.demo-scenes.home :refer [home-scene]]
            [webchange.demo-scenes.library.painting-tablet :refer [painting-tablet-scene]]
            [webchange.demo-scenes.map :refer [map-scene]]
            [webchange.demo-scenes.park.see-saw :refer [see-saw-scene]]
            [webchange.demo-scenes.park.swings :refer [swings-scene]]
            [webchange.demo-scenes.park.sandbox :refer [sandbox-scene]]
            [webchange.demo-scenes.park.park :refer [park-scene]]
            [webchange.demo-scenes.stadium.stadium :refer [stadium-scene]]
            [webchange.demo-scenes.stadium.volleyball :refer [volleyball-scene]]
            [webchange.demo-scenes.stadium.cycling :refer [cycling-scene]]
            [webchange.demo-scenes.park.hide-n-seek :refer [hide-n-seek-scene]]
            [webchange.demo-scenes.library.library :refer [library-scene]]
            [webchange.demo-scenes.library.book :refer [book-scene]]

            [clojure.tools.logging :as log]))

(def courses {"test" {"home"            home-scene
                      "map"             map-scene
                      "see-saw"         see-saw-scene
                      "swings"          swings-scene
                      "sandbox"         sandbox-scene
                      "painting-tablet" painting-tablet-scene
                      "park"            park-scene
                      "volleyball"      volleyball-scene
                      "hide-n-seek"     hide-n-seek-scene
                      "library"         library-scene
                      "book"            book-scene
                      "cinema"          cinema-scene
                      "cinema-video"    cinema-video-scene
                      "cycling"         cycling-scene
                      "stadium"         stadium-scene}})
(defn get-course
  [course-id]
  {:initial-scene    "map"
   :scene-list       {:home            {:name    "Casa"
                                        :preview "/images/dashboard/scene-preview/Casa_Room.jpg"
                                        :type    "non-scored"
                                        :outs    [{:name "map" :x 1457 :y 630}]}
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
                      "book" "painting-tablet" "cinema" "cinema-video" "cycling"]
   :lessons          [{:id 1 :lesson-sets {:concepts     "ls1"
                                           :assessment-1 "assessment1"}}
                       {:id 2 :lesson-sets {:concepts "ls2"
                                            :assessment-1 "assessment1"}}
                       {:id 3 :lesson-sets {:concepts "ls1"
                                            :assessment-1 "assessment1"}}]
   :workflow-actions [{:id 10 :type "init-progress"}
                      {:id 1 :type "set-activity" :activity "home" :activity-number 1 :lesson 1 :level 1 :time-expected 300}
                      {:id 2 :type "set-activity" :activity "see-saw" :activity-number 2 :lesson 1 :level 1 :time-expected 300}
                      {:id 3 :type "set-activity" :activity "swings" :activity-number 3 :lesson 1 :level 1 :time-expected 300}
                      {:id 4 :type "set-activity" :activity "sandbox" :activity-number 4 :lesson 1 :level 1 :time-expected 300}
                      {:id 5 :type "set-activity" :activity "volleyball" :activity-number 5 :lesson 1 :level 1 :time-expected 300 :scored true}
                      {:id 6 :type "set-activity" :activity "book" :activity-number 6 :lesson 1 :level 1 :time-expected 300}
                      {:id 7 :type "set-activity" :activity "cycling" :activity-number 7 :lesson 1 :level 1 :time-expected 300}
                      {:id 8 :type "set-activity" :activity "painting-tablet" :activity-number 8 :lesson 1 :level 1 :time-expected 300}

                      {:id 19 :type "set-scene-activities" :scene-activities {:home 11
                                                                              :see-saw 12
                                                                              :swings 13
                                                                              :sandbox 14
                                                                              :volleyball 15
                                                                              :book 16
                                                                              :cycling 17
                                                                              :painting-tablet 18
                                                                              :hide-n-seek 9}}

                      {:id 11 :type "set-activity" :activity "home" :activity-number 9 :lesson 2 :level 1 :time-expected 300}
                      {:id 12 :type "set-activity" :activity "see-saw" :activity-number 10 :lesson 2 :level 1 :time-expected 300}
                      {:id 13 :type "set-activity" :activity "swings" :activity-number 11 :lesson 2 :level 1 :time-expected 300}
                      {:id 14 :type "set-activity" :activity "sandbox" :activity-number 12 :lesson 2 :level 1 :time-expected 300}
                      {:id 15 :type "set-activity" :activity "volleyball" :activity-number 13 :lesson 2 :level 1 :time-expected 300 :scored true}
                      {:id 16 :type "set-activity" :activity "book" :activity-number 14 :lesson 2 :level 1 :time-expected 300}
                      {:id 17 :type "set-activity" :activity "cycling" :activity-number 15 :lesson 2 :level 1 :time-expected 300}
                      {:id 18 :type "set-activity" :activity "painting-tablet" :activity-number 16 :lesson 2 :level 1 :time-expected 300}

                      {:id 9 :type "set-activity" :activity "hide-n-seek" :activity-number 17 :lesson 1 :level 1 :time-expected 300
                       :scored true :expected-score-percentage 90}

                      {:id 20 :type "set-scene-activities" :scene-activities {:home 21
                                                                              :see-saw 22
                                                                              :swings 23
                                                                              :sandbox 24
                                                                              :volleyball 25
                                                                              :book 26
                                                                              :cycling 27
                                                                              :painting-tablet 28
                                                                              :hide-n-seek 29}}

                      {:id 21 :type "set-activity" :activity "home" :activity-number 18 :lesson 3 :level 1 :time-expected 300}
                      {:id 22 :type "set-activity" :activity "see-saw" :activity-number 19 :lesson 3 :level 1 :time-expected 300}
                      {:id 23 :type "set-activity" :activity "swings" :activity-number 20 :lesson 3 :level 1 :time-expected 300}
                      {:id 24 :type "set-activity" :activity "sandbox" :activity-number 21 :lesson 3 :level 1 :time-expected 300}
                      {:id 25 :type "set-activity" :activity "volleyball" :activity-number 22 :lesson 3 :level 1 :time-expected 300 :scored true}
                      {:id 26 :type "set-activity" :activity "book" :activity-number 23 :lesson 3 :level 1 :time-expected 300}
                      {:id 27 :type "set-activity" :activity "cycling" :activity-number 24 :lesson 3 :level 1 :time-expected 300}
                      {:id 28 :type "set-activity" :activity "painting-tablet" :activity-number 25 :lesson 3 :level 1 :time-expected 300}

                      {:id 29 :type "set-activity" :activity "hide-n-seek" :activity-number 26 :lesson 3 :level 1 :time-expected 300
                       :scored true :expected-score-percentage 90}
                      {:id 11 :type "set-activity" :activity "cinema" :activity-number 100 :lesson 1 :level 2}
                      {:id 12 :type "set-activity" :activity "cinema-video" :activity-number 101 :lesson 2 :level 2}
                      ]
   :default-progress {:current-activity "home"
                      :variables {:last-location "home"}
                      :finished-workflow-actions #{}
                      :scene-activities          {:home            1
                                                  :see-saw         2
                                                  :swings          3
                                                  :sandbox         4
                                                  :volleyball      5
                                                  :book            6
                                                  :cycling         7
                                                  :painting-tablet 8
                                                  :hide-n-seek 9}}})

(defn get-scene
  [course-id scene-id]
  (get-in courses [course-id scene-id]))
