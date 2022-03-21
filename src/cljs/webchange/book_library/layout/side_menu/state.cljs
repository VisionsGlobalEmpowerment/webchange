(ns webchange.book-library.layout.side-menu.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.book-library.pages.library.state :as library]
    [webchange.book-library.state :as paren-state]
    [webchange.i18n.translate :as i18n]
    [webchange.routes :as routes]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:book-library])
       (vec)))

(re-frame/reg-sub
  ::navigation-items
  (fn []
    [(re-frame/subscribe [::routes/active-route])
     (re-frame/subscribe [::i18n/t {:book-library-title [:book-library]
                                    :favorite-title     [:favorite]
                                    :search-title       [:search]}])])
  (fn [[{:keys [handler]} {:keys [book-library-title favorite-title search-title]}]]
    (->> [{:title book-library-title
           :icon  "book"
           :page  :book-library
           :event [::library/reset]}
          #_{:title favorite-title
             :icon  "heart"
             :page  :book-library-favorite}
          #_{:title search-title
             :icon  "search"
             :page  :book-library-search}]
         (map (fn [{:keys [page] :as item}]
                (cond-> item
                        (= page handler) (assoc :active? true)))))))

(re-frame/reg-event-fx
  ::open-page
  (fn [{:keys [db]} [_ {:keys [event page]}]]
    (let [current-course (paren-state/get-current-course db)]
      {:dispatch-n (cond-> [[::routes/redirect page :id current-course]]
                           (some? event) (conj event))})))

(def back-button-values
  {:book-library-read {:icon     "book"
                       :title    :book-library
                       :location [:book-library]}
   :default           {:icon     "home"
                       :title    :home-page
                       :location [:student-course-dashboard]}})

(defn- get-back-button-data
  [handler]
  (if (contains? back-button-values handler)
    (get back-button-values handler)
    (get back-button-values :default)))

(re-frame/reg-sub
  ::back-button-data
  (fn []
    [(re-frame/subscribe [::routes/active-route])
     (re-frame/subscribe [::i18n/t {:book-library [:book-library]
                                    :home-page    [:home-page]}])])
  (fn [[{:keys [handler]} translations]]
    (-> (get-back-button-data handler)
        (update :title #(get translations %)))))

(re-frame/reg-event-fx
  ::handle-back-button-click
  (fn [{:keys [db]} [_]]
    (let [{:keys [handler]} (routes/get-active-route db)
          {:keys [location]} (get-back-button-data handler)
          current-course (paren-state/get-current-course db)]
      {:dispatch (-> (concat [::routes/redirect] location [:id current-course])
                     (vec))})))
