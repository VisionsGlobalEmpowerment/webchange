(ns webchange.editor-v2.activity-form.generic.components.change-background.views-background
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [webchange.editor-v2.activity-form.generic.components.change-background.views-image-selector :refer [image-selector]]
    [webchange.editor-v2.activity-form.generic.components.change-background.views-preview :refer [preview]]
    [webchange.editor-v2.activity-form.generic.components.change-background.views-type-selector :refer [type-selector]]
    [webchange.editor-v2.activity-form.generic.components.change-background.state :as background]))

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

(defn open-change-background-window
  []
  (re-frame/dispatch [::background/open-window]))

(defn change-background-window
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
