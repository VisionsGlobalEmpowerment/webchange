(ns webchange.dev-templates.letter-intro
  (:require [webchange.dev-templates :as t]
            [webchange.templates.core :as templates]
            [webchange.course.core :as core]))
(comment
  "/courses/test-course-english-crjeqlzi/editor-v2/test-activity"

  (def test-course-slug (-> (t/create-test-course-with-dataset) :slug))
  (def test-course-slug (-> (t/create-test-course) :slug))
  (def test-course-slug "test-course-english-crjeqlzi")
  (def scene-slug "test-activity")

  (t/update-activity test-course-slug scene-slug :keep-dialogs true)

  (let [data {:activity-name "Letter-into"
              :template-id   39
              :lang          "English"
              :skills        []}
        activity (templates/activity-from-template data)
        metadata (templates/metadata-from-template data)
        [_ {scene-slug :scene-slug}] (core/create-scene! activity metadata test-course-slug scene-slug [] t/user-id)]
    (str "/courses/" test-course-slug "/editor-v2/" scene-slug))
  )

(comment
  (let [course-slug "english"]
    (->> (dataset/get-course-lessons course-slug)
         :items
         (map (fn [{:keys [data]}]
                [(get-in data [:letter]) (get-in data [:letter-path])])))))

(comment
  (let [get-letter (fn [letter list]
                     (filter #(= letter (first %)) list))
        get-nth (fn [idx list]
                  [(nth list idx)])

        action-template {:target "" :type "path-animation" :state "play"}
        object-template {:type         "animated-svg-path"
                         :width        325
                         :height       225
                         :scale-x      0.75
                         :scale-y      0.75
                         :stroke       "#323232"
                         :stroke-width 15
                         :line-cap     "round"
                         :animation    "stop"
                         :duration     5000}
        letters [
                 ["a" "M 144.76 92.43 A 37.5 37.5 0 1 0 144.76 131.71 L 144.76 74.5 M 144.76 74.5 v 75"]
                 ["b" "M 77 7 v 142 M 77 92.64 a 37.5 37.5 0 1 1 0 39.28"]
                 ["c" "M 147 92 a 37.51 37.51 0 1 0 0 39.27"]
                 ["d" "M 147 92 a 38 38 0 1 0 0 39 L 147 7 M 147 7 v 145"]
                 ["e" "M 77 112 h 74 M 151 112 A 32 32 0 1 0 77 112 Q 77 145 109 151 Q 135 155 151 142"]
                 ["f" "M 124 42 Q 124 24 108 24 T 92 42 V 154 M 69 85 H 116"]
                 ["g" "M 147 93 A 38 38 0 1 0 147 132 L 147 75 M 147 75 V 181 A 35 35 0 0 1 79 192"]
                 ["h" "M 77 7 v 142 M 77 92.64 a 37.51 37.51 0 0 1 69.46 19.64 v 37.5"]
                 ["i" "M 102 74 V 149 M 102 36 L 102 35"]
                 ["j" "M 107 74 V 180 A 35 35 0 0 1 39 191 M 107 35 L 107 34"]
                 ["k" "M 113 22 V 161 M 162 76 L 113 114 M 113 114 L 161 161"]
                 ["l" "M 112 7 v 143"]
                 ["m" "M 72 75 v 75 M 72 150 L 72 92 C 72 81 89 76 101.17 78.27 C 113.06 81.07 121.1 90.78 121.1 101.54 V 150 M 121 150 L 121 91 c 0 -10 15 -16 29.32 -13.52 c 11.89 2.8 19.93 12.51 19.93 23.27 V 150"]
                 ["n" "M 82 74.42 v 75 M 82 149 L 82 92 A 32.35 32.35 0 0 1 143 106.77 v 42.65"]
                 ["o2" "M110.65,74a37.5,37.5,0,1,0,37.5,37.5A37.5,37.5,0,0,0,110.65,74Z"]
                 ["p" "M 77.77 74.37 v 140 M 77.77 93.1 a 37.5 37.5 0 1 1 0 39.28"]
                 ["qu" "M 149 93 A 38 38 0 1 0 149 132 V 74 M 149 74 V 207 A 1 1 0 0 0 172 207"]
                 ["r" "M85.86,75.66v75M85.85,92.79a32.37,32.37,0,0,1,53.3-5.63"]
                 ["s" "M131.91,90.75c1.33-9.39-10.49-14.17-18.28-14.39A21.71,21.71,0,0,0,97.69,82.7a16.08,16.08,0,0,0-3.89,16c1.65,5.12,6.13,8.58,10.93,10.57c6.67,2.77,13.9,4,20.26,7.62c7,3.94,10.77,12.34,8,20.14c-5.06,14.06-27.62,17.67-37.73,5.77a18.91,18.91,0,0,1-4.11-10.2"]
                 ["t" "M112.5,35.29v115M89.4,75.29h46.2"]
                 ["u" "M 82 75 V 120 a 20 20 0 0 0 58 0 V 75 M 140 75 V 149"]
                 ["v" "M79.56,75l32.88,75M112.56,150l32.88-75"]
                 ["w" "M73.71,75.66l18.58,75M93.71,150.66l18.58-75M112.71,75.66l18.58,75M132.71,150.66l18.58-75"]
                 ["x" "M81.53,75.66l61.94,75M143.47,75.66l-61.94,75"]
                 ["y" "M78.19,74.78l34.84,75M146.81,74.78,96.61,188.91"]
                 ["z" "M 80 76 H 141 M 141 76 L 80 151 M 80 151 H 145"]
                 ]

        dx 160
        dy 200
        ox 200
        oy 80
        columns 9]
    (->> letters
         (get-letter "e")
         (map-indexed vector)
         (reduce (fn [result [idx [letter path]]]
                   (let [x (-> idx (mod columns) (* dx) (+ ox))
                         y (-> idx (quot columns) (* dy) (+ oy))

                         object-name (str "letter-path-" letter)
                         object-data (-> object-template
                                         (assoc :x x)
                                         (assoc :y y)
                                         (assoc :path path)
                                         (assoc :scene-name object-name))
                         action-data (assoc action-template :target object-name)]
                     (-> result
                         (assoc-in [:objects (keyword object-name)] object-data)
                         (update-in [:actions :start-scene :data] conj action-data)
                         (update-in [:scene-objects 0] conj object-name))))
                 {:objects       {}
                  :actions       {:start-scene {:type "parallel"
                                                :data []}}

                  :scene-objects [[]]
                  :assets        []
                  :skills        []
                  :triggers      {:start {:on "start", :action "start-scene"}}}))))
