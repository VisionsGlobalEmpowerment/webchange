(ns webchange.editor-v2.scene-diagram.views-diagram
  (:require
    [webchange.editor-v2.diagram-utils.diagram-model.init :refer [init-diagram-model]]
    [webchange.editor-v2.diagram-utils.diagram-widget :refer [diagram-widget]]
    [webchange.editor-v2.scene-diagram.nodes-factory.nodes-factory :refer [get-diagram-items]]
    [webchange.editor-v2.scene-diagram.scene-parser.scene-parser :refer [scene-data->actions-tracks]]))

(defn dialogs-diagram
  [{:keys [scene-data]}]
  (let [actions-tracks (scene-data->actions-tracks scene-data)
        {:keys [nodes links]} (get-diagram-items scene-data actions-tracks)
        {:keys [engine]} (init-diagram-model :phrases nodes links {:locked? true})]
    [diagram-widget {:engine engine
                     :zoom?  true}]))
