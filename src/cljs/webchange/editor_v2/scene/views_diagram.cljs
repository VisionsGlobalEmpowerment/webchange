(ns webchange.editor-v2.scene.views-diagram
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.graph-builder.graph :refer [get-diagram-graph]]
    [webchange.editor-v2.graph-builder.utils.root-nodes :refer [get-root-nodes]]
    [webchange.editor-v2.scene-diagram.views-diagram :refer [dialogs-diagram]]

    [webchange.subs :as subs]))

(defn diagram
  []
  (let [scene-data @(re-frame/subscribe [::subs/current-scene-data])]
    [dialogs-diagram {:scene-data scene-data}]))
