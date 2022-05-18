(ns webchange.progress.student-profile
  (:require [webchange.db.core :refer [*db*] :as db]
            [clojure.tools.logging :as log]
            [webchange.auth.core :as auth]
            [webchange.events :as events]))

(defn activity->id [level lesson activity activity-name]
  (str level "-" lesson "-" activity "-" activity-name))

(events/reg
 ::student-activity-score :activity-finished
 (fn [{:keys [user-id course-id unique-id score level lesson activity activity-name time-spent created-at] :or {time-spent 0}}]
   (let [activity-id (activity->id level lesson activity activity-name)]
     (if-let [{:keys [id data]} (db/get-activity-stat {:user_id user-id :course_id course-id :unique_id unique-id})]
       (let [old-score (:score data)]
         (db/save-activity-stat! {:id id :data (-> data
                                                   (assoc :last-played created-at)
                                                   (assoc :score score)
                                                   (update-in [:time-spent] (fnil + 0) time-spent))})
         (events/dispatch {:type :student-activity-score-changed :user-id user-id :course-id course-id :unique-id unique-id :activity-id activity-id
                           :old-score old-score :new-score score
                           :time-spent time-spent}))
       (do
         (db/create-activity-stat! {:user_id user-id :course_id course-id :unique_id unique-id :activity_id activity-id :data {:score score
                                                                                                                               :time-spent time-spent
                                                                                                                               :last-played created-at}})
         (events/dispatch {:type :student-activity-score-changed :user-id user-id :course-id course-id :unique_id unique-id :activity-id activity-id
                           :old-score nil :new-score score
                           :time-spent time-spent}))))))

(events/reg
 ::student-activity-time :activity-stopped
 (fn [{:keys [user-id course-id unique-id level lesson activity activity-name time-spent created-at] :or {time-spent 0}}]
   (let [activity-id (activity->id level lesson activity activity-name)]
     (if-let [{:keys [id data]} (db/get-activity-stat {:user_id user-id :course_id course-id :unique_id unique-id})]
       (do
         (db/save-activity-stat! {:id id :data (-> data
                                                   (assoc :last-played created-at)
                                                   (update-in [:time-spent] (fnil + 0) time-spent))})
         (events/dispatch {:type :student-activity-time-changed :user-id user-id :course-id course-id :unique_id unique-id :activity-id activity-id
                           :time-spent time-spent}))
       (do
         (db/create-activity-stat! {:user_id user-id :course_id course-id :unique_id unique-id :activity_id activity-id :data {:time-spent time-spent
                                                                                                                               :last-played created-at}})
         (events/dispatch {:type :student-activity-time-changed :user-id user-id :course-id course-id :unique_id unique-id :activity-id activity-id
                           :time-spent time-spent}))))))
