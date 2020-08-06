(ns webchange.editor-v2.diagram.widget.views
  (:require
    ["@projectstorm/react-diagrams" :refer [DiagramWidget]]
    [cljs-react-material-ui.reagent :as ui]
    [cljs-react-material-ui.icons :as ic]
    [reagent.core :as r]
    [webchange.editor-v2.diagram.diagram-model.init :refer [init-diagram-model]]
    [webchange.editor-v2.diagram.diagram-utils.utils :refer [reorder zoom-to-fit]]
    [webchange.editor-v2.utils :refer [keyword->caption]]
    [webchange.editor-v2.diagram.widget.items-factory.factory :refer [create-diagram-items]]
    [webchange.editor-v2.diagram.widget.views-toolbar :refer [toolbar]]
    [webchange.editor-v2.diagram.widget.utils :refer [get-graph-changes]]))

(defn reorder-nodes
  [engine]
  (when-not (nil? engine)
    (.setTimeout js/window #(do (reorder engine)
                                (zoom-to-fit engine)) 100)))

(defn empty-graph-placeholder
  []
  [:div {:style {:display         "flex"
                 :height          "100%"
                 :align-items     "center"
                 :justify-content "center"}}
   [:div {:style {:color     "white"
                  :font-size "20px"}}
    "Empty"]])

(defn update-graph
  [engine nodes old-graph new-graph]
  (let [changes-list (->> (get-graph-changes old-graph new-graph)
                          (filter #(= (:type %) :update)))]
    (doseq [{:keys [type node data]} changes-list]
      (case type
        :update (.updateProps (get nodes node) (assoc data :name node))))
    (.setTimeout js/window (fn [] (when-not (nil? engine) (.repaintCanvas engine))) 100)))

(defn diagram-widget
  []
  (let [diagram-engine (atom nil)
        diagram-nodes (atom nil)
        force-update? (r/atom false)]
    (r/create-class
      {:display-name "diagram-widget"

       :should-component-update
                     (fn [_ [_ old-props] [_ new-props]]
                       (let [old-graph (:graph old-props)
                             new-graph (:graph new-props)
                             should-rebuild? (or @force-update?
                                                (not (= (keys old-graph)
                                                        (keys new-graph))))]
                         (when-not should-rebuild?
                           (update-graph @diagram-engine @diagram-nodes old-graph new-graph))

                         should-rebuild?))

       :component-did-mount
                     (fn []
                       (reorder-nodes @diagram-engine))

       :component-did-update
                     (fn []
                       (reorder-nodes @diagram-engine))

       :reagent-render
                     (fn [{:keys [graph mode root-selector]}]
                       (if (empty? graph)
                         [:div.diagram-container
                          [toolbar {:force-update force-update?
                                    :engine       nil} root-selector]
                          [empty-graph-placeholder]]
                         (let [{:keys [nodes links]} (create-diagram-items graph)
                               {:keys [engine]} (init-diagram-model mode (vals nodes) links)]
                           (reset! diagram-engine engine)
                           (reset! diagram-nodes nodes)
                           [:div.diagram-container
                            [toolbar {:force-update force-update?
                                      :engine       engine} root-selector]
                            [:> DiagramWidget {:diagramEngine engine
                                               :deleteKeys    []}]])))})))
