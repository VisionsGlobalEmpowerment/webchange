(ns webchange.editor-v2.dialog.dialog-form.views-audios-list-item-menu
  (:require
    [cljs-react-material-ui.icons :as ic]
    [cljs-react-material-ui.reagent :as ui]
    [reagent.core :as r]
    [webchange.editor-v2.components.confirm.views :refer [with-confirmation]]))

(defn- get-styles
  []
  {:menu-button    {:padding "8px"}
   :menu-item-icon {:font-size "18px"}})

(defn audio-menu
  [{:keys [on-edit on-delete on-bring-to-top on-clear-selection]}]
  (r/with-let [menu-anchor (r/atom nil)]
    (let [handle-edit #(do (reset! menu-anchor nil) (on-edit))
          handle-bring-to-top #(do (reset! menu-anchor nil) (on-bring-to-top))
          handle-delete #(do (reset! menu-anchor nil) (on-delete))
          handle-cancel #(reset! menu-anchor nil)
          handle-clear-selection #(do (reset! menu-anchor nil) (on-clear-selection))
          styles (get-styles)]
      [:div
       [ui/icon-button
        {:style    (:menu-button styles)
         :on-click #(reset! menu-anchor (.-currentTarget %))}
        [ic/more-horiz {:style (:menu-item-icon styles)}]]
       [ui/menu
        {:anchor-el @menu-anchor
         :open      (boolean @menu-anchor)
         :on-close  #(reset! menu-anchor nil)}
        [ui/menu-item {:on-click handle-bring-to-top}
         [ui/list-item-icon
          [ic/vertical-align-top {:style (:menu-item-icon styles)}]]
         "Bring To Top"]
        [ui/menu-item {:on-click handle-edit}
         [ui/list-item-icon
          [ic/edit {:style (:menu-item-icon styles)}]]
         "Name Recording"]
        [ui/menu-item {:on-click handle-clear-selection}
         [ui/list-item-icon
          [ic/edit {:style (:menu-item-icon styles)}]]
         "Clear selection"]
        [with-confirmation {:message    "Remove audio asset from scene?"
                            :on-confirm handle-delete
                            :on-cancel  handle-cancel}
         [ui/menu-item
          [ui/list-item-icon
           [ic/delete {:style (:menu-item-icon styles)}]]
          "Delete"]]]])))