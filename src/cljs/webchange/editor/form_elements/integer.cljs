(ns webchange.editor.form-elements.integer
  (:require
    [webchange.editor.form-elements.controlled-input :refer [ControlledInput]]))

(defn- integer-string?
  [value]
  (->> value
       (re-matches #"^[-+]?[0-9]+$")
       (boolean)))

(defn- parser
  [value]
  (if (integer-string? value)
    (js/parseInt value)
    nil))

(defn IntegerInput
  []
  (ControlledInput {:parser parser}))
