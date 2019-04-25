(ns webchange.editor.common.properties_form_animation
  (:require
    [webchange.common.anim :refer [animations]]
    [webchange.editor.common.properties_form_common :refer [properties-form-common]]
    [soda-ash.core :refer [Checkbox
                           Divider
                           Dropdown
                           FormGroup
                           FormInput
                           FormSelect]]))

(defn- get-options-from-plain-list
  [l]
  (->> l
       (map #(hash-map :key % :value % :text %))
       (vec)))

(defn- get-animations-names
  []
  (->> (keys animations)
       (map name)))

(defn- get-animation-skins
  [animation-name]
  (->> animation-name
       (keyword)
       (get animations)
       (:skins)
       (apply list)))

(defn- get-animation-animations
  [animation-name]
  (->> animation-name
       (keyword)
       (get animations)
       (:animations)
       (apply list)))

(defn- get-name-options
  []
  (get-options-from-plain-list (get-animations-names)))

(defn- get-skin-options
  [animation-name]
  (get-options-from-plain-list (get-animation-skins animation-name)))

(defn- get-animation-options
  [animation-name]
  (get-options-from-plain-list (get-animation-animations animation-name)))

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
                 :options       (get-name-options)}]
    [FormSelect {:label         "skin"
                 :placeholder   "Select Skin"
                 :search        true
                 :selection     true
                 :on-change     #(swap! props assoc :skin (.-value %2))
                 :default-value (:skin @props)
                 :options       (get-skin-options (:name @props))}]
    ]
   [FormGroup {:width "equal"}
    [FormSelect {:label         "animation"
                 :placeholder   "Select Animation"
                 :search        true
                 :selection     true
                 :on-change     #(swap! props assoc :anim (.-value %2))
                 :default-value (:anim @props)
                 :options       (get-animation-options (:name @props))}]
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