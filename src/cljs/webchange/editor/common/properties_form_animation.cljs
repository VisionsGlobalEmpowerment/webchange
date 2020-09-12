(ns webchange.editor.common.properties_form_animation
  (:require
    [webchange.editor.common.properties_form_common :refer [properties-form-common]]
    [soda-ash.core :refer [Checkbox
                           Divider
                           Dropdown
                           FormGroup
                           FormInput
                           FormSelect]]))

(defn properties-form-animation
  [props]
  [:div
   [FormGroup {:width "equal"}]
   [Checkbox {:label           "start"
              :default-checked (:start @props)
              :on-change       #(swap! props assoc :start (.-checked %2))}]
   [Divider]
   [properties-form-common props]])
