(ns webchange.editor.common.properties_form_animation
  (:require
    [webchange.common.anim :refer [animations]]
    [webchange.editor.common.properties_form_common :refer [properties-form-common]]
    [soda-ash.core :refer [Checkbox
                           Divider
                           Dropdown
                           FormGroup
                           FormInput
                           FormSelect]]
    [sodium.core :as na]))

;; ToDo: Remove. Duplicated from webchange.editor.common.components
(defn- get-animation [name]
  (let [key (keyword name)]
    (get animations key)))

(defn properties-form-animation
  [props]
  [:div
   [FormGroup {:width "equal"}
    [FormSelect {:label         "name"
                 :placeholder   "Select Name"
                 :search        true
                 :selection     true
                 :on-change     #(swap! props assoc :name (.-value %2))
                 :default-value (:name @props)
                 :options       (na/dropdown-list (keys animations) name name)}]
    [FormSelect {:label         "skin"
                 :placeholder   "Select Skin"
                 :search        true
                 :selection     true
                 :on-change     #(swap! props assoc :skin (.-value %2))
                 :default-value (:skin @props)
                 :options       (na/dropdown-list (-> @props :name get-animation :skins) identity identity)}]
    ]
   [FormGroup {:width "equal"}
    [FormSelect {:label         "animation"
                 :placeholder   "Select Animation"
                 :search        true
                 :selection     true
                 :on-change     #(swap! props assoc :anim (.-value %2))
                 :default-value (:anim @props)
                 :options       (na/dropdown-list (-> @props :name get-animation :animations) identity identity)}]
    [FormInput {:label         "speed"
                :default-value (:speed @props)
                :on-change     #(swap! props assoc :speed (-> %2 .-value js/parseFloat))
                :inline?       true}]
    ]
   [Checkbox {:label            "start"
              :default-checked? (:start @props)
              :on-change        #(swap! props assoc :start (.-checked %2))}]
   [Divider]
   [properties-form-common props]])