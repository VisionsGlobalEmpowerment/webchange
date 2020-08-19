(ns webchange.editor-v2.diagram-utils.diagram-model.custom-nodes.custom-widget.widget-header
  (:require
    [webchange.editor-v2.utils :refer [str->caption]]))

(defn header
  [props]
  [:div
   [:h3 {:style {:margin      0
                 :padding     5
                 :text-align  "center"
                 :white-space "nowrap"}}
    (str->caption (:name props))]
   [:h4 {:style {:margin      0
                 :padding     "0 5px"
                 :text-align  "center"
                 :white-space "nowrap"}}
    (get-in props [:data :type])]])
