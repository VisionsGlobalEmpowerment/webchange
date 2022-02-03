(ns webchange.parent.core
  (:require
    [webchange.db.core :refer [*db*] :as db]
    [webchange.auth.core :as auth]
    [java-time :as jt]
    [clojure.tools.logging :as log]))

(def course {:id   4
             :slug "english"})

(defn- ->display-value
  [number]
  (-> number (or 0) (inc)))

(defn- progress-finished?
  [progress]
  (let [{:keys [next finished]} (get progress :data)
        {:keys [activity level lesson]} next]
    (-> (and (= level 0)
             (= lesson 0)
             (= activity 1)
             (some #{activity} (get-in finished [:0 :0] [])))
        (boolean))))

(defn- with-progress
  [{id :id :as child}]
  (let [progress (db/get-progress {:user_id   id
                                   :course_id (:id course)})
        {:keys [level lesson]} (get-in progress [:data :next])]
    (assoc child
      :level (->display-value level)
      :lesson (->display-value lesson)
      :finished (progress-finished? progress))))

(defn- ->student
  [child]
  {:id          (:id child)
   :name        (:first-name child)
   :first-name  (:first-name child)
   :last-name   (:last-name child)
   :course-slug (:slug course)
   :level       (:level child)
   :lesson      (:lesson child)
   :finished    (:finished child)})

(defn- ->parent
  [user]
  {:id         (:id user)
   :name       (:first-name user)
   :first-name (:first-name user)
   :last-name  (:last-name user)})

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
    [true (->student {:id         user-id
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

(defn parent-login!
  [user-id]
  (let [parent-id (if-let [child (db/get-child-by-id {:child_id user-id})]
                    (:parent-id child)
                    user-id)]
    [true (->> (db/get-user {:id parent-id})
               ->parent)]))

(comment
  (-> (db/get-progress {:user_id 1 :course_id 4})))
