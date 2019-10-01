(ns webchange.editor-v2.diagram-builder.custom-nodes.action-node.action-widget
  (:require
    ["@projectstorm/react-diagrams" :refer [PortWidget]]))

;; Custom version of of "DefaultNodeWidget" from
;; https://github.com/projectstorm/react-diagrams/blob/v5.3.2/src/defaults/widgets/DefaultNodeWidget.tsx

(defn action-widget
  [props]
  (let [node (:node props)
        props (.-props node)
        in-ports (->> node .getInPorts)
        out-ports (->> node .getOutPorts)]
    [:div {:style {:position         "relative"
                   :float            "left"
                   :padding          3
                   :border           "solid 2px #1b1b1b"
                   :border-radius    5
                   :background-color (.-color props)}}
     [:h3 {:style {:margin     0
                   :padding    5
                   :text-align "center"}}
      (.-name props)]
     [:h4 {:style {:margin     0
                   :padding    5
                   :text-align "center"}}
      (.-type props)]
     [:div {:style {:float        "left"
                    :margin-right 10}}
      [:div {:style {:margin-bottom 5}} "In"]
      [:div {:style {:float "left"}}
       (for [in-port in-ports]
         ^{:key (.-id in-port)}
         [:div {:style {:display "flex"}}
          [:> PortWidget {:name (.-name in-port) :node (.-parent in-port)}]
          [:span {:style {:font-size   10
                          :line-height "15px"}} (.-label in-port)]])]]

     [:div {:style {:float       "right"
                    :margin-left 10}}
      [:div {:style {:margin-bottom 5
                     :text-align    "right"}} "Out"]
      [:div {:style {:float "right"}}
       (for [out-port out-ports]
         ^{:key (.-id out-port)}
         [:div {:style {:display "flex"}}
          [:span {:style {:font-size   10
                          :line-height "15px"}}
           (.-label out-port)]
          [:> PortWidget {:name (.-name out-port) :node (.-parent out-port)}]])]]]
    ))
