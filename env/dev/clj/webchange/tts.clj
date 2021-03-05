(ns webchange.tts
  (:require [webchange.course.core :as course-core]
            [webchange.assets.handler :as assets-handler]
            [webchange.common.audio-parser.converter :as converter]
            [webchange.common.files :as files]
            [webchange.templates.core :as templates]))

(import '(marytts LocalMaryInterface))
(import '(marytts.util.data.audio MaryAudioUtils))

(def upload-dir "resources/public/upload")
(def user-id 1)
(def force-everything false)

(defn tts
  [text filename]

  (let [marytts (LocalMaryInterface.)
        _ (.setAudioEffects marytts "Rate(durScale:2)")
        audio (-> marytts
                  (.generateAudio text))
        samples (. MaryAudioUtils (getSamplesAsDoubleArray audio))]
    (. MaryAudioUtils (writeWavFile samples, filename, (.getFormat audio)))))

(defn file-from-text
  [text]
  (let [extension "wav"
        new-name (assets-handler/gen-filename extension)
        path (str upload-dir (if (.endsWith upload-dir "/") "" "/") new-name)
        relative-path (str "/upload/" new-name)]
    (tts text path)
    (converter/convert-to-mp3 relative-path)))



(defn process-animation-sequence
  [default-text item]
  (let [text (if (:phrase-text-translated item)
               (:phrase-text-translated item)
               (if (and (:phrase-text item) (not (= (:phrase-text item) "New action")))
                 (:phrase-text item)
                 default-text))]
    (assoc item :audio (file-from-text text))))

(defn update?
  [field-data]
  (or (not field-data) (= field-data "") force-everything))

(defn process-text-animation-audio
  [action scene-data]
  (let [audio (:audio action)
        target (keyword (:target action))
        text-object (get-in scene-data [:objects target])]
    (if (update? audio)
      (assoc-in action [:audio] (file-from-text (:text text-object)))
      action)))

(defn process-dialog-item
  [default-text item scene-data]
  (println "process-dialog-item" default-text item)
  (case (:type item)
    "animation-sequence" (if (update? (:audio item))
                           (process-animation-sequence default-text item) item)
    "text-animation" (process-text-animation-audio item scene-data)
    "parallel" (assoc item :data (vec (map (fn [value] (process-dialog-item default-text value scene-data)) (:data item))))
    "sequence-data" (assoc item :data (vec (map (fn [value] (process-dialog-item default-text value scene-data)) (:data item))))
    item
    ))


(defn process-dialog-audio
  [action scene-data]
  (let [phrase-description (:phrase-description action)]
    (println "process-dialog-audio" action)
    (assoc action :data
                  (doall (map (fn [item] (process-dialog-item phrase-description item scene-data)) (:data action))))))

(defn process-question-audio
  [{:keys [data] :as action}]
  (let [
        audio (get-in data [:audio-data :audio])
        action (if (update? audio) (assoc-in action [:data :audio-data :audio] (file-from-text (:text data))) action)
        action (assoc-in action [:data :answers :data]
                         (->> (get-in data [:answers :data])
                              (map (fn [answer]
                                     (let [audio (get-in answer [:audio-data :audio])]
                                     (if (update? audio)
                                       (assoc-in answer [:audio-data :audio] (file-from-text (:text answer))) answer))))
                              (vec)))]
    action))


(defn process-action
  [action scene-data]
  (case (:type action)
    "show-question" (process-question-audio action)
    "text-animation" (process-text-animation-audio action scene-data)
    "parallel" (assoc action :data (vec (map (fn [value] (process-action value scene-data)) (:data action))))
    "sequence-data" (if (= (:editor-type action) "dialog")
                      (process-dialog-audio action scene-data)
                      (assoc action :data (vec (map (fn [value] (process-action value scene-data)) (:data action)))))
    action))

(defn update-assets
  [scene-data]
  (let [assets (atom [])
        existed-asset (map (fn [asset] (:url asset)) (:assets scene-data))]
    (clojure.walk/postwalk (fn [x]
                             (if (and (or
                                        (= clojure.lang.MapEntry (type x))
                                        (= clojure.lang.PersistentVector (type x))
                                        )
                                      (= (count x) 2))
                               (let [[k v] x]
                                 (if (or
                                       (= k :audio)
                                       (= k :src))
                                   (if v (swap! assets conj v)))))
                             x) scene-data)
    (-> scene-data
        (update-in [:assets] concat
                   (map (fn [url]
                          (println "url" url)
                          (let [extension (files/get-extension url)
                                type (assets-handler/get-type extension)]
                            (println "type" type)
                            {:url url, :size 2, :type type}))
                        (clojure.set/difference (set @assets) (set existed-asset))))
        (update-in [:assets] vec))))

(defn process-audio
  [course-slug scene-slug]
  (let [scene-data (course-core/get-scene-data course-slug scene-slug)
        actions (into {} (map (fn [[key value]] [key (process-action value scene-data)]) (:actions scene-data)))
        _ (println "process-audio" scene-data)
        activity (-> scene-data
                     (assoc :actions actions)
                     (update-assets)
                     )
        ]
    (course-core/save-scene! course-slug scene-slug activity user-id)))

;(process-audio "sdf-sdf-kvwevntu" "sdf")
;http://localhost:3000/courses/sdf-sdf-kvwevntu/editor-v2/sdf