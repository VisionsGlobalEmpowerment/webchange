(ns webchange.book-library.layout.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.book-library.state :as paren-state]
    [webchange.routes :as routes]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:book-library])
       (vec)))

(re-frame/reg-sub
  ::navigation-items
  (fn []
    (re-frame/subscribe [::routes/active-route]))
  (fn [{:keys [handler]}]
    (->> [{:title "Library"
           :icon  "book"
           :page  :book-library}
          #_{:title "Favorite"
             :icon  "heart"
             :page  :book-library-favorite}
          #_{:title "Search"
             :icon  "search"
             :page  :book-library-search}]
         (map (fn [{:keys [page] :as item}]
                (cond-> item
                        (= page handler) (assoc :active? true)))))))

(re-frame/reg-event-fx
  ::open-page
  (fn [{:keys [db]} [_ page]]
    (let [current-course (paren-state/get-current-course db)]
      {:dispatch [::routes/redirect page :id current-course]})))

(re-frame/reg-event-fx
  ::open-home-page
  (fn [{:keys [_]} [_]]
    {:dispatch [::open-page :student-course-dashboard]}))
