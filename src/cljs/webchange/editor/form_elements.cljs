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

(defn audio-asset-dropdown [props field-name]
  (let [scene-id @(re-frame/subscribe [::subs/current-scene])
        scene @(re-frame/subscribe [::subs/scene scene-id])]
    [sa/FormDropdown {:label "Audio asset" :inline true  :clearable true :search true :selection true
                      :default-value (get @props field-name)
                      :options (na/dropdown-list (->> scene
                                                      :assets
                                                      (filter #(= "audio" (:type %)))) :url #(or (:alias %) (:url %)))
                      :on-change #(swap! props assoc field-name (.-value %2))}]))