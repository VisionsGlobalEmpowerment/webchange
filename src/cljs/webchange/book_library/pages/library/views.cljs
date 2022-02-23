(ns webchange.book-library.pages.library.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.book-library.layout.views :refer [layout]]
    [webchange.book-library.pages.library.state :as state]))

(defn- book-list-item
  [{:keys [cover title]}]
  [:li.book-list-item {:title title}
   [:img.book-title {:src cover}]])

(defn- book-list
  []
  (let [books @(re-frame/subscribe [::state/books-list])]
    [:ul.book-list
     (for [{:keys [id] :as book} books]
       ^{:key id}
       [book-list-item book])]))

(defn page
  [{:keys [id]}]
  (re-frame/dispatch [::state/init {:course-id id}])
  (fn []
    [layout {:title      "Book Library"
             :class-name "book-library-page"}
     [book-list]]))
