(ns webchange.book-library.layout.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.book-library.layout.icons.index :refer [icons]]
    [webchange.book-library.layout.state :as state]
    [webchange.page-title.views :refer [page-title]]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn- home
  []
  (let [handle-click #(re-frame/dispatch [::state/open-home-page])]
    [:div.home
     [:button {:on-click handle-click
               :title    "Home"}
      [:div.icon
       (get icons "home")]]]))

(defn- toolbar
  [{:keys [title]}]
  [:div.toolbar
   [:h1 title]])

(defn- navigation-item
  [{:keys [active? icon on-click page title]}]
  (let [handle-click #(on-click page)]
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

(defn- get-title
  [sub-title]
  (let [title "Book Library"]
    (if (some? sub-title)
      (str title " - " sub-title)
      title)))

(defn layout
  [{:keys [title document-title]}]
  [:div.book-library-layout
   [home]
   [toolbar {:title title}]
   [navigation]
   (into [:div.content]
         (-> (r/current-component) (r/children)))
   [page-title {:title (get-title document-title)}]])
