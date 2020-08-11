(ns webchange.editor-v2.scene.data.background.views-background
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [webchange.editor-v2.scene.state.background :as background]
    [webchange.editor-v2.scene.data.background.views-image-selector :refer [image-selector]]
    [webchange.editor-v2.scene.data.background.views-preview :refer [preview]]
    [webchange.editor-v2.scene.data.background.views-type-selector :refer [type-selector]]))

(defn- get-styles
  []
  {:main-form {:width "700px"}})

(defn- change-background-form
  []
  (let [styles (get-styles)]
    [:div {:style (:main-form styles)}
     [type-selector]
     [image-selector]
     [preview]]))

(defn- change-background-window
  []
  (let [window-open? @(re-frame/subscribe [::background/window-open?])
        handle-cancel (fn [] (re-frame/dispatch [::background/close-window]))
        handle-save (fn []
                      (re-frame/dispatch [::background/save-changes])
                      (re-frame/dispatch [::background/close-window]))]
    [ui/dialog {:open      window-open?
                :max-width "lg"
                :on-close  handle-cancel}
     [ui/dialog-title "Change Background"]
     [ui/dialog-content
      [change-background-form]]
     [ui/dialog-actions
      [ui/button {:on-click handle-cancel}
       "Cancel"]
      [ui/button {:color    "secondary"
                  :variant  "contained"
                  :on-click handle-save}
       "Save"]]]))

(defn change-background
  []
  (let [handle-click (fn [] (re-frame/dispatch [::background/open-window]))]
    [:<>
     [ui/form-control {:full-width true
                      :margin     "normal"}
     [ui/button
      {:on-click handle-click}
      "Change Background"]
     [change-background-window]]
     [ui/form-control {:full-width true
                      :margin     "normal"}
     [ui/button
      {:on-click handle-click}
      "Change Background"]
     [change-background-window]]]
    ))
