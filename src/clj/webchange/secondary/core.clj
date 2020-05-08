(ns webchange.secondary.core
  (:require [webchange.db.core :refer [*db*] :as db]
            [clojure.tools.logging :as log]
            [java-time :as jt]
            [webchange.common.date-time :as dt]
            [camel-snake-kebab.extras :refer [transform-keys]]
            [camel-snake-kebab.core :refer [->snake_case_keyword]]
            [clj-http.client :as client]
            [config.core :refer [env]]
            [clojure.reflect :as cr]
            [clojure.data.json :as json]
            [webchange.auth.core :as auth]
            [webchange.secondary.guid :as guid]

            ))

(defn make-url-absolute [path]
  (let [host-url (:host-url (env :secondary))
        url (str host-url (if (.endsWith host-url "/") "" "/") path)]
    url))

(defn update-school!
  [data]
  (db/update-school! data))

(defn prepare-users
  [users]
  (mapv #(let [last-login (dt/iso-str2date-time (:last-login %))
               created-at (dt/iso-str2date-time (:created-at %))
               guid (java.util.UUID/fromString (:guid %))]
           (-> %
               (assoc :last-login last-login)
               (assoc :created-at created-at)
               (assoc :guid guid)
               ))
        users))

(defn prepare-classes
  [classes]
  (mapv #(let [guid (java.util.UUID/fromString (:guid %))]
           (-> % (assoc :guid guid))) classes))

(defn update-users! [users]
  (let [users (prepare-users users)]
  (doseq [user users]
    (db/create-or-update-user! (transform-keys ->snake_case_keyword user)))))

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
  (let [classes (prepare-classes classes)]
    (doseq [class classes]
      (db/create-or-update-class! (transform-keys ->snake_case_keyword class)))))

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
        (assoc :guid (java.util.UUID/fromString (:guid event)))
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
               created-at (dt/date-time2iso-str (:created-at %))
               guid (.toString (:guid %))
               ]
           (-> %
               (assoc :last-login last-login)
               (assoc :created-at created-at)
               (assoc :guid guid)
               ))
        (db/get-users-by-school {:school_id id})))


(defn get-classes [id]
  (mapv #(let [guid (.toString (:guid %))]
           (-> %
               (assoc :guid guid)))
        (db/get-classes {:school_id id}))
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
               (assoc :created-at created-at)
               (assoc :guid (.toString (:guid %)))
               ))
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
        classes (get-classes id)
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

(defn get-stat [id]
  (let [school (db/get-school {:id id})
        users (get-users-by-school id)
        teachers (db/get-teacher-by-school {:school_id id})
        students (get-students-by-school id)
        classes (get-classes id)
        course-stats (db/get-course-stats-by-school {:school_id id})
        course-progresses (db/get-course-progresses-by-school {:school_id id})
        course-events (get-course-events-by-school id)
        activity-stats (db/get-activity-stats-by-school {:school_id id})
        ]
    {
     :school            school
     :users             users
     :teachers          teachers
     :students          students
     :classes           classes
     :course-stats      course-stats
     :course-progresses course-progresses
     :course-events     course-events
     :activity-stats    activity-stats
     })
  )

(defn upload-stat [id]
  (let [stat (get-stat id)
        url (make-url-absolute (str "api/school/update/" id))
        response (client/put url {:body (json/json-str stat) :body-encoding "UTF-8"})
        ]
    ))

(defn prepare-local-identity-data [items]
  (->> items
       (map (fn [item] [(.toString (:guid item)) item]))
       (into {})))

(defn prepare-remote-identity-data [items]
  (->> items
       (map (fn [item] (assoc item :guid (.toString (:guid item)))))
       (map (fn [item] [(:id item) item]))
       (into {})))

(defn duplicate-email-exception? [e]
  (clojure.string/includes? (:message (get (:via (Throwable->map e)) 1)) "users_email_unique")
  )

(defn process-users-imported! [users-imported]
  (let [users (prepare-users users-imported)]
    (doseq [user users]
      (try
        (db/create-or-update-user-by-guid! (transform-keys ->snake_case_keyword user))
        (catch Exception e
          (if (duplicate-email-exception? e)
            (do  (-> (transform-keys ->snake_case_keyword user)
                   (assoc :email nil)
                   (db/create-or-update-user-by-guid!))
               (log/warn (str "User with email " (:email user) " conflicted uuid " (:uuid user))))
            (throw e)))))
    (vec (set (map (fn [user] (:guid user)) users)))))

(defn process-class-imported! [classes]
  (let [classes (prepare-classes classes)]
    (doseq [class classes]
      (db/create-or-update-class-by-guid! (transform-keys ->snake_case_keyword class))
      )))

(defn prepare-imported-data [data field remote-map local-map]
  (into {} (map (fn [[table items]]
         [table (into [] (map (fn [item]
                (if (field item) (assoc item field (:id (get local-map (:guid (get remote-map (field item)))))) item))
              items))])
       data)))

(defn store-data-localy!
  [data]
    (do
      (update-teachers! (:teachers data))
      (update-students! (:students data))
      (update-course-stat! (:course-stats data))
      (update-progress! (:course-progresses data))
      (update-events! (:course-events data))
      (update-activity-stats! (:activity-stats data))
    )
  )

(defn delete-user [user]
  (db/delete-activity-stats-by-user-id! {:user_id (:id user)})
  (db/delete-course-events-by-user-id! {:user_id (:id user)})
  (db/delete-course-progresses-by-user-id! {:user_id (:id user)})
  (db/delete-course-stats-by-user-id! {:user_id (:id user)})
  (db/delete-student! {:user_id (:id user)})
  (db/delete-teachers-by-user-id! {:user_id (:id user)})
  (db/delete-user! {:id (:id user)})
  )

(defn delete-class [class]
  (db/delete-student-by-class-id! {:class_id (:id class)})
  (db/delete-course-stats-by-class-id! {:class_id (:id class)})
  (db/delete-class! {:id (:id class)})
  )

(defn delete-not-in-guid-list [guids entries extract-guid delete-entry]
  (doseq [entry entries]
    (if-not (contains? (set guids) (extract-guid entry))
      (delete-entry entry)
      nil)))

(defn delete-teacher [teacher]
  (db/delete-teacher-by-id! {:id (:id teacher)}))

(defn delete-student [student]
  (db/delete-student-by-id! {:id (:id student)}))

(defn delete-course-stats [course-stats]
  (db/delete-course-stats-by-id! {:id (:id course-stats)}))

(defn delete-course-progresses [course-progress]
  (db/delete-course-progresses-by-id! {:id (:id course-progress)}))

(defn delete-course-events [course-events]
  (db/delete-course-events-by-id! {:id (:id course-events)}))

(defn delete-activity-stats [activity-stats]
  (db/delete-activity-stats-by-id! {:id (:id activity-stats)}))

(defn import-secondary-data! [id data]
  (let [users-imported (:users data)
        classes-imported (:classes data)
        users-guid (process-users-imported! users-imported)
        ]
        (process-class-imported! classes-imported)
        (let [users (prepare-local-identity-data (db/find-users-by-guid  {:guids users-guid}))
              classes-db (db/get-classes {:school_id id})
              classes (map (fn [entry] (assoc entry :guid (.toString (:guid entry)))) classes-db)
              classes-identity (prepare-local-identity-data classes-db)
              remote-users (prepare-remote-identity-data users-imported)
              remote-classes (prepare-remote-identity-data classes-imported)
              data-with-users (prepare-imported-data data :user-id remote-users users)
              data-processed (prepare-imported-data data-with-users :class-id remote-classes classes-identity)
              ]
              (store-data-localy! data-processed)
              (let [users (db/get-users-by-school {:school_id id})
                    teachers (db/get-teacher-by-school {:school_id id})
                    students (db/get-students-by-school {:school_id id})
                    course-stats (db/get-course-stats-by-school {:school_id id})
                    course-progresses (db/get-course-progresses-by-school {:school_id id})
                    course-events (map (fn [entry] (assoc entry :guid (.toString (:guid entry)))) (db/get-course-events-by-school {:school_id id}))
                    activity-stats (db/get-activity-stats-by-school {:school_id id})

                    classes-guid (guid/guids-from-entries classes-imported guid/guid-from-class)
                    teachers-guid (guid/guids-from-entries (:teachers data-processed) guid/guid-from-teacher)
                    students-guid (guid/guids-from-entries (:students data-processed) guid/guid-from-student)
                    course-stats-guid (guid/guids-from-entries (:course-stats data-processed) guid/guid-from-course-stats)
                    course-progresses-guid (guid/guids-from-entries (:course-progresses data-processed) guid/guid-from-course-progresses)
                    course-events-guid (guid/guids-from-entries (:course-events data-processed) guid/guid-from-course-events)
                    activity-stats-guid (guid/guids-from-entries (:activity-stats data-processed) guid/guid-from-activity-stats)
                    ]
                (delete-not-in-guid-list users-guid users :guid delete-user)
                (delete-not-in-guid-list classes-guid classes guid/guid-from-class delete-class)
                (delete-not-in-guid-list teachers-guid teachers guid/guid-from-teacher delete-teacher)
                (delete-not-in-guid-list students-guid students guid/guid-from-student delete-student)
                (delete-not-in-guid-list course-stats-guid course-stats guid/guid-from-course-stats delete-course-stats)
                (delete-not-in-guid-list course-progresses-guid course-progresses guid/guid-from-course-progresses delete-course-progresses)
                (delete-not-in-guid-list course-events-guid course-events guid/guid-from-course-events delete-course-events)
                (delete-not-in-guid-list activity-stats-guid activity-stats guid/guid-from-activity-stats delete-activity-stats)
          ))))