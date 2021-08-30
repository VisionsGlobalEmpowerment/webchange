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
  (or (contains? action-data :phrase)
      (contains? action-data :phrase-text)))

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
   :mouth   3
   :hands   2
   :emotion 1
   :default 0})

(defn create-add-animation-action
  [{:keys [animation loop? target track] :or {loop? true}}]
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

;; Dialogs

(defn get-nth-in [data path]
  (reduce
    (fn [current-data path-step]
      (if (associative? current-data)
        (get current-data path-step)
        (nth current-data path-step nil)))
    data
    path))

(def empty-action-position 0)
(def inner-action-position 1)

(def empty-action-path [:data empty-action-position])
(def inner-action-path [:data inner-action-position])

(defn get-inner-action
  [action]
  (get-nth-in action inner-action-path))
