(ns webchange.test.course.handler
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [webchange.test.fixtures.core :as f]
            [clojure.data.json :as json]
            [clojure.tools.logging :as log]
            [webchange.auth.website :as website]
            [webchange.test.course.core :as core]
            [webchange.course.core :as course]
            [webchange.db.core :refer [*db*] :as db]
            [config.core :refer [env]]
            [ring.swagger.json-schema :as js])
  (:use clj-http.fake))

(use-fixtures :once f/init)
(use-fixtures :each f/clear-db-fixture f/with-default-school)

(deftest course-editor-page-can-be-opened-by-owner
  (let [{user-id :id} (f/website-user-created)
        course (f/course-created {:owner-id user-id})
        response (f/open-course-editor-page (:slug course) user-id)]
    (is (= 200 (:status response)))))

(deftest course-editor-page-can-be-opened-by-collaborator
  (let [{course-id :id :as course} (f/course-created)
        {user-id :id} (f/website-user-created)
        _ (f/add-collaborator {:course-id course-id :user-id user-id})
        response (f/open-course-editor-page (:slug course) user-id)]
    (is (= 200 (:status response)))))

(deftest course-editor-page-cannot-be-opened-by-non-collaborator
  (let [{user-id :id} (f/website-user-created)
        course (f/course-created {:owner-id user-id})
        {random-user-id :id} (f/website-user-created {:id 321 :email "random@example.com"})
        response (f/open-course-editor-page (:slug course) random-user-id)]
    (is (= 403 (:status response)))))

(deftest course-can-be-created
  (let [name "My test Course"
        course-data {:name name :lang "english" :concept-list-id 1}
        saved-value (-> (f/create-course! course-data) :body slurp (json/read-str :key-fn keyword))
        retrieved-value (-> (:slug saved-value) f/get-course :body slurp (json/read-str :key-fn keyword))]
    (is (= name (:name saved-value)))
    (is (not (nil? (:id saved-value))))
    (is (not (nil? (:slug saved-value))))
    (is (not (nil? (:scene-list retrieved-value))))))

(deftest course-can-be-retrieved
         (let [course (f/course-created)
               response (f/get-course (:slug course))
               body (-> response :body slurp (json/read-str :key-fn keyword))]
           (is (= 200 (:status response)))
           (is (= (get-in course [:data :initial-scene]) (:initial-scene body)))))

(deftest course-can-be-saved
  (let [{user-id :id} (f/website-user-created)
        course (f/course-created {:owner-id user-id})
        edited-value "test-scene-edited"
        _ (f/save-course! (:slug course) user-id {:course {:initial-scene edited-value}})
        retrieved-value (-> (:slug course) f/get-course :body slurp (json/read-str :key-fn keyword) :initial-scene)]
    (is (= edited-value retrieved-value))))

(deftest activity-can-be-created-from-template
  (let [{user-id :id} (f/website-user-created)
        course (f/course-created {:owner-id user-id})
        name "Test Activity"
        activity-data {:name name :template-id 1 :characters [{:skeleton "vera" :name "vera"}] :boxes 3 :skills [2]}
        saved-response (f/create-activity! (:slug course) user-id activity-data)
        saved-value (-> saved-response :body slurp (json/read-str :key-fn keyword))
        retrieved-response (f/get-scene (:course-slug saved-value) (:scene-slug saved-value))
        retrieved-value (-> retrieved-response :body slurp (json/read-str :key-fn keyword))]
    (testing "Activity can be created from template"
      (is (= 200 (:status saved-response)))
      (is (= 200 (:status retrieved-response)))
      (is (= name (:name saved-value)))
      (is (not (nil? (:id saved-value))))
      (is (not (nil? (:course-slug saved-value))))
      (is (not (nil? (:scene-slug saved-value))))
      (is (not (nil? (:objects retrieved-value)))))))

(deftest scene-can-be-retrieved
         (let [scene (f/scene-created)
               response (f/get-scene (:course-slug scene) (:name scene))]
           (is (= 200 (:status response)))
           (is (= (get-in scene [:data :test]) (-> response :body slurp (json/read-str :key-fn keyword) :test)))))

(deftest scene-can-be-saved
         (let [{user-id :id} (f/website-user-created)
               course (f/course-created {:owner-id user-id})
               scene (f/scene-created course)
               edited-value "test-edited"
               _ (f/save-scene! (:course-slug scene) (:name scene) user-id {:scene {:test edited-value}})
               retrieved-value (-> (f/get-scene (:course-slug scene) (:name scene))
                                   :body
                                   slurp
                                   (json/read-str :key-fn keyword)
                                   :test)]
           (is (= edited-value retrieved-value))))

(deftest course-versions-can-be-retrieved
         (let [{user-id :id} (f/website-user-created)
               course (f/course-created {:owner-id user-id})
               _ (f/save-course! (:slug course) user-id {:course {:initial-scene "edited-value"}})
               versions (-> (:slug course) f/get-course-versions :body slurp (json/read-str :key-fn keyword) :versions)]
           (is (= 2 (count versions)))))

(deftest course-version-can-be-restored
         (let [{user-id :id} (f/website-user-created)
               course (f/course-created {:owner-id user-id})
               original-value (-> course :data :initial-scene)
               _ (f/save-course! (:slug course) user-id {:course {:initial-scene "edited-value"}})
               _ (f/restore-course-version! (:version-id course) user-id)
               retrieved-value (-> (:slug course) f/get-course :body slurp (json/read-str :key-fn keyword) :initial-scene)]
           (is (= original-value retrieved-value))))

(deftest scene-versions-can-be-retrieved
         (let [{user-id :id} (f/website-user-created)
               course (f/course-created {:owner-id user-id})
               scene (f/scene-created course)
               _ (f/save-scene! (:course-slug scene) (:name scene) user-id {:scene {:test "edited-value"}})
               versions (-> (f/get-scene-versions (:course-slug scene) (:name scene))
                            :body
                            slurp
                            (json/read-str :key-fn keyword)
                            :versions)]
           (is (= 2 (count versions)))))

(deftest scene-version-can-be-restored
         (let [{user-id :id} (f/website-user-created)
               course (f/course-created {:owner-id user-id})
               scene (f/scene-created course)
               original-value (-> scene :data :test)
               _ (f/save-scene! (:course-slug scene) (:name scene) user-id {:scene {:test "edited-value"}})
               _ (f/restore-scene-version! (:version-id scene) user-id)
               retrieved-value (-> (f/get-scene (:course-slug scene) (:name scene))
                                   :body
                                   slurp
                                   (json/read-str :key-fn keyword)
                                   :test)]
           (is (= original-value retrieved-value))))

(deftest course-info-can-be-retrieved
  (let [keys [:id :slug :name :lang :image-src :status :website-user-id :owner-id]
        course (f/course-created)
        response (f/get-course-info (:slug course))
        body (-> response :body slurp (json/read-str :key-fn keyword))]
    (is (= 200 (:status response)))
    (is (= (select-keys course keys) (select-keys body keys)))))

(deftest course-info-can-be-saved
  (let [{user-id :id} (f/website-user-created)
        course (f/course-created {:owner-id user-id})
        name "name-edited"
        slug "slug-edited"
        lang "lang-edited"
        image-src "image-src-edited"
        _ (f/save-course-info! (:id course) user-id {:name name :slug slug :lang lang :image-src image-src})
        retrieved (-> slug f/get-course-info :body slurp (json/read-str :key-fn keyword))]
    (is (= name (:name retrieved)))
    (is (= slug (:slug retrieved)))
    (is (= lang (:lang retrieved)))
    (is (= image-src (:image-src retrieved)))))

(def website-user-id 123)
(def website-user {:id website-user-id :email "email@example.com" :first_name "First" :last_name "Last" :image "https://example.com/image.png"})

(deftest course-can-be-localized
  (let [course (f/course-created)
        new-language "new-language"]
    (with-global-fake-routes-in-isolation
      {(website/website-user-resource) (fn [request] {:status 200 :headers {} :body (json/write-str {:status "success" :data website-user})})}
      (let [new-course (-> (f/localize-course! (:id course) {:language new-language :user-id website-user-id}) :body slurp (json/read-str :key-fn keyword))]
        (is (= new-language (:lang new-course)))))))

(deftest localized-course-can-be-retrieved
  (let [course (f/course-created)
        new-language "new-language"]
    (with-global-fake-routes-in-isolation
      {(website/website-user-resource) (fn [request] {:status 200 :headers {} :body (json/write-str {:status "success" :data website-user})})}
      (let [_ (f/localize-course! (:id course) {:language new-language :user-id website-user-id})
            my-courses (-> (f/get-courses-by-website-user website-user-id) :body slurp (json/read-str :key-fn keyword))]
        (is (= 1 (count my-courses)))))))

(deftest available-courses-can-be-retrieved
  (let [course-name "available course"
        _ (f/course-created {:name course-name :status "published"})
        response (f/get-available-courses)
        courses (-> response :body slurp (json/read-str :key-fn keyword))]
    (is (= 200 (:status response)))
    (is (= 1 (count courses)))
    (is (= course-name (-> courses first :name)))))

(deftest retrieved-course-has-default-image-src
  (let [course-name "available course"
        _ (f/course-created {:name course-name :status "published"})
        course (-> (f/get-available-courses) :body slurp (json/read-str :key-fn keyword) first)]
    (is (not (nil? (:image-src course))))))

(deftest retrieved-course-image-src-has-host-name
  (let [course-name "available course"
        _ (f/course-created {:name course-name :status "published"})
        course (-> (f/get-available-courses) :body slurp (json/read-str :key-fn keyword) first)]
    (is (clojure.string/includes? (:image-src course) course/hostname))))

(deftest scene-version-do-not-create-same
  (let [{user-id :id} (f/website-user-created)
        course (f/course-created {:owner-id user-id})
        scene (f/scene-created course)
        data (-> scene :data)
        _ (f/save-scene! (:course-slug scene) (:name scene) user-id {:scene data})
        scene-new  (db/get-latest-scene-version {:scene_id (:id scene)})]
    (is (:version-id scene) (:id scene-new))))

(deftest scene-version-do-create-new
  (let [{user-id :id} (f/website-user-created)
        course (f/course-created {:owner-id user-id})
        scene (f/scene-created course)
        _ (f/save-scene! (:course-slug scene) (:name scene) user-id {:scene {:test "edited-value"}})
        scene-new  (db/get-latest-scene-version {:scene_id (:id scene)})]
    (is (not= (:version-id scene) (:id scene-new)))))

(deftest can-retrieve-editor-tags
  (let [tag (f/editor-tag-created "China")
        tag-retrieved (first (core/retrieve-editor-tags))]
    (assert (= tag tag-retrieved))))

(deftest can-retrieve-editor-assets
  (let [tag-china (f/editor-tag-created "China")
        tag-india (f/editor-tag-created "Idida")
        background (f/editor-asset-created course/editor_asset_type_background)
        details (f/editor-asset-created course/editor_asset_type_decoration "hello/example1.png")
        _ (f/link-editor-asset-tag (:id tag-china) (:id background))
        _ (f/link-editor-asset-tag (:id tag-india) (:id background))
        _ (f/link-editor-asset-tag (:id tag-china) (:id details))
        china-assets (core/retrieve-editor-assets (:id tag-china) nil)
        india-assets (core/retrieve-editor-assets (:id tag-india) nil)
        background-assets (core/retrieve-editor-assets nil course/editor_asset_type_background)
        background-china-assets (core/retrieve-editor-assets (:id tag-china) course/editor_asset_type_background)]
    (assert (= (count china-assets) 2))
    (assert (= (count india-assets) 1))
    (assert (= (count background-assets) 1))
    (assert (= (count background-china-assets) 1))))

(deftest can-retrieve-retrieve-editor-character-skin
  (let [ _ (course/update-character-skins {:public-dir "test/clj/webchange/resources"})
        skins (core/retrieve-editor-character-skin)]
    (assert (= (count skins) 2))))

(deftest skills-can-be-retrieved
  (let [retrieved (-> (f/get-skills) :body slurp (json/read-str :key-fn keyword))]
    (is (not (empty? (:strands retrieved))))
    (is (not (empty? (:topics retrieved))))
    (is (not (empty? (:skills retrieved))))))
