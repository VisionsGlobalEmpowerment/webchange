(ns webchange.editor-v2.layout.flipbook.page-text.views-save-button
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [webchange.editor-v2.layout.flipbook.page-text.state :as state]))

(defn- get-styles
  []
  {:container {:flex-grow  1
               :text-align "right"}})

(defn save-button
  [{:keys [id]}]
  (let [has-changes? @(re-frame/subscribe [::state/has-changes? id])
        loading-status @(re-frame/subscribe [::state/loading-status id])
        loading? @(re-frame/subscribe [::state/loading? id])
        handle-save #(re-frame/dispatch [::state/save id])
        styles (get-styles)]
    [:div {:style (:container styles)}
     [ui/button {:on-click handle-save
                 :color    "secondary"
                 :disabled (or loading? (not has-changes?))}
      (case loading-status
        :loading [ui/circular-progress {:size 20}]
        "Save")]]))
