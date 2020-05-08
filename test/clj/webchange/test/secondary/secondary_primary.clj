(ns webchange.test.secondary.secondary-primary
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [webchange.test.fixtures.core :as f]
            [webchange.handler :as handler]
            [webchange.db.core :refer [*db*] :as db]
            [webchange.common.date-time :as dt]
            [mount.core :as mount]
            [clojure.data.json :as json]
            [webchange.secondary.core :as core]
            [clojure.tools.logging :as log]))

(use-fixtures :once f/init)
(use-fixtures :each f/clear-db-fixture f/with-default-school)

(deftest can-create-update-stats
  (let [course-created (f/course-created)
        user-created (f/teacher-user-created)
        teacher-created (f/teacher-created {:user-id (:user-id user-created)})
        course-stat-created (f/course-stat-created course-created)
        course-progresses-created (f/course-progresses-created course-stat-created)
        course-events-created (f/course-events-created course-stat-created)
        activity-stat-created (f/activity-stat-created {:id (:user-id course-stat-created)})
        stat (core/get-stat f/default-school-id)
        ]
      (let [
            student (first (:students stat))
            school (:school stat)
            course-progress (first (:course-progresses stat))
            course-stat (first (:course-stats stat))
            teacher (first (:teachers stat))
            course-event (first (:course-events stat))
            activity-stat (first (:activity-stats stat))
            class (first (:classes stat))
            user (first (:users stat))
            ]
        (assert (= (:id school) f/default-school-id))
        (assert (= (:id student) (:student-id course-stat-created)))
        (assert (= (:id course-progress) (:id course-progresses-created)))
        (assert (= (:id course-stat) (:id course-stat-created)))
        (assert (= (:id teacher) (:id teacher-created)))
        (assert (= (:id course-events-created) (:id course-event)))
        (assert (= (:id activity-stat-created) (:id activity-stat)))
        (assert (= (:user-id course-stat-created) (:id user)))
        (assert (= (:class-id course-stat-created) (:id class))))))

(deftest can-update-classes
  (let  [name "NewName"
         user (f/student-created)
         class (db/get-class {:id (:class-id user)})
         update (core/get-stat f/default-school-id)
         update-new (assoc update :classes [
                                            {:id (:id class)
                                             :guid (.toString (:guid class))
                                             :name name
                                             :school-id (:school-id class)
                                             }])
         ]
    (core/import-secondary-data! f/default-school-id update-new)
    (let [class-updates (db/get-class {:id (:id class)})]
      (assert (= name (:name class-updates))))))

(deftest can-remove-classes
  (let  [user (f/student-created)
         class (db/get-class {:id (:class-id user)})
         update (core/get-stat f/default-school-id)
         update (assoc update :classes [])
         update (assoc update :students [])]
    (core/import-secondary-data! f/default-school-id update)
    (let [class-updates (db/get-class {:id (:id class)})]
      (assert (= nil class-updates)))))

(deftest can-update-user
  (let  [new-code "654321"
         email "hello@example.com"
         student-user (f/student-created)
         student (db/get-student {:id (:id student-user)})
         user (db/get-user {:id (:user-id student-user)})
         update (-> (core/get-stat f/default-school-id)
                    (assoc :users [(-> user (assoc :email email)
                                    (assoc :last-login (dt/date-time2iso-str (:last-login user)))
                                    (assoc :created-at (dt/date-time2iso-str (:created-at user)))
                                    (assoc :guid (.toString (:guid user))))])
                    (assoc :students [(-> student (assoc :access-code new-code)
                                   (assoc :date-of-birth (dt/date2str (:date-of-birth student))))]))]
    (core/import-secondary-data! f/default-school-id update)
    (let [student (db/get-student {:id (:id student-user)})
          user (db/get-user {:id (:user-id student-user)})]
      (assert (= new-code (:access-code student)))
      (assert (= email (:email user))))))

(deftest can-remove-student
  (let  [student-user (f/student-created)
         update (-> (core/get-stat f/default-school-id)
                    (assoc :students []))]
    (core/import-secondary-data! f/default-school-id update)
    (let [student (db/get-student {:id (:id student-user)})]
      (assert (= nil student)))))

(deftest can-remove-student
  (let  [user (f/teacher-user-created)
         teacher (f/teacher-created {:user-id (:id user)})
         update (-> (core/get-stat f/default-school-id)
                    (assoc :teachers []))]
    (core/import-secondary-data! f/default-school-id update)
    (let [teacher (db/get-teacher-by-user {:user_id (:user-id teacher)})]
      (assert (= nil teacher)))))

(deftest can-remove-user
  (let  [student-user (f/student-created)
         user (f/teacher-user-created)
         teacher (f/teacher-created {:user-id (:id user)})
         update (as-> (core/get-stat f/default-school-id) upd
                    (assoc upd :users (filter (fn [user] (not (= (:user-id student-user) (:id user)))) (:users upd)))
                    (assoc upd :students []))]
    (core/import-secondary-data! f/default-school-id update)
    (let [user (db/get-user {:id (:user-id student-user)})]
      (assert (= nil user)))))

(deftest can-update-course-stat
  (let  [data {:hello "data"}
         course (f/course-created)
         stat (f/course-stat-created course)
         update (as-> (core/get-stat f/default-school-id) upd
                      (assoc upd :course-stats (map (fn [stat] (assoc stat :data data)) (:course-stats upd))))]
    (core/import-secondary-data! f/default-school-id update)
    (let [updated-stat (db/get-user-course-stat {:user_id (:user-id stat) :course_id (:course-id stat)})]
      (assert (= data (:data updated-stat)))
    )))

(deftest can-remove-course-stat
  (let  [course (f/course-created)
         stat (f/course-stat-created course)
         update (as-> (core/get-stat f/default-school-id) upd
                      (assoc upd :course-stats []))]
    (core/import-secondary-data! f/default-school-id update)
    (let [updated-stat (db/get-user-course-stat {:user_id (:user-id stat) :course_id (:course-id stat)})]
      (assert (= nil updated-stat))
    )))

(deftest can-update-course-progresses
  (let  [data {:hello "data"}
         course (f/course-created)
         student-user (f/student-created)
         course-progresses (f/course-progresses-created {:course-id (:id course) :user-id (:user-id student-user)})
         update (as-> (core/get-stat f/default-school-id) upd
                      (assoc upd :course-progresses (map (fn [stat] (assoc stat :data data)) (:course-progresses upd)))
                      )]
    (core/import-secondary-data! f/default-school-id update)
    (let [updated-course-progresses (db/get-progress {:user_id (:user-id course-progresses) :course_id (:course-id course-progresses)})]
      (assert (= data (:data updated-course-progresses)))
      )))

(deftest can-remove-course-progresses
  (let  [data {:hello "data"}
         course (f/course-created)
         student-user (f/student-created)
         course-progresses (f/course-progresses-created {:course-id (:id course) :user-id (:user-id student-user)})
         update (as-> (core/get-stat f/default-school-id) upd
                      (assoc upd :course-progresses [])
                      )]
    (core/import-secondary-data! f/default-school-id update)
    (let [updated-course-progresses (db/get-progress {:user_id (:user-id course-progresses) :course_id (:course-id course-progresses)})]
      (assert (= nil updated-course-progresses))
      )))

(deftest can-update-course-events
  (let  [data {:hello "data"}
         course (f/course-created)
         student-user (f/student-created)
         course-events (f/course-events-created {:course-id (:id course) :user-id (:user-id student-user)})
         update (as-> (core/get-stat f/default-school-id) upd
                      (assoc upd :course-events (map (fn [stat] (assoc stat :data data)) (:course-events upd))))]
    (core/import-secondary-data! f/default-school-id update)
    (let [updated-course-events (db/find-course-events-by-id  course-events)]
      (assert (= data (:data updated-course-events))))))

(deftest can-remove-course-events
  (let  [course (f/course-created)
         student-user (f/student-created)
         course-events (f/course-events-created {:course-id (:id course) :user-id (:user-id student-user)})
         update (as-> (core/get-stat f/default-school-id) upd
                      (assoc upd :course-events [])
                      )]
    (core/import-secondary-data! f/default-school-id update)
    (let [updated-course-events (db/find-course-events-by-id  course-events)]
      (assert (= nil updated-course-events)))))

(deftest can-update-activity-stats
  (let  [data {:hello "data"}
         student-user (f/student-created)
         activity-stat (f/activity-stat-created {:id (:user-id student-user)})
         update (as-> (core/get-stat f/default-school-id) upd
                      (assoc upd :activity-stats (map (fn [stat] (assoc stat :data data)) (:activity-stats upd))))]
    (println update)
    (core/import-secondary-data! f/default-school-id update)
    (let [[updated-activity-stat] (db/get-user-activity-stats  activity-stat)]
      (println updated-activity-stat)
      (assert (= data (:data updated-activity-stat))))))

(deftest can-update-activity-stats
  (let  [data {:hello "data"}
         student-user (f/student-created)
         activity-stat (f/activity-stat-created {:id (:user-id student-user)})
         update (as-> (core/get-stat f/default-school-id) upd
                      (assoc upd :activity-stats [])
                      )]
    (core/import-secondary-data! f/default-school-id update)
    (let [[updated-activity-stat] (db/get-user-activity-stats  activity-stat)]
      (assert (= nil updated-activity-stat)))))
