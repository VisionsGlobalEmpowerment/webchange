(ns webchange.editor.form-elements.integer
  (:require
    [webchange.editor.form-elements.controlled-input :refer [controlled-input]]))

(defn- parser
  [value]
  (if (re-matches #"^[-+]?[0-9]+$" value)
    (js/parseInt value)
    nil))

(defn integer-input
  [props]
  [controlled-input props {:parser parser}])
