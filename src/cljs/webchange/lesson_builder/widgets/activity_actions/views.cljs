(ns webchange.lesson-builder.widgets.activity-actions.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.components.draggable.views :refer [draggable draggable-list]]
    [webchange.lesson-builder.components.options-list.views :refer [options-list]]
    [webchange.lesson-builder.widgets.activity-actions.state :as state]
    [webchange.lesson-builder.widgets.not-implemented.views :refer [not-implemented]]))

(defn activity-actions
  []
  (let [handle-click #(re-frame/dispatch [::state/handle-item-click %])]
    [:div.widget--activity-actions
     [options-list {:items    [{:id   :image-add
                                :text "Add Image"
                                :icon "plus"}
                               {:id   :character-add
                                :text "Add Character"
                                :icon "plus"}
                               {:id   :background-image
                                :text "Background"
                                :icon "plus"}
                               {:id   :background-music
                                :text "Background Music"
                                :icon "plus"}]
                    :on-click handle-click}]
     [draggable-list
      [draggable {:text "Add Character Dialogue"}]
      [draggable {:text "Add Text Dialogue"}]]]))
