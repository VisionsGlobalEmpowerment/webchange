(ns webchange.templates.core)

(def templates (atom {}))

(defn register-template
  [id metadata template]
  (swap! templates assoc id {:metadata metadata
                             :template template}))

(defn get-available-templates
  []
  (->> @templates
      (map second)
      (map :metadata)))

(defn activity-from-template
  [{id :template-id :as data}]
  (let [template (get-in @templates [id :template])]
    (template data)))
