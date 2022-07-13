(ns webchange.lesson-builder.blocks.stage.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.interpreter.renderer.scene.modes.modes :as modes]
    [webchange.interpreter.renderer.stage :refer [stage]]
    [webchange.lesson-builder.blocks.stage.state :as state]))

(defn block-stage
  [{:keys [class-name]}]
  (let [scene-data @(re-frame/subscribe [::state/scene-data])]
    [:div {:id         "block--stage"
           :class-name class-name}
     ;; ToDo: get rid of hard update
     ^{:key (rand)}
     [stage {:mode       ::modes/editor
             :scene-data scene-data}]]))
