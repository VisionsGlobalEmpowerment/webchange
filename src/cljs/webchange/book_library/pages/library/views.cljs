(ns webchange.book-library.pages.library.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.book-library.components.categories.views :refer [book-categories category-image]]
    [webchange.book-library.layout.views :refer [layout]]
    [webchange.book-library.pages.library.state :as state]))

(defn- book-list-item
  [{:keys [book-id cover title]}]
  (let [handle-click #(re-frame/dispatch [::state/open-book book-id])]
    [:li.book-list-item {:title    title
                         :on-click handle-click}
     [:img.book-title {:src cover}]]))

(defn- book-list
  []
  (let [books @(re-frame/subscribe [::state/books-list])]
    [:ul.book-list
     (for [{:keys [id] :as book} books]
       ^{:key id}
       [book-list-item book])]))

(defn- categories
  []
  (let [{:keys [value]} @(re-frame/subscribe [::state/current-category])
        handle-change #(re-frame/dispatch [::state/set-current-category %])]
    [book-categories {:value     value
                      :on-change handle-change}]))

(defn- current-category
  []
  (let [{:keys [name value] :as category} @(re-frame/subscribe [::state/current-category])]
    (when (some? category)
      [:div.current-category
       [category-image {:value value}]
       name])))

(defn page
  [{:keys [id]}]
  (re-frame/dispatch [::state/init {:course-id id}])
  (fn []
    [layout {:title           "Book Library"
             :class-name      "book-library-page"
             :toolbar-control [categories]}
     [current-category]
     [book-list]]))
