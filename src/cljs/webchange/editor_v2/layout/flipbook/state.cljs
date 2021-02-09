(ns webchange.editor-v2.layout.flipbook.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.layout.flipbook.utils :refer [scene-data->objects-list]]
    [webchange.editor-v2.layout.components.activity-stage.state :as stage]
    [webchange.state.state :as state]))

(re-frame/reg-sub
  ::stage-text-data
  (fn []
    [(re-frame/subscribe [::state/scene-data])
     (re-frame/subscribe [::stage/current-stage])])
  (fn [[scene-data current-stage]]
    (scene-data->objects-list scene-data current-stage)))
