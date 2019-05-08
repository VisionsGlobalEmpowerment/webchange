(ns webchange.editor.common.properties_form_common
  (:require
    [webchange.editor.form-elements.integer :refer [integer-input]]
    [webchange.editor.form-elements.number :refer [number-input]]
    [soda-ash.core :refer [FormGroup] :rename {FormGroup form-group}]))

(defn properties-form-common
  [props]
  [:div
   [form-group {:width "equal"}
    [integer-input {:label    "x"
                   :value     (:x @props)
                   :on-change #(swap! props assoc :x %)
                   :inline    true}]
    [integer-input {:label    "y"
                   :value     (:y @props)
                   :on-change #(swap! props assoc :y %)
                   :inline    true}]]
   [form-group {:width "equal"}
    [integer-input {:label    "width"
                   :value     (:width @props)
                   :on-change #(swap! props assoc :width %)
                   :inline    true}]
    [integer-input {:label    "height"
                   :value     (:height @props)
                   :on-change #(swap! props assoc :height %)
                   :inline    true}]]
   [form-group {}
    [integer-input {:label    "rotation"
                   :value     (:rotation @props)
                   :on-change #(swap! props assoc :rotation %)
                   :inline    true}]]
   [form-group {:width "equal"}
    [number-input {:label    "scale x"
                  :value     (:scale-x @props)
                  :on-change #(swap! props assoc :scale-x %)
                  :inline    true}]
    [number-input {:label    "scale y"
                  :value     (:scale-y @props)
                  :on-change #(swap! props assoc :scale-y %)
                  :inline    true}]]
   ])
