(ns webchange.book-creator.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.layout.components.activity-stage.state :as stage-state]
    [webchange.editor-v2.layout.components.interpreter_stage.views :as interpreter]
    [webchange.book-creator.text-form.views :refer [text-form]]
    [webchange.ui-framework.index :refer [icon-button]]))

(defn- stage
  []
  [:div.book-creator--stage
   {:style (interpreter/get-stage-size {:width 800})}
   [interpreter/stage]])

(defn- block-header
  [{:keys [title left-controls]}]
  [:div.block-header
   [:span.title title]
   (when (some? left-controls)
     [:div.left-controls
      left-controls])])

(defn book-creator
  []
  (let [prev-stage-available? @(re-frame/subscribe [::stage-state/prev-stage-available?])
        next-stage-available? @(re-frame/subscribe [::stage-state/next-stage-available?])
        show-prev-stage (fn [] (re-frame/dispatch [::stage-state/select-prev-stage]))
        show-next-stage (fn [] (re-frame/dispatch [::stage-state/select-next-stage]))]
    [:div.book-creator
     [:div.main-content
      [block-header {:title         "Layout"
                     :left-controls (r/as-element [:div.left-controls-content
                                                   [icon-button {:icon      "arrow-left"
                                                                 :disabled? (not prev-stage-available?)
                                                                 :on-click  show-prev-stage}]
                                                   [icon-button {:icon      "arrow-right"
                                                                 :disabled? (not next-stage-available?)
                                                                 :on-click  show-next-stage}]])}]
      [stage]]
     [:div.side-block
      [text-form]]]))
