(ns webchange.book-library.layout.side-menu.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.book-library.components.button.views :refer [button]]
    [webchange.book-library.layout.side-menu.state :as state]
    [webchange.ui.utils.get-class-name :refer [get-class-name]]))

(defn- back-button
  []
  (let [{:keys [icon title]} @(re-frame/subscribe [::state/back-button-data])
        handle-click #(re-frame/dispatch [::state/handle-back-button-click])]
    [:div.back-button-container
     [button {:icon     icon
              :title    title
              :on-click handle-click}]]))

(defn- navigation-item
  [{:keys [active? icon on-click title] :as props}]
  (let [handle-click #(on-click props)]
    [:div {:class-name (get-class-name {"navigation-item" true
                                        "active"          active?})
           :on-click   handle-click
           :title      title}
     [button {:icon              icon
              :active?           active?
              :button-class-name "navigation-item-button"}]]))

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
