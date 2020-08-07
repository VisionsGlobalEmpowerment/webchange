(ns webchange.editor-v2.diagram-utils.modes.full-scene.widget-data
  (:require
    [reagent.core :as r]
    [webchange.editor-v2.diagram-utils.diagram-model.custom-nodes.custom-widget.colors :refer [colors]]
    [webchange.editor-v2.diagram-utils.diagram-model.custom-nodes.custom-widget.widget-wrapper :as custom-wrapper]
    [webchange.editor-v2.graph-builder.utils.node-data :refer [object-node?
                                                               phrase-node?
                                                               trigger-node?]]))

(defn get-node-color
  [node-data]
  (cond
    (phrase-node? node-data) (get-in colors [:action :phrase])
    (object-node? node-data) (get-in colors [:object :default])
    (trigger-node? node-data) (get-in colors [:global-object :default])
    :else (:default colors)))

(defn wrapper
  [{:keys [node-data]}]
  (let [this (r/current-component)]
    (into [:div {:style (merge custom-wrapper/node-style
                               {:background-color (get-node-color node-data)})}]
          (r/children this))))

(defn get-widget-data
  []
  {:wrapper         wrapper
   :on-double-click #()})
