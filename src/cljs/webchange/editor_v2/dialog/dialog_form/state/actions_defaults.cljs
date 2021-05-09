(ns webchange.editor-v2.dialog.dialog-form.state.actions-defaults)

(def empty-action-position 0)
(def inner-action-position 1)

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
                                    :phrase-text "New text animation",
                                    :audio       nil}]})

(defn get-empty-action
  [action]
  (get-in action [:data empty-action-position]))

(defn get-inner-action
  [action]
  (get-in action [:data inner-action-position]))

(defn get-action
  [name]
  {:type "sequence-data"
   :data [{:type     "empty"
           :duration 0}
          {:type "action"
           :id   name}]})
