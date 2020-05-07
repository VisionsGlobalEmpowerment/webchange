(ns webchange.test.fixtures.core
  (:require [clojure.test :refer :all]
            [webchange.db.core :refer [*db*] :as db]
            [webchange.auth.core :as auth]
            [webchange.handler :as handler]
            [webchange.course.handler :as course-handler]
            [config.core :refer [env]]
            [mount.core :as mount]
            [luminus-migrations.core :as migrations]
            [ring.middleware.session.store :as store]
            [java-time :as jt]
            [ring.mock.request :as mock]
            [ring.util.mime-type :as mime]
            [clojure.java.io :as io]
            [clojure.data.json :as json]
            [camel-snake-kebab.extras :refer [transform-keys]]
            [camel-snake-kebab.core :refer [->snake_case_keyword ->kebab-case-keyword]]
            [clj-http.client :as c]
            [clojure.data.json :as json]
            [clojure.tools.logging :as log]))

(def default-school-id 1)

(defn clear-db []
  (db/clear-table :activity_stats)
  (db/clear-table :course_stats)
  (db/clear-table :course_events)
  (db/clear-table :course_progresses)
  (db/clear-table :students)
  (db/clear-table :classes)
  (db/clear-table :lesson_sets)
  (db/clear-table :dataset_items)
  (db/clear-table :datasets)
  (db/clear-table :scene_versions)
  (db/clear-table :scenes)
  (db/clear-table :course_versions)
  (db/clear-table :courses)
  (db/clear-table :teachers)
  (db/clear-table :schools)
  (db/clear-table :asset_hashes)
  (db/clear-table :users))

(defn clear-db-fixture [f]
  (clear-db)
  (f))

(defn init [f]
  (mount/start #'webchange.db.core/*db*)
  #_(migrations/migrate ["migrate"] (select-keys env [:database-url]))
  (f))

(def website-user-id 123)
(def website-user {:id website-user-id :email "email@example.com" :first_name "First" :last_name "Last" :image "https://example.com/image.png"})

(defn website-user-created
  ([] (website-user-created {}))
  ([options]
   (let [defaults website-user
         data (merge defaults options)]
     (auth/replace-user-from-website! data))))

(defn teacher-user-created
  ([] (teacher-user-created {}))
  ([options]
   (let [defaults {:first-name "Test" :last-name "Test" :email "test@example.com" :password "test"}
         data (merge defaults options)
         email (:email data)
         password (:password data)]
     (if-let [user (db/find-user-by-email {:email email})]
      (assoc user :password password)
      (let [[{user-id :id}] (auth/create-user-with-credentials! data)]
        (auth/activate-user! user-id)
        (assoc data :id user-id))))))

(defn student-user-created
  ([] (student-user-created {}))
  ([options]
   (let [defaults {:first-name "Test" :last-name "Test"}
         data (merge defaults options)
         [{user-id :id}] (auth/create-user! data)]
     (auth/activate-user! user-id)
     (assoc options :id user-id))))

(defn course-created
  ([] (course-created {}))
  ([options]
   (let [defaults {:name "test-course"
                   :slug "test-course-slug"
                   :lang nil
                   :image-src nil
                   :status "draft"
                   :website-user-id nil
                   :owner-id 1}
         course-data (->> (merge defaults options)
                          (transform-keys ->snake_case_keyword))
         [{course-id :id}] (db/create-course! course-data)
         data {:initial-scene "test-scene" :workflow-actions [{:id 1 :type "set-activity" :activity "home" :activity-number 1 :lesson 1 :level 1}] :templates {}}
         [{version-id :id}] (db/save-course! {:course_id course-id :data data :owner_id (:owner_id course-data) :created_at (jt/local-date-time)})]
     (->> (assoc course-data
           :id course-id
           :data data
           :version-id version-id)
          (transform-keys ->kebab-case-keyword)))))

(defn scene-created []
  (let [{course-id :id course-name :name course-slug :slug} (course-created)
        scene-name "test-scene"
        [{scene-id :id}] (db/create-scene! {:course_id course-id :name scene-name})
        data {:test "test" :test-dash "test-dash-value" :test3 "test-3-value"}
        [{version-id :id}] (db/save-scene! {:scene_id scene-id :data data :owner_id 0 :created_at (jt/local-date-time)})]
    {:id scene-id
     :course-name course-name
     :course-slug course-slug
     :name scene-name
     :data data
     :version-id version-id}))

(defn dataset-created
  ([]
   (let [{course-id :id} (course-created)]
     (dataset-created {:course-id course-id})))
  ([options]
    (let [defaults {:name "dataset" :scheme {:fields [{:name "src" :type "image"} {:name "width" :type "number"}]}}
          data (->> options
                    (merge defaults)
                    (transform-keys ->snake_case_keyword))
          [{id :id}] (db/create-dataset! data)]
      (assoc data :id id))))

(defn dataset-item-created
  ([]
   (let [{dataset-id :id} (dataset-created)]
     (dataset-item-created {:dataset-id dataset-id})))
  ([options]
   (let [defaults {:name "test-item" :data {:src "test-src" :width 100 :height 100}}
         data (->> options
                   (merge defaults)
                   (transform-keys ->snake_case_keyword))
         [{id :id}] (db/create-dataset-item! data)]
     (->> (assoc data :id id)
          (transform-keys ->kebab-case-keyword)))))

(defn lesson-set-created
  ([]
   (let [{item-id :id dataset-id :dataset-id} (dataset-item-created)]
     (lesson-set-created {:dataset-id dataset-id :data {:items [{:id item-id}]}})))
  ([options]
   (let [defaults {:name "test-lesson-set"}
         data (->> options
                   (merge defaults)
                   (transform-keys ->snake_case_keyword))
         [{id :id}] (db/create-lesson-set! data)]
     (->> (assoc data :id id)
          (transform-keys ->kebab-case-keyword)))))

(defn school-created
  ([]
   (school-created {}))
  ([options]
   (let [defaults {:id 1 :name "test-school"}
         data (->> options
                   (merge defaults)
                   (transform-keys ->snake_case_keyword))
         [{id :id}] (db/create-school! data)]
     (->> (assoc data :id id)
          (transform-keys ->kebab-case-keyword)))))

(defn class-created
  ([]
   (class-created {}))
  ([options]
   (let [defaults {:name "test-class" :school-id default-school-id}
         data (->> options
                   (merge defaults)
                   (transform-keys ->snake_case_keyword))
         [{id :id}] (db/create-class! data)]
     (->> (assoc data :id id)
          (transform-keys ->kebab-case-keyword)))))

(defn student-created
  ([]
   (student-created {}))
  ([options]
   (let [{user-id :id} (student-user-created)
         {class-id :id school-id :school-id} (class-created)
         defaults {:user-id user-id :class-id class-id :school-id school-id :gender 1 :date-of-birth (jt/local-date) :access-code "123456"}
         data (->> options
                   (merge defaults)
                   (transform-keys ->snake_case_keyword))
         [{id :id}] (db/create-student! data)]
     (->> (assoc data :id id :user-id user-id :class-id class-id)
          (transform-keys ->kebab-case-keyword)))))

(defn student-logged-in
  ([request]
   (let [{user-id :id} (student-user-created)
         _ (student-created {:user-id user-id})]
     (student-logged-in request user-id)))
  ([request user-id]
   (let [student (db/get-student-by-user {:user_id user-id})
         session {:identity {:id user-id :school-id (:school-id student)}}
         session-key (store/write-session handler/dev-store nil session)]
     (assoc request :cookies {"ring-session" {:value session-key}}))))

(defn teacher-created [options]
  (if-let [teacher (db/get-teacher-by-user {:user_id (:user-id options)})]
    teacher
    (let [defaults {:school-id default-school-id}
          data (->> options
                    (merge defaults)
                    (transform-keys ->snake_case_keyword))
          [{id :id}] (db/create-teacher! data)]
      (->> (assoc data :id id)
           (transform-keys ->kebab-case-keyword)))))

(defn teacher-logged-in
  ([request]
   (let [{user-id :id} (teacher-user-created)
         _ (teacher-created {:user-id user-id})]
     (teacher-logged-in request user-id)))
  ([request user-id]
   (let [teacher (db/get-teacher-by-user {:user_id user-id})
         session {:identity {:id user-id :school-id (:school-id teacher)}}
         session-key (store/write-session handler/dev-store nil session)]
     (assoc request :cookies {"ring-session" {:value session-key}}))))

(defn get-course
  [course-slug]
  (let [course-url (str "/api/courses/" course-slug)
        request (-> (mock/request :get course-url)
                    teacher-logged-in)]
    (handler/dev-handler request)))

(defn save-course!
  [course-slug data]
  (let [course-url (str "/api/courses/" course-slug)
        request (-> (mock/request :post course-url (json/write-str data))
                    (mock/header :content-type "application/json")
                    teacher-logged-in)]
    (handler/dev-handler request)))

(defn localize-course!
  [course-id data]
  (let [course-url (str "/api/courses/" course-id "/translate")
        request (-> (mock/request :post course-url)
                    (mock/content-type "application/json")
                    (mock/body (json/write-str data))
                    teacher-logged-in)]
    #_(course-handler/website-api-routes request)
    (handler/dev-handler request)))

(defn get-courses-by-website-user
  [user-id]
  (let [url (str "/api/courses/by-website-user/" user-id)
        request (-> (mock/request :get url)
                    (mock/header :content-type "application/json")
                    teacher-logged-in)]
    (handler/dev-handler request)))

(defn get-available-courses
  []
  (let [url (str "/api/courses/available")
        request (-> (mock/request :get url)
                    (mock/header :content-type "application/json")
                    teacher-logged-in)]
    (handler/dev-handler request)))

(defn get-course-info
  [course-slug]
  (let [course-url (str "/api/courses/" course-slug "/info")
        request (-> (mock/request :get course-url)
                    teacher-logged-in)]
    (handler/dev-handler request)))

(defn save-course-info!
  [course-id data]
  (let [course-url (str "/api/courses/" course-id "/info")
        request (-> (mock/request :put course-url (json/write-str data))
                    (mock/header :content-type "application/json")
                    teacher-logged-in)]
    (handler/dev-handler request)))

(defn restore-course-version!
  [version-id]
  (let [url (str "/api/course-versions/" version-id "/restore")
        request (-> (mock/request :post url (json/write-str {:id version-id}))
                    (mock/header :content-type "application/json")
                    teacher-logged-in)]
    (handler/dev-handler request)))

(defn get-course-versions
  [course-name]
  (let [url (str "/api/courses/" course-name "/versions")
        request (-> (mock/request :get url)
                    teacher-logged-in)]
    (handler/dev-handler request)))

(defn get-scene
  [course-name scene-name]
  (let [scene-url (str "/api/courses/"course-name "/scenes/" scene-name)
        request (-> (mock/request :get scene-url)
                    teacher-logged-in)]
    (handler/dev-handler request)))

(defn save-scene!
  [course-slug scene-name data]
  (let [url (str "/api/courses/" course-slug "/scenes/" scene-name)
        request (-> (mock/request :post url (json/write-str data))
                    (mock/header :content-type "application/json")
                    teacher-logged-in)]
    (handler/dev-handler request)))

(defn restore-scene-version!
  [version-id]
  (let [url (str "/api/scene-versions/" version-id "/restore")
        request (-> (mock/request :post url (json/write-str {:id version-id}))
                    (mock/header :content-type "application/json")
                    teacher-logged-in)]
    (handler/dev-handler request)))

(defn get-scene-versions
  [course-name scene-name]
  (let [url (str "/api/courses/" course-name "/scenes/" scene-name "/versions")
        request (-> (mock/request :get url)
                    teacher-logged-in)]
    (handler/dev-handler request)))

(defn get-course-datasets
  [course-name]
  (let [url (str "/api/courses/" course-name "/datasets")
        request (-> (mock/request :get url)
                    teacher-logged-in)]
    (handler/dev-handler request)))

(defn get-dataset
  [dataset-id]
  (let [url (str "/api/datasets/" dataset-id)
        request (-> (mock/request :get url)
                    teacher-logged-in)]
    (handler/dev-handler request)))

(defn create-dataset!
  [data]
  (let [url (str "/api/datasets")
        request (-> (mock/request :post url (json/write-str data))
                    (mock/header :content-type "application/json")
                    teacher-logged-in)]
    (handler/dev-handler request)))

(defn update-dataset!
  [dataset-id data]
  (let [url (str "/api/datasets/" dataset-id)
        request (-> (mock/request :put url (json/write-str data))
                    (mock/header :content-type "application/json")
                    teacher-logged-in)]
    (handler/dev-handler request)))

(defn get-dataset-item
  [id]
  (let [url (str "/api/dataset-items/" id)
        request (-> (mock/request :get url)
                    teacher-logged-in)]
    (handler/dev-handler request)))

(defn create-dataset-item!
  [data]
  (let [url (str "/api/dataset-items")
        request (-> (mock/request :post url (json/write-str data))
                    (mock/header :content-type "application/json")
                    teacher-logged-in)]
    (-> (handler/dev-handler request)
        :body
        (json/read-str :key-fn keyword))))

(defn update-dataset-item!
  [id data]
  (let [url (str "/api/dataset-items/" id)
        request (-> (mock/request :put url (json/write-str data))
                    (mock/header :content-type "application/json")
                    teacher-logged-in)]
    (handler/dev-handler request)))


(defn delete-dataset-item!
  [id]
  (let [url (str "/api/dataset-items/" id)
        request (-> (mock/request :delete url)
                    teacher-logged-in)]
    (handler/dev-handler request)))

(defn get-lesson-set
  [dataset-id name]
  (let [url (str "/api/datasets/" dataset-id "/lesson-sets/" name)
        request (-> (mock/request :get url)
                    teacher-logged-in)]
    (handler/dev-handler request)))

(defn create-lesson-set!
  [data]
  (let [url (str "/api/lesson-sets")
        request (-> (mock/request :post url (json/write-str data))
                    (mock/header :content-type "application/json")
                    teacher-logged-in)]
    (-> (handler/dev-handler request)
        :body
        (json/read-str :key-fn keyword))))

(defn update-lesson-set!
  [id data]
  (let [url (str "/api/lesson-sets/" id)
        request (-> (mock/request :put url (json/write-str data))
                    (mock/header :content-type "application/json")
                    teacher-logged-in)]
    (handler/dev-handler request)))


(defn delete-lesson-set!
  [id]
  (let [url (str "/api/lesson-sets/" id)
        request (-> (mock/request :delete url)
                    teacher-logged-in)]
    (handler/dev-handler request)))

(defn get-classes
  []
  (let [url (str "/api/classes")
        request (-> (mock/request :get url)
                    teacher-logged-in)]
    (handler/dev-handler request)))

(defn get-class
  [id]
  (let [url (str "/api/classes/" id)
        request (-> (mock/request :get url)
                    teacher-logged-in)]
    (handler/dev-handler request)))

(defn create-class!
  [data]
  (let [url (str "/api/classes")
        request (-> (mock/request :post url (json/write-str data))
                    (mock/header :content-type "application/json")
                    teacher-logged-in)]
    (-> (handler/dev-handler request)
        :body
        (json/read-str :key-fn keyword))))

(defn update-class!
  [id data]
  (let [url (str "/api/classes/" id)
        request (-> (mock/request :put url (json/write-str data))
                    (mock/header :content-type "application/json")
                    teacher-logged-in)]
    (handler/dev-handler request)))

(defn delete-class!
  [id]
  (let [url (str "/api/classes/" id)
        request (-> (mock/request :delete url)
                    teacher-logged-in)]
    (handler/dev-handler request)))

(defn get-students
  [class-id]
  (let [url (str "/api/classes/" class-id "/students")
        request (-> (mock/request :get url)
                    teacher-logged-in)]
    (handler/dev-handler request)))

(defn get-unassigned-students
  []
  (let [url (str "/api/unassigned-students")
        request (-> (mock/request :get url)
                    teacher-logged-in)]
    (handler/dev-handler request)))

(defn get-student
  [student-id]
  (let [url (str "/api/students/" student-id)
        request (-> (mock/request :get url)
                    teacher-logged-in)]
    (handler/dev-handler request)))

(defn create-student!
  [data]
  (let [url (str "/api/students")
        request (-> (mock/request :post url (json/write-str data))
                    (mock/header :content-type "application/json")
                    teacher-logged-in)]
    (-> (handler/dev-handler request)
        :body
        (json/read-str :key-fn keyword))))

(defn update-student!
  [id data]
  (let [url (str "/api/students/" id)
        request (-> (mock/request :put url (json/write-str data))
                    (mock/header :content-type "application/json")
                    teacher-logged-in)]
    (handler/dev-handler request)))

(defn unassigned-student!
  [id]
  (let [url (str "/api/students/" id "/class")
        request (-> (mock/request :delete url)
                    teacher-logged-in)]
    (handler/dev-handler request)))

(defn delete-student!
  [id]
  (let [url (str "/api/students/" id)
        request (-> (mock/request :delete url)
                    teacher-logged-in)]
    (handler/dev-handler request)))

(defn get-course-lessons
  [course-name]
  (let [url (str "/api/courses/" course-name "/lesson-sets")
        request (-> (mock/request :get url)
                    teacher-logged-in)]
    (handler/dev-handler request)))

(defn get-current-school []
  (let [url (str "/api/schools/current")
        request (mock/request :get url)]
    (handler/dev-handler request)))

(defn with-default-school [f]
  (school-created {:id default-school-id})
  (f))

(defn get-school
  [id]
  (let [url (str "/api/schools/" id)
        request (-> (mock/request :get url)
                    teacher-logged-in)]
    (handler/dev-handler request)))

(defn create-school!
  [data]
  (let [url (str "/api/schools")
        request (-> (mock/request :post url (json/write-str data))
                    (mock/header :content-type "application/json")
                    teacher-logged-in)]
    (-> (handler/dev-handler request)
        :body
        (json/read-str :key-fn keyword))))

(defn get-schools
  []
  (let [url (str "/api/schools")
        request (-> (mock/request :get url)
                    teacher-logged-in)]
    (handler/dev-handler request)))

(defn update-school!
  [id data]
  (let [url (str "/api/schools/" id)
        request (-> (mock/request :put url (json/write-str data))
                    (mock/header :content-type "application/json")
                    teacher-logged-in)]
    (handler/dev-handler request)))

(defn delete-school!
  [id]
  (let [url (str "/api/schools/" id)
        request (-> (mock/request :delete url)
                    teacher-logged-in)]
    (handler/dev-handler request)))

(defn is-created-asset-hash?
  [hash]
  (let [id (db/get-asset-hash {:path_hash hash})]
    (not (nil? id))
    ))

(defn extract-filename
  [path]
  (let [filename-start (inc (clojure.string/last-index-of path "/"))
        file-extension-start (clojure.string/last-index-of path ".")
        file-name (subs path filename-start file-extension-start)]
    file-name)
  )
(defn extract-extension
  [path]
  (let [
        file-extension-start (clojure.string/last-index-of path ".")
        file-extension (subs path file-extension-start)]
    file-extension
  ))

(defn create-temp-file
  [file-path]
  (let [
        file-name (extract-filename file-path)
        file-extension (extract-extension file-path)
        temp-file (java.io.File/createTempFile file-name file-extension)]
    (io/copy (io/file file-path) temp-file)
    temp-file))

(defn upload-file!
  [file-path]
    (let [
          file-extension (extract-extension file-path)
          filecontent {:tempfile (create-temp-file file-path)
                       :content-type (mime/ext-mime-type file-path),
                       :filename      (str (extract-filename file-path) "." file-extension)
                       :size (.length (io/file file-path))
                       }
          response (handler/dev-handler (-> (assoc
                              (mock/request :post "/api/assets/")
                              :params {:filecontent filecontent}
                              :multipart-params {"file" filecontent})
                                             (teacher-logged-in)
                                            ))]
      (is (= (:status response) 200))
      response))
