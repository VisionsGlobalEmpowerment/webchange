(ns webchange.progress.class-profile
  (:require [webchange.db.core :as db]
            [webchange.events :as events]
            [clojure.tools.logging :as log]))

(events/reg
 ::start-date :course-started
 (fn [{created-at :created-at user-id :user-id course-id :course-id}]
   (when-not (db/get-user-course-stat {:user_id user-id :course_id course-id})
     (let [{class-id :class-id} (db/get-student-by-user {:user_id user-id})]
       (db/create-course-stat! {:user_id user-id :class_id class-id :course_id course-id :data {:started-at created-at :last-login created-at}})))))

(events/reg
 ::last-login :course-started
 (fn [{created-at :created-at user-id :user-id course-id :course-id}]
   (when-let [{:keys [id data]} (db/get-user-course-stat {:user_id user-id :course_id course-id})]
     (db/save-course-stat! {:id id :data (assoc data :last-login created-at)}))))

(events/reg
 ::latest-activity :activity-started
 (fn [{:keys [user-id course-id level lesson activity activity-name course-in-progress]}]
   (when course-in-progress
     (when-let [{:keys [id data]} (db/get-user-course-stat {:user_id user-id :course_id course-id})]
       (db/save-course-stat! {:id id :data (assoc data :latest-activity {:id activity-name :level level :lesson lesson :activity activity})})))))

(events/reg
 ::activity-progress :activity-progress
 (fn [{:keys [user-id course-id activity-progress course-in-progress]}]
   (when course-in-progress
     (when-let [{:keys [id data]} (db/get-user-course-stat {:user_id user-id :course_id course-id})]
       (let [current-progress (or (:activity-progress data) 0)]
         (when (> activity-progress current-progress)
           (db/save-course-stat! {:id id :data (assoc data :activity-progress activity-progress)})))))))

(events/reg
 ::books-read :activity-finished
 (fn [{:keys [user-id course-id scene-id]}]
   (let [{:keys [type]} (db/get-scene-by-id {:id scene-id})
         {:keys [id data]} (db/get-user-course-stat {:user_id user-id :course_id course-id})]
     (when (and (= "book" type) data)
       (db/save-course-stat! {:id id :data (update data :books-read (fnil + 0) 1)})))))

(events/reg
 ::cumulative-score :student-activity-score-changed
 (fn [{:keys [user-id course-id old-score new-score]}]
   (when-let [{:keys [id data]} (db/get-user-course-stat {:user_id user-id :course_id course-id})]
     (let [calc (fn [name] (let [old (or (get old-score name) 0)
                                 new (or (get new-score name) 0)
                                 cumulative (or (get (:cumulative-score data) name) 0)]
                             (-> cumulative
                                 (+ new)
                                 (- old))))
           new-score {:correct (calc :correct)
                      :incorrect (calc :incorrect)
                      :mistake (calc :mistake)}]
       (db/save-course-stat! {:id id :data (assoc data :cumulative-score new-score)})))))

(events/reg
 ::time-goal-from-score :student-activity-score-changed
 (fn [{:keys [user-id course-id time-spent] :or {time-spent 0}}]
   (when-let [{:keys [id data]} (db/get-user-course-stat {:user_id user-id :course_id course-id})]
     (db/save-course-stat! {:id id :data (update-in data [:cumulative-time] (fnil + 0) time-spent)}))))

(events/reg
 ::time-goal-from-time :student-activity-time-changed
 (fn [{:keys [user-id course-id time-spent] :or {time-spent 0}}]
   (when-let [{:keys [id data]} (db/get-user-course-stat {:user_id user-id :course_id course-id})]
     (db/save-course-stat! {:id id :data (update-in data [:cumulative-time] (fnil + 0) time-spent)}))))

(events/reg
 ::class-activities-played :activity-finished
 (fn [{:keys [user-id scene-id time-spent] :or {time-spent 0}}]
   (when-let [{:keys [class-id]} (db/get-student-by-user {:user_id user-id})]
     (let [{:keys [type]} (db/get-scene-by-id {:id scene-id})
           stat-type (if (= "book" type)
                       :books-read
                       :activities-played)]
       (if-let [{:keys [data]} (db/get-class-stat {:class_id class-id})]
         (db/save-class-stat! {:class_id class-id
                               :data (-> data
                                         (update-in [stat-type] (fnil + 0) 1)
                                         (update-in [:time-spent] (fnil + 0) time-spent))})
         (db/create-class-stat! {:class_id class-id
                                 :data {stat-type 1
                                        :time-spent time-spent}}))))))

(events/reg
 ::class-activities-time :activity-stopped
 (fn [{:keys [user-id time-spent] :or {time-spent 0}}]
   (when-let [{:keys [class-id]} (db/get-student-by-user {:user_id user-id})]
     (if-let [{:keys [data]} (db/get-class-stat {:class_id class-id})]
       (db/save-class-stat! {:class_id class-id
                             :data (-> data
                                       (update-in [:time-spent] (fnil + 0) time-spent))})
       (db/create-class-stat! {:class_id class-id
                               :data {:time-spent time-spent}})))))
