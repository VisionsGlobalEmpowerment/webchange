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
            [camel-snake-kebab.core :refer [->snake_case_keyword ->kebab-case-keyword]]))

(def editor_asset_type_background "background")
(def editor_asset_type_surface "surface")
(def editor_asset_type_decoration "decoration")

(def editor_asset_types [editor_asset_type_background editor_asset_type_surface editor_asset_type_decoration])

(def hardcoded (env :hardcoded-courses {"test" true}))

(defn get-course-templates
  [course-slug]
  (if (contains? hardcoded course-slug)
    (scene/get-templates course-slug)
    (scene/get-templates course-slug) ;; "Getting templates for not-hardcoded courses is not implemented"
    ))

(defn get-course-info
  [course-slug]
  (db/get-course {:slug course-slug}))

(defn save-course-info!
  [id {:keys [name slug lang image-src]}]
  (db/save-course-info! {:id id
                         :name name
                         :slug slug
                         :lang lang
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

(defn get-scene-latest-version
  [course-slug scene-name]
  (let [{course-id :id} (db/get-course {:slug course-slug})
        {scene-id :id} (db/get-scene {:course_id course-id :name scene-name})
        latest-version (db/get-latest-scene-version {:scene_id scene-id})]
    (:data latest-version)))

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
        scene-data-old (:data (db/get-latest-scene-version {:scene_id scene-id}))
        ]
    (if (not (identical? scene-data-old scene-data))
      (db/save-scene! {:scene_id scene-id
                       :data scene-data
                       :owner_id owner-id
                       :created_at created-at}))

    [true {:id scene-id
           :name scene-name
           :created-at (str created-at)}]))

(defn save-course!
  [course-slug data owner-id]
  (let [{course-id :id} (db/get-course {:slug course-slug})
        created-at (jt/local-date-time)]
    (db/save-course! {:course_id course-id
                     :data data
                     :owner_id owner-id
                     :created_at created-at})
    [true {:created-at (str created-at)}]))

(defn restore-course-version!
  [version-id owner-id]
  (let [{data :data course-id :course-id} (db/get-course-version {:id version-id})
        created-at (jt/local-date-time)]
    (db/save-course! {:course_id course-id
                      :data data
                      :owner_id owner-id
                      :created_at created-at})
    [true {:created-at (str created-at)}]))

(defn restore-scene-version!
  [version-id owner-id]
  (let [{data :data scene-id :scene-id} (db/get-scene-version {:id version-id})
        created-at (jt/local-date-time)]
    (db/save-scene! {:scene_id scene-id
                     :data data
                     :owner_id owner-id
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
  (-> (select-keys course [:id :name :language :slug :image-src :lang])
      (with-course-page)
      (with-default-image)
      (with-host-name :image-src)))

(defn- rand-str [len]
  (apply str (take len (repeatedly #(char (+ (rand 26) 65))))))

(defn- slug
  [original lang]
  (let [suffix (rand-str 8)]
    (-> (str original "-" lang "-" suffix)
        (clojure.string/lower-case))))

(defn localize
  [course-id {:keys [lang owner-id website-user-id]}]
  (let [current-time (jt/local-date-time)
        {course-name :name course-slug :slug image :image-src} (db/get-course-by-id {:id course-id})
        localized-course-data {:name course-name
                               :slug (slug course-slug lang)
                               :lang lang
                               :owner_id owner-id
                               :image_src image
                               :website_user_id website-user-id
                               :status "draft"}
        [{new-course-id :id}] (db/create-course! localized-course-data)
        course-data (:data (db/get-latest-course-version {:course_id course-id}))
        scenes (->> course-data
                    :scene-list
                    keys
                    (map name))]
    (db/save-course! {:course_id new-course-id
                      :data course-data
                      :owner_id owner-id
                      :created_at current-time})
    (doseq [scene-name scenes]
      (let [{scene-id :id} (db/get-scene {:course_id course-id :name scene-name})
            scene-data (:data (db/get-latest-scene-version {:scene_id scene-id}))
            scene-id (get-or-create-scene! new-course-id scene-name)]
        (db/save-scene! {:scene_id scene-id
                         :data scene-data
                         :owner_id owner-id
                         :created_at current-time})))
    [true (-> (transform-keys ->kebab-case-keyword localized-course-data)
              (assoc :id new-course-id)
              (->website-course))]))

(defn get-available-courses
  []
  (->> (db/get-available-courses)
       (map ->website-course)))

(defn get-courses-by-website-user
  [website-user-id]
  (->> (db/get-courses-by-website-user {:website_user_id website-user-id})
       (map ->website-course)))

(defn read-character-data [dir]
  (let [filename (str dir "/skeleton.json")
        data (json/read-str (slurp filename) :key-fn keyword)
        ]
    (as-> {} character-data
        (assoc character-data :name (last (string/split dir #"/")))
        (assoc character-data :width (get-in data [:skeleton :width]))
        (assoc character-data :height (get-in data [:skeleton :height]))
        (if (vector? (:skins data))
          (assoc character-data :skins (vec (map #(:name %) (:skins data))))
          (assoc character-data :skins (vec (map #(name (get % 0)) (vec (:skins data))))))
        (assoc character-data :animations (vec (map #(name (get % 0)) (vec (:animations data)) ))))))

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
    assets ))

(defn find-all-tags []
  (db/find-all-tags))

(defn store-tag! [tag]
  (db/create-asset-tags! {:name tag})
  (db/find-editor-tag-by-name {:name tag}))

(defn store-editor-assets! [path thumbnail type]
  (db/create-or-update-editor-assets! {:path path :thumbnail_path thumbnail :type type})
  (db/find-editor-assets-by-path  {:path path}))

(defn remove-first-directory [string begin]
  (let [string (clojure.string/replace-first string (re-pattern (str "^" begin))  "")
        string (clojure.string/replace-first string #"^/"  "")]
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
