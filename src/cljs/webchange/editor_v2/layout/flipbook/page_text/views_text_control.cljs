(ns webchange.editor-v2.layout.flipbook.page-text.views-text-control
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [webchange.editor-v2.layout.flipbook.page-text.state :as state]))

(defn text-control
  [{:keys [id]}]
  (let [value @(re-frame/subscribe [::state/current-text id])
        handle-change (fn [event]
                        (let [text (.. event -target -value)]
                          (re-frame/dispatch [::state/set-current-text id text])))
        loading? @(re-frame/subscribe [::state/loading? id])]
    [:div
     [ui/text-field {:value       value
                     :on-change   handle-change
                     :placeholder "Enter page text here"
                     :multiline   true
                     :full-width  true
                     :variant     "outlined"
                     :disabled    loading?
                     :inputProps  {:style {:overflow "hidden"}}}]]))
