(ns webchange.native.core
  (:require
    [clojure.java.io :as io]
    [clojure.pprint :refer [pprint]]
    [clojure.walk :as w]
    [clojure.string :as str]
    [webchange.db.core :as db]
    [webchange.common.files :as f]
    [webchange.course.core :as course]
    [webchange.utils.book-library :as book-library]
    [webchange.secondary.core :refer [asset->url]]
    [webchange.resources.default-resources :refer [default-game-assets]]))

(comment
  (let [copy-asset (fn [path]
                     (let [source-path (f/relative->absolute-path path)
                           dest-path (str "resources/android" path)]
                       (when (f/file-exist? source-path)
                         (io/make-parents dest-path)
                         (io/copy (io/file source-path) (io/file dest-path)))
                       dest-path))
        course-id 4
        scenes (->> (db/get-scenes-by-course-id {:course_id course-id})
                    (concat (db/find-scenes {:type "book" :status "visible"})))
        
        scene-previews (->> scenes
                            (map :image-src)
                            (remove empty?))
        scene-assets (->> scenes
                          (map #(db/get-latest-scene-version {:scene_id (:id %)}))
                          (mapcat #(-> % :data :assets))
                          (map asset->url)
                          (remove nil?))
        mandatory-assets (->> ["/raw/clipart"]
                              (map f/relative->absolute-path)
                              (map io/file)
                              (mapcat file-seq)
                              (filter #(not (.isDirectory %)))
                              (map #(.getPath %))
                              (map #(str/replace % #"^resources/public" "")))
        ui-assets (->> default-game-assets
                       (map asset->url))]
    (->> (concat scene-previews scene-assets ui-assets mandatory-assets)
         (set)
         (map copy-asset)))

  (-> (course/get-course-data "english")
      (update :scene-list #(->> % (map (fn [[k v]] [(-> k str keyword) v])) (into {})))
      (get-in [:scene-list]))
  
  (let [prepare-uri #(-> %
                         (str/replace \/ \-)
                         (str/replace #"^-" ""))
        course-uri #(-> (str "/api/courses/" %) (prepare-uri))
        course-info-uri #(-> (str "/api/courses/" % "/info") (prepare-uri))
        course-id 4
        course-info (-> (db/get-course-by-id {:id course-id})
                        (dissoc :created-at))
        course-latest (-> (course/get-course-data (:slug course-info))
                          (update :scene-list #(->> % (map (fn [[k v]] [(-> k str keyword) v])) (into {}))))
        scenes (->> (db/get-scenes-by-course-id {:course_id course-id})
                    (concat (db/find-scenes {:type "book" :status "visible"}))
                    (map #(db/get-latest-scene-version {:scene_id (:id %)})))
        add-scene #(assoc-in %1
                             [:responses :get (prepare-uri (str "/api/activities/" (:scene-id %2) "/current-version"))]
                             (:data %2))

        library (->> (course/get-visible-books "english")
                     (map #(dissoc % :updated-at :created-at)))
        data (reduce add-scene
                     {:responses {:get {(course-uri "english") course-latest
                                        (course-info-uri "english") course-info
                                        (prepare-uri "/api/book-library/all") library
                                        (prepare-uri "/api/book-library/english") library
                                        (prepare-uri "/api/book-library/categories") book-library/categories}}}
                     scenes)
        data (w/postwalk (fn [x] (if (map? x) (dissoc x (keyword "")) x)) data)]
    (with-open [writer (io/writer "responses.edn")]
      (binding [*out* writer]
        (pprint data))))

  )
