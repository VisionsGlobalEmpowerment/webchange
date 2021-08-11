(ns webchange.ui-framework.layout.navigation.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.state.core :as state]
    [webchange.routes :refer [location redirect-to]]
    [webchange.ui-framework.components.utils :refer [get-class-name]]
    [webchange.ui-framework.layout.user-widget.views :refer [user-widget]]
    [webchange.ui-framework.layout.logo.views :refer [logo]]
    [webchange.ui-framework.components.index :refer [icon]]))

(def menu-items
  [{:title         "Dashboard"
    :icon-name     "user"
    :location-name :welcome-screen}
   {:title         "Create"
    :icon-name     "user"
    :location-name :title}
   {:title         "Game library"
    :icon-name     "user"
    :location-name :game-library}
   {:title         "Books library"
    :icon-name     "user"
    :location-name :book-library}
   {:title         "Translate"
    :icon-name     "user"
    :location-name :translate}
   {:title         "My Profile"
    :icon-name     "user"
    :location-name :my-profile}
   {:title         "My Books"
    :icon-name     "user"
    :location-name :my-books}
   {:title         "My Games"
    :icon-name     "user"
    :location-name :my-games}])

(declare menu)

(defn- goto
  [{:keys [location-name route-name route-params]}]
  (cond
    (some? location-name) (location location-name)
    (some? route-name) (if (sequential? route-params)
                         (->> (concat [route-name] route-params)
                              (apply redirect-to))
                         (redirect-to route-name))))

(defn- menu-item
  [{:keys [children title icon-name] :as props}]
  (let [active? false
        handle-click #(goto props)]
    [:li {:class-name (get-class-name {"menu-item" true
                                       "active"    active?})}
     [:span
      [icon {:icon       icon-name
             :class-name "user-icon"}]]
     [:span   {:on-click handle-click}
      title]
     (when-not (empty? children)
       [menu {:items children}])]))

(defn- menu
  [{:keys [items]}]
  [:ul
   (for [{:keys [title] :as props} items]
     ^{:key title}
     [menu-item props])])

(defn navigation-menu
  []
    [:div.left-menu
     [:div.icon-top
      [logo]]
     [:div.nav-menu
      [menu {:items menu-items}]]
      [user-widget]])
