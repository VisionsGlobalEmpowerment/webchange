(ns webchange.interpreter.utils.scenes-list)

(def data {:home            {:name    "Casa"
                             :preview "/images/dashboard/scene-preview/Casa_Room.jpg"
                             :type    "non-scored"
                             :outs    [{:name "map" :x 1457 :y 630 :object "door"}]}
           :letter-intro    {:name    "Letter Introduction"
                             :preview "/images/dashboard/scene-preview/Casa_Room.jpg"
                             :type    "non-scored"
                             :outs    [{:name "map" :x 1457 :y 330 :object "door"}]}
           :map             {:name    "Map"
                             :preview "/images/dashboard/scene-preview/Casa_Room.jpg"
                             :type    "non-scored"
                             :outs    [{:name "park" :x 1447 :y 860 :object "park"}
                                       {:name "library" :x 181 :y 419 :object "library"}
                                       {:name "stadium" :x 1581 :y 269 :object "stadium"}
                                       {:name "home" :x 881 :y 490 :object "home"}]}
           :park            {:name    "Park"
                             :preview "/images/dashboard/scene-preview/Park_Main.jpg"
                             :type    "non-scored"
                             :outs    [{:name "map" :x 100 :y 100 :object "back"}
                                       {:name "see-saw" :x 407 :y 860 :object "see-saw"}
                                       {:name "swings" :x 1637 :y 660 :object "swings"}
                                       {:name "sandbox" :x 937 :y 810 :object "sandbox"}
                                       {:name "hide-n-seek" :x 987 :y 670 :object "hide-n-seek"}]}
           :see-saw         {:name    "Sea-saw"
                             :preview "/images/dashboard/scene-preview/Park_See-Saw.jpg"
                             :type    "non-scored"
                             :outs    [{:name "park" :x 100 :y 100 :object "back"}]}
           :park-poem       {:name    "Poem"
                             :preview "/images/dashboard/scene-preview/Park_Main.jpg"
                             :type    "non-scored"
                             :outs    [{:name "park" :x 100 :y 100 :object "back"}]}
           :slide           {:name    "Slide"
                             :preview "/images/dashboard/scene-preview/Park_Slide.jpg"
                             :type    "non-scored"
                             :outs    [{:name "park" :x 100 :y 100 :object "back"}]}
           :swings          {:name    "Swings"
                             :preview "/images/dashboard/scene-preview/Park_Swing.jpg"
                             :type    "non-scored"
                             :outs    [{:name "park" :x 100 :y 100 :object "back"}]}
           :sandbox         {:name    "Sandbox"
                             :preview "/images/dashboard/scene-preview/Park_Sandbox.jpg"
                             :type    "non-scored"
                             :outs    [{:name "park" :x 100 :y 100 :object "back"}]}
           :hide-n-seek     {:name    "Hide and Seek"
                             :preview "/images/dashboard/scene-preview/Park_Hide-and-seek.jpg"
                             :type    "assessment"
                             :outs    [{:name "park" :x 100 :y 100 :object "back"}]}
           :stadium         {:name    "Stadium"
                             :preview "/images/dashboard/scene-preview/Stadium_Main.jpg"
                             :type    "non-scored"
                             :outs    [{:name "map" :x 100 :y 100 :object "back"}
                                       {:name "volleyball" :x 857 :y 870 :object "volleyball"}
                                       {:name "cycling" :x 357 :y 870 :object "volleyball"}]}
           :volleyball      {:name    "Volleyball"
                             :preview "/images/dashboard/scene-preview/Stadium_Volleyball.jpg"
                             :type    "non-scored"
                             :outs    [{:name "stadium" :x 100 :y 100 :object "back"}]}
           :cinema          {:name    "Cinema"
                             :preview "/images/dashboard/scene-preview/Cinema-Room.jpg"
                             :type    "non-scored"
                             :outs    [{:name "map" :x 100 :y 100 :object "back"}]}
           :cinema-video    {:name    "Cinema Video"
                             :preview "/images/dashboard/scene-preview/Cinema-Room.jpg"
                             :type    "non-scored"
                             :outs    [{:name "map" :x 100 :y 100 :object "back"}]}
           :cycling         {:name    "Cycling"
                             :preview "/images/dashboard/scene-preview/Stadium_Cycling-Race.jpg"
                             :type    "non-scored"
                             :outs    [{:name "stadium" :x 100 :y 100 :object "back"}]}
           :running         {:name    "Running"
                             :preview "/images/dashboard/scene-preview/Stadium_Running.jpg"
                             :type    "non-scored"
                             :outs    [{:name "stadium" :x 100 :y 100 :object "back"}]}
           :library         {:name    "Library"
                             :preview "/images/dashboard/scene-preview/Library_Room.jpg"
                             :type    "non-scored"
                             :outs    [{:name "map" :x 100 :y 100 :object "back"}
                                       {:name "book" :x 1357 :y 750 :object "book"}
                                       {:name "painting-tablet" :x 1057 :y 780 :object "tablet"}]}
           :book            {:name    "Book"
                             :preview "/images/dashboard/scene-preview/Library_Book.jpg"
                             :type    "non-scored"
                             :outs    [{:name "library" :x 100 :y 100 :object "back"}]}
           :painting-tablet {:name    "Painting"
                             :preview "/images/dashboard/scene-preview/Library_Drawing-Lesson.jpg"
                             :type    "non-scored"
                             :outs    [{:name "library" :x 100 :y 100 :object "back"}]}})
