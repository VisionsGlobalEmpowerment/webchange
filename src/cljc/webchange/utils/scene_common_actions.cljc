(ns webchange.utils.scene-common-actions
  (:require
    [webchange.utils.list :as lists]
    [webchange.utils.scene-action-data :refer [get-inner-action]]
    [webchange.utils.scene-data :refer [find-and-change-action-recursively get-available-actions get-updates-history set-available-actions set-updates-history]]))

;; Common

(defn- remove-from-assets
  [scene-data asset-src]
  (let [assets (-> (get scene-data :assets)
                   (vec))]
    (assoc scene-data :assets (lists/remove-by-predicate assets #(= (:url %) asset-src)))))

(defn- remove-from-objects
  [scene-data object-name]
  (update scene-data :objects dissoc (keyword object-name)))

(defn- remove-from-scene-objects
  [scene-data object-name]
  (let [scene-objects (->> (get scene-data :scene-objects [])
                           (map #(lists/without-item % object-name))
                           (filter #(not (empty? %)))
                           (vec))]
    (assoc scene-data :scene-objects scene-objects)))

(defn- remove-from-actions
  [scene-data action-name]
  (update scene-data :actions dissoc (keyword action-name)))

(defn- remove-from-available-actions
  [scene-data action-name]
  (->> (get-available-actions scene-data)
       (filter (fn [{:keys [action]}]
                 (not= action action-name)))
       (set-available-actions scene-data)))

(defn- remove-from-dialogs
  [scene-data target-action-name]
  (->> (:actions scene-data)
       (map (fn [[action-name action-data]]
              [action-name
               (if (sequential? (:data action-data))
                 (->> (:data action-data)
                      ;; Remove 'in-sequence' actions
                      (filter (fn [dialog-item]
                                (let [{:keys [type id]} (get-inner-action dialog-item)]
                                  (not (and (= type "action")
                                            (= id target-action-name))))))
                      ;; Remove actions in parallel sequences
                      (map (fn [dialog-item]
                             (if (= (:type dialog-item) "parallel")
                               (->> (:data dialog-item)
                                    (filter (fn [parallel-item]
                                              (let [{:keys [type id]} (get-inner-action parallel-item)]
                                                (not (and (= type "action")
                                                          (= id target-action-name))))))
                                    (assoc dialog-item :data))
                               dialog-item)))
                      (filter (fn [{:keys [data]}]
                                (not (and (sequential? data)
                                          (empty? data)))))
                      (assoc action-data :data))
                 action-data)]))
       (into {})
       (assoc scene-data :actions)))

;; Remove Image

(defn- remove-related-image-action
  [scene-data related-actions]
  (reduce (fn [scene-data action-name]
            (-> scene-data
                (remove-from-actions action-name)
                (remove-from-dialogs action-name)
                (remove-from-available-actions action-name)))
          scene-data
          related-actions))

(defn remove-image
  [scene-data {image-name :name}]
  (let [image-data (get-in scene-data [:objects (keyword image-name)])
        {image-src :src links :links} image-data
        related-actions (->> links
                             (filter #(= (:type %) "action"))
                             (map :id))]
    (-> scene-data
        (remove-from-assets image-src)
        (remove-from-objects image-name)
        (remove-from-scene-objects image-name)
        (remove-related-image-action related-actions))))

;; Remove Character

(defn- remove-related-character-action
  [scene-data related-actions]
  (reduce (fn [scene-data action-name]
            (-> scene-data
                (remove-from-actions action-name)
                (remove-from-dialogs action-name)
                (remove-from-available-actions action-name)))
          scene-data
          related-actions))

(defn- remove-character-from-dialogs
  [scene-data character-name]
  (->> (find-and-change-action-recursively
         (:actions scene-data)
         (fn [{:keys [target]}]
           (= target character-name))
         (fn [action-data]
           (assoc action-data :target nil)))
       (assoc scene-data :actions)))

(defn remove-character
  [scene-data {character-name :name}]
  (let [character-data (get-in scene-data [:objects (keyword character-name)])
        {links :links} character-data
        related-actions (->> links
                             (filter #(= (:type %) "action"))
                             (map :id))]
    (-> scene-data
        (remove-from-objects character-name)
        (remove-from-scene-objects character-name)
        (remove-character-from-dialogs character-name)
        (remove-related-character-action related-actions))))

;; Remove Question

(defn- remove-question-assets
  [scene-data assets]
  (reduce (fn [scene-data asset-url]
            (remove-from-assets scene-data asset-url))
          scene-data
          assets))

(defn- remove-question-objects
  [scene-data objects]
  (reduce (fn [scene-data object-name]
            (remove-from-objects scene-data object-name))
          scene-data
          objects))

(defn- remove-question-scene-objects
  [scene-data question-name]
  (remove-from-scene-objects scene-data question-name))

(defn- remove-question-actions
  [scene-data actions]
  (reduce (fn [scene-data action-name]
            (remove-from-actions scene-data action-name))
          scene-data
          actions))

(defn- remove-from-tracks
  [scene-data question-name]
  (->> (get-in scene-data [:metadata :tracks])
       (filter (fn [{:keys [question-id]}]
                 (not= question-id question-name)))
       (assoc-in scene-data [:metadata :tracks])))

(defn remove-question
  [scene-data {question-name :name}]
  (let [metadata (get-in scene-data [:objects (keyword question-name) :metadata])
        {:keys [assets actions objects]} metadata]
    (-> scene-data
        (remove-question-assets assets)
        (remove-question-objects objects)
        (remove-question-scene-objects question-name)
        (remove-question-actions actions)
        (remove-from-available-actions question-name)
        (remove-from-dialogs question-name)
        (remove-from-tracks question-name))))

(defn remove-anchor
  [scene-data {anchor-name :name}]
  (-> scene-data
      (remove-from-objects anchor-name)
      (remove-from-scene-objects anchor-name)))
