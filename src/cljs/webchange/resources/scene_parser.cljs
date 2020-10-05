(ns webchange.resources.scene-parser
  (:require
    [re-frame.core :as re-frame]
    [webchange.resources.default-resources :refer [default-game-assets]]
    [webchange.interpreter.renderer.scene.components.animation.animation-params :refer [get-animations-resource-path
                                                                                        get-animations-resources]]
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

(defn- parse-lesson-sets-data
  [scene-id lesson-sets-data]
  (->> lesson-sets-data
       (mapcat (fn [{:keys [item-ids dataset-id]}]
                 (for [item-id item-ids
                       [field-name field-type] (get-concept-fields scene-id dataset-id)]
                   (let [item @(re-frame/subscribe [::subs/dataset-item item-id])]
                     (parse-concept-field field-type (get-in item [:data field-name]))))))
       (flatten)))

(defn- parse-concept-resources
  [scene-id]
  (->> @(re-frame/subscribe [::subs/current-lesson-sets-data])
       (parse-lesson-sets-data scene-id)))

(defn- parse-lesson-sets-resources
  [scene-id lesson-sets]
  (->> @(re-frame/subscribe [::subs/lesson-sets-data lesson-sets])
       (parse-lesson-sets-data scene-id)))

(defn- parse-default-assets
  []
  (->> default-game-assets
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

(defn- get-animation-resources
  [animation-name expand?]
  (if expand?
    (let [animations-resources (get-animations-resources)]
      (->> (keyword animation-name)
           (get animations-resources)))
    [[animation-name (get-animations-resource-path animation-name "skeleton.json")]]))

(defn- parse-scene-objects
  [scene-data {:keys [expand-animation-resources?]
               :or   {expand-animation-resources? false}}]
  (->> (:objects scene-data)
       (reduce (fn [result [_ {:keys [type] :as object-data}]]
                 (case type
                   "background" (conj result (:src object-data))
                   "layered-background" (concat result [(get-in object-data [:background :src])
                                                        (get-in object-data [:decoration :src])
                                                        (get-in object-data [:surface :src])])
                   "image" (conj result (:src object-data))
                   "animation" (let [animation-name (:name object-data)]
                                 (concat result (get-animation-resources animation-name expand-animation-resources?)))
                   result))
               [])))

(defn- parse-scene-audio
  [scene-data]
  (->> (:audio scene-data)
       (vals)))

(defn- parse-scene
  ([scene-data]
   (parse-scene scene-data {}))
  ([scene-data options]
   (concat (parse-scene-assets scene-data)
           (parse-scene-objects scene-data options)
           (parse-scene-audio scene-data))))

(defn- parse-scenes-previews
  ([]
   (parse-scenes-previews nil))
  ([scene]
   (let [course-scenes @(re-frame/subscribe [::subs/course-scenes])
         previews (->> course-scenes
                       (map (fn [[scene-name {:keys [preview]}]]
                              [scene-name preview]))
                       (remove (fn [[_ preview]] (nil? preview)))
                       (into {}))]
     (if (some? scene)
       (get previews scene)
       (vals previews)))))

(defn- parse-next-activity-preview
  []
  (let [next-activity @(re-frame/subscribe [::subs/after-current-activity])
        next-activity-preview (->> (:activity next-activity)
                                   (keyword)
                                   (parse-scenes-previews))]
    [next-activity-preview]))

(defn- parse-additional-resources
  []
  ["/raw/img/bg.jpg"
   "/raw/img/ui/logo.png"])

(defn- cleanup-resources
  [resources]
  (->> resources
       (remove #(or (nil? %) (empty? %)))
       (distinct)))

(defn get-scene-resources
  [scene-id scene-data]
  (->> (concat (parse-concept-resources scene-id)
               (parse-scene scene-data)
               (parse-default-assets)
               (parse-next-activity-preview)
               (parse-additional-resources))
       (cleanup-resources)))

(defn get-lesson-resources
  [lesson scenes-data]
  (let [scenes-resources (->> (:activities lesson)
                              (map (fn [scene-name]
                                     (-> (get scenes-data scene-name)
                                         (parse-scene {:expand-animation-resources? true}))))
                              (flatten))
        concepts-resources (->> (:activities lesson)
                                (map (fn [scene-id]
                                       (parse-lesson-sets-resources scene-id (:lesson-sets lesson))))
                                (flatten))
        default-assets (parse-default-assets)
        scenes-previews (parse-scenes-previews)
        additional-resources (parse-additional-resources)]
    (->> (concat scenes-resources
                 concepts-resources
                 default-assets
                 scenes-previews
                 additional-resources)
         (cleanup-resources))))

(defn get-lesson-endpoints
  [course-slug {:keys [activities]}]
  (->> activities
       (map (fn [activity-name]
              (str "/api/courses/" course-slug "/scenes/" activity-name)))
       (concat ["/api/schools/current"
                (str "/api/courses/" course-slug)
                (str "/api/courses/" course-slug "/lesson-sets")])))
