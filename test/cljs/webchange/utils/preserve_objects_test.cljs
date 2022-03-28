(ns webchange.utils.preserve-objects-test
  (:require
    [cljs.test :refer [deftest testing is]]
    [webchange.utils.preserve-objects :refer [update-preserved-objects]]))

(deftest test-preserve-background
  (testing "Removed layered background items should not reappear"
    (let [new-activity-data
          {:objects {:background {:type       "layered-background"
                                  :background {:src "/raw/img/casa/background_casa.png"}
                                  :decoration {:src "/raw/img/casa/decoration_casa.png"}
                                  :surface    {:src "/raw/img/casa/surface_casa.png"}}}}

          old-activity-data
          {:objects {:background {:type       "layered-background"
                                  :surface    {:src "/raw/clipart/stadium/stadium_surface.png"}
                                  :background {:src "/raw/clipart/library_main/library_background.png"}}}}]
      (let [actual-result (update-preserved-objects new-activity-data old-activity-data)
            expected-result {:objects {:background {:type       "layered-background"
                                                    :surface    {:src "/raw/clipart/stadium/stadium_surface.png"}
                                                    :background {:src "/raw/clipart/library_main/library_background.png"}}}}]
        (is (= actual-result expected-result)))))

  (testing "Background should keep its type"
    (let [new-activity-data
          {:objects {:background {:type "background"
                                  :src  "/raw/img/casa/background.jpg"}}}

          old-activity-data
          {:objects {:background {:type       "layered-background"
                                  :surface    {:src "/raw/clipart/swing/swing_surface.png"}
                                  :background {:src "/raw/clipart/parque_hide_and_seek/hide_and_seek_background.png"}}}}]
      (let [actual-result (update-preserved-objects new-activity-data old-activity-data)
            expected-result {:objects {:background {:type       "layered-background"
                                                    :surface    {:src "/raw/clipart/swing/swing_surface.png"}
                                                    :background {:src "/raw/clipart/parque_hide_and_seek/hide_and_seek_background.png"}}}}]
        (is (= actual-result expected-result))))))
