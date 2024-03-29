(ns webchange.utils.scene-action-data
  (:require
    [clojure.string :as string]
    [webchange.utils.uid :refer [get-uid]]))

(def action-tags {:user-interactions-blocked "user-interactions-blocked"
                  :fx                        "fx"})

(def animation-tracks
  {:main    5
   :idle    0
   :eyes    4
   :mouth   3
   :hands   2
   :emotion 1
   :default 5})

(def action-templates
  {"activity-action"    {:type "action"
                         :id   "__tid__"}
   "add-animation"      {:type   "add-animation"
                         :id     "idle"
                         :loop   true
                         :target "__target__"
                         :track  0}
   "remove-animation"   {:type   "remove-animation"
                         :target "__target__"
                         :track  0}
   "animation-sequence" {:type               "animation-sequence"
                         :phrase-placeholder "Enter phrase text"
                         :audio              nil}
   "char-movement"      {:type          "char-movement"
                         :action        "__action__"
                         :transition-id "__character__"
                         :target        "__target__"}
   "effect"             {:type "__type__"}
   "text-animation"     {:type        "text-animation"
                         :animation   "color"
                         :fill        0x00B2FF
                         :phrase-text "Text animation"
                         :audio       nil}})

(defn- wrap-to-dialog-sequence-action
  [action-data]
  {:type "sequence-data"
   :data [{:type     "empty"
           :duration 0}
          action-data]
   :uid  (get-uid)})

(defn dialog-sequence-action?
  [{:keys [type data]}]
  (or (= type "parallel")
      (and (= type "sequence-data")
           (sequential? data)
           (= (count data) 2)
           (= (-> data first :type) "empty"))))

(defn- create-dialog-action
  ([action-type]
   (create-dialog-action action-type {}))
  ([action-type action-data]
   (-> (get action-templates action-type)
       (merge action-data)
       (wrap-to-dialog-sequence-action))))

(defn get-track-number
  [track]
  (cond
    (number? track) track
    (keyword? track) (get animation-tracks track)
    (string? track) (->> track (keyword) (get animation-tracks))
    :else (:default animation-tracks)))

(def create-dialog-activity-action #(create-dialog-action "activity-action" %))
(def create-dialog-animation-sequence-action #(create-dialog-action "animation-sequence"))
(def create-dialog-effect-action #(create-dialog-action "effect" %))
(def create-dialog-text-animation-action #(create-dialog-action "text-animation"))
(defn create-dialog-add-animation-action
  [{:keys [animation loop? target track] :or {loop? true}}]
  (create-dialog-action "add-animation" {:id     animation
                                         :loop?  loop?
                                         :target target
                                         :track  (get-track-number track)}))
(def create-dialog-char-movement-action #(create-dialog-action "char-movement" %))
(defn create-dialog-remove-animation-action
  [{:keys [target track]}]
  (create-dialog-action "remove-animation" {:target target
                                            :track  (get-track-number track)}))

(defn get-action-type
  [action-data]
  (get action-data :type))

(defn sequence-data-action?
  [action-data]
  (-> (get-action-type action-data)
      (= "sequence-data")))

(defn parallel-action?
  [action-data]
  (-> (get-action-type action-data)
      (= "parallel")))

(defn text-animation-action?
  [action-data]
  (-> (get-action-type action-data)
      (= "text-animation")))

(defn animation-sequence-action?
  [action-data]
  (-> (get-action-type action-data)
      (= "animation-sequence")))

(defn- dialog-inner-action?
  [{:keys [type audio]}]
  (and (= type "animation-sequence")
       (some? audio)))

(defn- dialog-main-action?
  [{:keys [type editor-type]}]
  (and (= type "sequence-data")
       (= editor-type "dialog")))

(defn dialog-action?
  [action-data]
  (or (dialog-inner-action? action-data)
      (dialog-main-action? action-data)))

(defn has-sub-actions?
  [action-data]
  (or (sequence-data-action? action-data)
      (parallel-action? action-data)))

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
      (when (some? current-data)
        (if (associative? current-data)
          (get current-data path-step)
          (nth current-data path-step nil))))
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

(defn update-empty-action
  [action data-patch]
  (update-in action empty-action-path merge data-patch))

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

(defn animation->display-name
  [effect-name]
  (cond
    (= effect-name "remove-animation") "Remove"
    :default (-> (or effect-name "")
                 (clojure.string/replace "emotion_" ""))))

(defn call-question-action?
  [{:keys [id type]}]
  (and (= type "action")
       (clojure.string/starts-with? id "question-")))
