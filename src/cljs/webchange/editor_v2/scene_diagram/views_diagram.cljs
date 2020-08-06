(ns webchange.editor-v2.scene-diagram.views-diagram
  (:require
    ["@projectstorm/react-diagrams" :refer [DiagramWidget]]
    [reagent.core :as r]
    [webchange.editor-v2.diagram.diagram-model.init :refer [init-diagram-model]]
    [webchange.editor-v2.diagram.diagram-utils.utils :refer [zoom-to-fit]]
    [webchange.editor-v2.scene-diagram.nodes-factory.nodes-factory :refer [get-diagram-items]]
    [webchange.editor-v2.scene-diagram.scene-parser.scene-parser :refer [scene-data->actions-tracks]]))

(defn- update-diagram
  [engine]
  (.setTimeout js/window
               #(do (zoom-to-fit engine))
               100))

(defn diagram-widget
  [{:keys [engine]}]
  (let [diagram-engine (atom engine)]
    (r/create-class
      {:display-name         "diagram-widget"
       :component-did-mount  (fn []
                               (update-diagram @diagram-engine))
       :component-did-update (fn [this]
                               (let [{:keys [engine]} (r/props this)]
                                 (reset! diagram-engine engine)
                                 (update-diagram @diagram-engine)))
       :reagent-render       (fn [{:keys [engine]}]
                               [:div.diagram-container
                                [:> DiagramWidget {:diagramEngine engine
                                                   :deleteKeys    []}]])})))

(defn dialogs-diagram
  [{:keys [scene-data]}]
  (let [actions-tracks (scene-data->actions-tracks scene-data)
        {:keys [nodes links]} (get-diagram-items scene-data actions-tracks)
        {:keys [engine]} (init-diagram-model :phrases nodes links {:locked? true})]
    [diagram-widget {:engine engine}]))
