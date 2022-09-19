(ns webchange.lesson-builder.blocks.stage.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.interpreter.renderer.scene.modes.modes :as modes]
    [webchange.interpreter.renderer.stage :refer [stage]]
    [webchange.lesson-builder.blocks.stage.second-stage.views :refer [second-stage]]
    [webchange.lesson-builder.blocks.stage.state :as state]
    [webchange.lesson-builder.widgets.page-remove.views :refer [remove-flipbook-page]]
    [webchange.lesson-builder.widgets.select-stage.views :refer [select-stage]]
    [webchange.ui.index :as ui]))

(defn block-stage
  [{:keys [class-name]}]
  (let [scene-data @(re-frame/subscribe [::state/scene-data])
        key @(re-frame/subscribe [::state/stage-key])
        show-flipbook-actions? @(re-frame/subscribe [::state/show-flipbook-actions?])
        busy? @(re-frame/subscribe [::state/stage-busy?])
        handle-ready #(re-frame/dispatch [::state/set-stage-ready true])]
    [:div {:id         "block--stage"
           :class-name class-name}
     (when show-flipbook-actions?
       [remove-flipbook-page {:class-name "stage-actions"}])
     [:div {:class-name (ui/get-class-name {"stage-wrapper"              true
                                            "stage-wrapper--has-actions" show-flipbook-actions?})}
      ^{:key key}
      [stage {:id         "main"
              :mode       ::modes/editor
              :scene-data scene-data
              :on-ready   handle-ready}]]
     (when show-flipbook-actions?
       [select-stage {:class-name "stage-actions"}])
     [second-stage]
     (when busy?
       [ui/loading-overlay])]))
