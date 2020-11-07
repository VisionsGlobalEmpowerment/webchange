(ns webchange.editor-v2.diagram-utils.diagram-widget
  (:require
    ["@projectstorm/react-diagrams" :refer [DiagramWidget]]
    [reagent.core :as r]
    [webchange.editor-v2.diagram-utils.diagram-model.init :refer [init-diagram-model]]
    [webchange.editor-v2.diagram-utils.diagram-arranger :refer [arrange-items]]))

(defn- reorder
  [engine]
  (let [model (.getDiagramModel engine)]
    (arrange-items model engine)
    (.repaintCanvas engine)))

(defn- zoom-to-fit
  [engine]
  (.zoomToFit engine))

(defn- update-diagram
  [engine {:keys [reorder? zoom?]}]
  (when-not (nil? engine)
    (.setTimeout js/window
                 #(do (when reorder? (reorder engine))
                      (when zoom? (zoom-to-fit engine)))
                 300)))

(defn diagram-widget
  [{:keys [engine] :as props}]
  (let [diagram-engine (atom engine)
        update-diagram-props (select-keys props [:reorder? :zoom?])]
    (r/create-class
      {:display-name         "diagram-widget"
       :component-did-mount  (fn []
                               (update-diagram @diagram-engine update-diagram-props))
       :component-did-update (fn [this]
                               (let [{:keys [engine]} (r/props this)]
                                 (reset! diagram-engine engine)
                                 (update-diagram @diagram-engine update-diagram-props)))
       :reagent-render       (fn [{:keys [engine]}]
                               [:div.diagram-container
                                [:> DiagramWidget {:diagramEngine engine
                                                   :deleteKeys    []}]])})))
