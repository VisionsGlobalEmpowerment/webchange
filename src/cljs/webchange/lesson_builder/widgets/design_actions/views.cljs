(ns webchange.lesson-builder.widgets.design-actions.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.widgets.design-actions.state :as state]
    [webchange.ui.index :as ui]))

(defn design-actions
  []
  (let [handle-add-character-click #(re-frame/dispatch [::state/open-add-character-menu])
        handle-add-image-click #(re-frame/dispatch [::state/open-add-image-menu])]
    [:div.widget--design-actions
     "design-actions"
     [ui/button {:on-click handle-add-image-click} "Add Image"]
     [ui/button {:on-click handle-add-character-click} "Add Character"]]))
