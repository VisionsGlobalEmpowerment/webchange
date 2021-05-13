(ns webchange.editor-v2.activity-form.generic.components.change-skin.views
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [webchange.editor-v2.activity-form.generic.components.change-skin.state :as skin]
    [webchange.editor-v2.layout.components.workflow-steps.views :as translation-steps]))

(defn change-skin
  []
  (let [enabled? @(re-frame/subscribe [::skin/animation-selected?])
        skins @(re-frame/subscribe [::skin/available-skins])
        current-skin @(re-frame/subscribe [::skin/current-skin])]
    [:div
     [translation-steps/select-skin]
     [ui/form-control {:full-width true
                       :margin     "normal"}
      [ui/select {:value         (or current-skin "")
                  :disabled      (not enabled?)
                  :display-empty true
                  :variant       "outlined"
                  :on-click      #(when enabled? (translation-steps/set-select-skin-complete))
                  :on-change     #(re-frame/dispatch [::skin/change-skin (.. % -target -value)])}
       [ui/menu-item {:value "" :disabled true} "Select skin"]
       (when enabled?
         (for [skin skins]
           ^{:key skin}
           [ui/menu-item {:value skin} skin]))]]]))
