(ns webchange.interpreter.resources-manager.scene-parser
  (:require
    [re-frame.core :as re-frame]
    [webchange.interpreter.defaults :refer [default-game-assets]]
    [webchange.interpreter.subs :as subs]))

;{:name word-image-4, :type image, :scenes [sandbox cycling volleyball], :template nil}


;dev:cljs.user=> (-> @re-frame.db/app-db (get-in [:lessons "ls4"]))
;{:id 319,
; :name "ls4",
; :dataset-id 4,
; :data {:items [{:id 159} {:id 140} {:id 157}]},
; :item-ids (159 140 157)}
;dev:cljs.user=>

(defn- get-concept-fields
  [scene-id dataset-fields]
  (->> dataset-fields
       (reduce (fn [result {:keys [name type scenes]}]
                 (if (and (= type "image")
                          (some #{scene-id} scenes))
                   (conj result name)
                   result))
               [])
       (map keyword)))

(defn- parse-concept-resources
  [scene-id]
  (let [lesson-sets @(re-frame/subscribe [::subs/current-lesson-sets])
        lesson-sets-data @(re-frame/subscribe [::subs/lesson-sets-data lesson-sets])]
    (->> lesson-sets-data
         (reduce (fn [resources {:keys [dataset-id item-ids]}]
                   (let [dataset @(re-frame/subscribe [::subs/course-dataset dataset-id])
                         concept-fields (get-concept-fields scene-id (get-in dataset [:scheme :fields]))]
                     (concat resources (reduce (fn [result dataset-id]
                                                 (let [dataset @(re-frame/subscribe [::subs/dataset-item dataset-id])
                                                       dataset-fields (select-keys (:data dataset) concept-fields)]
                                                   (concat result (map second dataset-fields))))
                                               []
                                               item-ids))))
                 [])
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
                                                        (get-in object-data [:decoration :src])])
                   "image" (conj result (:src object-data))
                   "animation" (let [animation-name (:name object-data)]
                                 (conj result [animation-name (get-animation-url animation-name)]))
                   nil))
               [])))

(defn get-scene-resources
  [scene-id scene-data]
  (->> (concat (parse-concept-resources scene-id)
               (parse-scene-assets scene-data)
               (parse-scene-objects scene-data)
               (parse-default-assets default-game-assets))
       (remove nil?)
       (distinct)))
