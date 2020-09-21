(ns webchange.resources.scene-parser
  (:require
    [re-frame.core :as re-frame]
    [webchange.resources.default-resources :refer [default-game-assets]]
    [webchange.interpreter.subs :as subs]))

(defn- get-concept-fields
  [scene-id dataset-id]
  (let [dataset-fields (-> @(re-frame/subscribe [::subs/course-dataset dataset-id])
                           (get-in [:scheme :fields]))]
    (->> dataset-fields
         (map (fn [{:keys [name type scenes]}]
                (when (some #{scene-id} scenes)
                  [(keyword name) type])))
         (remove nil?))))

(defn- get-action-resources
  [{:keys [type] :as action}]
  (cond
    (= type "audio") [(:id action)]
    (= type "animation-sequence") [(or (:id action) (:audio action))]
    (or (= type "sequence-data")
        (= type "parallel")) (->> (:data action)
                                  (map get-action-resources)
                                  (flatten))
    :else []))

(defn- parse-concept-field
  [type data]
  (case type
    "image" [data]
    "video" [data]
    "action" (get-action-resources data)
    nil))

(defn- parse-concept-resources
  [scene-id]
  (let [lesson-sets-data @(re-frame/subscribe [::subs/current-lesson-sets-data])]
    (->> lesson-sets-data
         (mapcat (fn [{:keys [item-ids dataset-id]}]
                   (for [item-id item-ids
                         [field-name field-type] (get-concept-fields scene-id dataset-id)]
                     (let [item @(re-frame/subscribe [::subs/dataset-item item-id])]
                       (parse-concept-field field-type (get-in item [:data field-name]))))))
         (flatten))))

(defn- parse-default-assets
  [default-assets]
  (->> default-assets
       (filter (fn [{:keys [type]}]
                 (or (= type "image")
                     (= type "animation"))))
       (map :url)))

(defn- parse-scene-assets
  [scene-data]
  (->> (:assets scene-data)
       (map :url)))

(defn- get-animation-url
  [animation-name]
  (str "/raw/anim/" animation-name "/skeleton.json"))

(defn- parse-scene-objects
  [scene-data]
  (->> (:objects scene-data)
       (reduce (fn [result [_ {:keys [type] :as object-data}]]
                 (case type
                   "background" (conj result (:src object-data))
                   "layered-background" (concat result [(get-in object-data [:background :src])
                                                        (get-in object-data [:decoration :src])
                                                        (get-in object-data [:surface :src])])
                   "image" (conj result (:src object-data))
                   "animation" (let [animation-name (:name object-data)]
                                 (conj result [animation-name (get-animation-url animation-name)]))
                   result))
               [])))

(defn- parse-scene-audio
  [scene-data]
  (->> (:audio scene-data)
       (vals)))

(defn- parse-additional-resources
  []
  (let [next-activity @(re-frame/subscribe [::subs/after-current-activity])]
    [(:preview next-activity)]))

(defn get-scene-resources
  [scene-id scene-data]
  (->> (concat (parse-concept-resources scene-id)
               (parse-scene-assets scene-data)
               (parse-scene-objects scene-data)
               (parse-scene-audio scene-data)
               (parse-default-assets default-game-assets)
               (parse-additional-resources))
       (remove #(or (nil? %) (empty? %)))
       (distinct)))
