(ns webchange.lesson-builder.blocks.stage.second-stage.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.interpreter.components :refer [get-scene-objects-data-by-scene-data get-activity-resources]]
    [webchange.interpreter.renderer.scene.modes.modes :as modes]
    [webchange.interpreter.renderer.stage :refer [stage]]
    [webchange.lesson-builder.blocks.stage.second-stage.state :as state]))

(defn second-stage
  []
  (r/create-class
    {:component-will-unmount
     (fn [this]
       (re-frame/dispatch [::state/reset (r/props this)]))

     :reagent-render
     (fn []
       (let [source @(re-frame/subscribe [::state/source])
             scene-data @(re-frame/subscribe [::state/scene-data source])
             objects (get-scene-objects-data-by-scene-data scene-data)
             resources (get-activity-resources nil scene-data)
             id (random-uuid)]
         (when (some? scene-data)
           [:div.widget--second-stage
            ^{:key id}
            [stage {:mode                ::modes/editor
                    :scene-data          {:scene-id  id
                                          :objects   objects
                                          :resources resources}
                    :show-loader-screen? false
                    :reset-resources?    false}]])))}))
