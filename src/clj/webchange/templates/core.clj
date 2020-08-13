(ns webchange.templates.core)

(def templates (atom {}))

(defn register-template
  [name metadata template]
  (swap! templates assoc name {:metadata metadata
                               :template template}))

(defn get-available-templates
  []
  (->> @templates
      (map second)
      (map :metadata)))
