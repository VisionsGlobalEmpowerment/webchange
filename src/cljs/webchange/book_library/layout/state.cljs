(ns webchange.book-library.layout.state
  (:require
    [re-frame.core :as re-frame]
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
          {:title "Favorite"
           :icon  "heart"
           :page  :book-library-favorite}
          {:title "Search"
           :icon  "search"
           :page  :book-library-search}]
         (map (fn [{:keys [page] :as item}]
                (cond-> item
                        (= page handler) (assoc :active? true)))))))

(re-frame/reg-event-fx
  ::open-page
  (fn [{:keys [_]} [_ page]]
    {:dispatch [::routes/redirect page]}))

(re-frame/reg-event-fx
  ::open-home-page
  (fn [{:keys [_]} [_]]
    (let [current-course "english"]
      {:dispatch [::routes/redirect :student-course-dashboard :id current-course]})))
