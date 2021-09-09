(ns webchange.ui-framework.layout.right-menu.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.ui-framework.components.index :refer [icon]]
    [webchange.ui-framework.layout.right-menu.object-tree-menu.views :refer [objects-tree-menu]]
    [webchange.editor-v2.activity-form.generic.components.activity-action.views :as activity-action]
    [webchange.editor-v2.activity-form.generic.components.add-character.views :as add-character]
    [webchange.editor-v2.activity-form.generic.components.background-music.views :as background-music]
    [webchange.editor-v2.activity-form.generic.components.change-background.views-background :as background]
    [webchange.editor-v2.activity-form.generic.components.activity-preview.state :as activity-preview]
    [webchange.editor-v2.activity-form.generic.components.add-image.views :as add-image]
    [webchange.ui-framework.components.utils :refer [get-class-name]]
    [webchange.ui-framework.layout.right-menu.edit-menu.views :refer [edit-menu]]
    [webchange.editor-v2.components.activity-tracks.state :as state]))

(def default-actions-list
  [{:text     "Change Background"
    :on-click background/open-change-background-window}
   {:text     "Background music"
    :on-click background-music/open-set-music-window}
   {:text     "Remove music"
    :confirm  "Are you sure you want remove background music?"
    :on-click background-music/remove-background-music}
   {:text     "Create preview image"
    :confirm  "Are you sure you want to update activity preview image?"
    :on-click activity-preview/create-preview}
   {:text     "Add image"
    :on-click add-image/open-add-image-window}
   {:text     "Add character"
    :on-click add-character/open-add-character-window}])

(defn actions-item
  [{:keys [text on-click]}]
  [:div.actions-section.clear {:on-click on-click}
   [:span.actions-name text]
   [:span.add-actions
    [icon {:icon       "add"
           :class-name "add-actions-icon"}]]])

(defn default-actions-item
  [{:keys [text on-click]}]
  [:div.default-actions-section.clear {:on-click on-click}
   [:span.actions-name text]
   [:span.add-actions
    [icon {:icon       "add"
           :class-name "add-default-actions-icon"}]]])

(defn right-menu
  [{:keys [actions class-name edit-menu-content on-edit-menu-back show-edit-menu? scene-data] :or {actions []}}]
  (let [activity-actions (activity-action/get-activity-actions-list scene-data)
        main-track-actions @(re-frame/subscribe [::state/main-track-actions])
        combined-actions (concat main-track-actions activity-actions)]
    [:div {:class-name (get-class-name (cond-> {"right-side-bar" true}
                                         (some? class-name) (assoc class-name true)))}
     (into [:div.header-section]
           actions)
     [:div.content-section
      [edit-menu {:edit-menu-content edit-menu-content
                  :show-edit-menu?   show-edit-menu?
                  :on-edit-menu-back on-edit-menu-back}]
      [activity-action/activity-action-modal]
      [add-character/add-character-window]
      [background/change-background-window]
      [background-music/set-music-window]
      [add-image/add-image-window]
      (when-not show-edit-menu?
        [:div
         (for [{:keys [text on-click] :as props} combined-actions]
           ^{:key text}
           [actions-item props])
         [:div.scene-section
          [:h3 "Scene Layers"]
          [objects-tree-menu]]
         (for [{:keys [text on-click] :as props} default-actions-list]
           ^{:key text}
           [default-actions-item props])])]]))
