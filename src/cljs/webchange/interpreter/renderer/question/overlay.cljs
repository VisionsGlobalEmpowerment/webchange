(ns webchange.interpreter.renderer.question.overlay
  (:require
    [re-frame.core :as re-frame]
    [webchange.interpreter.events :as ie]
    [webchange.interpreter.renderer.scene.components.create-component :refer [create-component]]
    [webchange.interpreter.renderer.overlays.utils :as utils]
    [webchange.interpreter.renderer.state.scene :as scene]))

(def menu-padding {:x 20 :y 20})

(defn create
  [{:keys [viewport parent]}]
  {:type        "group"
   :parent      parent
   :object-name :question-overlay
   :visible     false
   :children    []})

