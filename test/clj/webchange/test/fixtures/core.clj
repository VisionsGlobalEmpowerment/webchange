(ns webchange.test.fixtures.core
  (:require [clojure.test :refer :all]
            [webchange.db.core :refer [*db*] :as db]
            [webchange.auth.core :as auth]
            [webchange.handler :as handler]
            [config.core :refer [env]]
            [mount.core :as mount]
            [luminus-migrations.core :as migrations]
            [ring.middleware.session.store :as store]
            [java-time :as jt]
            [ring.mock.request :as mock]
            [clojure.data.json :as json]
            [camel-snake-kebab.extras :refer [transform-keys]]
            [camel-snake-kebab.core :refer [->snake_case_keyword ->kebab-case-keyword]]))

(defn clear-db []
  (db/clear-table :lesson_sets)
  (db/clear-table :dataset_items)
  (db/clear-table :datasets)
  (db/clear-table :scene_versions)
  (db/clear-table :scenes)
  (db/clear-table :course_versions)
  (db/clear-table :courses)
  (db/clear-table :users))

(defn clear-db-fixture [f]
  (clear-db)
  (f))

(defn init [f]
  (mount/start #'webchange.db.core/*db*)
  (migrations/migrate ["migrate"] (select-keys env [:database-url]))
  (f))

(defn user-created []
  (if-let [user (db/find-user-by-email {:email "test@example.com"})]
    (assoc user :password "test")
    (let [options {:first-name "Test" :last-name "Test" :email "test@example.com" :password "test"}
          [{user-id :id}] (-> options auth/prepare-register-data auth/create-user!)]
      (auth/activate-user! user-id)
      (assoc options :id user-id))))

(defn user-logged-in
  [request]
  (let [user (user-created)
        session {:identity (:email user)}
        session-key (store/write-session handler/dev-store nil session)]
    (assoc request :cookies {"ring-session" {:value session-key}})))

(defn course-created []
  (let [course-name "test-course"
        [{course-id :id}] (db/create-course! {:name course-name})
        data {:initial-scene "test-scene"}
        [{version-id :id}] (db/save-course! {:course_id course-id :data data :owner_id 0 :created_at (jt/local-date-time)})]
    {:id course-id
     :name course-name
     :data data
     :version-id version-id}))

(defn scene-created []
  (let [{course-id :id course-name :name} (course-created)
        scene-name "test-scene"
        [{scene-id :id}] (db/create-scene! {:course_id course-id :name scene-name})
        data {:test "test"}
        [{version-id :id}] (db/save-scene! {:scene_id scene-id :data data :owner_id 0 :created_at (jt/local-date-time)})]
    {:id scene-id
     :course-name course-name
     :name scene-name
     :data data
     :version-id version-id}))

(defn dataset-created
  ([]
   (let [{course-id :id} (course-created)]
     (dataset-created {:course-id course-id})))
  ([options]
    (let [defaults {:name "dataset" :scheme {:fields [{:name "src" :type "string"}]}}
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
   (let [defaults {:data {:src "test-src" :width 100 :height 100}}
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

(defn get-course
  [course-name]
  (let [course-url (str "/api/courses/" course-name)
        request (-> (mock/request :get course-url)
                    user-logged-in)]
    (handler/dev-handler request)))

(defn save-course!
  [course-name data]
  (let [course-url (str "/api/courses/" course-name)
        request (-> (mock/request :post course-url (json/write-str data))
                    (mock/header :content-type "application/json")
                    user-logged-in)]
    (handler/dev-handler request)))

(defn restore-course-version!
  [version-id]
  (let [url (str "/api/course-versions/" version-id "/restore")
        request (-> (mock/request :post url (json/write-str {:id version-id}))
                    (mock/header :content-type "application/json")
                    user-logged-in)]
    (handler/dev-handler request)))

(defn get-course-versions
  [course-name]
  (let [url (str "/api/courses/" course-name "/versions")
        request (-> (mock/request :get url)
                    user-logged-in)]
    (handler/dev-handler request)))

(defn get-scene
  [course-name scene-name]
  (let [scene-url (str "/api/courses/"course-name "/scenes/" scene-name)
        request (-> (mock/request :get scene-url)
                    user-logged-in)]
    (handler/dev-handler request)))

(defn save-scene!
  [course-name scene-name data]
  (let [url (str "/api/courses/" course-name "/scenes/" scene-name)
        request (-> (mock/request :post url (json/write-str data))
                    (mock/header :content-type "application/json")
                    user-logged-in)]
    (handler/dev-handler request)))

(defn restore-scene-version!
  [version-id]
  (let [url (str "/api/scene-versions/" version-id "/restore")
        request (-> (mock/request :post url (json/write-str {:id version-id}))
                    (mock/header :content-type "application/json")
                    user-logged-in)]
    (handler/dev-handler request)))

(defn get-scene-versions
  [course-name scene-name]
  (let [url (str "/api/courses/" course-name "/scenes/" scene-name "/versions")
        request (-> (mock/request :get url)
                    user-logged-in)]
    (handler/dev-handler request)))

(defn get-course-datasets
  [course-name]
  (let [url (str "/api/courses/" course-name "/datasets")
        request (-> (mock/request :get url)
                    user-logged-in)]
    (handler/dev-handler request)))

(defn get-dataset
  [dataset-id]
  (let [url (str "/api/datasets/" dataset-id)
        request (-> (mock/request :get url)
                    user-logged-in)]
    (handler/dev-handler request)))

(defn create-dataset!
  [data]
  (let [url (str "/api/datasets")
        request (-> (mock/request :post url (json/write-str data))
                    (mock/header :content-type "application/json")
                    user-logged-in)]
    (handler/dev-handler request)))

(defn update-dataset!
  [dataset-id data]
  (let [url (str "/api/datasets/" dataset-id)
        request (-> (mock/request :put url (json/write-str data))
                    (mock/header :content-type "application/json")
                    user-logged-in)]
    (handler/dev-handler request)))

(defn get-dataset-item
  [id]
  (let [url (str "/api/dataset-items/" id)
        request (-> (mock/request :get url)
                    user-logged-in)]
    (handler/dev-handler request)))

(defn create-dataset-item!
  [data]
  (let [url (str "/api/dataset-items")
        request (-> (mock/request :post url (json/write-str data))
                    (mock/header :content-type "application/json")
                    user-logged-in)]
    (-> (handler/dev-handler request)
        :body
        (json/read-str :key-fn keyword))))

(defn update-dataset-item!
  [id data]
  (let [url (str "/api/dataset-items/" id)
        request (-> (mock/request :put url (json/write-str data))
                    (mock/header :content-type "application/json")
                    user-logged-in)]
    (handler/dev-handler request)))


(defn delete-dataset-item!
  [id]
  (let [url (str "/api/dataset-items/" id)
        request (-> (mock/request :delete url)
                    user-logged-in)]
    (handler/dev-handler request)))

(defn get-lesson-set
  [name]
  (let [url (str "/api/lesson-sets/" name)
        request (-> (mock/request :get url)
                    user-logged-in)]
    (handler/dev-handler request)))

(defn create-lesson-set!
  [data]
  (let [url (str "/api/lesson-sets")
        request (-> (mock/request :post url (json/write-str data))
                    (mock/header :content-type "application/json")
                    user-logged-in)]
    (-> (handler/dev-handler request)
        :body
        (json/read-str :key-fn keyword))))

(defn update-lesson-set!
  [id data]
  (let [url (str "/api/lesson-sets/" id)
        request (-> (mock/request :put url (json/write-str data))
                    (mock/header :content-type "application/json")
                    user-logged-in)]
    (handler/dev-handler request)))


(defn delete-lesson-set!
  [id]
  (let [url (str "/api/lesson-sets/" id)
        request (-> (mock/request :delete url)
                    user-logged-in)]
    (handler/dev-handler request)))