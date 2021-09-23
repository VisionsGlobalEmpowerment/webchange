(ns webchange.course.translate
  (:require
   [clojure.java.io :as io]
   [config.core :refer [env]]
   [webchange.course.core :as course])
  (:import
   (com.google.cloud.translate Translate$TranslateOption
                               TranslateOptions)
   (com.google.auth.oauth2 GoogleCredentials)))

(def translate-service (atom nil))

(defn- init-credentials!
  []
  (let [json-path (env :google-application-credentials)
        credentials (-> (GoogleCredentials/fromStream (io/input-stream json-path))
                        (.createScoped ["https://www.googleapis.com/auth/cloud-platform"]))
        translate (-> (TranslateOptions/newBuilder)
                      (.setCredentials credentials)
                      (.build)
                      (.getService))]
    (reset! translate-service translate)))

(defn- text-lines
  [{:keys [objects]}]
  (let [lines (->> objects
                   (filter #(-> % second :type (= "text")))
                   (map (fn [[k v]] {:type :object
                                     :path [:objects k]
                                     :text (:text v)})))]
    lines))

(defn- walk-dialog
  [path {:keys [type data] :as action}]
  (case type
    "parallel" (->> data
                    (map-indexed (fn [i d] (walk-dialog (concat path [:data i]) d)))
                    flatten)
    "sequence-data" (->> data
                         (map-indexed (fn [i d] (walk-dialog (concat path [:data i]) d)))
                         flatten)
    "animation-sequence" [{:type :action
                           :path path
                           :text (:phrase-text action)}]
    nil))


(defn- dialog-lines
  [{:keys [actions]}]
  (let [dialogs (->> actions
                     (filter #(-> % second :editor-type (= "dialog"))))]
    (->> dialogs
         (mapcat (fn [[key action]] (walk-dialog [:actions key] action)))
         (remove nil?))))

(defn- translate-scene
  [scene-data lang]
  (let [source-language "en"
        target-language lang
        items (concat (text-lines scene-data) (dialog-lines scene-data))
        translation (.translate @translate-service
                                (mapv :text items)
                                (into-array Translate$TranslateOption [(Translate$TranslateOption/sourceLanguage source-language)
                                                                       (Translate$TranslateOption/targetLanguage target-language)]))
        translated-items (map (fn [t item]
                                (assoc item :text (.getTranslatedText t)))
                              translation
                              items)]
    (->> translated-items
         (reduce (fn [scene {:keys [path text type]}]
                   (let [text-path (if (= type :object)
                                     :text
                                     :phrase-text)]
                     (assoc-in scene (concat path [text-path]) text)))
                 scene-data))))

(defn translate-activity!
  [course-slug scene-slug data user-id]
  (init-credentials!)
  (let [latest-version (course/get-scene-latest-version course-slug scene-slug)
        translated-scene (translate-scene latest-version (:language data))]
    (course/save-scene! course-slug scene-slug translated-scene user-id :description "Translate")))

(comment
  (init-credentials!)

  (let [translation (.translate @translate-service
                                ["Hello World" "My name is Ivan"]
                                (into-array Translate$TranslateOption [(Translate$TranslateOption/sourceLanguage "en")
                                                                       (Translate$TranslateOption/targetLanguage "es")]))]
    
    (map #(.getTranslatedText %) translation))

  (let [course-slug "english"
        scene-slug "interactive-read-aloud-newest"
        latest-version (course/get-scene-latest-version course-slug scene-slug)]
    (-> latest-version
        (translate-scene "es")))
  )
