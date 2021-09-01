(ns webchange.ui-framework.layout.right-menu.edit-menu.views
  (:require
    [webchange.ui-framework.components.index :refer [icon-button]]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn- header
  [{:keys [on-back-click]}]
  [:div.edit-menu-header
   [icon-button {:icon     "back"
                 :on-click on-back-click}]])

(defn edit-menu
  [{:keys [edit-menu-content show-edit-menu? on-edit-menu-back]}]
  [:div {:class-name (get-class-name {"edit-menu" true?
                                      "active"    show-edit-menu?})}
   (when (fn? on-edit-menu-back)
     [header {:on-back-click on-edit-menu-back}])
   (when (some? edit-menu-content)
     (into [:div]
           edit-menu-content))])
