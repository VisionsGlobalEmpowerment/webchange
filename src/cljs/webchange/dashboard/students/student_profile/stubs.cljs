(ns webchange.dashboard.students.student-profile.stubs)

(defn get-rand-scores [] (vec (take (+ 3 (rand-int 3)) (repeatedly #(+ 70 (rand-int 50))))))

(defn scores-stub
  []
  {:title       "Activity"
   :items-title "Lesson"
   :marks       [80 95]
   :levels      {1 [{:name   "L1"
                     :values (get-rand-scores)}
                    {:name   "L2"
                     :values (get-rand-scores)}
                    {:name   "L3"
                     :values (get-rand-scores)}]
                 2 [{:name   "L4"
                     :values (get-rand-scores)}
                    {:name   "L5"
                     :values (get-rand-scores)}
                    {:name   "L6"
                     :values (get-rand-scores)}]
                 3 [{:name   "L7"
                     :values (get-rand-scores)}
                    {:name   "L8"
                     :values (get-rand-scores)}
                    {:name   "L9"
                     :values (get-rand-scores)}]}})
