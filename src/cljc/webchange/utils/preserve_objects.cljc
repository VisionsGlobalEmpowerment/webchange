(ns webchange.utils.preserve-objects
  (:require
    [webchange.utils.scene-object-data :as utils]))

(defn- editable-object-names
  [{:keys [objects]}]
  (->> objects
       (filter (fn [[_ object]] (or (utils/editable? object)
                                    (utils/background? object))))
       (map first)))

(defn- preserve-background
  [prev-data _]
  prev-data)

(defn- get-object-keys-to-update
  [{:keys [editable? type]}]
  (cond-> [:type :editable? :origin :max-width :max-height :image-size :metadata :actions :filters]
          (and
           (-> (get editable? :drag) true? not)
           (not (true? editable?))) (concat [:x :y])
          (not editable?) (concat [:visible])
          (= type "group") (concat [:children])
          (not= type "text") (concat [:width :height])))

(defn- preserve-default
  [prev-data new-data]
  (let [object-keys-to-update (get-object-keys-to-update new-data)
        object-props-to-update (select-keys new-data object-keys-to-update)
        dissoc-updated #(apply dissoc % object-keys-to-update)]
    (-> new-data
        (merge prev-data)
        (dissoc-updated)
        (merge object-props-to-update))))

(defn- update-object
  [created-activity]
  (fn [[key object]]
    (let [created-object (get-in created-activity [:objects key])]
      [key (cond
             (utils/background? object) (preserve-background object created-object)
             :else (preserve-default object created-object))])))

(defn- preserve-objects
  [scene-data created-activity]
  (let [object-names (->> (editable-object-names scene-data)
                          (filter #(contains? (:objects created-activity) %)))
        preserve-objects (-> scene-data
                             :objects
                             (select-keys object-names))]
    (->> preserve-objects
         (map (update-object created-activity))
         (into {}))))

(defn update-preserved-objects
  [new-activity-data old-activity-data]
  (->> (preserve-objects old-activity-data new-activity-data)
       (update new-activity-data :objects merge)))
