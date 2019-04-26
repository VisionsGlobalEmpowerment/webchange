(ns webchange.editor.common.properties_form_common
  (:require
    [soda-ash.core :refer [FormGroup
                           FormInput]]))

(defn properties-form-common
  [props]
  [:div
   [FormGroup {:width "equal"}
    [FormInput {:label         "x"
                :default-value (:x @props)
                :on-change     #(swap! props assoc :x (-> %2 .-value js/parseInt))
                :inline?       true}]
    [FormInput {:label         "y"
                :default-value (:y @props)
                :on-change     #(swap! props assoc :y (-> %2 .-value js/parseInt))
                :inline?       true}]]
   [FormGroup {:width "equal"}
    [FormInput {:label         "width"
                :default-value (:width @props)
                :on-change     #(swap! props assoc :width (-> %2 .-value js/parseInt))
                :inline?       true}]
    [FormInput {:label         "height"
                :default-value (:height @props)
                :on-change     #(swap! props assoc :height (-> %2 .-value js/parseInt))
                :inline?       true}]]
   [FormGroup {}
    [FormInput {:label         "rotation"
                :default-value (:rotation @props)
                :on-change     #(swap! props assoc :rotation (-> %2 .-value js/parseInt))
                :inline?       true}]]
   [FormGroup {:width "equal"}
    [FormInput {:label         "scale x"
                :default-value (:scale-x @props)
                :on-change     #(swap! props assoc :scale-x (-> %2 .-value js/parseFloat))
                :inline?       true}]
    [FormInput {:label         "scale y"
                :default-value (:scale-y @props)
                :on-change     #(swap! props assoc :scale-y (-> %2 .-value js/parseFloat))
                :inline?       true}]]
   ])
