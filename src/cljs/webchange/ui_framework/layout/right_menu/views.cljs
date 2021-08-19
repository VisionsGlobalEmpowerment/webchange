(ns webchange.ui-framework.layout.right-menu.views
  (:require
    [webchange.ui-framework.components.index :refer [icon]]))

(defn right-menu
  [{:keys [actions] :or {actions []}}]
  [:div.right-side-bar
   [:div.right-side-bar-top.clear
    [:div.float-left
     [:span
      [icon {:icon       "sync"
             :class-name "rotate-icon"}]]
     [:span.font-style "Saving Process"]]
    [:div.float-right
     [:button.button-style.margin-right "Publish"]
     [:button.button-style.blue-button "Preview"]]]

   [:div.right-side-menu
    [:div.title.pos-r.clear
     [:span.float-left "Add Ball"]
     [:span.float-right.plus-icon-r
      [icon {:icon       "plus"
             :class-name "plus-icon"}]]]

    [:div.side-menu-r
     [:h3 "Scene Layers"]
     [:ul
      [:li.clear
       [:div.float-left
        [:span.margin-right 
         [icon {:icon       "text"
                :class-name "text-icon"}]]
        [:span "Text 01"]]
       [:div.float-right
        [:span.margin-right 
         [icon {:icon       "eye"
                :class-name "text-icon"}]]
        [:span.margin-right 
         [icon {:icon       "slider"
                :class-name "text-icon"}]]
        [:span.margin-zero
         [icon {:icon       "trash"
                :class-name "text-icon"}]]]]]]]])
