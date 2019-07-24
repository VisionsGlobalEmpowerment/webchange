(ns webchange.migrations.cycling-items
  (:require [webchange.dataset.core :as core]
            [mount.core :as mount]
            [clojure.tools.logging :as log]))

(defn game-voice-action [concept-name]
  (case concept-name
    "ardilla" {:id "/raw/audio/l1/a8/GameVoice.m4a" :type "audio" :start 6.76 :duration 1.094 :scene-id "cycling"}
    "oso" {:id "/raw/audio/l1/a8/GameVoice.m4a" :type "audio" :start 8.24 :duration 0.907 :scene-id "cycling"}
    "iman" {:id "/raw/audio/l1/a8/GameVoice.m4a" :type "audio" :start 9.534 :duration 0.853 :scene-id "cycling"}
    {}))

(defn migrate-up [config]
  (mount/start)
  (let [dataset (-> (core/get-dataset-by-name "test" "concepts")
                    (update-in [:scheme :fields] conj {:name "word-1-skin", :type "string"})
                    (update-in [:scheme :fields] conj {:name "word-2-skin", :type "string"})
                    (update-in [:scheme :fields] conj {:name "word-3-skin", :type "string"})
                    (update-in [:scheme :fields] conj {:name "word-4-skin", :type "string"})
                    (update-in [:scheme :fields] conj {:name "game-voice-action", :type "action"}))
        items (->> (core/get-dataset-items (:id dataset))
                   :items
                   (map #(assoc-in % [:data :word-1-skin] (-> % :data :sandbox-change-skin-1-action :skin)))
                   (map #(assoc-in % [:data :word-2-skin] (-> % :data :sandbox-change-skin-2-action :skin)))
                   (map #(assoc-in % [:data :word-3-skin] (-> % :data :sandbox-change-skin-3-action :skin)))
                   (map #(assoc-in % [:data :word-4-skin] (-> % :data :sandbox-change-skin-4-action :skin)))
                   (map #(assoc-in % [:data :game-voice-action] (-> % :data :concept-name game-voice-action))))]
    (core/update-dataset! (:id dataset) dataset)
    (doseq [item items]
      (core/update-dataset-item! (:id item) item))))

(defn migrate-down [config]
  (mount/start)
  (spit)
  (let [dataset (-> (core/get-dataset-by-name "test" "concepts")
                    (update-in [:scheme :fields] #(remove (fn [field] (= "word-1-skin" (:name field))) %))
                    (update-in [:scheme :fields] #(remove (fn [field] (= "word-2-skin" (:name field))) %))
                    (update-in [:scheme :fields] #(remove (fn [field] (= "word-3-skin" (:name field))) %))
                    (update-in [:scheme :fields] #(remove (fn [field] (= "word-4-skin" (:name field))) %))
                    (update-in [:scheme :fields] #(remove (fn [field] (= "game-voice-action" (:name field))) %)))
        items (->> (core/get-dataset-items (:id dataset))
                   :items
                   (map #(update-in % [:data] dissoc :word-1-skin))
                   (map #(update-in % [:data] dissoc :word-2-skin))
                   (map #(update-in % [:data] dissoc :word-3-skin))
                   (map #(update-in % [:data] dissoc :word-4-skin))
                   (map #(update-in % [:data] dissoc :game-voice-action)))]
    (core/update-dataset! (:id dataset) dataset)
    (doseq [item items]
      (core/update-dataset-item! (:id item) item))))