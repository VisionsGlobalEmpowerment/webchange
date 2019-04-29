(ns webchange.editor.common.properties_form_common
  (:require
    [webchange.editor.form-elements.integer :refer [IntegerInput]]
    [webchange.editor.form-elements.number :refer [NumberInput]]
    [soda-ash.core :refer [FormGroup
                           FormInput]]))

(defn properties-form-common
  [props]
  [:div
   [FormGroup {:width "equal"}
    [IntegerInput {:label     "x"
                   :value     (:x @props)
                   :on-change #(swap! props assoc :x %)
                   :inline    true}]
    [IntegerInput {:label     "y"
                   :value     (:y @props)
                   :on-change #(swap! props assoc :y %)
                   :inline    true}]]
   [FormGroup {:width "equal"}
    [IntegerInput {:label     "width"
                   :value     (:width @props)
                   :on-change #(swap! props assoc :width %)
                   :inline    true}]
    [IntegerInput {:label     "height"
                   :value     (:height @props)
                   :on-change #(swap! props assoc :height %)
                   :inline    true}]]
   [FormGroup {}
    [IntegerInput {:label     "rotation"
                   :value     (:rotation @props)
                   :on-change #(swap! props assoc :rotation %)
                   :inline    true}]]
   [FormGroup {:width "equal"}
    [NumberInput {:label     "scale x"
                  :value     (:scale-x @props)
                  :on-change #(swap! props assoc :scale-x %)
                  :inline    true}]
    [NumberInput {:label     "scale y"
                  :value     (:scale-y @props)
                  :on-change #(swap! props assoc :scale-y %)
                  :inline    true}]]
   ])
