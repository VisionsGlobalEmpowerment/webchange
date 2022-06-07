(ns webchange.test.course.handler
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [webchange.test.fixtures.core :as f]
            [clojure.data.json :as json]
            [mockery.core :as mockery]
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

(deftest create-course
  (testing "Course can be create"
    (let [name "My test Course"
          course-data {:name name :lang "english" :concept-list-id 1}
          saved-value (-> (f/create-course! course-data) :body slurp (json/read-str :key-fn keyword))
          retrieved-value (-> (:slug saved-value) f/get-course :body slurp (json/read-str :key-fn keyword))]
      (is (= name (:name saved-value)))
      (is (not (nil? (:id saved-value))))
      (is (not (nil? (:slug saved-value))))
      (is (not (nil? (:scene-list retrieved-value))))))
  (testing "Course slug should be escaped"
    (let [name "My test ? Course ! ,."
          course-data {:name name :lang "english" :concept-list-id 1}
          saved-value (-> (f/create-course! course-data) :body slurp (json/read-str :key-fn keyword))
          retrieved-value (-> (:slug saved-value) f/get-course :body slurp (json/read-str :key-fn keyword))]
      (is (= name (:name saved-value)))
      (is (not (nil? (:id saved-value))))
      (is (not (nil? (:slug saved-value))))
      (is (not (nil? (:scene-list retrieved-value)))))))

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

(deftest book-can-be-created
  (let [name "My test Book"
        course-data {:name name :lang "English" :type "book"}
        saved-value (-> (f/create-course! course-data) :body slurp (json/read-str :key-fn keyword))
        retrieved-value (-> (:slug saved-value) f/get-course :body slurp (json/read-str :key-fn keyword))]
    (is (= name (:name saved-value)))
    (is (not (nil? (:id saved-value))))
    (is (not (nil? (:slug saved-value))))
    (is (not (nil? (:scene-list retrieved-value))))))

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

(deftest create-activity-placeholder
  (let [{user-id :id} (f/website-user-created)
        course (f/course-created {:owner-id user-id})
        name "Test Activity"
        activity-data {:name name}
        saved-response (f/create-activity-placeholder! (:slug course) user-id activity-data)
        saved-value (-> saved-response :body slurp (json/read-str :key-fn keyword))
        retrieved-response (f/get-scene (:course-slug saved-value) (:scene-slug saved-value))
        retrieved-value (-> retrieved-response :body)]
    (testing "Activity placeholder can be created"
      (is (= 200 (:status saved-response)))
      (is (= 200 (:status retrieved-response)))
      (is (= name (:name saved-value)))
      (is (not (nil? (:id saved-value))))
      (is (not (nil? (:course-slug saved-value))))
      (is (not (nil? (:scene-slug saved-value)))))
    (testing "Activity data is empty"
      (is (nil? retrieved-value)))))

(deftest create-activity-version
  (let [{:keys [course-slug scene-slug owner-id name]} (f/activity-placeholder-created)
        activity-data {:template-id 1 :characters [{:skeleton "vera" :name "vera"}] :boxes 3}
        saved-response (f/create-activity-version! course-slug scene-slug owner-id activity-data)
        retrieved-response (f/get-scene course-slug scene-slug)
        retrieved-value (-> retrieved-response :body)]
    (testing "Activity version can be created"
      (is (= 200 (:status saved-response)))
      (is (= 200 (:status retrieved-response)))
      (is (not (nil? retrieved-value))))))

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

(deftest set-scene-preview
  (let [{user-id :id} (f/website-user-created)
        course (f/course-created {:owner-id user-id})
        scene (f/scene-created course)
        edited-value "test-edited"
        _ (f/save-activity-preview! (:course-slug scene) (:name scene) user-id {:preview edited-value})
        retrieved-body (-> (f/get-course (:course-slug scene))
                           :body
                           slurp
                           (json/read-str :key-fn keyword))
        retrieved-value (-> retrieved-body
                            :scene-list
                            (get (keyword (:name scene)))
                            :preview)]
    (testing "Scene preview can be set"
      (is (= edited-value retrieved-value)))))

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
  (let [keys [:id :slug :name :lang :image-src :status :website-user-id :owner-id :type]
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

(deftest localized-book-is-still-a-book
  (let [course (f/course-created {:type "book"})
        new-language "new-language"]
    (with-global-fake-routes-in-isolation
      {(website/website-user-resource) (fn [request] {:status 200 :headers {} :body (json/write-str {:status "success" :data website-user})})}
      (let [_ (f/localize-course! (:id course) {:language new-language :user-id website-user-id})
            my-books (-> (f/get-books-by-website-user website-user-id) :body slurp (json/read-str :key-fn keyword))
            first-book-info (-> my-books first :slug (f/get-course-info) :body slurp (json/read-str :key-fn keyword))]
        (is (= 1 (count my-books)))
        (is (= "book" (:type first-book-info)))))))

(deftest books-by-website-user
  (f/course-created {:type "book" :status "archived" :name "Archived book" :website-user-id website-user-id})
  (f/course-created {:type "book" :status "draft" :name "Draft book" :website-user-id website-user-id})
  (f/course-created {:type "book" :status "in-review" :name "In review book" :website-user-id website-user-id})
  (f/course-created {:type "book" :status "declined" :name "Declined book" :website-user-id website-user-id})
  (f/course-created {:type "book" :status "published" :name "Published book" :website-user-id website-user-id})
  (f/course-created {:type "course" :status "published" :name "Published course" :website-user-id website-user-id})
  (with-global-fake-routes-in-isolation
    {(website/website-user-resource) (fn [request] {:status 200 :headers {} :body (json/write-str {:status "success" :data website-user})})}
    (let [my-books (-> (f/get-books-by-website-user website-user-id) :body slurp (json/read-str :key-fn keyword))]
      (testing "Archived books and published courses are not returned"
        (is (= 4 (count my-books)))))))

(deftest available-courses-can-be-retrieved
  (let [course-name "available course"
        _ (f/course-created {:name course-name :status "published"})
        response (f/get-available-courses)
        courses (-> response :body slurp (json/read-str :key-fn keyword))]
    (is (= 200 (:status response)))
    (is (= 1 (count courses)))
    (is (= course-name (-> courses first :name)))))

(deftest book-library-can-be-retrieved
  (let [course-name "published book"
        _ (f/course-created {:name course-name :status "published" :type "book"})
        response (f/get-book-library)
        courses (-> response :body slurp (json/read-str :key-fn keyword))]
    (is (= 200 (:status response)))
    (is (= 1 (count courses)))
    (is (= course-name (-> courses first :name)))))

(deftest book-in-review-can-be-retrieved
  (let [{user-id :id} (f/website-user-created)
        course-name "book in review"
        _ (f/course-created {:name course-name :status "in-review" :type "book"})
        response (with-redefs [webchange.auth.roles/is-admin? (fn [user-id] true)]
                   (f/get-courses-admin-list "book" "in-review" user-id))
        courses (-> response :body slurp (json/read-str :key-fn keyword))]
    (is (= 200 (:status response)))
    (is (= 1 (count courses)))
    (is (= course-name (-> courses first :name)))))

(deftest book-in-review-can-be-published
  (let [{user-id :id} (f/website-user-created)
        {course-slug :slug course-id :id} (f/course-created {:status "in-review" :type "book"})
        approve-response (with-redefs [webchange.auth.roles/is-admin? (fn [user-id] true)]
                           (f/review-course! course-id user-id {:status "published"}))
        get-course-response  (-> (f/get-course-info course-slug) :body slurp (json/read-str :key-fn keyword))]
    (is (= 200 (:status approve-response)))
    (is (= "published" (:status get-course-response)))))

(deftest book-in-draft-can-be-send-to-review
  (let [{user-id :id} (f/website-user-created)
        {course-slug :slug} (f/course-created {:owner-id user-id :status "draft" :type "book"})
        publish-response (f/publish-course! course-slug user-id)
        get-course-response  (-> (f/get-course-info course-slug) :body slurp (json/read-str :key-fn keyword))]
    (is (= 200 (:status publish-response)))
    (is (= "in-review" (:status get-course-response)))))

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

(deftest scenes-with-skills-can-be-retrieved
  (let [{user-id :id} (f/website-user-created)
        course (f/course-created {:owner-id user-id})
        scene (f/scene-created course)
        skill-id 3
        _ (f/scene-skills-created (:id scene) skill-id)
        retrieved (-> (f/get-scene-with-skills (:slug course))
                      :body
                      slurp
                      (json/read-str :key-fn keyword))]
    (is (= 1 (count retrieved)))
    (is (= 1 (count (-> retrieved (get 0) :skills))))
    (is (= skill-id (get-in retrieved [0 :skills 0 :id])))))

(def scene-data {
                 :actions {:act-1
                           {:type  "animation-sequence",
                            :audio "/raw/audio/english/l1/a1/vaca.m4a",
                            :data  [{:start 3.436, :end 6.436, :duration 3, :anim "talk"}],
                            },
                           :act-2
                           {:type "parallel",
                            :data
                                  [{:type  "animation-sequence",
                                    :audio "/raw/audio/english/l1/a1/vaca.m4a",
                                    :data  [{:start 3.436, :end 6.436, :duration 3, :anim "talk"}],
                                    },
                                   {:id "visible", :type "state", :target "box2"}
                                   {:type "sequence-data"
                                    :data [{:type  "animation-sequence",
                                            :audio "/raw/audio/english/l1/a1/vaca.m4a",
                                            :data  [{:start 3.436, :end 6.436, :duration 3, :anim "talk"}],
                                            }
                                           {:id "mari-voice-touch", :type "action"}]}
                                   ]},
                           }
                  :metadata {:autostart    true
                                     :prev         "map"
                                     :lip-not-sync true}
                 })


(def talking-animation ["ook"])

(deftest save-scene-with-audio-processing
  (mockery/with-mock mock
                     {:target :webchange.common.audio-parser/get-talking-animation
                      :return talking-animation}
                     (let [{course-id :course-id course-slug :course-slug name :name} (f/scene-created)
                           {user-id :id} (f/website-user-created)
                           _ (f/add-collaborator {:course-id course-id :user-id user-id})
                            scene-data (:data (-> (f/save-scene! course-slug name user-id {:scene scene-data})
                                      :body
                                      slurp
                                      (json/read-str :key-fn keyword)))]
                       (is (false? (get-in scene-data [:metadata :lip-not-sync])))
                       (is (= (get-in scene-data [:actions :act-1 :data]) talking-animation))
                       (is (= (get-in scene-data [:actions :act-2 :data 0 :data]) talking-animation))
                       (is (nil? (get-in scene-data [:actions :act-2 :data 1 :data])))
                       (is (= (get-in scene-data [:actions :act-2 :data 2 :data 0 :data]) talking-animation))
                       (is (nil? (get-in scene-data [:actions :act-2 :data 2 :data 1 :data]))))))

(deftest course-can-be-archived
  (let [{slug :slug} (f/course-created {:type "book" :status "draft" :name "Draft book" :owner-id website-user-id})
        saved-value (-> (f/archive-course! slug website-user-id) :body slurp (json/read-str :key-fn keyword))
        retrieved-value (->  slug f/get-course-info :body slurp (json/read-str :key-fn keyword))]
    (is (= "archived" (:status saved-value)))
    (is (= "archived" (:status retrieved-value)))))

(deftest schools-available-courses-can-be-retrieved
  (let [course-name "available course"
        _ (f/course-created {:name course-name :status "published"})
        response (f/get-school-available-courses)
        courses (-> response :body slurp (json/read-str :key-fn keyword))]
    (is (= 200 (:status response)))
    (is (= 1 (count courses)))
    (is (= course-name (-> courses first :name)))))

(deftest schools-courses
  (with-redefs [webchange.auth.roles/is-admin? (fn [user-id] true)]
    (let [{course-id :id} (f/course-created {:name "test course" :status "published"})]
      (testing "course can be assigned to school"
        (let [response (f/assign-school-course! f/default-school-id {:course-id course-id})
              school-course (-> response :body slurp (json/read-str :key-fn keyword))]
          (is (= 200 (:status response)))
          (is (= course-id (:course-id school-course)))))
      (testing "school courses can be retrieved"
        (let [response (f/get-school-courses f/default-school-id)
              courses (-> response :body slurp (json/read-str :key-fn keyword))]
          (is (= 200 (:status response)))
          (is (= 1 (count courses)))
          (is (= course-id (-> courses first :id))))))))
