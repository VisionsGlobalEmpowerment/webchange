(ns webchange.editor-v2.diagram.widget.views
  (:require
    ["@projectstorm/react-diagrams" :refer [DiagramWidget]]
    [cljs-react-material-ui.reagent :as ui]
    [cljs-react-material-ui.icons :as ic]
    [reagent.core :as r]
    [webchange.editor-v2.diagram.diagram-model.init :refer [init-diagram-model]]
    [webchange.editor-v2.diagram.diagram-utils.utils :refer [reorder zoom-to-fit]]
    [webchange.editor-v2.utils :refer [keyword->caption]]
    [webchange.editor-v2.diagram.widget.views-toolbar :refer [toolbar]]))

(defn reorder-nodes
  [this]
  (let [engine (aget this "engine")]
    (when-not (nil? engine)
      (.setTimeout js/window #(do (reorder engine)
                                  (zoom-to-fit engine)) 100))))

(defn empty-graph-placeholder
  []
  [:div {:style {:display         "flex"
                 :height          "100%"
                 :align-items     "center"
                 :justify-content "center"}}
   [:div {:style {:color     "white"
                  :font-size "20px"}}
    "Empty"]])

(defn diagram-widget
  []
  (let [force-update? (r/atom false)]
    (r/create-class
      {:display-name "diagram-widgett"

       :should-component-update
                     (fn [_ [_ old-props] [_ new-props]]
                       (let [old-graph (:graph old-props)
                             new-graph (:graph new-props)
                             should-update? (or @force-update?
                                                (not (= (count (keys old-graph))
                                                        (count (keys new-graph)))))]
                         should-update?))

       :component-did-mount
                     (fn [this]
                       (reorder-nodes this))

       :component-did-update
                     (fn [this]
                       (reorder-nodes this))

       :reagent-render
                     (fn [{:keys [graph mode root-selector]}]
                       (if (empty? graph)
                         [:div.diagram-container
                          [toolbar {:force-update force-update?
                                    :engine       nil} root-selector]
                          [empty-graph-placeholder]]
                         (let [engine (init-diagram-model graph mode)
                               this (r/current-component)]
                           (aset this "engine" engine)
                           [:div.diagram-container
                            [toolbar {:force-update force-update?
                                      :engine       engine} root-selector]
                            [:> DiagramWidget {:diagramEngine engine
                                               :deleteKeys    []}]])))})))
