(ns webchange.ui-framework.layout.right-menu.views
  (:require
    [webchange.ui-framework.components.index :refer [icon]]))

(defn right-menu
  [{:keys [actions] :or {actions []}}]
  [:div.right-side-bar
   [:div.right-side-bar-top.clear
    [:div.float-left
     [:span.margin-right
      [icon {:icon       "sync"
             :class-name "rotate-icon"}]]
     [:span.font-style "Saving Process"]]
    [:div.float-right
     [:button.button-style.margin-right "Publish"]
     [:button.button-style.blue-button "Preview"]]]

   [:div.title-txt-img.pos-r.clear
    [:span.float-left "Add Ball"]
    [:span.float-right.plus-icon-r
     [:img {:alt "", :src "images/plus-icon.svg"}]]]])
