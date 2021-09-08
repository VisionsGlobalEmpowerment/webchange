(ns webchange.ui-framework.layout.navigation.views
  (:require
    [webchange.routes :refer [location redirect-to]]
    [webchange.ui-framework.components.index :refer [icon]]
    [webchange.ui-framework.components.utils :refer [get-class-name]]
    [webchange.ui-framework.layout.user-widget.views :refer [user-widget]]
    [webchange.ui-framework.layout.logo.views :refer [logo]]))

(def dashboard-menu 
  [{:title         "Dashboard"
    :icon-name     "dashboard"
    :location-name :welcome-screen
    :url           "https://www.webchange.com/user/dashboard"
    :icon-class    "menu-icon-style"}
   {:title         "My Games"
    :icon-name     "game"
    :location-name :title
    :url           "https://www.webchange.com/user/mygames"
    :icon-class    "menu-icon-style"}
   {:title         "My Books"
    :icon-name     "book"
    :location-name :game-library
    :url           "https://www.webchange.com/user/mybooks"
    :icon-class    "menu-icon-style"}])

(def create-menu
  [{:title         "Create a game"
    :icon-name     "create-game"
    :location-name :book-library
    :url           "https://www.webchange.com/user/profile"
    :icon-class    "menu-icon-style"}
   {:title         "Create a book"
    :icon-name     "create-book"
    :location-name :translate
    :url           "https://host.webchange.com/book-creator"
    :icon-class    "menu-icon-style"}])

(def explore-menu
  [{:title         "Game Library"
    :icon-name     "game-library"
    :location-name :my-profile
    :url           "https://www.webchange.com/user/games"
    :icon-class    "menu-icon-style"}
   {:title         "Book Library"
    :icon-name     "book-library"
    :location-name :my-books
    :url           "https://www.webchange.com/user/books"
    :icon-class    "menu-icon-style"}])

(def logout-menu
  [{:title         "Logout"
    :icon-name     "logout"
    :location-name :my-profile
    :url           ""
    :icon-class    "menu-icon-style"}])

(defn- menu-item
  [{:keys [title icon-name icon-class url] :as props}]
  [:li
   [:a {:href url}
    [:span.menu-icon
     [icon {:icon       icon-name
            :class-name icon-class}]]
    [:span.menu-label
     title]]])

(defn- menu
  [{:keys [items]}]
  [:ul.nav-item-section
   (for [{:keys [title] :as props} items]
     ^{:key title}
     [menu-item props])])

(defn navigation-menu
  [{:keys [class-name]}]
  [:div {:class-name (get-class-name (cond-> {"left-menu" true}
                                       (some? class-name) (assoc class-name true)))}
   [:div.logo-icon
    [logo]]
   [:section.nav-link-section
     [menu {:items dashboard-menu}]
    [:div.menu-title.sub-menu-title
     "Create"]
     [menu {:items create-menu}]
    [:div.menu-title.sub-menu-title
     "Explore"]
     [menu {:items explore-menu}]
    [:section.logout-section
      [menu {:items logout-menu}]]
    [user-widget]]])
