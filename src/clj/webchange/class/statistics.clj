(ns webchange.class.statistics
  (:require
    [webchange.db.core :as db]
    [webchange.events :as e]))

(defn- calculate-class-stats
  [class-id]
  (let [stats (db/calculate-class-stats {:id class-id})]
    (db/update-class-stats! {:id class-id :stats stats})))

(e/reg
 ::recalculate-on-create-student :classes/created
 (fn [{class-id :class-id}]
   (calculate-class-stats class-id)))

(e/reg
 ::recalculate-on-create-student :students/created
 (fn [{class-id :class-id}]
   (calculate-class-stats class-id)))

(e/reg
  ::recalculate-on-assign-student :students/assigned-to-class
 (fn [{class-id :class-id}]
   (calculate-class-stats class-id)))

(e/reg
 ::recalculate-on-assign-teacher :teachers/assigned-to-class
 (fn [{class-id :class-id}]
   (calculate-class-stats class-id)))
