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
    :li-class      "dashboard-li"
    :location-name :welcome-screen
    :url           "https://www.webchange.com/user/dashboard"
    :icon-class    "menu-icon-style"}
   {:title         "My Games"
    :icon-name     "game"
    :li-class      "dashboard-li"
    :location-name :title
    :url           "https://www.webchange.com/user/mygames"
    :icon-class    "rocket-icon"}
   {:title         "My Books"
    :icon-name     "book"
    :li-class      "dashboard-li"
    :location-name :game-library
    :url           "https://www.webchange.com/user/mybooks"
    :icon-class    "menu-icon-style"}])

(def create-menu
  [{:title         "Create a game"
    :icon-name     "create-game"
    :li-class      "create-li"
    :location-name :book-library
    :url           "/game-changer"
    :icon-class    "menu-icon-style"}
   {:title         "Create a book"
    :icon-name     "create-book"
    :li-class      "create-li"
    :location-name :translate
    :url           "/book-creator"
    :icon-class    "menu-icon-style"}])

(def explore-menu
  [{:title         "Game Library"
    :icon-name     "game-library"
    :li-class      "explore-li"
    :location-name :my-profile
    :url           "https://www.webchange.com/user/games"
    :icon-class    "rocket-icon"}
   {:title         "Book Library"
    :icon-name     "book-library"
    :li-class      "explore-li"
    :location-name :my-books
    :url           "https://www.webchange.com/user/books"
    :icon-class    "menu-icon-style"}])

(def logout-menu
  [{:title         "Logout"
    :icon-name     "logout"
    :li-class      "logout-li"
    :location-name :my-profile
    :url           ""
    :icon-class    "menu-icon-style"}])

(defn- menu-item
  [{:keys [title icon-name li-class icon-class url] :as props}]
  [:li {:class-name li-class}
   [:a {:href url}
    [:span.menu-icon
     [icon {:icon       icon-name
            :class-name icon-class}]]
    [:span.menu-label
     title]
    [:div.clear]]])

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
    [:div.menu-title.sub-menu-title.explore-section
     "Explore"]
    [menu {:items explore-menu}]
    [:section.logout-section
     [menu {:items logout-menu}]]
    [user-widget]]])
