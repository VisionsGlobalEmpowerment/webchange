(ns webchange.editor-v2.diagram.diagram-model.custom-nodes.custom-widget.widget-ports
  (:require
    ["@projectstorm/react-diagrams" :refer [PortWidget]]))

(defn ports
  [{:keys [in-ports out-ports]}]
  [:div
   [:div {:style {:float        "left"
                  :margin-right 10}}
    [:div {:style {:margin-bottom 5}} ""]
    [:div {:style {:float      "left"
                   :visibility "hidden"}}
     (for [in-port in-ports]
       ^{:key (.-id in-port)}
       [:div {:style {:display "flex"}}
        [:> PortWidget {:name (.-name in-port) :node (.-parent in-port)}]])]]

   [:div {:style {:float       "right"
                  :margin-left 10}}
    [:div {:style {:margin-bottom 5
                   :text-align    "right"}} ""]
    [:div {:style {:float      "right"
                   :visibility "hidden"}}
     (for [out-port out-ports]
       ^{:key (.-id out-port)}
       [:div {:style {:display        "flex"
                      :flex-direction "row-reverse"}}
        [:> PortWidget {:name (.-name out-port) :node (.-parent out-port)}]])]]])