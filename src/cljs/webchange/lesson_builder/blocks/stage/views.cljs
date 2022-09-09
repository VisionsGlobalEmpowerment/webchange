(ns webchange.lesson-builder.blocks.stage.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.interpreter.renderer.scene.modes.modes :as modes]
    [webchange.interpreter.renderer.stage :refer [stage]]
    [webchange.lesson-builder.blocks.stage.second-stage.views :refer [second-stage]]
    [webchange.lesson-builder.blocks.stage.state :as state]
    [webchange.lesson-builder.widgets.select-stage.views :refer [select-stage]]
    [webchange.ui.index :as ui]))

(defn block-stage
  [{:keys [class-name]}]
  (let [scene-data @(re-frame/subscribe [::state/scene-data])
        key @(re-frame/subscribe [::state/stage-key])
        show-bottom-actions? @(re-frame/subscribe [::state/show-bottom-actions?])
        busy? @(re-frame/subscribe [::state/stage-busy?])
        handle-ready #(re-frame/dispatch [::state/set-stage-ready true])]
    [:div {:id         "block--stage"
           :class-name class-name}
     [:div {:class-name (ui/get-class-name {"stage-wrapper"      true
                                            "has-bottom-actions" show-bottom-actions?})}
      ^{:key key}
      [stage {:id         "main"
              :mode       ::modes/editor
              :scene-data scene-data
              :on-ready   handle-ready}]]
     (when show-bottom-actions?
       [select-stage {:class-name "stage-actions"}])
     [second-stage]
     (when busy?
       [ui/loading-overlay])]))
