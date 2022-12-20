(ns webchange.course.loader
  (:require [java-time :as jt]
            [clojure.tools.logging :as log]
            [clojure.data.json :as json]
            [webchange.course.core :as core]
            [clojure.pprint :as p]
            [mount.core :as mount]
            [clojure.edn :as edn]
            [clojure.java.io :as io]
            [config.core :refer [env]]
            [clojure.string :refer [join]]
            [camel-snake-kebab.core :refer [->snake_case_string]]))

(def owner-id 1)

(defn- course-path
  [config course-name]
  (let [dir (or (:course-dir config) "resources/courses/")]
    (str dir course-name "/course.edn")))

(defn- scene-path
  [config course-name scene-name]
  (let [dir (or (:course-dir config) "resources/courses/")]
    (str dir course-name "/scenes/" scene-name ".edn")))

(defn- index-of
  [order item]
  (let [index (.indexOf order item)
        last-index (count order)]
    (if (= index -1)
      last-index
      index)))

(defn- by-order
  [order]
  (fn [x y]
    (let [x-i (index-of order x)
          y-i (index-of order y)]
      (compare [x-i x] [y-i y]))))

(defn- sort-by-keys-order
  "Sort map keys by provided vector of keys.
  Keys not in order list will be ordered by name and conj-ed at the end of result"
  [data order]
  (let [ordered (->> (select-keys data order)
                     (into (sorted-map-by (by-order order))))
        other (->> (apply dissoc data order)
                   (into (sorted-map)))]
    (merge ordered other)))

(defn- sort-course
  [data]
  (let [order [:initial-scene :scene-list :scenes :locations :levels :default-progress]]
    (sort-by-keys-order data order)))

(defn- sort-action
  [data]
  (let [order [:type :data
               :phrase :phrase-description :phrase-description-translated :phrase-text :phrase-text-translated
               :id :target
               :audio :start :duration :track :offset
               :var-name :var-value :value :value1 :value2 :success :fail
               :from-var :from-params
               ]]
    (sort-by-keys-order data order)))

(defn- sort-actions
  [actions]
  (let [k (keys actions)
        v (vals actions)]
    (->> (zipmap k (map sort-action v))
         (into (sorted-map)))))

(defn- sort-object
  [data]
  (let [order [:type
               :x :y :width :height :scale :origin
               :scene-name :transition
               ]]
    (sort-by-keys-order data order)))

(defn- sort-objects
  [objects]
  (let [k (keys objects)
        v (vals objects)]
    (->> (zipmap k (map sort-object v))
         (into (sorted-map)))))

(defn- sort-scene
  [data]
  (let [order [:assets :objects :scene-objects :actions :triggers :metadata]]
    (-> data
        (update-in [:actions] sort-actions)
        (update-in [:objects] sort-objects)
        (sort-by-keys-order order))))

(defn save-scene
  ([config course-slug scene-name] (save-scene config course-slug course-slug scene-name))
  ([config course-slug new-name scene-name]
   (let [scene-info (-> (core/get-scene-latest-version course-slug scene-name)
                        sort-scene)
         path-name (->snake_case_string scene-name)
         path (scene-path config new-name path-name)]
     (clojure.java.io/make-parents path)
     (binding [p/*print-right-margin* 121]
       (p/pprint
         scene-info
         (clojure.java.io/writer path))))))

(defn load-scene
  ([config course-slug scene-name] (load-scene config course-slug course-slug scene-name))
  ([config course-slug saved-name scene-name]
   (let [path-name (->snake_case_string scene-name)
         path (scene-path config saved-name path-name)
         data (-> path io/reader java.io.PushbackReader. edn/read)
         course-data (core/get-course-data course-slug)
         scene-exists? (get-in course-data [:scene-list (keyword scene-name)])]
     (core/save-scene! course-slug scene-name data owner-id)
     (when-not scene-exists?
       (core/save-course! course-slug
                          (-> course-data
                              (assoc-in [:scene-list (keyword scene-name)] {:name scene-name}))
                          owner-id)))))

(defn merge-scene-info
  [config course-slug saved-name scene-name & fields]
  (let [ks (->> fields (map keyword) (into []))
        path-name (->snake_case_string scene-name)
        path (scene-path config saved-name path-name)
        data (-> path io/reader java.io.PushbackReader. edn/read)
        original (core/get-scene-latest-version course-slug scene-name)
        merged-value (assoc-in original ks (get-in data ks))]
    (core/save-scene! course-slug scene-name merged-value owner-id)))

(defn save-course
  ([config course-slug] (save-course config course-slug course-slug))
  ([config course-slug new-name]
    (let [course-info (-> (core/get-course-latest-version course-slug)
                          sort-course)
          path (course-path config new-name)
          scenes (->> course-info
                      :scene-list
                      keys
                      (map name))]
      (clojure.java.io/make-parents path)
      (binding [p/*print-right-margin* 121]
        (p/pprint
          course-info
          (clojure.java.io/writer path)))
      (doseq [scene-name scenes]
        (save-scene config course-slug new-name scene-name)))))

(defn load-course
  ([config course-slug] (load-course config course-slug course-slug))
  ([config course-slug saved-name]
    (let [path (course-path config saved-name)
          data (-> path io/reader java.io.PushbackReader. edn/read)
          scenes (->> data
                      :scene-list
                      keys
                      (map name))]
      (core/save-course! course-slug data owner-id)
      (doseq [scene-name scenes]
        (load-scene config course-slug saved-name scene-name)))))

(defn save-course-info
  [config course-slug new-name]
  (let [course-info (-> (core/get-course-latest-version course-slug)
                        sort-course)
        path (course-path config new-name)]
    (clojure.java.io/make-parents path)
    (binding [p/*print-right-margin* 121]
      (p/pprint
        course-info
        (clojure.java.io/writer path)))))

(defn load-course-info
  [config course-slug saved-name]
  (let [path (course-path config saved-name)
        data (-> path io/reader java.io.PushbackReader. edn/read)]
    (core/save-course! course-slug data owner-id)))

(defn merge-course-info
  [config course-slug saved-name & fields]
  (let [ks (->> fields (map keyword) (into []))
        path (course-path config saved-name)
        data (-> path io/reader java.io.PushbackReader. edn/read)
        original (core/get-course-latest-version course-slug)
        merged-value (assoc-in original ks (get-in data ks))]
    (core/save-course! course-slug merged-value owner-id)))

(def commands
  {"save-course"
   (fn [config args]
     (apply save-course config args))

   "load-course"
   (fn [config args]
     (apply load-course config args))

   "save-course-info"
   (fn [config args]
     (apply save-course-info config args))

   "load-course-info"
   (fn [config args]
     (apply load-course-info config args))

   "update-character-skins"
   (fn [config args]
     (apply core/update-character-skins config args))

   "update-editor-assets"
   (fn [config args]
     (let [config (merge env config)]
       (apply core/update-editor-assets config args)))

   "save-scene"
   (fn [config args]
     (apply save-scene config args))

   "load-scene"
   (fn [config args]
     (apply load-scene config args))

   "merge-course-info"
   (fn [config args]
     (apply merge-course-info config args))

   "merge-scene-info"
   (fn [config args]
     (apply merge-scene-info config args))
   })

(defn command? [[arg]]
  (contains? (set (keys commands)) arg))

(defn execute
  "args - vector of arguments, e.g: [\"save-course\" \"test\" \"new-test\"]"
  [args opts]
  (when-not (command? args)
    (throw
      (IllegalArgumentException.
        (str "unrecognized option: " (first args)
             ", valid options are:" (join ", " (keys commands))))))
  ((get commands (first args)) opts (rest args)))
