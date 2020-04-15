(ns webchange.secondary.core
  (:require [webchange.db.core :refer [*db*] :as db]
            [clojure.tools.logging :as log]
            [java-time :as jt]
            [webchange.common.date-time :as dt]
            [camel-snake-kebab.extras :refer [transform-keys]]
            [camel-snake-kebab.core :refer [->snake_case_keyword]]
            [clj-http.client :as client]
            [config.core :refer [env]]
            [clojure.data.json :as json]
            [webchange.auth.core :as auth]))

(defn make-url-absolute [path]
  (let [host-url (:host-url (env :secondary))
        url (str host-url (if (.endsWith host-url "/") "" "/") path)]
    url))

(defn update-school!
  [data]
  (db/update-school! data))

(defn update-users!
  [users]
  (doseq [user users]
    (-> user
        (assoc :created-at (dt/iso-str2date-time (:created-at user)))
        (assoc :last-login (dt/iso-str2date-time (:last-login user)))
        (#(transform-keys ->snake_case_keyword %))
        (db/create-or-update-user!)
        )))

(defn update-teachers!
  [teachers]
  (doseq [teacher teachers]
    (db/create-or-update-teacher! (transform-keys ->snake_case_keyword teacher))))

(defn update-students!
  [students]
  (doseq [student students]
    (-> student
        (assoc :date-of-birth (dt/str2date (:date-of-birth student)))
        (#(transform-keys ->snake_case_keyword %))
        (db/create-or-update-student!)
        )))

(defn update-classes!
  [classes]
  (doseq [class classes]
    (db/create-or-update-class! (transform-keys ->snake_case_keyword class))))

(defn update-courses!
  [courses]
  (doseq [course courses]
    (db/create-or-update-courses! (transform-keys ->snake_case_keyword course))))

(defn update-course-stat!
  [course-stats]
  (doseq [stat course-stats]
    (db/create-or-update-course-stat! (transform-keys ->snake_case_keyword stat))))

(defn update-course-versions!
  [course-versions]
  (doseq [version course-versions]
    (-> version
        (assoc :created-at (dt/iso-str2date-time (:created-at version)))
        (#(transform-keys ->snake_case_keyword %))
        (db/create-or-update-course-versions!)
        )
    ))

(defn update-progress!
  [course-progresses]
  (doseq [progress course-progresses]
    (db/create-or-update-progress! (transform-keys ->snake_case_keyword progress))))

(defn update-events!
  [events]
  (doseq [event events]
    (-> event
        (assoc :created-at (dt/iso-str2date-time (:created-at event)))
        (#(transform-keys ->snake_case_keyword %))
        (db/create-or-update-event!)
        )
    ))

(defn update-dataset!
  [datasets]
  (doseq [dataset datasets]
    (db/create-or-update-dataset! (transform-keys ->snake_case_keyword dataset))))

(defn update-dataset-item-with-id!
  [dataset-items]
  (doseq [dataset-item dataset-items]
    (db/create-or-update-dataset-item-with-id! (transform-keys ->snake_case_keyword dataset-item))))

(defn update-lesson-set!
  [lesson-sets]
  (doseq [lesson-set lesson-sets]
    (db/create-or-update-lesson-set! (transform-keys ->snake_case_keyword lesson-set))))

(defn update-scene!
  [scenes]
  (doseq [scene scenes]
    (db/create-or-update-scene! (transform-keys ->snake_case_keyword scene))))

(defn update-scene-versions!
  [scene-versions]
  (doseq [scene-version scene-versions]
    (-> scene-version
        (assoc :created-at (dt/iso-str2date-time (:created-at scene-version)))
        (#(transform-keys ->snake_case_keyword %))
        (db/create-or-update-scene-version!)
        )))

(defn update-activity-stats!
  [activity-stats]
  (doseq [activity-stat activity-stats]
    (db/create-or-update-activity-stat! (transform-keys ->snake_case_keyword activity-stat))))

(defn process-data [data]
  (if-let [school (:school data)]
    (update-school! school))
  (if-let [users (:users data)]
    (update-users! users))
  (if-let [teachers (:teachers data)]
    (update-teachers! teachers))
  (if-let [classes (:classes data)]
    (update-classes! classes))
  (if-let [students (:students data)]
    (update-students! students))
  (if-let [courses (:courses data)]
    (update-courses! courses))
  (if-let [course-versions (:course-versions data)]
    (update-course-versions! course-versions))
  (if-let [course-stats (:course-stats data)]
    (update-course-stat! course-stats))
  (if-let [course-progresses (:course-progresses data)]
    (update-progress! course-progresses))
  (if-let [course-events (:course-events data)]
    (update-events! course-events))
  (if-let [datasets (:datasets data)]
    (update-dataset! datasets))
  (if-let [dataset-items (:dataset-items data)]
    (update-dataset-item-with-id! dataset-items))
  (if-let [lesson-sets (:lesson-sets data)]
    (update-lesson-set! lesson-sets))
  (if-let [scenes (:scenes data)]
    (update-scene! scenes))
  (if-let [scene-versions (:scene-versions data)]
    (update-scene-versions! scene-versions))
  (if-let [activity-stats (:activity-stats data)]
    (update-activity-stats! activity-stats))
  )

(defn load-full! [school]
  (let [url (make-url-absolute (str "api/school/dump-full/" (:id school)))
        response (client/get url {:accept :json})
        data (json/read-str (:body response) :key-fn keyword)]
    (process-data data)
    ))


(defn get-users-by-school [id]
  (mapv #(let [last-login (dt/date-time2iso-str (:last-login %))
               created-at (dt/date-time2iso-str (:created-at %))]
           (-> %
               (assoc :last-login last-login)
               (assoc :created-at created-at)))
        (db/get-users-by-school {:school_id id}))
  )

(defn get-students-by-school [id]
  (mapv #(let [date-of-birth (dt/date2str (:date-of-birth %))]
           (-> %
               (assoc :date-of-birth date-of-birth)))
        (db/get-students-by-school {:school_id id})
        ))

(defn get-course-versions [id]
  (mapv #(let [created-at (dt/date-time2iso-str (:created-at %))]
           (-> %
               (assoc :created-at created-at)))
        (db/get-course-versions-by-school)
        )
  )

(defn get-course-events-by-school [id]
  (mapv #(let [created-at (dt/date-time2iso-str (:created-at %))]
           (-> %
               (assoc :created-at created-at)))
        (db/get-course-events-by-school {:school_id id})
        )
  )

(defn get-scene-versions [id]
  (mapv #(let [created-at (dt/date-time2iso-str (:created-at %))]
           (-> %
               (assoc :created-at created-at)))
        (db/get-scene-versions-by-school)
        )
  )

(defn get-dump-by-school [id]
  (let [id (Integer/parseInt id)
        school (db/get-school {:id id})
        users (get-users-by-school id)
        teachers (db/get-teacher-by-school {:school_id id})
        students (get-students-by-school id)
        classes (db/get-classes {:school_id id})
        courses (db/get-courses)
        course-versions (get-course-versions id)
        course-stats (db/get-course-stats-by-school {:school_id id})
        course-progresses (db/get-course-progresses-by-school {:school_id id})
        course-events (get-course-events-by-school id)
        datasets (db/get-datasets)
        dataset-items (db/get-dataset-items-by-school)
        lesson-sets (db/get-lesson-sets)
        scenes (db/get-scenes)
        scene-versions (get-scene-versions id)
        activity-stats (db/get-activity-stats-by-school {:school_id id})
        ]
    {
     :school            school
     :users             users
     :teachers          teachers
     :students          students
     :classes           classes
     :courses           courses
     :course-versions   course-versions
     :course-stats      course-stats
     :course-progresses course-progresses
     :course-events     course-events
     :datasets          datasets
     :dataset-items     dataset-items
     :lesson-sets       lesson-sets
     :scenes            scenes
     :scene-versions    scene-versions
     :activity-stats    activity-stats
     })
  )
