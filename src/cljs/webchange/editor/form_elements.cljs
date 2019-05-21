(ns webchange.editor.form-elements
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.subs :as subs]
    [webchange.common.kimage :refer [kimage]]
    [webchange.common.anim :refer [anim animations init-spine-player]]
    [webchange.common.events :as ce]
    [webchange.interpreter.variables.subs :as vars.subs]
    [webchange.interpreter.core :refer [get-data-as-blob]]
    [webchange.interpreter.components :refer [scene with-origin-offset] :rename {scene play-scene}]
    [webchange.interpreter.events :as ie]
    [webchange.editor.events :as events]
    [webchange.editor.subs :as es]
    [konva :refer [Transformer]]
    [react-konva :refer [Stage Layer Group Rect Text Custom]]
    [sodium.core :as na]
    [sodium.extensions :as nax]
    [soda-ash.core :as sa]))

(defn audio-asset-dropdown [props field-name scene-id]
  (let [scene @(re-frame/subscribe [::subs/scene scene-id])]
    [sa/FormDropdown {:label "Audio asset" :inline true :clearable true :search true :selection true
                      :default-value (get @props field-name)
                      :options (na/dropdown-list (->> scene
                                                      :assets
                                                      (filter #(= "audio" (:type %)))) :url #(or (:alias %) (:url %)))
                      :on-change #(swap! props assoc field-name (.-value %2))}]))

(defn object-dropdown [props field-name label]
  (let [scene-id @(re-frame/subscribe [::subs/current-scene])
        scene @(re-frame/subscribe [::subs/scene scene-id])]
    [sa/FormDropdown {:label (or label "Target") :inline true :clearable true :search true :selection true
                      :default-value (get @props field-name)
                      :options (na/dropdown-list (->> scene :objects (map first) (map name)) identity identity)
                      :on-change #(swap! props assoc field-name (.-value %2))}]))

(defn action-dropdown [props field-name label]
  (let [scene-id @(re-frame/subscribe [::subs/current-scene])
        scene @(re-frame/subscribe [::subs/scene scene-id])]
    [sa/FormDropdown {:label (or label "Target") :inline true :clearable true :search true :selection true
                      :default-value (get @props field-name)
                      :options (na/dropdown-list (->> scene :actions (map first) (map name)) identity identity)
                      :on-change #(swap! props assoc field-name (.-value %2))}]))

(defn target-animation-dropdown [props field-name]
  (let [scene-id @(re-frame/subscribe [::subs/current-scene])
        scene @(re-frame/subscribe [::subs/scene scene-id])
        animations (->> scene :objects (map second) (map :scene-name) (remove nil?))]
    [sa/FormDropdown {:label "Target" :inline true :clearable true :search true :selection true
                      :default-value (get @props field-name)
                      :options (na/dropdown-list animations identity identity)
                      :on-change #(swap! props assoc field-name (.-value %2))}]))

(defn transition-dropdown [props field-name label]
  (let [scene-id @(re-frame/subscribe [::subs/current-scene])
        scene @(re-frame/subscribe [::subs/scene scene-id])
        transitions (->> scene :objects (map second) (map :transition) (remove nil?))]
    [sa/FormDropdown {:label (or label "Target") :inline true :clearable true :search true :selection true
                      :default-value (get @props field-name)
                      :options (na/dropdown-list transitions identity identity)
                      :on-change #(swap! props assoc field-name (.-value %2))}]))

(defn object-states-dropdown [props field-name object-name]
  (let [scene-id @(re-frame/subscribe [::subs/current-scene])
        object @(re-frame/subscribe [::subs/scene-object scene-id object-name])]
    [sa/FormDropdown {:label "State" :inline true :clearable true :search true :selection true
                      :default-value (get @props field-name)
                      :options (na/dropdown-list (->> object :states (map first) (map name)) identity identity)
                      :on-change #(swap! props assoc field-name (.-value %2))}]))

(defn scene-dropdown [props field-name]
  (let [scenes @(re-frame/subscribe [::subs/course-scenes])]
    [sa/FormDropdown {:label "Scene" :inline true :clearable true :search true :selection true
                      :default-value (get @props field-name)
                      :options (na/dropdown-list scenes identity identity)
                      :on-change #(swap! props assoc field-name (.-value %2))}]))

(defn counter-action-dropdown [props field-name label]
  (let [actions ["increase" "decrease"]]
    [sa/FormDropdown {:label (or label "Counter action") :inline true :clearable true :search true :selection true
                      :default-value (get @props field-name)
                      :options (na/dropdown-list actions identity identity)
                      :on-change #(swap! props assoc field-name (.-value %2))}]))