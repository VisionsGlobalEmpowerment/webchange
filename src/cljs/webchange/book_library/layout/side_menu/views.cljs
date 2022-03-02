(ns webchange.book-library.layout.side-menu.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.book-library.layout.icons.index :refer [icons]]
    [webchange.book-library.layout.side-menu.state :as state]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn- back-button
  []
  (let [{:keys [icon title]} @(re-frame/subscribe [::state/back-button-data])
        handle-click #(re-frame/dispatch [::state/handle-back-button-click])]
    [:div.back-button
     [:button {:on-click handle-click
               :title    title}
      (get icons icon)]]))

(defn- navigation-item
  [{:keys [active? icon on-click title] :as props}]
  (let [handle-click #(on-click props)]
    [:button {:class-name (get-class-name {"navigation-item" true
                                           "active"          active?})
              :on-click   handle-click
              :title      title}
     [:div.icon
      (get icons icon)]]))

(defn- navigation
  []
  (let [items @(re-frame/subscribe [::state/navigation-items])
        handle-item-click #(re-frame/dispatch [::state/open-page %])]
    [:div.navigation
     (for [{:keys [page] :as item} items]
       ^{:key page}
       [navigation-item (merge item {:on-click handle-item-click})])]))

(defn side-menu
  [{:keys [show-navigation?]}]
  [:div {:class-name (get-class-name {"side-menu"       true
                                      "with-background" show-navigation?})}
   [back-button]
   (when show-navigation?
     [navigation])])
