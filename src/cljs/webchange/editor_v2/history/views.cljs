(ns webchange.editor-v2.history.views
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [webchange.editor-v2.history.state :as state]
    [webchange.editor-v2.history.state-undo :as undo]))

(defn history-controls
  []
  (let [has-history? @(re-frame/subscribe [::state/has-history])]
    [:div {:style {:padding "8px"}}
     (when has-history?
       [ui/button {:color    "secondary"
                   :on-click #(re-frame/dispatch [::undo/apply-undo])}
        "Undo"])]))
