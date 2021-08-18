(ns webchange.utils.scene-action-data
  (:require
    [clojure.string :as string]))

(defn get-action-type
  [action-data]
  (get action-data :type))

(defn text-animation-action?
  [action-data]
  (-> (get-action-type action-data)
      (= "text-animation")))

(defn animation-sequence-action?
  [action-data]
  (-> (get-action-type action-data)
      (= "animation-sequence")))

(defn dialog-action?
  [action-data]
  (contains? action-data :phrase))

(defn fix-available-effect
  [available-effect]
  (if (string? available-effect)
    {:name   (string/replace available-effect "-" " ")
     :action available-effect}
    available-effect))

(defn get-available-effects
  [action-data]
  (->> (get action-data :available-activities [])
       (map fix-available-effect)))

;; Animation

(def animation-tracks
  {:main    0
   :mouth   1
   :hands   2
   :emotion 3
   :default 0})

(defn create-add-animation-action
  [{:keys [animation loop? target track] :or {loop? false}}]
  {:pre [(string? animation) (string? target)]}
  (let [track-number (cond
                       (number? track) track
                       (keyword? track) (get animation-tracks track)
                       (string? track) (->> track (keyword) (get animation-tracks))
                       :else (:default animation-tracks))]
    {:type   "add-animation"
     :id     animation
     :loop   loop?
     :target target
     :track  track-number}))

(defn create-remove-animation-action
  [{:keys [target track]}]
  {:pre [(string? target)]}
  (let [track-number (cond
                       (number? track) track
                       (keyword? track) (get animation-tracks track)
                       (string? track) (->> track (keyword) (get animation-tracks))
                       :else (:default animation-tracks))]
    {:type   "remove-animation"
     :target target
     :track  track-number}))
