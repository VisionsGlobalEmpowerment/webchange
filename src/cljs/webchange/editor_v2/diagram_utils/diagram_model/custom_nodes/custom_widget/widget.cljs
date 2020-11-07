(ns webchange.editor-v2.diagram-utils.diagram-model.custom-nodes.custom-widget.widget
  (:require
    [webchange.editor-v2.diagram-utils.diagram-model.custom-nodes.custom-widget.widget-header :as custom-header]
    [webchange.editor-v2.diagram-utils.diagram-model.custom-nodes.custom-widget.widget-wrapper :as custom-wrapper]
    [webchange.editor-v2.diagram-utils.diagram-model.custom-nodes.custom-widget.widget-ports :refer [ports]]))

;; Custom version of of "DefaultNodeWidget" from
;; https://github.com/projectstorm/react-diagrams/blob/v5.3.2/src/defaults/widgets/DefaultNodeWidget.tsx

(defn custom-widget
  [{:keys [node header wrapper]}]
  (let [header (or header custom-header/header)
        wrapper (or wrapper custom-wrapper/wrapper)]
    (let [node-data (.-props node)
          in-ports (->> node .getInPorts)
          out-ports (->> node .getOutPorts)]
      [wrapper {:node-data node-data}
       [header node-data]
       [ports {:in-ports  in-ports
               :out-ports out-ports}]])))
