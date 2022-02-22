(ns webchange.book-library.pages.library.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.book-library.layout.views :refer [layout]]
    [webchange.book-library.pages.library.state :as state]))

(defn page
  [{:keys [id]}]
  (re-frame/dispatch [::state/init {:course-id id}])
  (fn []
    [layout {:title "Book Library"}
     [:div]]))
