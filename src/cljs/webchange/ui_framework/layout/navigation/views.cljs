(ns webchange.ui-framework.layout.navigation.views
  (:require
    [webchange.routes :refer [location redirect-to]]
    [webchange.ui-framework.components.index :refer [button]]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(def menu-items
  [{:title         "My Profile"
    :location-name :profile}
   {:title         "Courses"
    :location-name :courses}
   {:title         "Books"
    :location-name :books}
   {:title      "Create new book"
    :route-name :book-creator}])

(defn- menu-item
  [{:keys [title location-name route-name]}]
  (let [active? false
        handle-click (fn []
                       (cond
                         (some? location-name) (location location-name)
                         (some? route-name) (redirect-to route-name)))]
    [:li {:class-name (get-class-name {"active" active?})}
     [button {:on-click handle-click
              :color    "default"
              :variant  "outlined"}
      title]]))

(defn navigation-menu
  []
  [:ul.navigation-menu
   (for [{:keys [title] :as props} menu-items]
     ^{:key title}
     [menu-item props])])
