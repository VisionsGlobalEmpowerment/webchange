(ns webchange.editor-v2.diagram.diagram-model.custom-nodes.custom-widget.widget-wrapper
  (:require
    [reagent.core :as r]
    [webchange.editor-v2.diagram.diagram-model.custom-nodes.custom-widget.colors :refer [colors]]))

(def node-style {:background-color (:default colors)
                 :border           "solid 2px #1b1b1b"
                 :border-radius    5
                 :float            "left"
                 :padding          3
                 :position         "relative"})

(defn wrapper
  [{:keys []}]
  (let [this (r/current-component)]
    (into [:div {:style node-style}]
          (r/children this))))
