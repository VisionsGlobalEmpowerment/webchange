(ns webchange.editor-v2.activity-dialogs.common.character-movements)

(defn get-action-text
  [action {:keys [character-name character-data target-name target-data]}]
  (let [character (or (:alias character-data) character-name)
        target (or (:alias target-data) target-name)]
    (-> (case action
          "go-to" (str character " goes to " target)
          "pick-up" (str character " picks up " target)
          "give" (str character " gives item to " target)
          action)
        clojure.string/capitalize)))
