(ns webchange.book-library.pages.library.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.book-library.state :as parent-state]
    [webchange.routes :as routes]
    [webchange.state.warehouse :as warehouse]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:library])
       (parent-state/path-to-db)))

(re-frame/reg-event-fx
  ::init
  (fn [{:keys [_]} [_ {:keys [course-id]}]]
    {:dispatch-n [[::parent-state/init {:course-id course-id}]
                  [::load-books]]}))

(re-frame/reg-event-fx
  ::load-books
  (fn [{:keys [_]} [_]]
    {:dispatch [::warehouse/load-course-books {:on-success [::load-books-success]}]}))

(re-frame/reg-event-fx
  ::load-books-success
  (fn [{:keys [_]} [_ response]]
    (let [books (->> response
                     (map (fn [book]
                            (assoc book :id (random-uuid)))))]
      {:dispatch [::set-available-books books]})))

;; Available books

(def available-books-path (path-to-db [:available-books]))

(defn get-available-books
  [db]
  (get-in db available-books-path))

(re-frame/reg-sub
  ::available-books
  get-available-books)

(re-frame/reg-event-fx
  ::set-available-books
  (fn [{:keys [db]} [_ data]]
    {:db (assoc-in db available-books-path data)}))

;; Books list

(re-frame/reg-sub
  ::books-list
  (fn []
    [(re-frame/subscribe [::available-books])])
  (fn [[available-books]]
    available-books))

(re-frame/reg-event-fx
  ::open-book
  (fn [{:keys [db]} [_ book-id]]
    (let [current-course (parent-state/get-current-course db)]
      {:dispatch [::routes/redirect :book-library-read :id current-course :book-id book-id]})))
