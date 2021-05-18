(ns webchange.ui-framework.layout.navigation.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.routes :refer [location redirect-to]]
    [webchange.state.core :as state]
    [webchange.ui-framework.components.index :refer [button]]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

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
  [{:keys [children title] :as props}]
  (let [active? false
        handle-click #(goto props)]
    [:li {:class-name (get-class-name {"menu-item" true
                                       "active"    active?})}
     [button {:on-click handle-click
              :color    "default"
              :variant  "outlined"}
      title]
     (when-not (empty? children)
       [menu {:items children}])]))

(defn- menu
  [{:keys [items]}]
  [:ul.menu
   (for [{:keys [title] :as props} items]
     ^{:key title}
     [menu-item props])])

(defn navigation-menu
  []
  (let [current-course-id @(re-frame/subscribe [::state/current-course-id])
        menu-items [{:title         "My Profile"
                     :location-name :profile}
                    (cond-> {:title         "Courses"
                             :location-name :courses}
                            (some? current-course-id) (assoc :children [{:title        "Current course"
                                                                         :route-name   :course-editor-v2
                                                                         :route-params [:id current-course-id]}]))
                    {:title         "Books"
                     :location-name :books}
                    {:title      "Create new book"
                     :route-name :book-creator}]]
    [:div.navigation-menu
     [menu {:items menu-items}]]))
