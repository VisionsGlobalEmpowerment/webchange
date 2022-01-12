(ns webchange.utils.scene-action-data
  (:require
    [clojure.string :as string]))

(def action-tags {:user-interactions-blocked "user-interactions-blocked"
                  :fx                        "fx"})

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
      (contains? action-data :phrase-text)
      (-> (:editor-type action-data)
          (= "dialog"))))

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
  {:main    5
   :idle    0
   :eyes    4
   :mouth   3
   :hands   2
   :emotion 1
   :default 5})


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

;; Movements

(defn create-character-movement-action
  [{:keys [action character target]}]
  {:pre [(string? action)
         (string? character)
         (string? target)
         (not= character target)]}
  {:type          "char-movement"
   :action        action
   :transition-id character
   :target        target})

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
  (if (map? action)
    (get-nth-in action inner-action-path)))

(defn update-inner-action
  [action data-patch]
  (update-in action inner-action-path merge data-patch))

(def text-animation-action {:type "sequence-data"
                            :data [{:type     "empty"
                                    :duration 0}
                                   {:type        "text-animation"
                                    :animation   "color"
                                    :fill        0x00B2FF
                                    :phrase-text "Text animation"
                                    :audio       nil}]})

(defn create-text-animation-action
  [{:keys [inner-action] :or {inner-action {}}}]
  (-> text-animation-action
      (update-inner-action inner-action)))
