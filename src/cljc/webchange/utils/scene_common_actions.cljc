(ns webchange.utils.scene-common-actions
  (:require
    [webchange.utils.list :as lists]
    [webchange.utils.scene-action-data :refer [get-inner-action]]
    [webchange.utils.scene-data :refer [get-available-actions get-updates-history set-available-actions set-updates-history]]))

;; Remove Image

(defn- remove-from-assets
  [scene-data image-src]
  (update scene-data :assets lists/remove-by-predicate #(= (:url %) image-src)))

(defn- remove-from-objects
  [scene-data image-name]
  (update scene-data :objects dissoc (keyword image-name)))

(defn- remove-from-scene-objects
  [scene-data image-name]
  (update scene-data :scene-objects #(map (fn [layer]
                                            (lists/without-item layer image-name)) %)))

(defn- remove-from-actions
  [scene-data action-name]
  (update scene-data :actions dissoc (keyword action-name)))

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

(defn- remove-from-available-actions
  [scene-data action-name]
  (->> (get-available-actions scene-data)
       (filter (fn [{:keys [action]}]
                 (not= action action-name)))
       (set-available-actions scene-data)))

(defn- remove-related-action
  [scene-data related-actions]
  (reduce (fn [scene-data action-name]
            (-> scene-data
                (remove-from-actions action-name)
                (remove-from-dialogs action-name)
                (remove-from-available-actions action-name)))
          scene-data
          related-actions))

(defn- remove-from-updates-history
  [scene-data image-src]
  (->> (get-updates-history scene-data)
       (filter (fn [{:keys [action data]}]
                 (not (and (= action "add-image")
                           (= (get-in data [:image :src]) image-src)))))
       (set-updates-history scene-data)))

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
        (remove-related-action related-actions)
        (remove-from-updates-history image-src))))
