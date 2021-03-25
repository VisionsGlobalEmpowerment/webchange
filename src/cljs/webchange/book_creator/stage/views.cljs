(ns webchange.book-creator.stage.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.layout.components.activity-stage.state :as stage-state]
    [webchange.editor-v2.layout.components.interpreter_stage.views :as interpreter]
    [webchange.book-creator.views-content-block :refer [content-block]]
    [webchange.ui-framework.components.index :refer [icon-button]]))

(defn stage-block
  []
  (let [prev-stage-available? @(re-frame/subscribe [::stage-state/prev-stage-available?])
        next-stage-available? @(re-frame/subscribe [::stage-state/next-stage-available?])
        show-prev-stage (fn [] (re-frame/dispatch [::stage-state/select-prev-stage]))
        show-next-stage (fn [] (re-frame/dispatch [::stage-state/select-next-stage]))
        this (r/current-component)]
    [content-block {:title         "Layout"
                    :left-controls (r/as-element [:div.left-controls-content
                                                  [icon-button {:icon      "arrow-left"
                                                                :disabled? (not prev-stage-available?)
                                                                :on-click  show-prev-stage}]
                                                  [icon-button {:icon      "arrow-right"
                                                                :disabled? (not next-stage-available?)
                                                                :on-click  show-next-stage}]])}
     [:div.stage-wrapper
      [:div.book-creator--stage
       {:style (interpreter/get-stage-size {:width 800})}
       [interpreter/stage]]
      (into [:div.stage-controls]
            (r/children this))]]))
