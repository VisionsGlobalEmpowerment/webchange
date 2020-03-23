(ns webchange.progress.class-profile
  (:require [webchange.db.core :refer [*db*] :as db]
            [clojure.tools.logging :as log]
            [camel-snake-kebab.extras :refer [transform-keys]]
            [camel-snake-kebab.core :refer [->snake_case_keyword]]
            [webchange.auth.core :as auth]
            [webchange.events :as events]))

(events/reg
  ::start-date :course-started
  (fn [{created-at :created-at user-id :user-id course-id :course-id}]
    (if-not (db/get-user-course-stat {:user_id user-id :course_id course-id})
      (let [{class-id :class-id} (db/get-student-by-user {:user_id user-id})]
        (db/create-course-stat! {:user_id user-id :class_id class-id :course_id course-id :data {:started-at created-at :last-login created-at}})))))

(events/reg
  ::last-login :course-started
  (fn [{created-at :created-at user-id :user-id course-id :course-id}]
    (if-let [{:keys [id data]} (db/get-user-course-stat {:user_id user-id :course_id course-id})]
      (db/save-course-stat! {:id id :data (assoc data :last-login created-at)}))))

(events/reg
  ::latest-activity :activity-started
  (fn [{:keys [user-id course-id level lesson activity]}]
    (if-let [{:keys [id data]} (db/get-user-course-stat {:user_id user-id :course_id course-id})]
      (db/save-course-stat! {:id id :data (assoc data :latest-activity {:id activity :level level :lesson lesson})}))))

(events/reg
  ::activity-progress :activity-progress
  (fn [{:keys [user-id course-id activity-progress]}]
    (if-let [{:keys [id data]} (db/get-user-course-stat {:user_id user-id :course_id course-id})]
      (let [current-progress (or (:activity-progress data) 0)]
        (if (> activity-progress current-progress)
          (db/save-course-stat! {:id id :data (assoc data :activity-progress activity-progress)}))))))

(events/reg
  ::cumulative-score :student-activity-score-changed
  (fn [{:keys [user-id course-id old-score new-score]}]
    (if-let [{:keys [id data]} (db/get-user-course-stat {:user_id user-id :course_id course-id})]
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
    (if-let [{:keys [id data]} (db/get-user-course-stat {:user_id user-id :course_id course-id})]
      (db/save-course-stat! {:id id :data (update-in data [:cumulative-time] (fnil + 0) time-spent)}))))

(events/reg
  ::time-goal-from-time :student-activity-time-changed
  (fn [{:keys [user-id course-id time-spent] :or {time-spent 0}}]
    (if-let [{:keys [id data]} (db/get-user-course-stat {:user_id user-id :course_id course-id})]
      (db/save-course-stat! {:id id :data (update-in data [:cumulative-time] (fnil + 0) time-spent)}))))
