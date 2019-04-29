(ns webchange.editor.form-elements.number
  (:require
    [webchange.editor.form-elements.controlled-input :refer [ControlledInput]]))

(defn- number-string?
  [value]
  (->> value
       (re-matches #"^[-+]?[0-9]*\.?[0-9]+$")
       (boolean)))

(defn- parser
  [value]
  (if (number-string? value)
    (js/parseFloat value)
    nil))


(defn NumberInput
  []
  (ControlledInput {:parser parser}))
