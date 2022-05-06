(ns webchange.school.statistics
  (:require
    [webchange.db.core :as db]
    [webchange.events :as e]))

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
