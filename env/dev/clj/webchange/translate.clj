(ns webchange.translate
  (:require
   [clojure.java.io :as io]
   [config.core :refer [env]])
  (:import
   (com.google.cloud.translate Translate$TranslateOption
                               TranslateOptions)
   (com.google.auth.oauth2 GoogleCredentials)))

;;GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(jsonPath))
;;          .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));
;;    Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();

(def translate-service (atom nil))

(defn init-credentials
  []
  (let [json-path "/Users/ikhaldeev/tabschool/creds/webchange-test-6f38df542d02.json" ;(env :google-application-credentials)
        credentials (-> (GoogleCredentials/fromStream (io/input-stream json-path))
                        (.createScoped ["https://www.googleapis.com/auth/cloud-platform"]))
        translate (-> (TranslateOptions/newBuilder)
                      (.setCredentials credentials)
                      (.build)
                      (.getService))]
    (reset! translate-service translate)))

(comment
  (init-credentials)

  (let [translation (.translate @translate-service
                                ["Hello World" "My name is Ivan"]
                                (into-array Translate$TranslateOption [(Translate$TranslateOption/sourceLanguage "en")
                                                                       (Translate$TranslateOption/targetLanguage "es")]))]
    
    (map #(.getTranslatedText %) translation))
  )
