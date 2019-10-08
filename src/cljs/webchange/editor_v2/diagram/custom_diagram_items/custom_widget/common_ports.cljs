(ns webchange.editor-v2.diagram.custom-diagram-items.custom-widget.common-ports
  (:require
    ["@projectstorm/react-diagrams" :refer [PortWidget]]))

(defn ports
  [{:keys [in-ports out-ports]}]
  [:div
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
       [:div {:style {:display "flex"
                      :flex-direction "row-reverse"}}
        [:> PortWidget {:name (.-name out-port) :node (.-parent out-port)}]
        [:span {:style {:font-size   10
                        :line-height "15px"}}
         (.-label out-port)]])]]])