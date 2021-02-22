(ns webchange.templates.library.flipbook.actions-utils)

(def defaults {:text-fill   0x00B2FF
               :phrase-text "New action"})

(declare create-empty-action)
(declare create-animation-sequence-action)
(declare create-text-animation-action)
(declare create-dialog-action)

(defn- create-action
  [type data]
  (case type
    :empty (create-empty-action data)
    :animation-sequence (create-animation-sequence-action data)
    :text-animation (create-text-animation-action data)
    :dialog (create-dialog-action data)))

(defn- create-empty-action
  ([]
   (create-empty-action {}))
  ([_]
   {:type "empty" :duration 0}))

(defn- create-animation-sequence-action
  ([]
   (create-animation-sequence-action {}))
  ([{:keys [phrase-text]
     :or   {phrase-text (:phrase-text defaults)}}]
   {:type        "animation-sequence"
    :audio       nil
    :phrase-text phrase-text}))

(defn- create-text-animation-action
  [{:keys [animation fill phrase-text target]
    :or   {animation   "color"
           fill        (:text-fill defaults)
           phrase-text (:phrase-text defaults)}}]
  {:pre [(string? target)]}
  {:type        "text-animation"
   :animation   animation
   :audio       nil
   :data        []
   :target      target
   :fill        fill
   :phrase-text phrase-text})

(defn create-dialog-action
  [{:keys [concept-var phrase dialog-actions phrase-description]
    :or   {concept-var        "current-word"
           dialog-actions     [(create-animation-sequence-action)]
           phrase-description "Page Action"}}]
  {:type               "sequence-data"
   :editor-type        "dialog"
   :phrase             phrase
   :phrase-description phrase-description
   :concept-var        concept-var
   :data               (->> dialog-actions
                            (map (fn [action]
                                   {:type "sequence-data"
                                    :data [(create-empty-action)
                                           (if (map? action)
                                             action
                                             (create-action (first action) (second action)))]}))
                            (vec))})
