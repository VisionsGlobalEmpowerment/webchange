(ns webchange.editor.common.components
  (:require
    [webchange.common.anim :refer [animations]]
    [webchange.editor.common.properties_form_animation :refer [properties-form-animation]]
    [webchange.editor.common.properties_form_common :refer [properties-form-common]]
    [webchange.editor.events :as events]
    [webchange.subs :as subs]
    [re-frame.core :as re-frame]
    [soda-ash.core :as sa]
    [sodium.core :as na]))

(defn- properties-panel-image
  [props]
  (let [scene-id @(re-frame/subscribe [::subs/current-scene])
        scene @(re-frame/subscribe [::subs/scene scene-id])]
    [:div
     [sa/Dropdown {:placeholder "src" :search true :selection true :on-change #(swap! props assoc :src (.-value %2))
                   :default-value (:src @props) :options (na/dropdown-list (:assets scene) :url :url) }]
     [properties-form-common props]]))

(defn- properties-panel-transparent
  [props]
  [:div
   [properties-form-common props]])

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
    :animation [properties-form-animation props]
    :background [properties-panel-background props]
    [properties-form-common props]))

(defn update-current-scene-object
  [name state]
  (re-frame/dispatch [::events/edit-current-scene-object {:target name :state state}]))

(defn update-object
  [scene-id name state]
  (re-frame/dispatch [::events/edit-object {:scene-id scene-id :target name :state state}]))

(defn update-object-action
  [scene-id name action state]
  (re-frame/dispatch [::events/edit-object-action {:scene-id scene-id :target name :action action :state state}]))
