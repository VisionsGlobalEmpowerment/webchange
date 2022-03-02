(ns webchange.templates.loader
  (:require
    [clojure.string :refer [join]]
    [webchange.templates.core :as t]
    [webchange.templates.library]
    [webchange.course.core :as core]
    [webchange.db.core :as db]))

(defn update-templates
  [config]
  (println "Start updating templates" (-> @t/templates keys count))
  (time 
   (let [user-id 1
         courses (db/get-courses)]
     (doseq [course courses]
       (let [scenes (db/get-scenes-by-course-id {:course_id (:id course)})]
         (doseq [scene scenes]
           (let [version (db/get-latest-scene-version {:scene_id (:id scene)})]
             (if (get-in version [:data :metadata :history :created :template-id])
               (try
                 (core/update-activity-template! (:slug course) (:name scene) user-id)
                 (print ".")
                 (catch Exception e (str "caught exception: " (.getMessage e))))
               (print "x"))
             (flush)))))))
  (println "Done!"))
(def commands
  {"update-templates"
   (fn [config args]
     (apply update-templates config args))
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
