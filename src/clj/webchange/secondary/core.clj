(ns webchange.secondary.core
  (:require
    [camel-snake-kebab.core :refer [->snake_case_keyword]]
    [clj-http.client :as client]
    [clojure.data.json :as json]
    [clojure.java.io :as io]
    [clojure.set :as set]
    [clojure.string :as str]
    [clojure.tools.logging :as log]
    [config.core :refer [env]]
    [java-time :as jt]
    [webchange.assets.core :as assets]
    [webchange.common.date-time :as dt]
    [webchange.common.files :as f]
    [webchange.common.hmac-sha256 :as sign]
    [webchange.course.core :as course]
    [webchange.db.core :as db]
    [webchange.secondary.guid :as guid]))

(import '(java.util.concurrent Executors))

(defn make-url-absolute [path]
  (let [host-url (:host-url (env :secondary))
        url (str host-url (if (.endsWith host-url "/") "" "/") path)]
    url))

(defn update-school!
  [data]
  (let [defaults {:location "" :about ""}]
    (db/update-school! (merge defaults data))))

(defn prepare-users
  [users]
  (mapv #(let [guid (java.util.UUID/fromString (:guid %))]
           (cond-> %
                   :always (assoc :guid guid)
                   (seq (:last-login %)) (assoc :last-login (dt/iso-str2date-time (:last-login %)))
                   (seq (:created-at %)) (assoc :created-at (dt/iso-str2date-time (:created-at %)))))
        users))

(defn prepare-classes
  [classes]
  (mapv #(let [guid (java.util.UUID/fromString (:guid %))]
           (cond-> %
                   :always (assoc :guid guid)
                   (seq (:created-at %)) (assoc :created_at (dt/str2date (:created-at %)))))
        classes))

(defn update-users!
  [users]
  (let [users (prepare-users users)]
    (doseq [user users]
      (db/create-or-update-user! (db/transform-keys-one-level ->snake_case_keyword user)))
    (db/reset-users-seq!)))

(defn update-teachers!
  [teachers]
  (doseq [teacher teachers]
    (db/create-or-update-teacher! (db/transform-keys-one-level ->snake_case_keyword teacher))))

(defn update-students!
  [students]
  (doseq [student students]
    (-> student
        (assoc :date-of-birth (dt/str2date (:date-of-birth student)))
        (#(db/transform-keys-one-level ->snake_case_keyword %))
        (db/create-or-update-student!))))

(defn update-classes!
  [classes]
  (let [classes (prepare-classes classes)]
    (doseq [class classes]
      (db/create-or-update-class! (db/transform-keys-one-level ->snake_case_keyword class)))
    (db/reset-classes-seq!)))

(defn update-courses!
  [courses]
  (doseq [course courses]
    (db/create-or-update-courses! (db/transform-keys-one-level ->snake_case_keyword course)))
  (db/reset-courses-seq!))

(defn update-school-courses!
  [school-courses]
  (db/clear-school-courses!)
  (doseq [school-course school-courses]
    (db/create-school-course! (db/transform-keys-one-level ->snake_case_keyword school-course))))

(defn update-course-scenes!
  [course-scenes]
  (db/clear-course-scenes!)
  (doseq [course-scene course-scenes]
    (db/create-course-scene! (db/transform-keys-one-level ->snake_case_keyword course-scene))))

(defn update-scene-skills!
  [scene-skills]
  (doseq [scene-skill scene-skills]
    (db/create-or-update-scene-skills! (db/transform-keys-one-level ->snake_case_keyword scene-skill))))

(defn update-course-stat!
  [course-stats]
  (doseq [stat course-stats]
    (db/create-or-update-course-stat! (db/transform-keys-one-level ->snake_case_keyword stat))))

(defn update-course-versions!
  [course-versions]
  (doseq [version course-versions]
    (let [current-latest (db/get-latest-course-version {:course_id (:course-id version)})
          version (-> version
                      (assoc :created-at (jt/local-date-time)))]
      (when-not (= (:data version) (:data current-latest))
        (-> version
            (#(db/transform-keys-one-level ->snake_case_keyword %))
            (db/save-course!)))))
  (db/reset-course-versions-seq!))

(defn update-progress!
  [course-progresses]
  (doseq [progress course-progresses]
    (db/create-or-update-progress! (db/transform-keys-one-level ->snake_case_keyword progress))))

(defn update-events!
  [events]
  (doseq [event events]
    (-> event
        (assoc :created-at (dt/iso-str2date-time (:created-at event)))
        (assoc :guid (java.util.UUID/fromString (:guid event)))
        (#(db/transform-keys-one-level ->snake_case_keyword %))
        (db/create-or-update-event!))))

(defn update-scene!
  [scenes]
  (doseq [scene scenes]
    (as-> scene s
          (assoc s :created-at (dt/iso-str2date-time (:created-at s)))
          (assoc s :updated-at (dt/iso-str2date-time (:updated-at s)))
          (db/transform-keys-one-level ->snake_case_keyword s)
          (db/create-or-update-scene! s)))
  (db/reset-scenes-seq!))

(defn update-scene-versions!
  [scene-versions]
  (doseq [scene-version scene-versions]
    (let [current-latest (db/get-latest-scene-version {:scene_id (:scene-id scene-version)})
          scene-version (-> scene-version
                            (assoc :created-at (jt/local-date-time)))]
      (when-not (= (:data scene-version) (:data current-latest))
        (-> scene-version
            (#(db/transform-keys-one-level ->snake_case_keyword %))
            (assoc :data (:data scene-version))
            (db/save-scene!))))
    (db/reset-scene-versions-seq!)))

(defn update-activity-stats!
  [activity-stats]
  (doseq [activity-stat activity-stats]
    (db/create-or-update-activity-stat! (db/transform-keys-one-level ->snake_case_keyword activity-stat))))

(defn process-data [data]
  (when-let [school (:school data)]
    (update-school! school))
  (when-let [users (:users data)]
    (update-users! users))
  (when-let [teachers (:teachers data)]
    (update-teachers! teachers))
  (when-let [classes (:classes data)]
    (update-classes! classes))
  (when-let [students (:students data)]
    (update-students! students))
  (when-let [courses (:courses data)]
    (update-courses! courses))
  (when-let [course-versions (:course-versions data)]
    (update-course-versions! course-versions))
  (when-let [course-stats (:course-stats data)]
    (update-course-stat! course-stats))
  (when-let [course-progresses (:course-progresses data)]
    (update-progress! course-progresses))
  (when-let [course-events (:course-events data)]
    (update-events! course-events))
  (when-let [scenes (:scenes data)]
    (update-scene! scenes))
  (when-let [scene-versions (:scene-versions data)]
    (update-scene-versions! scene-versions))
  (when-let [scene-skills (:scene-skills data)]
    (update-scene-skills! scene-skills))
  (when-let [activity-stats (:activity-stats data)]
    (update-activity-stats! activity-stats))
  (when-let [school-courses (:school-courses data)]
    (update-school-courses! school-courses))
  (when-let [course-scenes (:course-scenes data)]
    (update-course-scenes! course-scenes)))

(defn load-full! [school]
  (let [url (make-url-absolute (str "api/school/dump-full/" (:id school)))
        response (client/get url {:accept :json})
        data (json/read-str (:body response) :key-fn keyword)]
    (process-data data)))

(defn get-users-by-school [id]
  (mapv #(let [last-login (dt/date-time2iso-str (:last-login %))
               created-at (dt/date-time2iso-str (:created-at %))
               guid (.toString (:guid %))]
           (-> %
               (assoc :last-login last-login)
               (assoc :created-at created-at)
               (assoc :guid guid)))
        (db/get-users-by-school {:school_id id})))


(defn get-classes [id]
  (mapv #(let [guid (.toString (:guid %))
               created-at (dt/date2str (:created-at %))]
           (-> %
               (assoc :guid guid)
               (assoc :created-at created-at)))
        (db/get-classes {:school_id id})))

(defn get-students-by-school [id]
  (mapv #(let [date-of-birth (dt/date2str (:date-of-birth %))]
           (-> %
               (assoc :date-of-birth date-of-birth)))
        (db/get-students-by-school {:school_id id})))

(defn get-course-versions []
  (mapv #(let [created-at (dt/date-time2iso-str (:created-at %))]
           (-> %
               (assoc :created-at created-at)))
        (db/get-course-versions-by-school)))

(defn get-course-events-by-school [id]
  (mapv #(let [created-at (dt/date-time2iso-str (:created-at %))]
           (-> %
               (assoc :created-at created-at)
               (assoc :guid (.toString (:guid %)))))
        (db/get-course-events-by-school {:school_id id})))

(defn get-scene-versions []
  (mapv #(let [created-at (dt/date-time2iso-str (:created-at %))]
           (-> %
               (assoc :created-at created-at)))
        (db/get-scene-versions-by-school)))

(defn get-dump-by-school [id]
  (let [id (Integer/parseInt id)
        school (db/get-school {:id id})
        users (get-users-by-school id)
        teachers (db/teachers-by-school {:school_id id})
        students (get-students-by-school id)
        scene-skills (db/get-scene-skills)
        classes (get-classes id)
        courses (db/get-courses)
        course-versions (get-course-versions)
        course-stats (db/get-course-stats-by-school {:school_id id})
        course-progresses (db/get-course-progresses-by-school {:school_id id})
        course-events (get-course-events-by-school id)
        scenes (db/get-scenes)
        scene-versions (get-scene-versions)
        activity-stats (db/get-activity-stats-by-school {:school_id id})]
    {:school            school
     :users             users
     :teachers          teachers
     :scene-skills      scene-skills
     :students          students
     :classes           classes
     :courses           courses
     :course-versions   course-versions
     :course-stats      course-stats
     :course-progresses course-progresses
     :course-events     course-events
     :scenes            scenes
     :scene-versions    scene-versions
     :activity-stats    activity-stats}))

(defn- available-courses
  [school-id requested-courses]
  (let [courses (db/get-sync-courses)
        school-courses (db/get-courses-by-school {:school_id school-id})
        additional-courses (->> (env :additional-course-slugs)
                                (concat requested-courses)
                                (map #(db/get-course {:slug %})))]
    (->> (concat courses school-courses additional-courses)
         (group-by :slug)
         (vals)
         (map first)
         (remove empty?))))

(defn get-course-update
  [id requested-courses]
  (let [id (Integer/parseInt id)
        school (db/get-school {:id id})
        courses (available-courses id requested-courses)
        school-courses (->> courses
                            (map (fn [{course-id :id}] {:course-id course-id :school-id id})))
        
        course-versions (->> courses
                             (map #(db/get-latest-course-version {:course_id (:id %)}))
                             (remove empty?)
                             (map #(update % :created-at dt/date-time2iso-str)))

        books (db/find-scenes {:type "book" :status "visible"})
        
        scenes (->> courses
                    (mapcat #(db/get-scenes-by-course-id {:course_id (:id %)}))
                    (concat books)
                    (map #(assoc % :course-id nil))
                    (map #(update % :created-at dt/date-time2iso-str))
                    (map #(update % :updated-at dt/date-time2iso-str)))

        scene-versions (->> scenes
                            (map #(db/get-latest-scene-version {:scene_id (:id %)}))
                            (remove empty?)
                            (map #(update % :created-at dt/date-time2iso-str)))
        scene-skills (->> scenes
                          (mapcat #(db/get-scene-skills-by-scene {:scene_id (:id %)})))
        course-scenes (->> courses
                           (mapcat #(db/course-scenes {:course_id (:id %)})))]
    {:school          school
     :courses         courses
     :school-courses  school-courses
     :scene-skills    scene-skills
     :course-versions course-versions
     :scenes          scenes
     :scene-versions  scene-versions
     :course-scenes   course-scenes}))

(defn get-stat [id]
  (let [school (db/get-school {:id id})
        users (get-users-by-school id)
        teachers (db/teachers-by-school {:school_id id})
        students (get-students-by-school id)
        classes (get-classes id)
        course-stats (db/get-course-stats-by-school {:school_id id})
        course-progresses (db/get-course-progresses-by-school {:school_id id})
        course-events (get-course-events-by-school id)
        activity-stats (db/get-activity-stats-by-school {:school_id id})]
    {:school            school
     :users             users
     :teachers          teachers
     :students          students
     :classes           classes
     :course-stats      course-stats
     :course-progresses course-progresses
     :course-events     course-events
     :activity-stats    activity-stats}))

(defn upload-stat [id]
  (let [stat (get-stat id)
        url (make-url-absolute (str "api/school/update/" id))]
    (client/put url {:body (json/json-str stat) :body-encoding "UTF-8"})))

(defn prepare-local-identity-data [items]
  (->> items
       (map (fn [item] [(.toString (:guid item)) item]))
       (into {})))

(defn prepare-remote-identity-data [items]
  (->> items
       (map (fn [item] (assoc item :guid (.toString (:guid item)))))
       (map (fn [item] [(:id item) item]))
       (into {})))

(defn duplicate-email-exception?
  [e]
  (clojure.string/includes? (:message (get (:via (Throwable->map e)) 1)) "users_email_unique"))

(defn process-users-imported!
  [users-imported]
  (let [users (prepare-users users-imported)]
    (doseq [user users]
      (try
        (db/create-or-update-user-by-guid! (db/transform-keys-one-level ->snake_case_keyword user))
        (catch Exception e
          (if (duplicate-email-exception? e)
            (do (-> (db/transform-keys-one-level ->snake_case_keyword user)
                    (assoc :email nil)
                    (db/create-or-update-user-by-guid!))
                (log/warn (str "User with email " (:email user) " conflicted uuid " (:uuid user))))
            (throw e)))))
    (vec (set (map (fn [user] (:guid user)) users)))))

(defn process-class-imported! [classes]
  (let [classes (prepare-classes classes)]
    (doseq [class classes]
      (db/create-or-update-class-by-guid! (db/transform-keys-one-level ->snake_case_keyword class)))))

(defn prepare-imported-data
  [data field remote-map local-map]
  (->> data
       (map (fn [[table items]]
              [table (->> items
                          (map (fn [item]
                                 (if (field item)
                                   (let [guid-field-value (-> remote-map
                                                              (get (field item))
                                                              :guid)
                                         value (-> local-map
                                                   (get guid-field-value)
                                                   :id)]
                                     (assoc item field value))
                                   item)))
                          (into []))]))
       (into {})))

(defn store-data-localy!
  [data]
  (update-teachers! (:teachers data))
  (update-students! (:students data))
  (update-course-stat! (:course-stats data))
  (update-progress! (:course-progresses data))
  (update-events! (:course-events data))
  (update-activity-stats! (:activity-stats data)))

(defn delete-user [user]
  (db/delete-activity-stats-by-user-id! {:user_id (:id user)})
  (db/delete-course-events-by-user-id! {:user_id (:id user)})
  (db/delete-course-progresses-by-user-id! {:user_id (:id user)})
  (db/delete-course-stats-by-user-id! {:user_id (:id user)})
  (db/delete-student! {:user_id (:id user)})
  (db/delete-teachers-by-user-id! {:user_id (:id user)})
  (db/delete-user! {:id (:id user)}))

(defn delete-class [class]
  (db/delete-student-by-class-id! {:class_id (:id class)})
  (db/delete-course-stats-by-class-id! {:class_id (:id class)})
  (db/delete-class! {:id (:id class)}))

(defn delete-not-in-guid-list [guids entries extract-guid delete-entry]
  (doseq [entry entries]
    (if-not (contains? (set guids) (extract-guid entry))
      (delete-entry (db/transform-keys-one-level ->snake_case_keyword entry))
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
        users-guid (process-users-imported! users-imported)]
    (process-class-imported! classes-imported)
    (let [users (prepare-local-identity-data (db/find-users-by-guid {:guids users-guid}))
          classes-db (db/get-classes {:school_id id})
          classes (map (fn [entry] (assoc entry :guid (.toString (:guid entry)))) classes-db)
          classes-identity (prepare-local-identity-data classes-db)
          remote-users (prepare-remote-identity-data users-imported)
          remote-classes (prepare-remote-identity-data classes-imported)
          data-with-users (prepare-imported-data data :user-id remote-users users)
          data-processed (prepare-imported-data data-with-users :class-id remote-classes classes-identity)]
      (store-data-localy! data-processed)
      (let [users (db/get-users-by-school {:school_id id})
            teachers (db/teachers-by-school {:school_id id})
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
            activity-stats-guid (guid/guids-from-entries (:activity-stats data-processed) guid/guid-from-activity-stats)]
        (delete-not-in-guid-list users-guid users :guid delete-user)
        (delete-not-in-guid-list classes-guid classes guid/guid-from-class delete-class)
        (delete-not-in-guid-list teachers-guid teachers guid/guid-from-teacher delete-teacher)
        (delete-not-in-guid-list students-guid students guid/guid-from-student delete-student)
        (delete-not-in-guid-list course-stats-guid course-stats guid/guid-from-course-stats delete-course-stats)
        (delete-not-in-guid-list course-progresses-guid course-progresses guid/guid-from-course-progresses delete-course-progresses)
        (delete-not-in-guid-list course-events-guid course-events guid/guid-from-course-events delete-course-events)
        (delete-not-in-guid-list activity-stats-guid activity-stats guid/guid-from-activity-stats delete-activity-stats)))))

(defn- delete-datasets-by-course-id!
  [course-id]
  (doseq [dataset (db/get-datasets-by-course {:course_id course-id})]
    (db/delete-dataset-items-by-dataset-id! {:dataset_id (:id dataset)})
    (db/delete-lesson-sets-by-dataset-id! {:dataset_id (:id dataset)})
    (db/delete-dataset-by-id! {:id (:id dataset)})))

(defn delete-course-by-id!
  [{:keys [id]}]
  (db/delete-collaborators-by-course-id! {:course_id id})
  (db/delete-course-stats-by-course-id! {:course_id id})
  (db/delete-activity-stats-by-course-id! {:course_id id})
  (db/delete-course-progresses-by-course-id! {:course_id id})
  (db/delete-course-events-by-course-id! {:course_id id})
  (db/delete-course-version-by-course-id! {:course_id id})
  (delete-datasets-by-course-id! id)
  (db/delete-course-by-id! {:id id}))

(defn delete-scene-by-id!
  [{:keys [id]}]
  (db/delete-course-scenes-by-scene-id! {:scene_id id})
  (db/delete-scene-version-by-scene-id! {:scene_id id})
  (db/delete-scene-by-id! {:id id}))

(defn import-primary-data! [data]
  (when-let [school (:school data)]
    (update-school! school))
  (when-let [courses (:courses data)]
    (update-courses! courses))
  (when-let [course-versions (:course-versions data)]
    (update-course-versions! course-versions))
  (when-let [scenes (:scenes data)]
    (update-scene! scenes))
  (when-let [scene-versions (:scene-versions data)]
    (update-scene-versions! scene-versions))
  (when-let [scene-skills (:scene-skills data)]
    (update-scene-skills! scene-skills))
  (when-let [school-courses (:school-courses data)]
    (update-school-courses! school-courses))
  (when-let [course-scenes (:course-scenes data)]
    (update-course-scenes! course-scenes))
  (let [courses (db/get-courses)
        scenes (db/get-scenes)
        scene-skills (db/get-scene-skills)

        courses-guid (guid/guids-from-entries (:courses data) guid/guid-is-id)
        scenes-guid (guid/guids-from-entries (:scenes data) guid/guid-is-id)
        scene-skills-guid (guid/guids-from-entries (:scene-skills data) guid/guid-from-scene-skill)]
    (delete-not-in-guid-list scene-skills-guid scene-skills guid/guid-from-scene-skill db/delete-scene-skills-by-scene-skill!)
    (delete-not-in-guid-list scenes-guid scenes :id delete-scene-by-id!)
    (delete-not-in-guid-list courses-guid courses :id delete-course-by-id!)))

(defn update-course-data!
  [id requested-courses]
  (let [url (make-url-absolute (str "api/school/courses-update/" id))
        response (client/post url {:body (json/json-str {:requested-courses requested-courses})
                                   :body-encoding "UTF-8"
                                   :content-type "application/json"
                                   :accept :json})
        data (json/read-str (:body response) :key-fn keyword)]
    (import-primary-data! data)))

(defn to-set [hashes]
  (->> hashes
       (map (fn [item] (str (:path-hash item) "-" (:file-hash item))))
       (set)))

(defn to-list [hashes]
  (map (fn [item]
         (let [ids (clojure.string/split item #"-")]
           {:path-hash (get ids 0) :file-hash (get ids 1)}))
       hashes))

(defn fill-additional-data [update]
  (map (fn [item]
         (let [file-hash (db/get-asset-hash {:path_hash (:path-hash item)})]
           file-hash))
       update))

(defn- asset->url
  [asset]
  (cond
    (map? asset) (:url asset)
    (string? asset) asset
    :else asset))

(defn- published-upload-assets
  [school-id requested-courses]
  (let [published-course-ids (->> (available-courses school-id requested-courses)
                                  (map :id))
        course-assets (->> published-course-ids
                           (map #(db/get-course-by-id {:id %}))
                           (map :image-src)
                           (remove empty?))
        scenes (->> published-course-ids
                    (mapcat #(db/get-scenes-by-course-id {:course_id %}))
                    (concat (db/find-scenes {:type "book" :status "visible"})))
        
        scene-previews (->> scenes
                            (map :image-src)
                            (remove empty?))
        scene-assets (->> scenes
                          (map #(db/get-latest-scene-version {:scene_id (:id %)}))
                          (mapcat #(-> % :data :assets))
                          (map asset->url)
                          (remove nil?))]
    (->> (concat course-assets scene-previews scene-assets)
         (filter #(str/starts-with? % "/upload/"))
         (set))))

(defn calc-asset-update
  [school-id school-hashes requested-courses]
  (let [published-asset-urls (published-upload-assets school-id requested-courses)
        raw? (fn [hash] (str/starts-with? (:path hash) "/raw/"))
        published? (fn [hash] (published-asset-urls (:path hash)))
        hashes (->> (db/get-all-asset-hash)
                    (filter #(or (raw? %) (published? %)))
                    (map #(select-keys % [:path-hash :file-hash])))
        school-hashes-set (to-set school-hashes)
        hashes-set (to-set hashes)
        to-remove (set/difference school-hashes-set hashes-set)
        to-update (set/difference hashes-set school-hashes-set)
        remove (to-list to-remove)
        update (fill-additional-data (to-list to-update))]
    {:remove remove :update update}))

(defn download-files [files]
  (let [nthreads 10
        pool (Executors/newFixedThreadPool nthreads)
        tasks (map (fn [item] (fn [] (assets/update-file-from-primary (:path item)))) files)
        ret (.invokeAll pool tasks)]
    (.shutdown pool)
    (map #(.get %) ret)))

(defn get-difference-data
  [school-id requested-courses]
  (let [asset-hashes (db/get-all-asset-hash)
        hashes (map (fn [item] {:path-hash (:path-hash item)
                                :file-hash (:file-hash item)}) asset-hashes)
        url (make-url-absolute "api/school/asset/difference/")
        response (client/post url {:body          (json/json-str {:school-id school-id
                                                                  :hashes hashes
                                                                  :requested-courses requested-courses})
                                   :body-encoding "UTF-8"
                                   :content-type  "application/json"
                                   :accept        :json})
        update (-> response :body (json/read-str :key-fn keyword))
        remove (fill-additional-data (:remove update))
        download (:update update)]
    {:remove remove :download download}))

(defn update-assets!
  ([] (update-assets! true))
  ([remove-files?]
   (let [school-id (env :default-school)
         requested-courses (env :requested-courses)]
     (update-assets! school-id requested-courses remove-files?)))
  ([school-id requested-courses remove-files?]
   (let [{remove :remove download :download} (get-difference-data school-id requested-courses)]
     (when remove-files? (doseq [item remove]
                           (assets/remove-file-with-hash! item)))
     (download-files download))))

(defn update-course-previews!
  [requested-courses]
  (doall
   (for [course-slug requested-courses]
     (let [course (course/get-course-latest-version course-slug)
           files (->> course :scene-list
                      (map second)
                      (map :preview)
                      (remove nil?)
                      (map (fn [path] {:path path})))]
       (download-files files)))))

(defn calc-upload-assets
  [school-id requested-courses]
  (let [{remove :remove} (get-difference-data school-id requested-courses)]
    remove))

(defn upload-file [path]
  (let [file-path (f/relative->absolute-path path)
        url (make-url-absolute "api/assets/by-path/")]
    (client/with-additional-middleware [#'sign/wrap-apikey]
      (client/post url {:headers   {:api-key (:api-key env)}
                        :multipart [{:name "target-path" :content path}
                                    {:name "file" :content (io/file file-path)}]}))))
