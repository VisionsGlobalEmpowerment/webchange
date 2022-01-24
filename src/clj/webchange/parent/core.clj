(ns webchange.parent.core
  (:require
    [webchange.db.core :refer [*db*] :as db]
    [webchange.auth.core :as auth]
    [java-time :as jt]))

(def course {:id 4
             :slug "english"})

(defn- with-progress
  [{id :id :as child}]
  (let [course-id 4
        course-slug "english"
        {{:keys [level lesson]} :next} (db/get-progress {:user_id id
                                                         :course_id (:course-id course)})]
    (assoc child
      :level level
      :lesson lesson)))

(defn- ->student
  [child]
  {:id (:id child)
   :name (:first-name child)
   :first-name (:first-name child)
   :last-name (:last-name child)
   :course-slug (:slug course)
   :level (or (:level child) 1)
   :lesson (or (:lesson child) 1)})

(defn find-students-by-parent
  [parent-id]
  (let [children (db/find-users-by-parent {:parent_id parent-id})
        students (->> children
                      (map with-progress)
                      (map ->student))]
    [true students]))

(defn create-student
  [{:keys [name age device]} parent-id]
  (let [[{user-id :id}] (auth/create-user! {:first-name name})]
    (db/create-child! {:parent_id parent-id :child_id user-id :data {:age age :device device}})
    (auth/activate-user! user-id)
    [true (->student {:id user-id
                      :first-name name})]))

(defn parent-of?
  [student-id parent-id]
  (db/is-parent? {:child_id student-id :parent_id parent-id}))

(defn delete-student
  [student-id]
  (db/delete-course-progresses-by-user-id! {:user_id student-id})
  (db/delete-activity-stats-by-user-id! {:user_id student-id})
  (db/delete-course-stat! {:user_id student-id})
  (db/delete-course-events-by-user-id! {:user_id student-id})
  (db/delete-child! {:child_id student-id})
  (db/delete-user! {:id student-id})
  [true {:user-id student-id}])

(defn child-login!
  [child-id]
  (if-let [child (db/get-child-by-id {:child_id child-id})]
    (let [user (-> (db/get-user {:id (:child-id child)})
                   with-progress
                   ->student)]
      [true user])
    [false {:errors {:form "Child not found"}}]))

(comment
  (-> (db/get-progress {:user_id 1 :course_id 4})))
