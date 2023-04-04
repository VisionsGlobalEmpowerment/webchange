(ns webchange.progress.school-profile
  (:require [webchange.db.core :as db]
            [clojure.tools.logging :as log]
            [webchange.events :as events]))

(events/reg
 ::school-activities-played :activity-finished
 (fn [{:keys [school-id scene-id time-spent] :or {time-spent 0}}]
   (let [{:keys [type]} (db/get-scene-by-id {:id scene-id})
         stat-type (if (= "book" type)
                     :books-read
                     :activities-played)]
     (if-let [{:keys [data]} (db/get-school-stat {:school_id school-id})]
       (db/save-school-stat! {:school_id school-id
                              :data (-> data
                                        (update-in [stat-type] (fnil + 0) 1)
                                        (update-in [:time-spent] (fnil + 0) time-spent))})
       (db/create-school-stat! {:school_id school-id
                                :data {stat-type 1
                                       :time-spent time-spent}})))))

(events/reg
 ::school-activities-time :activity-stopped
 (fn [{:keys [school-id time-spent] :or {time-spent 0}}]
   (if-let [{:keys [data]} (db/get-school-stat {:school_id school-id})]
     (db/save-school-stat! {:school_id school-id
                            :data (-> data
                                      (update-in [:time-spent] (fnil + 0) time-spent))})
     (db/create-school-stat! {:school_id school-id
                              :data {:time-spent time-spent}}))))
