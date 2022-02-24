(ns webchange.book-library.layout.side-menu.state
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
  (fn [{:keys [db]} [_ page]]
    (let [current-course (paren-state/get-current-course db)]
      {:dispatch [::routes/redirect page :id current-course]})))

(def back-button-values
  {:book-library-read {:icon     "book"
                       :title    "Library"
                       :location [:book-library]}
   :default           {:icon     "home"
                       :title    "Home"
                       :location [:student-course-dashboard]}})

(defn- get-back-button-data
  [handler]
  (if (contains? back-button-values handler)
    (get back-button-values handler)
    (get back-button-values :default)))

(re-frame/reg-sub
  ::back-button-data
  (fn []
    (re-frame/subscribe [::routes/active-route]))
  (fn [{:keys [handler]}]
    (get-back-button-data handler)))

(re-frame/reg-event-fx
  ::handle-back-button-click
  (fn [{:keys [db]} [_]]
    (let [{:keys [handler]} (routes/get-active-route db)
          {:keys [location]} (get-back-button-data handler)
          current-course (paren-state/get-current-course db)]
      {:dispatch (-> (concat [::routes/redirect] location [:id current-course])
                     (vec))})))
