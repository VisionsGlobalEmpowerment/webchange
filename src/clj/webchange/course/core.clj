(ns webchange.course.core
  (:require
    [camel-snake-kebab.core :refer [->kebab-case ->kebab-case-keyword
                                    ->snake_case_keyword]]
    [camel-snake-kebab.extras :refer [transform-keys]]
    [clojure.data.json :as json]
    [clojure.set :as cset]
    [clojure.string :as string]
    [clojure.tools.logging :as log]
    [config.core :refer [env]]
    [java-time :as jt]
    [ring.util.codec :as codec]
    [webchange.assets.core :as assets]
    [webchange.common.audio-parser :as ap]
    [webchange.common.files :as f]
    [webchange.course.skills :refer [skills]]
    [webchange.db.core :as db]
    [webchange.events :as e]
    [webchange.scene :as scene]
    [webchange.templates.core :as templates]
    [webchange.utils.preserve-objects :refer [update-preserved-objects]]))

(def editor_asset_type_single-background "single-background")
(def editor_asset_type_background "background")
(def editor_asset_type_surface "surface")
(def editor_asset_type_decoration "decoration")
(def editor_asset_type_etc "etc")

(def editor_asset_types [editor_asset_type_single-background
                         editor_asset_type_background
                         editor_asset_type_surface
                         editor_asset_type_decoration
                         editor_asset_type_etc])

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
  [id {:keys [name slug lang image-src metadata]}]
  (let [data {:id        id
              :name      name
              :slug      slug
              :lang      lang
              :image_src image-src
              :metadata  metadata}
        scenes (db/scene-list {:course_id id})]
    (when (:locked metadata)
      (doseq [scene scenes]
        (db/edit-scene! (assoc-in scene [:metadata :locked] true))))
    (db/save-course-info! data)
    [true (transform-keys ->kebab-case-keyword data)]))

(defn get-course-latest-version
  [course-slug]
  (let [{course-id :id} (db/get-course {:slug course-slug})
        latest-version (db/get-latest-course-version {:course_id course-id})]
    (:data latest-version)))

(defn- ->scene-list-item
  [{:keys [id name image-src]}]
  {:id      id
   :name    name
   :preview image-src})

(defn get-course-data
  [course-slug]
  (let [{course-id :id} (db/get-course {:slug course-slug})
        course (if (contains? hardcoded course-slug)
                 (scene/get-course course-slug)
                 (get-course-latest-version course-slug))
        scene-list (->> (db/scene-list {:course_id course-id})
                        (map ->scene-list-item)
                        (map (juxt :id identity))
                        (into {}))]
    (merge course {:scene-list scene-list})))

(defn- get-scene-skills
  [scene-id]
  (->> (db/get-scene-skills-by-scene {:scene_id scene-id})
       (map (fn [{:keys [skill-id]}]
              (some (fn [skill]
                      (and (= (:id skill) skill-id) skill))
                    skills)))))

(defn scene-slug->id
  [course-slug scene-name]
  (let [{course-id :id} (db/get-course {:slug course-slug})
        {scene-id :id} (db/get-course-scene {:course_id course-id :slug scene-name})]
    {:course-id course-id :scene-id scene-id}))

(defn get-scene-latest-version
  [course-slug scene-name]
  (let [{:keys [scene-id]} (scene-slug->id course-slug scene-name)]
    (-> (db/get-latest-scene-version {:scene_id scene-id})
        :data)))

(defn get-scene-data
  [course-slug scene-name]
  (if (contains? hardcoded course-slug)
    (scene/get-scene course-slug scene-name)
    (let [{:keys [scene-id]} (scene-slug->id course-slug scene-name)
          latest-version (db/get-latest-scene-version {:scene_id scene-id})
          scene-skills (get-scene-skills scene-id)]
      (when latest-version
        (merge (:data latest-version)
               {:skills scene-skills})))))

(defn first-activity
  [course-slug]
  (let [{course-id :id} (db/get-course {:slug course-slug})
        {scene-slug :name} (-> (db/get-scenes-by-course-id {:course_id course-id}) first)]
    {:course-slug course-slug
     :scene-slug  scene-slug}))

(defn get-first-scene-data
  [course-slug]
  (let [{scene-slug :scene-slug} (first-activity course-slug)]
    (-> (get-scene-data course-slug scene-slug)
        (assoc :scene-slug scene-slug))))

(defn get-or-create-scene! [course-id scene-name]
  (if-let [{scene-id :id} (db/get-course-scene {:course_id course-id :slug scene-name})]
    scene-id
    (let [[{scene-id :id}] (db/create-scene! {:course_id course-id :name scene-name})]
      scene-id)))

(defn save-scene!
  [course-slug scene-name scene-data owner-id & options]
  (let [{:keys [description] :or {description "Save"}} options
        {course-id :id} (db/get-course {:slug course-slug})
        scene-id (get-or-create-scene! course-id scene-name)
        created-at (jt/local-date-time)
        scene-data-old (:data (db/get-latest-scene-version {:scene_id scene-id}))]
    (if (not (identical? scene-data-old scene-data))
      (db/save-scene! {:scene_id    scene-id
                       :data        scene-data
                       :owner_id    owner-id
                       :created_at  created-at
                       :description description}))
    [true {:id          scene-id
           :name        scene-name
           :course-slug course-slug
           :scene-id    scene-name
           :data        scene-data}]))

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
      (db/save-scene! {:scene_id    scene-id
                       :data        new-scene-data
                       :owner_id    owner-id
                       :created_at  created-at
                       :description "Update"}))
    [true {:id          scene-id
           :name        scene-name
           :course-slug course-slug
           :created-at  (str created-at)
           :data        new-scene-data}]))

(defn update-scene-skills!
  [course-slug scene-name skills _]
  (let [{course-id :id} (db/get-course {:slug course-slug})
        scene-id (get-or-create-scene! course-id scene-name)]
    (reset-scene-skills! scene-id skills)
    [true {:scene  scene-name
           :skills (get-scene-skills scene-id)}]))

(defn- update-course-scenes!
  [course-id data]
  (let [received-scene-ids (->> data :levels
                                (mapcat :lessons)
                                (mapcat :activities)
                                (map :scene-id)
                                (set))
        existing-scene-ids (->> (db/course-scenes {:course_id course-id})
                                (map :scene-id)
                                (set))
        new-course-scenes (->> (cset/difference received-scene-ids existing-scene-ids)
                               (remove nil?)
                               (map (fn [id] [course-id id])))
        obsolete-scene-ids (-> (cset/difference existing-scene-ids received-scene-ids)
                               (into []))]
    (when (seq new-course-scenes)
      (db/insert-course-scenes {:course_scenes new-course-scenes}))
    (when (seq obsolete-scene-ids)
      (db/delete-course-scenes {:course_id course-id :scene_ids obsolete-scene-ids}))))

(defn save-course!
  [course-slug data owner-id]
  (let [{course-id :id} (db/get-course {:slug course-slug})
        created-at (jt/local-date-time)]
    (db/save-course! {:course_id  course-id
                      :data       data
                      :owner_id   owner-id
                      :created_at created-at})
    (update-course-scenes! course-id data)
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
        {name :name} (db/get-scene-by-id {:id scene-id})
        created-at (jt/local-date-time)]
    (db/save-scene! {:scene_id    scene-id
                     :data        data
                     :owner_id    owner-id
                     :created_at  created-at
                     :description "Restore"})
    [true {:name       name
           :data       data
           :created-at (str created-at)}]))

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
  (let [{course-id :course-id scene-id :scene-id} (scene-slug->id course-slug scene-name)
        versions (db/get-scene-versions {:scene_id scene-id})]
    {:versions (->> versions
                    (map #(dissoc % :data))
                    (map #(assoc % :created-at (-> % :created-at str)))
                    (map #(assoc % :owner-name "todo")))}))

(defn- with-course-page
  [{slug :slug :as course}]
  (let [url (str "/courses/" slug "/edit")]
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
  ([course]
   (->website-course course {}))
  ([course {:keys [with-host-name?] :or {with-host-name? true}}]
   (cond-> (-> (select-keys course [:id :name :slug :lang :image-src :status :metadata :owner-id])
               (assoc :updated-at (-> course :updated-at (str)))
               (with-course-page)
               (with-default-image))
           (-> course :slug string?) (assoc :slug (-> course :slug (codec/url-encode)))
           with-host-name? (with-host-name :image-src))))

(defn- ->website-scene
  [scene]
  (-> scene
      (assoc :course-slug (-> scene :course-slug (codec/url-encode)))
      (assoc :scene-slug (-> scene :scene-slug (codec/url-encode)))))

(defn- rand-str [len]
  (apply str (take len (repeatedly #(char (+ (rand 26) 65))))))

(defn slug
  [text]
  (-> (->kebab-case text)
      (string/replace #"[_~.<>{}()!№%:,;#$%^&*+='’`\"/?'\\@]" "")
      (clojure.string/lower-case)))

(defn course-slug
  [original lang]
  (let [suffix (rand-str 8)]
    (-> (str original "-" lang "-" suffix)
        (slug))))

(defn- scene-slug
  [original]
  (slug original))

(defn get-available-courses
  []
  (->> (db/get-available-courses)
       (map ->website-course)))

(defn my-courses
  [user-id]
  (->> (db/find-courses {:type "course" :not_status "archived" :user_id user-id})
       (map ->website-course)))

(defn my-courses-admin
  [user-id]
  (->> (db/find-courses {:type "course" :not_status "archived"})
       (filter (fn [{:keys [status owner-id]}] (or (not= status "published") (= owner-id user-id))))
       (map ->website-course)))

(defn get-courses-by-website-user
  [website-user-id]
  (->> (db/get-courses-by-website-user {:website_user_id website-user-id :type "course"})
       (map ->website-course)))

(defn get-books-by-website-user
  [website-user-id]
  (->> (db/get-courses-by-website-user {:website_user_id website-user-id :type "book"})
       (map ->website-course)))

(defn read-character-data [character-dir public-dir]
  (let [character-name (-> character-dir .getName)
        character-skeleton (str character-dir "/skeleton.json")
        data (json/read-str (slurp character-skeleton) :key-fn keyword)
        atlas-data (-> character-dir (str "/skeleton.atlas") (slurp))
        preview-path (-> (str "/images/characters/" character-name "/character_preview.png")
                         (string/replace " " "_"))
        preview-file-path (f/relative->absolute-path preview-path public-dir)]
    (as-> {} character-data
          (assoc character-data :name (last (string/split (str character-dir) #"/")))
          (assoc character-data :width (get-in data [:skeleton :width]))
          (assoc character-data :height (get-in data [:skeleton :height]))
          (if (->> preview-file-path clojure.java.io/file .isFile)
            (assoc character-data :preview preview-path)
            character-data)
          (if (vector? (:skins data))
            (assoc character-data :skins (vec (map #(:name %) (:skins data))))
            (assoc character-data :skins (vec (map #(name (get % 0)) (vec (:skins data))))))
          (assoc character-data :skins (map (fn [skin]
                                              (let [preview-path (-> (str "/images/characters/" character-name "/" skin ".png")
                                                                     (string/replace " " "_"))
                                                    preview-file-path (f/relative->absolute-path preview-path public-dir)]
                                                (cond-> {:name skin}
                                                        (->> preview-file-path clojure.java.io/file .isFile) (assoc :preview preview-path))))
                                            (:skins character-data)))
          (assoc character-data :animations (vec (map #(name (get % 0)) (vec (:animations data)))))
          (assoc character-data :resources (concat ["skeleton.json"
                                                    "skeleton.atlas"]
                                                   (->> (re-seq #"\n(skeleton\d*.png)" atlas-data) (map second)))))))

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
         (map #(read-character-data % {:public-dir dir})))))

(defn update-character-skins [config]
  (let [character-skins (read-character-skins config)
        _ (db/clear-character-skins!)]
    (doseq [skin character-skins]
      (db/create-character-skins! {:name (:name skin) :data skin}))))

(defn editor-assets [tag tags type]
  (if (seq tags)
    (let [assets (map (fn [tag]
                        (db/find-editor-assets {:tag tag :type type}))
                      tags)]
      (->> assets
           (map clojure.core/set)
           (apply cset/intersection)
           (into [])))
    (db/find-editor-assets {:tag tag :type type})))

(defn editor-assets-search
  [query tag type]
  (let [tags (db/find-editor-tags-by-query {:query (str query "%")})]
    (if (seq tags)
      (let [assets-idx (->> (db/find-editor-assets-by-tags {:tags (map :id tags) :tag tag :type type})
                            (map (juxt :id identity))
                            (into {}))
            assets-tags (->> (db/find-editor-tags-by-assets {:assets (map first assets-idx)})
                             (group-by :editor-asset-id))]
        (->> assets-tags
             (map (fn [[asset-id tags]]
                    (let [asset (get assets-idx asset-id)
                          tags (mapv #(dissoc % :editor-asset-id) tags)]
                      (assoc asset :tags tags))))))
      [])))

(defn find-all-tags []
  (db/find-all-tags))

(defn find-tags-by-name
  [tags]
  (->> tags
       (map #(db/find-editor-tag-by-name {:name %}))
       (remove nil?)))

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

;; Editor assets

(defn- get-asset-data
  [file-name]
  (if (re-matches #"([^-]+)--(([^-]+-?))+--([^-]+)" file-name)
    (let [[type tags tag-name] (clojure.string/split file-name #"--")
          type (clojure.string/replace type "_" "-")
          tags (->> (clojure.string/split tags #"-")
                    (map (fn [tag]
                           (-> tag
                               (clojure.string/replace "_" " ")
                               (clojure.string/trim)))))]
      {:tags (conj tags tag-name)
       :type type})
    (let [parts (clojure.string/split file-name #"_")
          type (last parts)
          tag (->> (drop-last parts)
                   (clojure.string/join " ")
                   (clojure.string/trim))]
      {:tags [tag]
       :type type})))

(defn store-editor-asset [source-path target-path public-dir file]
  (let [{:keys [tags type]} (get-asset-data (-> file java.io.File. .getName
                                                (clojure.string/split #"\.")
                                                first))
        path-in-dir (remove-first-directory file source-path)
        thumbnail-file (str target-path "/" path-in-dir)
        file-public (str "/" (remove-first-directory file public-dir))
        thumbnail-file-public (str "/" (remove-first-directory thumbnail-file public-dir))]
    (when (.contains editor_asset_types type)
      (clojure.java.io/make-parents thumbnail-file)
      (assets/make-thumbnail file thumbnail-file 180)
      (let [created-editor-assets (store-editor-assets! file-public thumbnail-file-public type)]
        (doseq [tag tags]
          (let [created-tag (store-tag! tag)]
            (db/create-editor-asset-tag! {:tag_id (:id created-tag) :asset_id (:id created-editor-assets)})))))))

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
                               (db/transform-keys-one-level ->snake_case_keyword data)
                               {:slug            (course-slug name lang)
                                :owner_id        owner-id
                                :website_user_id website-id
                                :status          "draft"})
        [{new-course-id :id}] (db/create-course! new-course-data)
        course-data {:navigation-mode  :activity
                     :default-progress {}
                     :levels           [{:lessons [{:activities []}]}]}]
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
    (db/save-scene! {:scene_id    scene-id
                     :data        scene-data
                     :owner_id    owner-id
                     :created_at  created-at
                     :description "Create"})
    (db/insert-course-scenes {:course_scenes [[course-id scene-id]]})
    {:scene-id scene-id}))

(defn- name-in-list? [{name :name} names]
  (let [s (into #{} names)]
    (contains? s name)))

(defn- add-field-scene
  [field scene-slug]
  (if (nil? (:scenes field))
    (assoc field :scenes [scene-slug])
    (-> field
        (update :scenes conj scene-slug)
        (update :scenes distinct)
        (update :scenes vec))))

(defn merge-fields
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
         (remove #(contains? field-names (:name %)))
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

(defn create-activity-placeholder!
  [course-slug scene-name]
  (let [{course-id :id} (db/get-course {:slug course-slug})
        scene-slug (scene-slug scene-name)
        [{scene-id :id}] (db/create-scene! {:course_id course-id :name scene-slug})]
    (db/insert-course-scenes {:course_scenes [[course-id scene-id]]})
    [true (->website-scene {:id          scene-id
                            :name        scene-name
                            :scene-slug  scene-slug
                            :course-slug course-slug})]))

(defn- add-activity-lesson-sets!
  [course-id scene-slug {:keys [lesson-sets]} owner-id]
  (let [created-at (jt/local-date-time)
        {course-data :data} (db/get-latest-course-version {:course_id course-id})]
    (db/save-course! {:course_id  course-id
                      :data       (assoc-in course-data [:scene-list (-> scene-slug codec/url-encode string/lower-case keyword) :lesson-sets] lesson-sets)
                      :owner_id   owner-id
                      :created_at created-at})))

(defn create-activity-version!
  [scene-data metadata course-slug scene-slug owner-id]
  (let [{course-id :id} (db/get-course {:slug course-slug})
        {scene-id :id} (db/get-scene {:course_id course-id :name scene-slug})
        {course-data :data} (db/get-latest-course-version {:course_id course-id})
        created-at (jt/local-date-time)]
    (db/save-scene! {:scene_id    scene-id
                     :data        scene-data
                     :owner_id    owner-id
                     :created_at  created-at
                     :description "Create"})
    (save-dataset-on-create! course-id scene-slug metadata)
    (add-activity-lesson-sets! course-id scene-slug metadata owner-id)
    [true {:id          scene-id
           :name        (get-in course-data [:scene-list (-> scene-slug codec/url-encode string/lower-case keyword) :name])
           :scene-slug  (codec/url-encode scene-slug)
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


(defn replace-anim-data [{:keys [audio start duration] :as action}]
  (let [animation (try
                    (ap/get-talking-animation audio start duration)
                    (catch Exception e nil))]
    (cond-> action
            animation (assoc :data (vec animation)))))

(defn process-action
  [action]
  (case (:type action)
    "animation-sequence" (replace-anim-data action)
    "parallel" (assoc action :data (vec (map (fn [value] (process-action value)) (:data action))))
    "sequence-data" (assoc action :data (vec (map (fn [value] (process-action value)) (:data action))))
    action))


(defn update-scene-lip-data
  [scene-data]
  (let [actions (into {} (map (fn [[key value]] [key (process-action value)]) (:actions scene-data)))]
    (-> scene-data
        (assoc :actions actions)
        (assoc-in [:metadata :lip-not-sync] false))))

(defn save-scene-with-processing
  [course-slug scene-name data owner-id]
  (let [scene-data (update-scene-lip-data data)]
    (save-scene! course-slug scene-name scene-data owner-id)))

(defn update-activity!
  [course-slug scene-slug data user-id]
  (let [scene-data (-> (get-scene-latest-version course-slug scene-slug)
                       (templates/update-activity-from-template data))]
    (save-scene! course-slug scene-slug scene-data user-id)))

(defn- dialog-names
  [{:keys [actions]}]
  (->> actions
       (filter (fn [[_ action]] (= "dialog" (:editor-type action))))
       (map first)))

(defn- preserve-actions
  [scene-data created-activity]
  (let [action-names (dialog-names created-activity)
        preserve-actions (-> scene-data
                             :actions
                             (select-keys action-names))]
    (->> preserve-actions
         (map (fn [[key action]] (let [preserved-action-keys [:available-activities :concept-var :tags :skippable :unique-tag :phrase-description]
                                       created-action (-> (get-in created-activity [:actions key])
                                                          (select-keys preserved-action-keys))]
                                   [key (merge action created-action)])))
         (into {}))))

(defn update-course-activity-template!
  [course-slug scene-slug user-id]
  (let [scene-data (get-scene-latest-version course-slug scene-slug)
        {:keys [created updated]} (-> scene-data templates/prepare-history (get-in [:metadata :history]))
        created-activity (as-> (templates/activity-from-template created) a
                               (reduce #(templates/update-activity-from-template %1 %2) a updated))
        original-assets (:assets scene-data)
        preserved-actions (preserve-actions scene-data created-activity)
        activity (-> created-activity
                     (update :actions merge preserved-actions)
                     (update-preserved-objects scene-data)
                     (update :assets #(->> (concat original-assets %)
                                           (flatten)
                                           (distinct))))]
    (save-scene! course-slug scene-slug activity user-id :description "Update template")))

(defn publish-course!
  [course-slug]
  (let [{course-id :id status :status} (db/get-course {:slug course-slug})
        can-publish? (contains? #{"draft" "changes-requested"} status)]
    (if can-publish?
      (do (db/update-course-status! {:id course-id :status "in-review"})
          [true (-> (get-course-info course-slug)
                    (->website-course))])
      [false {:message "invalid status"}])))

(defn archive-course!
  [course-slug course-id]
  (db/update-course-status! {:id course-id :status "archived"})
  [true (-> (get-course-info course-slug)
            (->website-course))])

(defn review-course!
  [course-id {new-status :status}]
  (let [{course-slug :slug status :status} (db/get-course-by-id {:id course-id})
        can-review? (contains? #{"in-review" "published"} status)
        new-status-valid? (contains? #{"published" "declined" "changes-requested" "draft"} new-status)]
    (if (and can-review? new-status-valid?)
      (do (db/update-course-status! {:id course-id :status new-status})
          [true (-> (get-course-info course-slug)
                    (->website-course))])
      [false {:message "invalid status"}])))

(defn get-on-review-courses
  [type status]
  (let [courses (db/get-courses-by-status-and-type {:type type :status status})]
    [true (map ->website-course courses)]))

(defn get-school-courses
  "Return list of courses assigned to school"
  [school-id user-id]
  (let [school (db/get-school {:id school-id})
        personal? #(= "personal" (:type %))]
    (if (personal? school)
      (let [published-courses (->> (db/get-available-courses)
                                   (map #(dissoc % :updated-at)))
            my-courses (db/find-courses {:type "course" :not_status "archived" :user_id user-id})]
        (->> (concat published-courses my-courses)
             (distinct)))
      (db/get-courses-by-school {:school_id school-id}))))

(defn assign-school-course
  [school-id {:keys [course-id]}]
  (db/assign-school-course! {:school_id school-id :course_id course-id})
  (e/dispatch {:type :courses/assigned-to-school :school-id school-id :course-id course-id})
  {:school-id school-id :course-id course-id})

(defn assign-school-courses
  [school-id {:keys [courses-id]}]
  (doseq [course-id courses-id]
    (db/assign-school-course! {:school_id school-id :course_id course-id}))
  (e/dispatch {:type :courses/assigned-to-school :school-id school-id :courses-id courses-id})
  {:school-id school-id :courses-id courses-id})

(defn unassign-school-course
  [school-id {:keys [course-id] :as props}]
  (db/unassign-school-course! {:school_id school-id :course_id course-id})
  (e/dispatch {:type :courses/unassigned-to-school :school-id school-id :course-id course-id})
  {:school-id school-id :course-id course-id})

(defn get-class-course
  "Return course assigned to given class"
  [class-id]
  (let [{course-id :course-id} (db/get-class {:id class-id})
        {course-name :name} (db/get-course-by-id {:id course-id})
        latest-version (db/get-latest-course-version {:course_id course-id})]
    {:id   course-id
     :name course-name
     :data (:data latest-version)}))

(defn- ->activity-info
  [{:keys [image-src] :as scene-data}]
  (-> scene-data
      (assoc :preview image-src)
      (dissoc :image-src)))

(defn get-available-activities
  [lang]
  (->> (db/find-scenes {:type "activity" :lang lang :not_status "archived"})
       (map ->activity-info)))

(defn get-available-books
  [lang]
  (->> (db/find-scenes {:type "book" :lang lang :not_status "archived"})
       (map ->activity-info)))

(defn get-visible-activities
  [lang]
  (->> (db/find-scenes {:type "activity" :status "visible" :lang lang})
       (map ->activity-info)))

(defn get-visible-books
  [lang]
  (->> (db/find-scenes {:type "book" :status "visible" :lang lang})
       (map ->activity-info)))

(defn my-activities
  [user-id lang]
  (->> (db/find-scenes {:type "activity" :lang lang :not_status "archived" :user_id user-id})
       (map ->activity-info)))

(defn my-activities-admin
  [user-id lang]
  (->> (db/find-scenes {:type "activity" :lang lang :not_status "archived"})
       (filter (fn [{:keys [status owner-id]}] (or (not= status "visible") (= owner-id user-id))))
       (map ->activity-info)))

(defn my-books
  [user-id lang]
  (->> (db/find-scenes {:type "book" :lang lang :not_status "archived" :user_id user-id})
       (map ->activity-info)))

(defn my-books-admin
  [user-id lang]
  (->> (db/find-scenes {:type "book" :lang lang :not_status "archived"})
       (filter (fn [{:keys [status owner-id]}] (or (not= status "visible") (= owner-id user-id))))
       (map ->activity-info)))

(defn get-activity-current-version
  [activity-id]
  (-> (db/get-latest-scene-version {:scene_id activity-id})
      :data))

(defn- get-activity-user
  [user-id]
  (-> (db/get-user {:id user-id})
      (select-keys [:first-name :last-name])))

(defn get-activity-versions
  [activity-id]
  (let [versions (db/get-scene-versions {:scene_id activity-id})]
    (->> versions
         (map #(dissoc % :data))
         (map #(assoc % :created-at (-> % :created-at str)))
         (map #(assoc % :owner (-> % :owner-id get-activity-user))))))

(defn get-activity
  [activity-id]
  (let [{:keys [course-id owner-id] :as activity-info} (-> (db/get-scene-by-id {:id activity-id})
                                                           ->activity-info)
        {course-slug :slug} (db/get-course-by-id {:id course-id})
        created-by-user (get-activity-user owner-id)
        updated-by-user (-> (db/get-latest-scene-version {:scene_id activity-id})
                            :owner-id
                            (get-activity-user))]
    (assoc activity-info
           :course-slug course-slug
           :created-by-user created-by-user
           :updated-by-user updated-by-user)))

(defn- get-activity-info
  [activity-id]
  (-> (db/get-scene-by-id {:id activity-id})
      ->activity-info))

(defn edit-activity
  [activity-id {:keys [metadata] :as data}]
  (let [current-metadata (-> (db/get-scene-by-id {:id activity-id}) :metadata)
        prepared-data (db/transform-keys-one-level ->snake_case_keyword data)
        updated-at (jt/local-date-time)]
    (db/edit-scene! (assoc prepared-data
                           :id activity-id
                           :updated_at updated-at
                           :metadata (merge current-metadata metadata)))
    (merge data {:id activity-id})))

(defn archive-activity
  [activity-id _]
  (db/update-scene-status! {:id     activity-id
                            :status "archived"})
  {:id     activity-id
   :status "archived"})

(defn toggle-activity-visibility
  [activity-id {:keys [visible]}]
  (let [status (if visible "visible" "invisible")]
    (db/update-scene-status! {:id     activity-id
                              :status status})
    {:id     activity-id
     :status status}))

(defn toggle-activity-locked
  [activity-id {:keys [locked]}]
  (let [data (-> (db/get-scene-by-id {:id activity-id})
                 (assoc-in [:metadata :locked] locked)
                 (select-keys [:id :name :lang :metadata]))]
    (db/edit-scene! data)
    data))

(defn duplicate-activity
  [activity-id {:keys [name lang status]} owner-id]
  (let [created-at (jt/local-date-time)
        updated-at (jt/local-date-time)
        source (db/get-scene-by-id {:id activity-id})
        [{scene-id :id}] (db/create-activity! {:name       name
                                               :lang       lang
                                               :image_src  (:image-src source)
                                               :status     (or status "invisible")
                                               :owner_id   owner-id
                                               :created_at created-at
                                               :updated_at updated-at
                                               :metadata   (-> source :metadata (dissoc :locked))
                                               :type       (:type source)})
        source-data (get-activity-current-version activity-id)]
    (db/save-scene! {:scene_id    scene-id
                     :data        source-data
                     :owner_id    owner-id
                     :created_at  created-at
                     :description "Create"})
    {:id scene-id}))

(defn toggle-course-visibility
  [course-slug {:keys [visible]}]
  (let [status (if visible "published" "draft")
        {course-id :id} (db/get-course {:slug course-slug})]
    (db/update-course-status! {:id     course-id
                               :status status})
    {:id     course-id
     :status status}))

(defn- duplicate-levels
  [levels scenes-map]
  (let [update-activity (fn [activity]
                          (update activity :scene-id #(get scenes-map %)))
        update-lesson (fn [lesson]
                        (update lesson :activities #(map update-activity %)))
        update-level (fn [level]
                       (update level :lessons #(map update-lesson %)))]
    (map update-level levels)))

(defn duplicate-course
  [course-id {:keys [lang]} owner-id]
  (let [current-time (jt/local-date-time)
        {course-name :name image :image-src type :type} (db/get-course-by-id {:id course-id})
        localized-course-data {:name            course-name
                               :slug            (course-slug course-name lang)
                               :lang            lang
                               :owner_id        owner-id
                               :image_src       image
                               :website_user_id nil
                               :status          "draft"
                               :type            type}
        [{new-course-id :id}] (db/create-course! localized-course-data)
        original-scenes (db/get-scenes-by-course-id {:course_id course-id})
        new-scenes (->> original-scenes
                        (map #(duplicate-activity (:id %) (assoc % :lang lang) owner-id))
                        (map :id))
        scenes-map (zipmap (map :id original-scenes) new-scenes)
        course-data (-> (db/get-latest-course-version {:course_id course-id})
                        :data
                        (update :levels #(duplicate-levels % scenes-map))
                        (dissoc :templates :scene-list))]
    (db/save-course! {:course_id  new-course-id
                      :data       course-data
                      :owner_id   owner-id
                      :created_at current-time})
    {:id new-course-id}))

(defn create-book
  [data owner-id]
  (let [created-at (jt/local-date-time)
        updated-at (jt/local-date-time)
        template-id 24
        [{scene-id :id}] (db/create-activity! {:name       (:cover-title data)
                                               :lang       (:lang data)
                                               :image_src  (-> data :cover-image :src)
                                               :status     "invisible"
                                               :owner_id   owner-id
                                               :created_at created-at
                                               :updated_at updated-at
                                               :metadata   {:template-id template-id}
                                               :type       "book"})
        activity-data (templates/activity-from-template (assoc data :template-id template-id))]
    (db/save-scene! {:scene_id    scene-id
                     :data        activity-data
                     :owner_id    owner-id
                     :created_at  created-at
                     :description "Create"})
    {:id scene-id}))

(defn save-activity-version
  [activity-id activity-data owner-id]
  (let [created-at (jt/local-date-time)]
    (db/save-scene! {:scene_id    activity-id
                     :data        activity-data
                     :owner_id    owner-id
                     :created_at  created-at
                     :description (str "Version " created-at)})
    {:id activity-id}))

(defn apply-template-action
  [activity-id {:keys [action] :as data} owner-id]
  (let [created-at (jt/local-date-time)
        scene-data (-> (get-activity-current-version activity-id)
                       (templates/update-activity-from-template data))]
    (db/save-scene! {:scene_id    activity-id
                     :data        scene-data
                     :owner_id    owner-id
                     :created_at  created-at
                     :description (str "Template Action (" action ")")})
    {:data scene-data}))

(defn apply-template-options
  [activity-id data owner-id]
  (let [created-at (jt/local-date-time)
        scene-data (-> (get-activity-current-version activity-id)
                       (templates/update-activity-from-template {:action "template-options"
                                                                 :data   data}))]
    (db/save-scene! {:scene_id    activity-id
                     :data        scene-data
                     :owner_id    owner-id
                     :created_at  created-at
                     :description "Apply Template Options"})
    {:data scene-data}))

(defn- restore-image-assets
  [activity-data]
  (let [existing? (->> activity-data
                       :assets
                       (map :url)
                       (remove nil?)
                       (set))
        object-images (->> activity-data
                           :objects
                           (vals)
                           (filter #(= "image" (:type %)))
                           (map :src)
                           (remove empty?))
        new-assets (->> object-images
                        (remove existing?)
                        (map (fn [src] {:url src :type "image" :size 1})))]
    (update activity-data :assets concat new-assets)))

(defn- prepare-assets
  [assets]
  (let [prepare-asset (fn [asset] (if (string? asset)
                                    {:url asset :size 1 :type "image"}
                                    asset))]
    (loop [result []
           processed #{}
           current (first assets)
           tail (rest assets)]
      (if (empty? tail)
        result
        (let [{:keys [url] :as prepared} (prepare-asset current)]
          (if (or (processed url) (nil? url))
            (recur result processed (first tail) (rest tail))
            (recur (conj result prepared) (conj processed url) (first tail) (rest tail))))))))

(defn update-activity-template!
  [activity-id user-id]
  (let [scene-data (get-activity-current-version activity-id)
        {:keys [created updated]} (get-in scene-data [:metadata :history])
        created-activity (as-> (templates/activity-from-template created) a
                               (reduce #(templates/update-activity-from-template %1 %2) a updated))
        original-assets (:assets scene-data)
        preserved-actions (preserve-actions scene-data created-activity)
        activity (-> created-activity
                     (update :actions merge preserved-actions)
                     (update-preserved-objects scene-data)
                     (update :assets #(->> (concat original-assets %)
                                           (remove nil?)
                                           (prepare-assets)))
                     (restore-image-assets))
        created-at (jt/local-date-time)]
    (db/save-scene! {:scene_id    activity-id
                     :data        activity
                     :owner_id    user-id
                     :created_at  created-at
                     :description "Update Template"})
    {:activity-id activity-id
     :data        activity}))

(defn update-activity-settings!
  [activity-id {:keys [activity-settings preview animation-settings guide-settings]} owner-id]
  (let [created-at (jt/local-date-time)
        activity-info (db/get-scene-by-id {:id activity-id})
        scene-data (-> (get-activity-current-version activity-id)
                       (templates/update-activity-from-template {:action         "set-animation-settings"
                                                                 :common-action? true
                                                                 :data           animation-settings})
                       (templates/update-activity-from-template {:action         "set-guide-settings"
                                                                 :common-action? true
                                                                 :data           guide-settings}))]
    (db/edit-scene! {:id       activity-id
                     :metadata (merge (:metadata activity-info) (:metadata activity-settings))
                     :name     (:name activity-settings)
                     :lang     (:lang activity-settings)})
    (db/update-scene-image! {:id        activity-id
                             :image_src preview})
    (db/save-scene! {:scene_id    activity-id
                     :data        scene-data
                     :owner_id    owner-id
                     :created_at  created-at
                     :description "Activity Settings"})
    {:data scene-data
     :info (get-activity activity-id)}))

(defn owner?
  [user-id activity-id]
  (let [{owner-id :owner-id} (get-activity-info activity-id)]
    (= user-id owner-id)))
