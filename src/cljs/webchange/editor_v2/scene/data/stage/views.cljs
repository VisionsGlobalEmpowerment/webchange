(ns webchange.editor-v2.scene.data.stage.views
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [webchange.editor-v2.scene.state.stage :as stage-state]))

(defn select-stage
  []
  (let [stages @(re-frame/subscribe [::stage-state/stage-options])
        enabled? (seq stages)
        current-stage @(re-frame/subscribe [::stage-state/current-stage])]
    (when enabled?
      [ui/form-control {:full-width true
                        :margin     "normal"}
       [ui/select {:value         (or current-stage "")
                   :display-empty true
                   :variant       "outlined"
                   :on-change     #(re-frame/dispatch [::stage-state/select-stage (.. % -target -value)])}
        [ui/menu-item {:value "" :disabled true} "Select stage"]
        (for [{:keys [idx name]} stages]
          ^{:key idx}
          [ui/menu-item {:value idx} name])]])))
