(ns webchange.editor-v2.diagram.custom-diagram-items.custom-widget.common-header)

(defn header
  [props]
  [:div
   [:h3 {:style {:margin      0
                 :padding     5
                 :text-align  "center"
                 :white-space "nowrap"}}
    (:name props)]
   [:h4 {:style {:margin      0
                 :padding     5
                 :text-align  "center"
                 :white-space "nowrap"}}
    (:type props)]])
