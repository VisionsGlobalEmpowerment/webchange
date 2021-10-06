(ns webchange.course.translate
  (:require
    [clojure.java.io :as io]
    [clojure.tools.logging :as log]
    [config.core :refer [env]]
    [webchange.course.core :as course]
    [webchange.course.walk :as course-walk])
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

(defn- translate-scene
  [scene-data lang]
  (let [source-language "en"
        target-language lang
        items (concat (course-walk/text-lines scene-data) (course-walk/dialog-lines scene-data))
        translation (.translate @translate-service
                                (mapv :text items)
                                (into-array Translate$TranslateOption [(Translate$TranslateOption/sourceLanguage source-language)
                                                                       (Translate$TranslateOption/targetLanguage target-language)]))
        translated-items (map (fn [t item]
                                (-> item
                                    (assoc :origin-text (:text item))
                                    (assoc :text (.getTranslatedText t))))
                              translation
                              items)]
    (->> translated-items
         (reduce (fn [scene {:keys [path text origin-text type]}]
                   (let [text-path (if (= type :object)
                                     :text
                                     :phrase-text)
                         origin-text-path :origin-text]
                     (-> scene
                         (assoc-in (concat path [text-path]) text)
                         (assoc-in (concat path [origin-text-path]) origin-text))))
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
