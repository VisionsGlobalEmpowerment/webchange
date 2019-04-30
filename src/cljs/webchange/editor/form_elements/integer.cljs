(ns webchange.editor.form-elements.integer
  (:require
    [webchange.editor.form-elements.controlled-input :refer [ControlledInput]]))

(defn parser
  [value]
  (if (re-matches #"^[-+]?[0-9]+$" value)
    (js/parseInt value)
    nil))

(defn IntegerInput
  []
  (ControlledInput {:parser parser}))
