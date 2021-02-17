(ns webchange.course.core
  (:require [webchange.db.core :refer [*db*] :as db]
            [java-time :as jt]
            [clojure.tools.logging :as log]
            [webchange.assets.core :as assets]
            [webchange.common.files :as f]
            [clojure.data.json :as json]
            [webchange.scene :as scene]
            [config.core :refer [env]]
            [clojure.string :as string]
            [camel-snake-kebab.extras :refer [transform-keys]]
            [camel-snake-kebab.core :refer [->snake_case_keyword ->kebab-case-keyword ->kebab-case]]
            [webchange.course.skills :refer [skills]]
            [ring.util.codec :as codec]))

(def editor_asset_type_single-background "single-background")
(def editor_asset_type_background "background")
(def editor_asset_type_surface "surface")
(def editor_asset_type_decoration "decoration")

(def editor_asset_types [editor_asset_type_single-background editor_asset_type_background editor_asset_type_surface editor_asset_type_decoration])

(def hardcoded (env :hardcoded-courses {}))

(defn collaborator?
  [user-id {course-id :id owner-id :owner-id}]
  (let [owner? (= owner-id user-id)
        {collaborator? :result} (db/is-collaborator? {:course_id course-id :user_id user-id})]
    (or owner? collaborator?)))

(defn collaborator-by-course-slug?
  [user-id course-slug]
  (let [course (db/get-course {:slug course-slug})]
    (collaborator? user-id course)))

(defn collaborator-by-course-id?
  [user-id course-id]
  (let [course (db/get-course-by-id {:id course-id})]
    (collaborator? user-id course)))

(defn collaborator-by-course-version?
  [user-id course-version]
  (let [{course-id :course-id} (db/get-course-version {:id course-version})
        course (db/get-course-by-id {:id course-id})]
    (collaborator? user-id course)))

(defn collaborator-by-scene-version?
  [user-id scene-version]
  (let [{scene-id :scene-id} (db/get-scene-version {:id scene-version})
        {course-id :course-id} (db/get-scene-by-id {:id scene-id})
        course (db/get-course-by-id {:id course-id})]
    (collaborator? user-id course)))

(defn get-course-templates
  [course-slug]
  (if (contains? hardcoded course-slug)
    (scene/get-templates course-slug)
    (scene/get-templates course-slug)                       ;; "Getting templates for not-hardcoded courses is not implemented"
    ))

(defn get-course-info
  [course-slug]
  (db/get-course {:slug course-slug}))

(defn save-course-info!
  [id {:keys [name slug lang image-src]}]
  (db/save-course-info! {:id        id
                         :name      name
                         :slug      slug
                         :lang      lang
                         :image_src image-src})
  [true {:id id}])

(defn get-course-latest-version
  [course-slug]
  (let [{course-id :id} (db/get-course {:slug course-slug})
        latest-version (db/get-latest-course-version {:course_id course-id})]
    (:data latest-version)))

(defn get-course-data
  [course-slug]
  (let [course (if (contains? hardcoded course-slug)
                 (scene/get-course course-slug)
                 (get-course-latest-version course-slug))
        templates (get-course-templates course-slug)]
    (merge course {:templates templates})))

(defn- get-scene-skills
  [scene-id]
  (->> (db/get-scene-skills-by-scene {:scene_id scene-id})
       (map (fn [{:keys [skill-id]}]
              (some (fn [skill]
                      (and (= (:id skill) skill-id) skill))
                    skills)))))

(defn get-scene-latest-version
  [course-slug scene-name]
  (let [{course-id :id} (db/get-course {:slug course-slug})
        {scene-id :id} (db/get-scene {:course_id course-id :name scene-name})
        latest-version (db/get-latest-scene-version {:scene_id scene-id})
        scene-skills (get-scene-skills scene-id)]
    (when latest-version
      (merge (:data latest-version)
             {:skills scene-skills}))))

(defn get-scene-data
  [course-slug scene-name]
  (if (contains? hardcoded course-slug)
    (scene/get-scene course-slug scene-name)
    (get-scene-latest-version course-slug scene-name)))

(defn get-or-create-scene! [course-id scene-name]
  (if-let [{scene-id :id} (db/get-scene {:course_id course-id :name scene-name})]
    scene-id
    (let [[{scene-id :id}] (db/create-scene! {:course_id course-id :name scene-name})]
      scene-id)))

(defn save-scene!
  [course-slug scene-name scene-data owner-id]
  (let [{course-id :id} (db/get-course {:slug course-slug})
        scene-id (get-or-create-scene! course-id scene-name)
        created-at (jt/local-date-time)
        scene-data-old (:data (db/get-latest-scene-version {:scene_id scene-id}))]
    (if (not (identical? scene-data-old scene-data))
      (db/save-scene! {:scene_id   scene-id
                       :data       scene-data
                       :owner_id   owner-id
                       :created_at created-at}))
    [true {:id         scene-id
           :name       scene-name
           :course-slug course-slug
           :created-at (str created-at)}]))

(defn- reset-scene-skills!
  [scene-id skills]
  (db/delete-scene-skills! {:scene_id scene-id})
  (doall (map #(db/create-scene-skill! {:scene_id scene-id :skill_id %}) skills)))

(defn update-scene!
  [course-slug scene-name scene-data owner-id]
  (let [{course-id :id} (db/get-course {:slug course-slug})
        scene-id (get-or-create-scene! course-id scene-name)
        created-at (jt/local-date-time)
        scene-data-old (:data (db/get-latest-scene-version {:scene_id scene-id}))
        new-scene-data (merge scene-data-old scene-data)]
    (if (not (identical? scene-data-old new-scene-data))
      (db/save-scene! {:scene_id   scene-id
                       :data       new-scene-data
                       :owner_id   owner-id
                       :created_at created-at}))
    [true {:id         scene-id
           :name       scene-name
           :course-slug course-slug
           :created-at (str created-at)
           :data       new-scene-data}]))

(defn update-scene-skills!
  [course-slug scene-name skills _]
  (let [{course-id :id} (db/get-course {:slug course-slug})
        scene-id (get-or-create-scene! course-id scene-name)]
    (reset-scene-skills! scene-id skills)
    [true {:scene  scene-name
           :skills (get-scene-skills scene-id)}]))

(defn save-course!
  [course-slug data owner-id]
  (let [{course-id :id} (db/get-course {:slug course-slug})
        created-at (jt/local-date-time)]
    (db/save-course! {:course_id  course-id
                      :data       data
                      :owner_id   owner-id
                      :created_at created-at})
    [true {:created-at (str created-at)
           :data       data}]))

(defn restore-course-version!
  [version-id owner-id]
  (let [{data :data course-id :course-id} (db/get-course-version {:id version-id})
        created-at (jt/local-date-time)]
    (db/save-course! {:course_id  course-id
                      :data       data
                      :owner_id   owner-id
                      :created_at created-at})
    [true {:created-at (str created-at)}]))

(defn restore-scene-version!
  [version-id owner-id]
  (let [{data :data scene-id :scene-id} (db/get-scene-version {:id version-id})
        created-at (jt/local-date-time)]
    (db/save-scene! {:scene_id   scene-id
                     :data       data
                     :owner_id   owner-id
                     :created_at created-at})
    [true {:created-at (str created-at)}]))

(defn get-course-versions
  [course-slug]
  (let [{course-id :id} (db/get-course {:slug course-slug})
        versions (db/get-course-versions {:course_id course-id})]
    {:versions (->> versions
                    (map #(dissoc % :data))
                    (map #(assoc % :created-at (-> % :created-at str)))
                    (map #(assoc % :owner-name "todo")))}))

(defn get-scene-versions
  [course-slug scene-name]
  (let [{course-id :id} (db/get-course {:slug course-slug})
        {scene-id :id} (db/get-scene {:course_id course-id :name scene-name})
        versions (db/get-scene-versions {:scene_id scene-id})]
    {:versions (->> versions
                    (map #(dissoc % :data))
                    (map #(assoc % :created-at (-> % :created-at str)))
                    (map #(assoc % :owner-name "todo")))}))

(defn- with-course-page
  [{slug :slug :as course}]
  (let [url (str "/courses/" slug "/editor-v2")]
    (assoc course :url url)))

(defn- with-default-image
  [{image-src :image-src :as course}]
  (if image-src
    course
    (assoc course :image-src "/images/default-course.jpg")))

(def hostname (get env :platform-host "webchange.local"))

(defn- with-host-name
  [course field]
  (let [original (get course field)
        value (str "https://" hostname original)]
    (assoc course field value)))

(defn- ->website-course
  [course]
  (-> (select-keys course [:id :name :language :slug :image-src :lang :level :subject])
      (assoc :slug (-> course :slug (codec/url-encode)))
      (with-course-page)
      (with-default-image)
      (with-host-name :image-src)))

(defn- ->website-scene
  [scene]
  (-> scene
      (assoc :course-slug (-> scene :course-slug (codec/url-encode)))
      (assoc :scene-slug (-> scene :scene-slug (codec/url-encode)))))

(defn- rand-str [len]
  (apply str (take len (repeatedly #(char (+ (rand 26) 65))))))

(defn- slug
  [text]
  (-> (->kebab-case text)
      (string/replace #"[_~.<>{}()!№%:,;#$%^&*+=]" "")
      (clojure.string/lower-case)))

(defn- course-slug
  [original lang]
  (let [suffix (rand-str 8)]
    (-> (str original "-" lang "-" suffix)
        (slug))))

(defn- scene-slug
  [original]
  (slug original))

(defn localize
  [course-id {:keys [lang owner-id website-user-id]}]
  (let [current-time (jt/local-date-time)
        {course-name :name image :image-src type :type} (db/get-course-by-id {:id course-id})
        localized-course-data {:name            course-name
                               :slug            (course-slug course-name lang)
                               :lang            lang
                               :owner_id        owner-id
                               :image_src       image
                               :website_user_id website-user-id
                               :status          "draft"
                               :type            type}
        [{new-course-id :id}] (db/create-course! localized-course-data)
        course-data (:data (db/get-latest-course-version {:course_id course-id}))
        scenes (->> course-data
                    :scene-list
                    keys
                    (map name))]
    (db/save-course! {:course_id  new-course-id
                      :data       course-data
                      :owner_id   owner-id
                      :created_at current-time})
    (doseq [scene-name scenes]
      (let [{scene-id :id} (db/get-scene {:course_id course-id :name scene-name})
            scene-data (:data (db/get-latest-scene-version {:scene_id scene-id}))
            scene-id (get-or-create-scene! new-course-id scene-name)]
        (db/save-scene! {:scene_id   scene-id
                         :data       scene-data
                         :owner_id   owner-id
                         :created_at current-time})))
    [true (-> (transform-keys ->kebab-case-keyword localized-course-data)
              (assoc :id new-course-id)
              (->website-course))]))

(defn get-available-courses
  []
  (->> (db/get-courses-by-status-and-type {:type "course" :status "published"})
       (map ->website-course)))

(defn get-courses-by-website-user
  [website-user-id]
  (->> (db/get-courses-by-website-user {:website_user_id website-user-id :type "course"})
       (map ->website-course)))

(defn get-book-library
  []
  (->> (db/get-courses-by-status-and-type {:type "book" :status "published"})
       (map ->website-course)))

(defn get-books-by-website-user
  [website-user-id]
  (->> (db/get-courses-by-website-user {:website_user_id website-user-id :type "book"})
       (map ->website-course)))

(defn read-character-data [dir]
  (let [filename (str dir "/skeleton.json")
        data (json/read-str (slurp filename) :key-fn keyword)]
    (as-> {} character-data
          (assoc character-data :name (last (string/split dir #"/")))
          (assoc character-data :width (get-in data [:skeleton :width]))
          (assoc character-data :height (get-in data [:skeleton :height]))
          (if (vector? (:skins data))
            (assoc character-data :skins (vec (map #(:name %) (:skins data))))
            (assoc character-data :skins (vec (map #(name (get % 0)) (vec (:skins data))))))
          (assoc character-data :animations (vec (map #(name (get % 0)) (vec (:animations data))))))))

(defn find-all-character-skins []
  (let [character-skins (db/find-character-skins)]
    (map #(:data %) character-skins)))

(defn read-character-skins [config]
  (let [dir (or (:public-dir config) (:public-dir env))
        anim-path (f/relative->absolute-path "/raw/anim" {:public-dir dir})]
    (->> anim-path
         clojure.java.io/file
         file-seq
         (filter #(.isDirectory %))
         (filter #(.isFile (clojure.java.io/file (str % "/skeleton.json"))))
         (map #(read-character-data (str %))))))

(defn update-character-skins [config]
  (let [character-skins (read-character-skins config)
        _ (db/clear-character-skins!)]
    (doseq [skin character-skins]
      (db/create-character-skins! {:name (:name skin) :data skin}))))

(defn editor-assets [tag type]
  (let [assets (db/find-editor-assets {:tag tag :type type})]
    assets))

(defn find-all-tags []
  (db/find-all-tags))

(defn store-tag! [tag]
  (db/create-asset-tags! {:name tag})
  (db/find-editor-tag-by-name {:name tag}))

(defn store-editor-assets! [path thumbnail type]
  (db/create-or-update-editor-assets! {:path path :thumbnail_path thumbnail :type type})
  (db/find-editor-assets-by-path {:path path}))

(defn remove-first-directory [string begin]
  (let [string (clojure.string/replace-first string (re-pattern (str "^" begin)) "")
        string (clojure.string/replace-first string #"^/" "")]
    string))

(defn store-editor-asset [source-path target-path public-dir file]
  (let [parts (-> file java.io.File. .getName
                  (clojure.string/split #"\.")
                  first
                  (clojure.string/split #"_"))
        type (last parts)
        tag (->> (drop-last parts)
                 (clojure.string/join " ")
                 (clojure.string/trim)
                 (clojure.string/capitalize))
        path-in-dir (remove-first-directory file source-path)
        thumbnail-file (str target-path "/" path-in-dir)
        file-public (str "/" (remove-first-directory file public-dir))
        thumbnail-file-public (str "/" (remove-first-directory thumbnail-file public-dir))]
    (when (.contains editor_asset_types type)
      (clojure.java.io/make-parents thumbnail-file)
      (assets/make-thumbnail file thumbnail-file 180)
      (let [created-tag (store-tag! tag)
            created-editor-assets (store-editor-assets! file-public thumbnail-file-public type)]
        (db/create-editor-asset-tag! {:tag_id (:id created-tag) :asset_id (:id created-editor-assets)})))))

(defn clear-before-update []
  (do
    (db/truncate-editor-assets-tags!)
    (db/truncate-editor-assets!)
    (db/truncate-editor-tags!)))

(defn update-editor-assets
  ([config] (update-editor-assets config "clear" "clipart" "clipart-thumbs"))
  ([config clear] (update-editor-assets config clear "clipart" "clipart-thumbs"))
  ([config clear source] (update-editor-assets config clear source "clipart-thumbs"))
  ([config clear source target]
   (let [source-path (f/relative->absolute-path (str "/raw/" source) config)
         target-path (f/relative->absolute-path (str "/raw/" target) config)]
     (when (= clear "clear") (clear-before-update))
     (doseq [file (filter (fn [f] (. f isFile)) (assets/files source-path))]
       (store-editor-asset source-path target-path (config :public-dir) (. file getPath))))))

(defn create-course
  [{:keys [name lang] :as data} owner-id]
  (let [current-time (jt/local-date-time)
        user (db/get-user {:id owner-id})
        website-id (:website-id user)
        defaults {:type      "course"
                  :image_src nil}
        new-course-data (merge defaults
                               data
                               {:slug            (course-slug name lang)
                                :owner_id        owner-id
                                :website_user_id website-id
                                :status          "draft"})
        [{new-course-id :id}] (db/create-course! new-course-data)
        course-data {:initial-scene    nil
                     :navigation-mode  :activity
                     :scene-list       {}
                     :default-progress {}
                     :levels [{:level 1 :name "Level 1" :scheme {:lesson {:name "Lesson" :lesson-sets []}}
                               :lessons [{:lesson 1
                                          :name "Lesson 1"
                                          :type :lesson
                                          :activities []}]}]}]
    (db/save-course! {:course_id  new-course-id
                      :data       course-data
                      :owner_id   owner-id
                      :created_at current-time})
    [true (-> (transform-keys ->kebab-case-keyword new-course-data)
              (assoc :id new-course-id)
              (->website-course))]))

(defn default-lesson-name
  [lesson-set-name]
  (str lesson-set-name "-l1-ls1"))

(defn- create-default-lesson!
  [course-id lesson-sets]
  (let [items (db/get-course-items {:course_id course-id})
        dataset-id (-> items first :dataset-id)
        item-ids (map #(select-keys % [:id]) items)]
    (doall (for [lesson-name lesson-sets]
             (db/create-lesson-set! {:name (default-lesson-name lesson-name) :dataset_id dataset-id :data {:items item-ids}})))))

(defn- save-scene-on-create!
  [course-id scene-slug scene-data owner-id]
  (let [created-at (jt/local-date-time)
        [{scene-id :id}] (db/create-scene! {:course_id course-id :name scene-slug})]
    (db/save-scene! {:scene_id   scene-id
                     :data       scene-data
                     :owner_id   owner-id
                     :created_at created-at})
    {:scene-id scene-id}))

(defn- name-in-list? [{name :name} names]
  (let [s (into #{} names)]
    (contains? s name)))

(defn- add-field-scene
  [field scene-slug]
  (if (nil? (:scenes field))
    (assoc field :scenes [scene-slug])
    (update field :scenes conj scene-slug)))

(defn- merge-fields
  [original fields scene-slug]
  (let [field-names (->> fields
                         (map :name)
                         (into #{}))
        existing-fields (->> original
                             (filter #(contains? field-names (:name %)))
                             (map #(add-field-scene % scene-slug)))
        original-names (->> original
                            (map :name)
                            (into #{}))
        new-fields (->> fields
                       (remove #(contains? original-names (:name %)))
                       (map #(add-field-scene % scene-slug)))]
    (->> original
         (remove #(contains? field-names %))
         (concat existing-fields new-fields)
         (sort-by :name)
         (into []))))

(defn save-dataset-on-create!
  [course-id scene-slug {:keys [fields lesson-sets]}]
  (let [course-lessons (db/get-course-lessons {:course_id course-id})]
    (when (empty? course-lessons)
      (create-default-lesson! course-id lesson-sets)))
  (let [datasets (db/get-datasets-by-course {:course_id course-id})]
    (doall (for [{:keys [id scheme]} datasets]
             (db/update-dataset! {:id     id
                                  :scheme (-> scheme
                                              (update :fields #(merge-fields % fields scene-slug)))})))))

(defn- merge-lesson-sets
  [original new]
  (->> original
       (concat new)
       (distinct)
       (into [])))

(defn- save-course-on-create!
  [course-id scene-slug {:keys [lesson-sets]} scene-name owner-id]
  (let [created-at (jt/local-date-time)
        {course-data :data} (db/get-latest-course-version {:course_id course-id})
        lesson-required? (empty? (get-in course-data [:levels 0 :scheme :lesson :lesson-sets]))
        lesson-sets-scheme (->> lesson-sets (map keyword) (into []))
        lesson-sets-lessons (->> lesson-sets (map (juxt keyword default-lesson-name)) (into {}))
        initial-scene-required? (nil? (get course-data :initial-scene))
        data (cond-> course-data
                     :always (assoc-in [:scene-list (keyword scene-slug)] {:name scene-name})
                     :always (update-in [:levels 0 :lessons 0 :activities] conj {:activity scene-slug :time-expected 300})
                     :always (update-in [:levels 0 :scheme :lesson :lesson-sets] merge-lesson-sets lesson-sets-scheme)
                     lesson-required? (assoc-in [:levels 0 :lessons 0 :lesson-sets] lesson-sets-lessons)
                     initial-scene-required? (assoc :initial-scene scene-slug))]
    (db/save-course! {:course_id  course-id
                      :data       data
                      :owner_id   owner-id
                      :created_at created-at})))

(defn create-scene!
  [scene-data metadata course-slug scene-name skills owner-id]
  (let [{course-id :id} (db/get-course {:slug course-slug})
        scene-slug (scene-slug scene-name)
        {scene-id :scene-id} (save-scene-on-create! course-id scene-slug scene-data owner-id)]
    (reset-scene-skills! scene-id skills)
    (save-dataset-on-create! course-id scene-slug metadata)
    (save-course-on-create! course-id scene-slug metadata scene-name owner-id)
    [true (->website-scene {:id          scene-id
                            :name        scene-name
                            :scene-slug  scene-slug
                            :course-slug course-slug})]))

(defn create-activity-placeholder!
  [course-slug scene-name]
  (let [{course-id :id} (db/get-course {:slug course-slug})
        scene-slug (scene-slug scene-name)
        [{scene-id :id}] (db/create-scene! {:course_id course-id :name scene-slug})]
    [true (->website-scene {:id          scene-id
                            :name        scene-name
                            :scene-slug  scene-slug
                            :course-slug course-slug})]))

(defn- add-activity-lesson-sets!
  [course-id scene-slug {:keys [lesson-sets]} owner-id]
  (let [created-at (jt/local-date-time)
        {course-data :data} (db/get-latest-course-version {:course_id course-id})]
    (db/save-course! {:course_id  course-id
                      :data       (assoc-in course-data [:scene-list (keyword scene-slug) :lesson-sets] lesson-sets)
                      :owner_id   owner-id
                      :created_at created-at})))

(defn create-activity-version!
  [scene-data metadata course-slug scene-slug owner-id]
  (let [{course-id :id} (db/get-course {:slug course-slug})
        {scene-id :id} (db/get-scene {:course_id course-id :name scene-slug})
        {course-data :data} (db/get-latest-course-version {:course_id course-id})
        created-at (jt/local-date-time)]
    (db/save-scene! {:scene_id   scene-id
                     :data       scene-data
                     :owner_id   owner-id
                     :created_at created-at})
    (save-dataset-on-create! course-id scene-slug metadata)
    (add-activity-lesson-sets! course-id scene-slug metadata owner-id)
    [true {:id          scene-id
           :name        (get-in course-data [:scene-list (keyword scene-slug) :name])
           :scene-slug  scene-slug
           :course-slug course-slug}]))

(defn- is-placeholder
  [scene-id]
  (let [latest-version (db/get-latest-scene-version {:scene_id scene-id})]
    (-> latest-version
        boolean
        not)))

(defn get-course-scene-skills
  [course-slug]
  (let [{course-id :id} (db/get-course {:slug course-slug})
        scenes (db/get-scenes-by-course-id {:course_id course-id})
        with-skills (fn [scene] (assoc scene :skills (get-scene-skills (:id scene))))
        with-is-placeholder (fn [scene] (assoc scene :is-placeholder (is-placeholder (:id scene))))]
    (->> scenes
         (map with-skills)
         (map with-is-placeholder))))
