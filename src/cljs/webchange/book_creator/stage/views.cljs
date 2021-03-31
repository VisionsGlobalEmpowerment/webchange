(ns webchange.book-creator.stage.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.book-creator.stage.state :as state]
    [webchange.editor-v2.layout.components.activity-stage.state :as stage-state]
    [webchange.editor-v2.layout.components.interpreter_stage.views :as interpreter]
    [webchange.book-creator.views-content-block :refer [content-block]]
    [webchange.ui-framework.components.index :refer [icon-button]]))

(defn remove-page
  [{:keys [side]}]
  (let [removable? @(re-frame/subscribe [::state/page-removable? side])
        handle-click #(re-frame/dispatch [::state/remove-current-stage-page side])]
    (when removable?
      [icon-button {:icon       "remove"
                    :class-name (str "remove-page remove-" (clojure.core/name side) "-page")
                    :on-click   handle-click}])))

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
       [interpreter/stage]
       [remove-page {:side :left}]
       [remove-page {:side :right}]]
      (into [:div.stage-controls]
            (r/children this))]]))
