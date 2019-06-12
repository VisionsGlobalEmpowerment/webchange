(ns webchange.student-dashboard.stubs)

(def stories [{:id        11
               :name      "See saw"
               :image     "/raw/img/student_dashboard/scenes/see_saw.jpg"
               :completed true}
              {:id        12
               :name      "Swing"
               :image     "/raw/img/student_dashboard/scenes/swing.jpg"
               :completed true}
              {:id        13
               :name      "Sandbox"
               :image     "/raw/img/student_dashboard/scenes/sandbox.jpg"
               :completed false}])

(def assessments [{:id        21
                   :name      "Hide and seek"
                   :image     "/raw/img/student_dashboard/scenes/hide_and_seek.jpg"
                   :completed true}
                  {:id        22
                   :name      "Pinata"
                   :image     "/raw/img/student_dashboard/scenes/pinata.jpg"
                   :completed false}])

(def related-content [{:id   31
                       :name "The Alphabet Chant"
                       :type :video
                       :link {:source :youtube
                              :id     "cR-Qr1V8e_w"}}])

(def life-skills [{:id   41
                   :name "Tooth Brushing Song by Blippi"
                   :type :video
                   :link {:source :youtube
                          :id     "Ku-ForS6G3I"}}
                  {:id   42
                   :name "Blippi Farm Tour"
                   :type :video
                   :link {:source :youtube
                          :id     "Dqq4H6JsP5A"}}])
