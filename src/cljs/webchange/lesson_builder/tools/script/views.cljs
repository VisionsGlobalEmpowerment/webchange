(ns webchange.lesson-builder.tools.script.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.tools.script.state :as state]
    [webchange.lesson-builder.tools.script.dialog.views :refer [dialog]]
    [webchange.lesson-builder.tools.script.track-selector.views :refer [track-selector]]
    [webchange.ui.index :as ui]))

(defn- header
  []
  [:div.script--header
   [ui/icon {:icon "edit-boxed"}]
   [:div.script--header--text "Script"]])

(defn- choose-sequence
  []
  (let [show-control? @(re-frame/subscribe [::state/show-track-selector?])]
    (when show-control?
      [:<>
       [:h2 "Choose Sequence"]
       [track-selector]])))

(defn script
  []
  (let [dialogs @(re-frame/subscribe [::state/track-dialogs])]
    [:div.widget--script
     [header]
     [choose-sequence]
     [:div.widget--script--content
      (for [{:keys [id] :as dialog-data} dialogs]
        ^{:key id}
        [dialog dialog-data])]]))
