(ns webchange.utils.scene-common-actions
  (:require
    [webchange.utils.list :as lists]
    [webchange.utils.scene-action-data :refer [get-inner-action]]
    [webchange.utils.scene-data :refer [find-and-change-action-recursively get-available-actions get-updates-history set-available-actions set-updates-history]]))

;; Common

(defn- remove-from-objects
  [scene-data object-name]
  (update scene-data :objects dissoc (keyword object-name)))

(defn- remove-from-scene-objects
  [scene-data object-name]
  (update scene-data :scene-objects #(map (fn [layer]
                                            (lists/without-item layer object-name)) %)))

(defn- remove-from-actions
  [scene-data action-name]
  (update scene-data :actions dissoc (keyword action-name)))

(defn- remove-from-available-actions
  [scene-data action-name]
  (->> (get-available-actions scene-data)
       (filter (fn [{:keys [action]}]
                 (not= action action-name)))
       (set-available-actions scene-data)))

;; Remove Image

(defn- remove-from-assets
  [scene-data image-src]
  (update scene-data :assets lists/remove-by-predicate #(= (:url %) image-src)))

(defn- remove-from-dialogs
  [scene-data target-action-name]
  (->> (:actions scene-data)
       (map (fn [[action-name action-data]]
              [action-name
               (if (sequential? (:data action-data))
                 (->> (:data action-data)
                      (filter (fn [dialog-item]
                                (let [{:keys [type id]} (get-inner-action dialog-item)]
                                  (not (and (= type "action")
                                            (= id target-action-name))))))
                      (assoc action-data :data))
                 action-data)]))
       (into {})
       (assoc scene-data :actions)))

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
