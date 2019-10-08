(ns webchange.editor-v2.diagram.custom-diagram-items.custom-widget.widget
  (:require
    [webchange.editor-v2.diagram.custom-diagram-items.custom-widget.common-colors :refer [get-node-color]]
    [webchange.editor-v2.diagram.custom-diagram-items.custom-widget.common-header :refer [header]]
    [webchange.editor-v2.diagram.custom-diagram-items.custom-widget.common-ports :refer [ports]]))

;; Custom version of of "DefaultNodeWidget" from
;; https://github.com/projectstorm/react-diagrams/blob/v5.3.2/src/defaults/widgets/DefaultNodeWidget.tsx

(def node-style {:border        "solid 2px #1b1b1b"
                 :border-radius 5
                 :float         "left"
                 :padding       3
                 :position      "relative"})

(defn custom-widget
  [props]
  (let [node (:node props)
        props (.-props node)]
    [:div {:style (merge node-style
                         {:background-color (get-node-color props)})}
     [header props]
     [ports {:in-ports  (->> node .getInPorts)
             :out-ports (->> node .getOutPorts)}]]))
