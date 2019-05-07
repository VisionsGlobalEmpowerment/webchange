(ns webchange.editor.components.data-sets.data-set-item.dataset-item-field
  (:require
    [soda-ash.core :refer [FormInput
                           Label]]))

(defmulti dataset-item-control #(:type %))

(defmethod dataset-item-control "string"
  [{:keys [on-change value]}]
  [FormInput {:inline        true
              :default-value value
              :on-change     #(on-change (-> %2 .-value))}])

(defmethod dataset-item-control :default
  [_]
  [Label {:color      "red"
          :horizontal true}
   "Unsupported type"])

