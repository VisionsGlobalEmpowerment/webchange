(ns webchange.editor.common
  (:require
    [webchange.common.anim :refer [animations]]
    [webchange.editor.events :as events]
    [webchange.subs :as subs]
    [re-frame.core :as re-frame]
    [soda-ash.core :as sa]
    [sodium.core :as na]))

(def object-types [{:key :image :value :image :text "Image"}
                   {:key :transparent :value :transparent :text "Transparent"}
                   {:key :group :value :group :text "Group"}
                   {:key :placeholder :value :placeholder :text "Placeholder"}
                   {:key :animation :value :animation :text "Animation"}
                   {:key :text :value :text :text "Text"}
                   {:key :background :value :background :text "Background"}])

(defn- properties-panel-common
  [props]
  [:div
   [na/form-input {:label "x" :default-value (:x @props) :on-change #(swap! props assoc :x (-> %2 .-value js/parseInt)) :inline? true}]
   [na/form-input {:label "y" :default-value (:y @props) :on-change #(swap! props assoc :y (-> %2 .-value js/parseInt)) :inline? true}]
   [na/form-input {:label "width" :default-value (:width @props) :on-change #(swap! props assoc :width (-> %2 .-value js/parseInt)) :inline? true}]
   [na/form-input {:label "height" :default-value (:height @props) :on-change #(swap! props assoc :height (-> %2 .-value js/parseInt)) :inline? true}]
   [na/form-input {:label "rotation" :default-value (:rotation @props) :on-change #(swap! props assoc :rotation (-> %2 .-value js/parseInt)) :inline? true}]
   [na/form-input {:label "scale x" :default-value (:scale-x @props) :on-change #(swap! props assoc :scale-x (-> %2 .-value js/parseFloat)) :inline? true}]
   [na/form-input {:label "scale y" :default-value (:scale-y @props) :on-change #(swap! props assoc :scale-y (-> %2 .-value js/parseFloat)) :inline? true}]])

(defn- properties-panel-image
  [props]
  (let [scene-id @(re-frame/subscribe [::subs/current-scene])
        scene @(re-frame/subscribe [::subs/scene scene-id])]
    [:div
     [sa/Dropdown {:placeholder "src" :search true :selection true :on-change #(swap! props assoc :src (.-value %2))
                   :default-value (:src @props) :options (na/dropdown-list (:assets scene) :url :url) }]
     [properties-panel-common props]]))

(defn- properties-panel-transparent
  [props]
  [:div
   [properties-panel-common props]])

(defn- get-animation [name]
  (let [key (keyword name)]
    (get animations key)))

(defn- properties-panel-animation
  [props]
  [:div
   [:div
    [:label "name"]
    [sa/Dropdown {:placeholder "name" :search true :selection true :on-change #(swap! props assoc :name (.-value %2))
                  :default-value (:name @props) :options (na/dropdown-list (keys animations) name name) }]]
   [:div
    [:label "anim"]
    [sa/Dropdown {:placeholder "anim" :search true :selection true :on-change #(swap! props assoc :anim (.-value %2))
                  :default-value (:anim @props) :options (na/dropdown-list (-> @props :name get-animation :animations) identity identity) }]]
   [:div
    [:label "skin"]
    [sa/Dropdown {:placeholder "skin" :search true :selection true :on-change #(swap! props assoc :skin (.-value %2))
                  :default-value (:skin @props) :options (na/dropdown-list (-> @props :name get-animation :skins) identity identity) }]]
   [na/form-input {:label "speed" :default-value (:speed @props) :on-change #(swap! props assoc :speed (-> %2 .-value js/parseFloat)) :inline? true}]
   [na/checkbox {:label "start" :default-checked? (:start @props) :on-change #(swap! props assoc :start (.-checked %2))}]
   [properties-panel-common props]])

(defn- properties-panel-background
  [props]
  (let [scene-id @(re-frame/subscribe [::subs/current-scene])
        scene @(re-frame/subscribe [::subs/scene scene-id])]
    [:div
     [sa/Dropdown {:placeholder "src" :search true :selection true :on-change #(swap! props assoc :src (.-value %2))
                   :default-value (:src @props) :options (na/dropdown-list (:assets scene) :url :url) }]]))

(defn dispatch-properties-panel
  [props]
  (case (-> @props :type keyword)
    :image [properties-panel-image props]
    :transparent [properties-panel-transparent props]
    :animation [properties-panel-animation props]
    :background [properties-panel-background props]
    [properties-panel-common props]))

(defn update-current-scene-object
  [name state]
  (re-frame/dispatch [::events/edit-current-scene-object {:target name :state state}]))

(defn update-object
  [scene-id name state]
  (re-frame/dispatch [::events/edit-object {:scene-id scene-id :target name :state state}]))

(defn update-object-action
  [scene-id name action state]
  (re-frame/dispatch [::events/edit-object-action {:scene-id scene-id :target name :action action :state state}]))
