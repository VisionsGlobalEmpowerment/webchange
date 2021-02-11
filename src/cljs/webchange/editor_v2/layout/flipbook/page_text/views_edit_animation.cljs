(ns webchange.editor-v2.layout.flipbook.page-text.views-edit-animation
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [webchange.editor-v2.layout.flipbook.page-text.state :as state]))

(defn- get-styles
  []
  {:button {:margin      "0 16px"
            :white-space "nowrap"}})

(defn edit-animation-button
  [{:keys [id]}]
  (let [window-options {:components     {:description  {:hide? true}
                                         :node-options {:hide? true}
                                         :target       {:hide? true}
                                         :phrase       {:hide? true}
                                         :diagram      {:hide? true}}
                        :single-phrase? true}
        handle-click (fn []
                       (re-frame/dispatch [::state/open-dialog-window id window-options]))
        loading? @(re-frame/subscribe [::state/loading? id])
        styles (get-styles)]
    [ui/button {:on-click handle-click
                :disabled loading?
                :style    (:button styles)}
     "Edit Animation"]))
