(ns webchange.ui-framework.layout.right-menu.views
  (:require
    [webchange.ui-framework.components.index :refer [icon]]
    [webchange.editor-v2.activity-form.common.objects-tree.views :refer [objects-tree]]

    ;;for action
    [webchange.editor-v2.activity-form.generic.components.activity-action.views :as activity-action]
    [webchange.editor-v2.activity-form.generic.components.add-character.views :as add-character]
    [webchange.editor-v2.activity-form.generic.components.background-music.views :as background-music]
    [webchange.editor-v2.activity-form.generic.components.change-background.views-background :as background]
    [webchange.editor-v2.activity-form.generic.components.activity-preview.state :as activity-preview]
    [webchange.editor-v2.activity-form.generic.components.add-image.views :as add-image]

    [webchange.editor-v2.layout.components.course-status.views :refer [publish-button]]

    [webchange.ui-framework.components.utils :refer [get-class-name]]
    [webchange.ui-framework.layout.right-menu.edit-menu.views :refer [edit-menu]]))

(def actions-list
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

(defn right-menu
  [{:keys [actions class-name edit-menu-content on-edit-menu-back show-edit-menu? scene-data] :or {actions []}}]
  (let [activity-actions (activity-action/get-activity-actions-list scene-data)]
    [:div {:class-name (get-class-name (cond-> {"right-side-bar" true}
                                               (some? class-name) (assoc class-name true)))}
     [:div.right-side-bar-top
      [:div
       (first actions)]
      [:div.actions
       [publish-button]
       (nth actions 1)]]
     [:div.right-side-menu-content
      [edit-menu {:edit-menu-content edit-menu-content
                  :show-edit-menu?   show-edit-menu?
                  :on-edit-menu-back on-edit-menu-back}]
      (for [{:keys [text on-click] :as props} activity-actions]
        ^{:key text}
        [:div.title.pos-r.clear
         [:span.float-left text]
         [:span.float-right.plus-icon-r
          [icon {:icon       "plus"
                 :class-name "plus-icon"
                 :on-click   on-click}]]])

      [:div.side-menu-r
       [:h3 "Scene Layers"]
       [:ul
        [:li.clear
         [objects-tree]]]]

      (for [{:keys [text on-click] :as props} actions-list]
        ^{:key text}
        [:div.title.pos-r.clear.white
         [:span.float-left text]
         [:span.float-right.plus-icon-r
          [icon {:icon       "plus-grey"
                 :class-name "plus-icon"
                 :on-click   on-click}]]])]]))
