(ns webchange.utils.scene-object-data)

(defn get-type
  [object-data]
  (get object-data :type))

(defn single-background?
  [object-data]
  (-> (get-type object-data)
      (= "background")))

(defn layered-background?
  [object-data]
  (-> (get-type object-data)
      (= "layered-background")))

(defn background?
  [object-data]
  (or (single-background? object-data)
      (layered-background? object-data)))

(defn get-editable-data
  [object-data]
  (get object-data :editable?))

(defn editable?
  [object-data]
  (-> (get-editable-data object-data)
      (some?)))
