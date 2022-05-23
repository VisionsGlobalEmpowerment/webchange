(ns webchange.secondary.loader
  (:require [webchange.db.core :refer [*db*] :as db]
            [clojure.string :refer [join]]
            [webchange.accounts.core :as accounts]
            [webchange.secondary.core :as core]
            [webchange.school.core :as school-core]
            [config.core :refer [env]]
            [webchange.assets.loader :as assets-loader]
            [clj-http.client :as client]
            ))

(defn init-secondary!
  [config school-id email password]
  (let [
        [{school-id :id}] (db/create-school! {:id (Integer/parseInt school-id) :name "default"})
        [{user-id :id}] (accounts/create-user-with-credentials! {:email email
                                                                 :password password
                                                                 :type "teacher"})
        [{teacher-id :id}] (db/create-teacher! {:user_id user-id
                                                :school_id school-id
                                                :type "admin"
                                                :status "active"})]
    (println "School id:" school-id)
    (println "User id:" user-id)
    (println "Teacher id:" teacher-id)
    {:school-id school-id
     :user-id user-id
     :teacher-id teacher-id}))

(defn load-secondary-school! []
  (doseq [school (:schools (school-core/get-schools))]
    (core/load-full! school)))

(defn update-assets! [config]
  (println "Update assets....")
  (core/update-assets!))

(defn download-course-data! [config]
  (println "Update course data....")
  (core/update-course-data! (env :default-school) (env :requested-courses))
  (update-assets! config)
  (println "Done!"))

(defn download-assets! [config]
  (println "Update assets....")
  (core/update-assets! false))

(defn upload-local-files!
  [config path]
  (print "Do you want to update asset hashes. (Y) ")
  (flush)
  (when (= (read-line) "Y")
    (println "Updating asset hashes....")
    (assets-loader/calc-hashes! config))
  (println "Calculate difference and search files to upload....")
  (let [default-school (env :default-school)
        requested-courses (env :requested-courses)
        to-upload (->> (core/calc-upload-assets default-school requested-courses)
                       (filter  (fn [file] (clojure.string/starts-with? (:path file) path))))]
    (println "About to upload....")
    (doseq [file to-upload]
      (println (:path file)))
    (print "Are you sure you want to continue(Y)?")
    (flush)
    (let [user-input (read-line)]
      (when (= user-input "Y")
        (doseq [file to-upload]
          (println "Uploading file" (:path file))
          (core/upload-file (:path file))))
      (println "Done!")
      )))


(def commands
  {"init-secondary"
   (fn [config args]
     (apply init-secondary! config args))
   "load-secondary-school"
   (fn [config args]
     (load-secondary-school!))
   "download-course-data"
   (fn [config args]
     (apply download-course-data! config args))
   "update-assets"
   (fn [config args]
     (apply update-assets! config args))
   "download-assets"
   (fn [config args]
     (apply download-assets! config args))
   "upload-local-files"
   (fn [config args]
     (apply upload-local-files! env args))
   })

(defn command? [[arg]]
  (contains? (set (keys commands)) arg))

(defn execute
  "args - vector of arguments, e.g: [\"init-secondary\" \"school_id\" \"email\" \"password\"]"
  [args opts]
  (when-not (command? args)
    (throw
      (IllegalArgumentException.
        (str "unrecognized option: " (first args)
             ", valid options are:" (join ", " (keys commands))))))
  ((get commands (first args)) opts (rest args)))
