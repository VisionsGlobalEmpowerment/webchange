(ns webchange.lesson-builder.widgets.activity-actions.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.components.draggable.views :refer [draggable draggable-list]]
    [webchange.lesson-builder.components.options-list.views :refer [options-list]]
    [webchange.lesson-builder.widgets.activity-actions.state :as state]
    [webchange.lesson-builder.widgets.not-implemented.views :refer [not-implemented]]))

(defn activity-actions
  []
  (let [menu-items @(re-frame/subscribe [::state/menu-items])
        handle-click #(re-frame/dispatch [::state/handle-item-click %])]
    [:div.widget--activity-actions
     [options-list {:items    menu-items
                    :on-click handle-click}]
     [draggable-list
      [draggable {:text   "Add Character Dialogue"
                  :action "add-character-dialogue"}]
      [draggable {:text   "Add Text Dialogue"
                  :action "add-text-animation"}]]]))
