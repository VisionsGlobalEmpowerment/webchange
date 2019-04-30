(ns webchange.editor.form-elements.number
  (:require
    [webchange.editor.form-elements.controlled-input :refer [ControlledInput]]))

(defn parser
  [value]
  (if (re-matches #"^[-+]?[0-9]*\.?[0-9]+$" value)
    (js/parseFloat value)
    nil))

(defn NumberInput
  []
  (ControlledInput {:parser parser}))
