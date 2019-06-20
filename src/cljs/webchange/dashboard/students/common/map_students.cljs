(ns webchange.dashboard.students.common.map-students
  (:require
    [clojure.string :as s]))

(defn map-student
  [{:keys [id class-id user class gender date-of-birth] :as student}]
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
     :class      (if class (:name class) nil)
     :course     nil
     :tablet?    nil}))

(defn map-students-list
  [students]
  (map map-student students))
