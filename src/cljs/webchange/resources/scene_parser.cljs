(ns webchange.resources.scene-parser
  (:require
    [re-frame.core :as re-frame]
    [webchange.resources.default-resources :refer [default-game-assets]]
    [webchange.interpreter.renderer.scene.components.animation.animation-params :refer [get-animations-resource-path
                                                                                        get-animations-resources]]
    [webchange.interpreter.subs :as subs]
    [webchange.logger.index :as logger]))

(defn- cleanup-resources
  [resources]
  (->> resources
       (remove #(or (nil? %) (empty? %)))
       (distinct)
       (doall)))

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
  (logger/trace "lesson-sets-data" lesson-sets-data)
  (->> lesson-sets-data
       (mapcat (fn [{:keys [item-ids dataset-id]}]
                 (for [item-id item-ids
                       [field-name field-type] (get-concept-fields scene-id dataset-id)]
                   (let [item @(re-frame/subscribe [::subs/dataset-item item-id])]
                     (parse-concept-field field-type (get-in item [:data field-name]))))))
       (flatten)
       (cleanup-resources)))

(defn- parse-concept-resources
  [scene-id]
  (logger/group-folded (str "get concept resources: " scene-id))
  (->> @(re-frame/subscribe [::subs/current-lesson-sets-data])
       (parse-lesson-sets-data scene-id)
       (logger/with-trace-list)
       (logger/->>with-group-end (str "get concept resources: " scene-id))))

(defn- parse-lesson-sets-resources
  [scene-id lesson-sets]
  (logger/group-folded (str "get lesson sets resources: " scene-id))
  (logger/trace-list-folded "lesson-sets" lesson-sets)
  (->> @(re-frame/subscribe [::subs/lesson-sets-data lesson-sets])
       (parse-lesson-sets-data scene-id)
       (logger/->>with-trace-list-folded "lesson sets resources")
       (logger/->>with-group-end (str "get lesson sets resources: " scene-id))))

(defn- parse-default-assets
  []
  (->> default-game-assets
       (filter (fn [{:keys [type]}]
                 (or (= type "image")
                     (= type "animation"))))
       (map :url)))

(defn- parse-scene-assets
  [scene-data]
  (logger/group-folded "get scene assets resources")
  (->> (:assets scene-data)
       (map :url)
       (logger/with-trace-list)
       (logger/->>with-group-end "get scene assets resources")))

(defn- get-animation-url
  [animation-name]
  (str "/raw/anim/" animation-name "/skeleton.json"))

(defn get-animation-resources
  ([animation-name]
   (get-animation-resources animation-name false))
  ([animation-name expand?]
   (if expand?
     (let [animations-resources (get-animations-resources)]
       (->> (keyword animation-name)
            (get animations-resources)))
     [[animation-name (get-animations-resource-path animation-name "skeleton.json")]])))

(defn- parse-scene-objects
  [scene-data {:keys [expand-animation-resources?]
               :or   {expand-animation-resources? false}}]
  (logger/group-folded "get scene objects resources")
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
               [])
       (logger/with-trace-list)
       (logger/->>with-group-end "get scene objects resources")))

(defn- parse-scene-audio
  [scene-data]
  (logger/group-folded "get scene audio resources")
  (->> (:audio scene-data)
       (vals)
       (logger/with-trace-list)
       (logger/->>with-group-end "get scene audio resources")))

(defn- parse-scene-metadata
  [scene-data]
  (logger/group-folded "get scene metadata resources")
  (-> scene-data
      (get-in [:metadata :resources])
      (logger/with-trace-list)
      (logger/->with-group-end "get scene metadata resources")))

(defn- get-scene-resources
  ([scene-data]
   (get-scene-resources scene-data {}))
  ([scene-data options]
   (concat (parse-scene-assets scene-data)
           (parse-scene-objects scene-data options)
           (parse-scene-audio scene-data)
           (parse-scene-metadata scene-data))))

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
        next-activity-preview (->> (:activity-name next-activity)
                                   (keyword)
                                   (parse-scenes-previews))]
    [next-activity-preview]))

(defn- parse-additional-resources
  []
  ["/raw/img/ui/activity_finished/bg.png"
   "/raw/img/ui/activity_finished/form.png"
   "/raw/img/ui/activity_finished/home.png"
   "/raw/img/ui/activity_finished/next.png"
   "/raw/img/ui/activity_finished/vera.png"
   ["ui-shooting-star" "/raw/anim/ui-shooting-star/skeleton.json"]
   "/raw/img/bg.png"
   "/raw/img/cross.png"
   "/raw/img/ui/logo.png"
   "/raw/img/questions/sound-icon.png"
   "/raw/img/questions/sound-icon-white.png"
   "/raw/img/questions/skip.png"
   "/raw/img/ui/guide/bg-01.png"
   ["teacher" "/raw/anim/teacher/skeleton.json"]
   ["student" "/raw/anim/student/skeleton.json"]
   ["lion" "/raw/anim/senoravaca/skeleton.json"]
   ["guide" "/raw/anim/guide/skeleton.json"]])

(defn get-activity-resources
  [scene-id scene-data]
  (logger/group-folded (str "get activity resources: " scene-id))
  (->> (concat (parse-concept-resources scene-id)
               (get-scene-resources scene-data)
               (parse-default-assets)
               (parse-next-activity-preview)
               (parse-additional-resources))
       (cleanup-resources)
       (logger/->>with-group-end (str "get activity resources: " scene-id))))

(defn get-lesson-resources
  [lesson scenes-data]
  (logger/group-folded (str "get lesson resources: " (:name lesson)))
  (let [scenes-resources (->> (:activities lesson)
                              (map (fn [scene-name]
                                     (logger/group-folded (str "get scene resources: " scene-name))
                                     (-> (get scenes-data scene-name)
                                         (get-scene-resources {:expand-animation-resources? true})
                                         (logger/->with-group-end (str "get scene resources: " scene-name)))))
                              (flatten)
                              (cleanup-resources))
        concepts-resources (->> (:activities lesson)
                                (map (fn [scene-id]
                                       (parse-lesson-sets-resources scene-id (:lesson-sets lesson))))
                                (flatten)
                                (cleanup-resources))
        default-assets (-> (parse-default-assets)
                           (cleanup-resources))
        scenes-previews (-> (parse-scenes-previews)
                            (cleanup-resources))
        additional-resources (-> (parse-additional-resources)
                                 (cleanup-resources))]

    (logger/group-folded "result")
    (logger/trace-list-folded "scenes-resources" scenes-resources)
    (logger/trace-list-folded "concepts resources" concepts-resources)
    (logger/trace-list-folded "default assets" default-assets)
    (logger/trace-list-folded "scenes previews" scenes-previews)
    (logger/trace-list-folded "additional resources" additional-resources)
    (logger/group-end "result")

    (->> (concat scenes-resources
                 concepts-resources
                 default-assets
                 scenes-previews
                 additional-resources)
         (cleanup-resources)
         (logger/->>with-group-end (str "get lesson resources: " (:name lesson))))))

(defn get-lesson-endpoints
  [course-slug {:keys [activities]}]
  (logger/group-folded (str "get lesson endpoints"))
  (logger/trace "course-slug" course-slug)
  (logger/trace "activities" activities)
  (->> activities
       (map (fn [activity-name]
              (str "/api/courses/" course-slug "/scenes/" activity-name)))
       (concat [(str "/api/courses/" course-slug)
                (str "/api/courses/" course-slug "/lesson-sets")])
       (doall)
       (logger/->>with-trace-list-folded "result")
       (logger/->>with-group-end "get lesson endpoints")))
