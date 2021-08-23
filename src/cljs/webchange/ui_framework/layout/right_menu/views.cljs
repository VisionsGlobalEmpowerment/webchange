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
))

(def actions-list
  [{:text     "Change Background"
    :on-click background/open-change-background-window}
   {:text     "Background music"
    :on-click background-music/open-set-music-window}
   {:text     "Remove music"
    :confirm  "Are you sure you want remove background music?"
    :on-click background-music/remove-background-music}
   {:text "Create preview image"
    :confirm "Are you sure you want to update activity preview image?"
    :on-click activity-preview/create-preview}
   {:text     "Add image"
    :on-click add-image/open-add-image-window}
   {:text     "Add character"
    :on-click add-character/open-add-character-window}])

;; (defn actions-menu
;;   []
;;   (for [{:keys [text on-click] :as props} activity-actions]
;;     ^{:key text}
;;     [:div.title.pos-r.clear
;;      [:span.float-left text]
;;      [:span.float-right.plus-icon-r
;;       [icon {:icon       "plus"
;;              :class-name "plus-icon"
;;              :on-click   on-click}]]]))

(defn right-menu
  [{:keys [actions scene-data] :or {actions []}}]
  (let [activity-actions (activity-action/get-activity-actions-list scene-data)]
;;     (js/console.log "activity-actions----------------" (actions-list {:scene-data scene-data}))
    [:div.right-side-bar
     [:div.right-side-bar-top.clear
      [:div.float-left
       (first actions)]
      [:div.float-right
       [publish-button]
       (nth actions 1)]]

     [:div.right-side-menu
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
      [:div.title.pos-r.clear.white
       [:span.float-left text]
       [:span.float-right.plus-icon-r
        [icon {:icon       "plus-grey"
               :class-name "plus-icon"
               :on-click   on-click}]]])
        ]]))


;; (defn right-menu
;;   [{:keys [actions] :or {actions []}}]
;;   [:div.right-side-bar
;;    [:div.right-side-bar-top.clear
;;     [:div.float-left
;;      [:span
;;       [icon {:icon       "sync"
;;              :class-name "rotate-icon"}]]
;;      [:span.font-style "Saving Process"]]
;;     [:div.float-right
;;      [:button.button-style.margin-right "Publish"]
;;      [:button.button-style.blue-button "Preview"]]]

;;    [:div.right-side-menu
;;     [:div.title.pos-r.clear
;;      [:span.float-left "Add Ball"]
;;      [:span.float-right.plus-icon-r
;;       [icon {:icon       "plus"
;;              :class-name "plus-icon"}]]]

;;     [:div.side-menu-r
;;      [:h3 "Scene Layers"]
;;      [:ul
;;       [:li.clear
;;        [:div.float-left
;;         [:span.margin-right
;;          [icon {:icon       "text"
;;                 :class-name "text-icon"}]]
;;         [:span "Text 01"]]
;;        [:div.float-right
;;         [:span.margin-right
;;          [icon {:icon       "eye"
;;                 :class-name "text-icon"}]]
;;         [:span.margin-right
;;          [icon {:icon       "slider"
;;                 :class-name "text-icon"}]]
;;         [:span.margin-zero
;;          [icon {:icon       "trash"
;;                 :class-name "text-icon"}]]]]]]
    
;;     [:div.title.pos-r.clear.white
;;      [:span.float-left "Add Image"]
;;      [:span.float-right.plus-icon-r
;;       [icon {:icon       "plus-grey"
;;              :class-name "plus-icon"}]]]
    
;;     [:div.side-menu-r
;;      [:ul
;;       [:li.clear
;;        [:div.float-left
;;         [:span.margin-right
;;          [icon {:icon       "text"
;;                 :class-name "text-icon"}]]
;;         [:span "Text 01"]]
;;        [:div.float-right
;;         [:span.margin-right
;;          [icon {:icon       "eye"
;;                 :class-name "text-icon"}]]
;;         [:span.margin-right
;;          [icon {:icon       "slider"
;;                 :class-name "text-icon"}]]
;;         [:span.margin-zero
;;          [icon {:icon       "trash"
;;                 :class-name "text-icon"}]]]]]]
    
;;     ]])