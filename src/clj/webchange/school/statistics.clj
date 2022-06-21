(ns webchange.school.statistics
  (:require
    [webchange.db.core :as db]
    [webchange.events :as e]))

(def overall-statistics (atom nil))

(defn- reset-statistics!
  []
  (reset! overall-statistics nil))

(defn get-overall-statistics
  []
  (when-not @overall-statistics
    (reset! overall-statistics (first (db/calculate-overall-statistics))))
  @overall-statistics)

(defn- calculate-school-stats
  [school-id]
  (let [stats (db/calculate-school-stats {:id school-id})]
    (db/update-school-stats! {:id school-id :stats stats})))

(e/reg
 ::init :schools/created
 (fn [{school-id :school-id}]
   (calculate-school-stats school-id)))

(e/reg
 ::recalculate-on-create-teacher :teachers/created
 (fn [{school-id :school-id}]
   (calculate-school-stats school-id)))

(e/reg
 ::recalculate-on-create-student :students/created
 (fn [{school-id :school-id}]
   (calculate-school-stats school-id)))

(e/reg
 ::recalculate-on-create-class :classes/created
 (fn [{school-id :school-id}]
   (calculate-school-stats school-id)))

(e/reg
 ::recalculate-on-assign-course :courses/assigned-to-school
 (fn [{school-id :school-id}]
   (calculate-school-stats school-id)))

(e/reg
 ::recalculate-on-archive-class :classes/archived
 (fn [{class-id :class-id}]
   (let [{school-id :school-id} (db/get-class {:id class-id})]
     (calculate-school-stats school-id))))

(e/reg
 ::recalculate-on-archive-teacher :teachers/archived
 (fn [{teacher-id :teacher-id}]
   (let [{school-id :school-id} (db/get-teacher {:id teacher-id})]
     (calculate-school-stats school-id))))

(e/reg
 ::recalculate-on-archive-student :students/archived
 (fn [{student-id :student-id}]
   (let [{school-id :school-id} (db/get-student {:id student-id})]
     (calculate-school-stats school-id))))

(e/reg
 ::overall-on-archive-school :schools/archived
 (fn [_]
   (reset-statistics!)))

(e/reg
 ::overall-on-archive-class :classes/archived
 (fn [_]
   (reset-statistics!)))

(e/reg
 ::overall-on-archive-teacher :teachers/archived
 (fn [_]
   (reset-statistics!)))

(e/reg
 ::overall-on-archive-student :students/archived
 (fn [_]
   (reset-statistics!)))

(e/reg
 ::overall-on-school-created :schools/created
 (fn [_]
   (reset-statistics!)))

(e/reg
 ::overall-on-class-created :classes/created
 (fn [_]
   (reset-statistics!)))

(e/reg
 ::overall-on-teacher-created :teachers/created
 (fn [_]
   (reset-statistics!)))

(e/reg
 ::overall-on-student-created :students/created
 (fn [_]
   (reset-statistics!)))
