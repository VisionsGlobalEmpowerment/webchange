(ns webchange.editor-v2.dialog.dialog-text-form.action-unit.utils)

(defn get-effect-name
  [effect]
  (clojure.string/replace effect "-" " "))
