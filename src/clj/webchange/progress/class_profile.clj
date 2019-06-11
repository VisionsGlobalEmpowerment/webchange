(ns webchange.progress.class-profile
  (:require [webchange.db.core :refer [*db*] :as db]
            [clojure.tools.logging :as log]
            [camel-snake-kebab.extras :refer [transform-keys]]
            [camel-snake-kebab.core :refer [->snake_case_keyword]]
            [webchange.auth.core :as auth]
            [webchange.events :as events]))

(events/reg
  ::start-date "course-started"
  (fn [{created-at :created-at user-id :user-id course-id :course-id}]
    (if-not (db/get-user-course-stat {:user_id user-id :course_id course-id})
      (let [{class-id :class-id} (db/get-student-by-user {:user_id user-id})]
        (db/create-course-stat! {:user_id user-id :class_id class-id :course_id course-id :data {:started-at created-at}})))))
