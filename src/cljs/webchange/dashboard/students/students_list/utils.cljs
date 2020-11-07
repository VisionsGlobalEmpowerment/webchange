(ns webchange.dashboard.students.students-list.utils
  (:require
    [clojure.string :as s]))

(defn map-student
  [{:keys [id class-id user class gender date-of-birth]}]
  (let [{:keys [first-name last-name]} user
        age (if (and date-of-birth (not (s/blank? date-of-birth)))
              (- (.getFullYear (let [current-time (.getTime (js/Date.))
                                     birth-time (.getTime (js/Date. date-of-birth))
                                     age-time (- current-time birth-time)
                                     age-date (js/Date.)
                                     _ (.setTime age-date age-time)]
                                 age-date)) 1970)
              nil)]
    {:id         id
     :class-id   class-id
     :first-name first-name
     :last-name  last-name
     :name       (str first-name " " last-name)
     :gender     gender
     :age        age
     :class      (s/upper-case (:name class))
     :course     nil
     :tablet?    nil}))

(defn map-students-list
  [students]
  (map map-student students))

(defn- check-student-by-filter
  [student filter]
  (let [get-value #(->> (get %1 %2)
                        (str)
                        (s/lower-case))
        check-filter-map (reduce
                           #(assoc %1 %2
                                      (s/includes? (get-value student %2) (get-value filter %2))
                                      ) {} (vec (keys filter)))]
    (->> check-filter-map
         (vals)
         (vec)
         (reduce #(and %1 %2) true))))

(defn filter-students-list
  [custom-filter students]
  (filter #(check-student-by-filter % custom-filter) students))
