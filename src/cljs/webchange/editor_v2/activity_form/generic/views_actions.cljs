(ns webchange.editor-v2.activity-form.generic.views-actions
  (:require
    [webchange.editor-v2.activity-form.generic.components.activity-action.views :as activity-action]
    [webchange.editor-v2.activity-form.generic.components.background-music.views :as background-music]
    [webchange.editor-v2.activity-form.generic.components.change-background.views-background :as background]
    [webchange.editor-v2.activity-form.generic.components.activity-preview.state :as activity-preview]
    [webchange.editor-v2.activity-form.generic.components.add-image.views :as add-image]
    [webchange.ui-framework.components.index :refer [menu]]))

(defn actions
  [{:keys [scene-data]}]
  (let [activity-actions (activity-action/get-activity-actions-list scene-data)]
    [:div.actions
     [activity-action/activity-action-modal]
     [background/change-background-window]
     [background-music/set-music-window]
     [add-image/add-image-window]
     [menu {:icon  "vertical"
            :items (concat activity-actions
                           [{:icon     "background"
                             :text     "Change Background"
                             :on-click background/open-change-background-window}
                            {:icon     "music"
                             :text     "Background music"
                             :on-click background-music/open-set-music-window}
                            {:icon     "music-off"
                             :text     "Remove music"
                             :confirm  "Are you sure you want remove background music?"
                             :on-click background-music/remove-background-music}
                            {:icon ""
                             :text "Create preview image"
                             :confirm "Are you sure you want to update activity preview image?"
                             :on-click activity-preview/create-preview}
                            {:icon     ""
                             :text     "Add image"
                             :on-click add-image/open-add-image-window}])}]]))
