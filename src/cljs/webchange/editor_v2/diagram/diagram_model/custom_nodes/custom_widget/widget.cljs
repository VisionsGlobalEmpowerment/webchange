(ns webchange.editor-v2.diagram.diagram-model.custom-nodes.custom-widget.widget
  (:require
    [webchange.editor-v2.diagram.diagram-model.custom-nodes.custom-widget.common-colors :refer [get-node-color]]
    [webchange.editor-v2.diagram.diagram-model.custom-nodes.custom-widget.common-header :refer [header]]
    [webchange.editor-v2.diagram.diagram-model.custom-nodes.custom-widget.common-ports :refer [ports]]))

;; Custom version of of "DefaultNodeWidget" from
;; https://github.com/projectstorm/react-diagrams/blob/v5.3.2/src/defaults/widgets/DefaultNodeWidget.tsx

(def node-style {:border        "solid 2px #1b1b1b"
                 :border-radius 5
                 :float         "left"
                 :padding       3
                 :position      "relative"})

(defn custom-widget
  [{:keys [node get-node-custom-color on-click on-double-click]}]
  (let [node-data (.-props node)
        in-ports (->> node .getInPorts)
        out-ports (->> node .getOutPorts)
        click-handler (if-not (nil? on-click)
                        {:on-click #(on-click node-data)}
                        {:on-double-click #(on-double-click node-data)})]
    [:div (merge click-handler
                 {:style (merge node-style
                                {:background-color (or (get-node-custom-color node-data)
                                                       (get-node-color node-data))})})

     [header node-data]
     [ports {:in-ports  in-ports
             :out-ports out-ports}]]))
