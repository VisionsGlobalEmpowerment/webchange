(ns webchange.templates.loader
  (:require
    [clojure.string :refer [join includes?]]
    [webchange.course.core :as core]
    [webchange.db.core :as db]
    [webchange.templates.core :as t]
    [webchange.templates.library]))

(defn update-templates
  [config]
  (println "Start updating templates" (-> @t/templates keys count))
  (time 
   (let [user-id 1
         scenes (db/get-scenes)]
     (doseq [scene scenes]
       (let [version (db/get-latest-scene-version {:scene_id (:id scene)})]
         (if (get-in version [:data :metadata :history :created :template-id])
           (try
             (core/update-activity-template! (:id scene) user-id)
             (print ".")
             (catch Exception e (str "caught exception: " (.getMessage e))))
           (print "x"))
         (flush)))))
  (println "Done!"))

(defn search-scenes-for
  [config text]
  (time 
   (let [scenes (db/get-scenes)]
     (doseq [scene scenes]
       (let [version (db/get-latest-scene-version {:scene_id (:id scene)})
             assets (-> version :data :assets)
             links (->> assets
                        (map #(if (map? %) (:url %) %))
                        (remove nil?)
                        (filter #(includes? % text)))]
         (if (empty? links)
           (print ".")
           (do
             (println)
             (print (:id scene) (apply str links))
             (println)))
         (flush)))))
  (println "Done!"))

(def commands
  {"update-templates"
   (fn [config args]
     (apply update-templates config args))
   "search-scenes-for"
   (fn [config args]
     (apply search-scenes-for config args))})

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
