(ns webchange.book-library.pages.library.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.book-library.components.categories.views :refer [book-categories category-image]]
    [webchange.book-library.components.loading-indicator.views :refer [loading-indicator]]
    [webchange.book-library.layout.views :refer [layout]]
    [webchange.book-library.pages.library.state :as state]
    [webchange.i18n.translate :as i18n]))

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
     (if-not (empty? books)
       (for [{:keys [id] :as book} books]
         ^{:key id}
         [book-list-item book])
       [:div.user-message "There are no books yet..."])]))

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
    (let [loading? @(re-frame/subscribe [::state/loading?])]
      [layout {:title           @(re-frame/subscribe [::i18n/t [:book-library]])
               :class-name      "book-library-page"
               :toolbar-control [categories]}
       [current-category]
       (if loading?
         [loading-indicator]
         [book-list])])))
