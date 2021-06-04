(ns webchange.editor-v2.dialog.utils.dialog-action)

(def empty-action-position 0)
(def inner-action-position 1)

(def inner-action-path [:data inner-action-position])

(def default-phrase-text "New action")
(def default-action {:type "sequence-data"
                     :data [{:type     "empty"
                             :duration 0},
                            {:type        "animation-sequence",
                             :phrase-text default-phrase-text,
                             :audio       nil}]})

(def default-concept-action {:type "sequence-data",
                             :data [default-action]})

(def text-animation-action {:type "sequence-data"
                            :data [{:type     "empty"
                                    :duration 0},
                                   {:type        "text-animation",
                                    :animation   "color"
                                    :fill        0x00B2FF
                                    :phrase-text "Text animation",
                                    :audio       nil}]})

(defn get-empty-action
  [action]
  (get-in action [:data empty-action-position]))

(defn get-inner-action
  [action]
  (get-in action inner-action-path))

(defn update-inner-action
  [action data-patch]
  (update-in action inner-action-path merge data-patch))

(defn update-inner-concept-action
  [action data-patch]
  (update-in action [:data 0 :data inner-action-position] merge data-patch))

(defn get-action-data
  [{:keys [action-name action-data]}]
  {:pre [(string? action-name)
         (or (nil? action-data)
             (map? action-data))]}
  {:type "sequence-data"
   :data [{:type     "empty"
           :duration 0}
          (cond-> {:type "action"
                   :id   action-name}
                  (some? action-data) (merge action-data))]})

(defn get-effect-action-data
  [{:keys [action-name]}]
  {:pre [(string? action-name)]}
  (get-action-data {:action-name action-name
                    :action-data {}}))
