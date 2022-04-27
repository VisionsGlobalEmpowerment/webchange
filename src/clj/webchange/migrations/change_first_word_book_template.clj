(ns webchange.migrations.change-first-word-book-template
  (:require
    [clojure.tools.logging :as log]
    [mount.core :as mount]
    [webchange.course.core :as course]
    [webchange.templates.core :as templates]
    [webchange.templates.library]
    [webchange.templates.library.first-word-book.add-spread :refer [spread-idx->dialog-name spread-idx->text-name]]
    [webchange.utils.list :refer [distinct-by-key]]
    [webchange.utils.scene-action-data :refer [update-empty-action update-inner-action]]
    [webchange.utils.map :refer [remove-empty-fields]]))

(defn fix-asset
  [asset]
  (cond
    (string? asset) {:url asset}
    (and (map? asset)
         (contains? asset :url)) asset
    :default nil))

(defn fix-assets
  [assets]
  (->> assets
       (map fix-asset)
       (remove nil?)))

(defn migrate-assets
  [new-scene origin-scene default-page-data]
  (let [new-scene-assets (-> new-scene (get :assets) (fix-assets))
        origin-scene-assets (-> origin-scene (get :assets) (fix-assets))
        merged-assets (->> (concat [(fix-assets (:image default-page-data))]
                                   origin-scene-assets
                                   new-scene-assets)
                           (distinct-by-key :url))]
    (assoc new-scene :assets merged-assets)))

(defn copy-action
  [new-scene origin-scene new-action-name origin-action-name]
  (->> (get-in origin-scene [:actions origin-action-name])
       (assoc-in new-scene [:actions new-action-name])))

(defn migrate-common-dialogs
  [new-scene origin-scene]
  (-> new-scene
      (copy-action origin-scene :dialog-intro :dialog-intro)
      (copy-action origin-scene :dialog-finish-activity :dialog-finish-activity)
      (copy-action origin-scene :spread-0 :dialog-spread-0)
      (copy-action origin-scene :dialog-timeout-instructions :dialog-timeout-instructions)
      (copy-action origin-scene :spread-0 :dialog-spread-0)
      (copy-action origin-scene :spread-1 :dialog-spread-1)))

(defn migrate-page-data
  [new-scene origin-action-data spread-idx side]
  (let [new-dialog-name (-> (spread-idx->dialog-name spread-idx side) (keyword))
        fixed-action-data (-> origin-action-data
                              (update-inner-action {:target (spread-idx->text-name spread-idx side)})
                              (update-empty-action {:duration 0}))]
    (assoc-in new-scene [:actions new-dialog-name :data] [fixed-action-data])))

(defn migrate-spread-data
  [new-scene origin-scene spread-idx]
  (let [origin-dialog-name (->> spread-idx (str "dialog-spread-") (keyword))
        [origin-dialog-left origin-dialog-right] (get-in origin-scene [:actions origin-dialog-name :data])]
    (cond-> new-scene
            (some? origin-dialog-left) (migrate-page-data origin-dialog-left spread-idx :left)
            (some? origin-dialog-right) (migrate-page-data origin-dialog-right spread-idx :right))))

(defn migrate-content-dialogs
  [new-scene origin-scene]
  (let [spreads-number (-> new-scene (get-in [:metadata :last-spread-idx]) (dec))]
    (->> (range 2 (+ spreads-number 2))
         (reduce (fn [new-scene spread-number]
                   (migrate-spread-data new-scene origin-scene spread-number))
                 new-scene))))

(defn get-template-params
  [scene-data {default-image :image default-text :text}]
  (let [creation-params (get-in scene-data [:metadata :history :created])
        update-params (get-in scene-data [:metadata :history :updated])
        result-params (->> update-params
                           (filter (fn [{:keys [action]}] (= action "edit")))
                           (map :data)
                           (map remove-empty-fields)
                           (reduce merge creation-params))
        add-spread-params (->> [1 2 3]
                               (map (fn [idx] [(-> idx (* 2) (- 1)) (-> idx (* 2))]))
                               (map (fn [[idx-left idx-right]]
                                      {:text-left   (or (->> idx-left (str "text") (keyword) (get result-params))
                                                        default-text)
                                       :image-left  (or (->> idx-left (str "image") (keyword) (get result-params))
                                                        {:src default-image})
                                       :text-right  (or (->> idx-right (str "text") (keyword) (get result-params))
                                                        default-text)
                                       :image-right (or (->> idx-right (str "image") (keyword) (get result-params))
                                                        {:src default-image})}))
                               (map (fn [data]
                                      {:action "add"
                                       :data   data})))
        rest-params (->> update-params
                         (filter (fn [{:keys [action]}] (not= action "edit"))))]
    {:create (select-keys result-params [:letters :title :subtitle])
     :update (concat add-spread-params rest-params)}))

(defn migrate-scene!
  [{:keys [course scene data]} default-page-data template-id user-id]
  (log/debug "Migrate scene" course scene)
  (try
    (let [template-params (get-template-params data default-page-data)
          new-scene-data (-> (:create template-params)
                             (assoc :template-id template-id)
                             (templates/activity-from-template)
                             (assoc-in [:metadata :history :updated] (:update template-params)))]
      (course/save-scene! course scene new-scene-data user-id)
      (let [updated-scene-data (-> (-> (course/update-activity-template! course scene user-id)
                                       (second)
                                       (get :data))
                                   (migrate-assets data default-page-data)
                                   (migrate-common-dialogs data)
                                   (migrate-content-dialogs data))]
        (course/save-scene! course scene updated-scene-data user-id)))
    (catch Exception e
      (log/debug "Migrate scene exception:" course scene)
      (log/debug e)
      (log/debug "Restore origin data")
      (course/save-scene! course scene data user-id))))

(defn get-scenes-to-update
  [target-template-id courses]
  {:pre  [(number? target-template-id)]
   :post [(every? (fn [{:keys [course scene data]}]
                    (and (string? course)
                         (string? scene)
                         (map? data))) %)]}
  (let [course-scenes-slugs (->> courses
                                 (map (fn [course]
                                        (let [scene-slugs (-> (course/get-course-data course)
                                                              (get :scene-list [])
                                                              (keys))]
                                          (map (fn [scene-slug]
                                                 {:course course
                                                  :scene  (clojure.core/name scene-slug)})
                                               scene-slugs))))
                                 (flatten))]
    (->> course-scenes-slugs
         (map (fn [{:keys [course scene] :as params}]
                (let [scene-data (course/get-scene-data course scene)
                      template-id (get-in scene-data [:metadata :template-id])]
                  (when (= template-id target-template-id)
                    (assoc params :data scene-data)))))
         (remove nil?))))

(def courses ["english" "spanish"])
(def user-id 1)
(def old-template-id 44)
(def new-template-id 49)
(def default-page-data {:image "/images/default-course.jpp"
                        :text  "Enter Text"})

(defn migrate-up
  [_]
  (mount/start)
  (doseq [data (get-scenes-to-update old-template-id courses)]
    (migrate-scene! data default-page-data new-template-id user-id)))

(defn migrate-down
  [_]
  (mount/start))

(comment
  (first (get-scenes-to-update old-template-id))

  (let [course "spanish"
        scene "first-letters-book-letter-v-2"]
    (migrate-scene! {:course course
                     :scene  scene
                     :data   (course/get-scene-data course scene)}
                    new-template-id
                    user-id))

  (get-template-params (course/get-scene-data "spanish" "first-letters-book-letter-v-2"))
  (course/get-scene-data "spanish" "first-letters-book-letter-v-2")

  (-> (migrate-content-dialogs (course/get-scene-data "english" "first-words-book-letter-a-updated")
                               (course/get-scene-data "english" "first-words-book-letter-a"))
      (keys)
      (print))

  (course/get-scene-data "english" "first-words-book-letter-a-updated"))
