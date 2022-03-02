(ns webchange.book-library.pages.library.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.book-library.state :as parent-state]
    [webchange.routes :as routes]
    [webchange.state.warehouse :as warehouse]
    [webchange.utils.map :refer [map-keys]]))

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
  ::reset
  (fn [{:keys [_]} [_]]
    {:dispatch [::reset-current-category]}))

(re-frame/reg-event-fx
  ::load-books
  (fn [{:keys [_]} [_]]
    {:dispatch-n [[::warehouse/load-course-books {:on-success [::load-books-success]}]
                  [::set-state {:loading? true}]]}))

(re-frame/reg-event-fx
  ::load-books-success
  (fn [{:keys [_]} [_ response]]
    (let [books (->> response
                     (map (fn [book]
                            (-> (map-keys book
                                          {:id        :id
                                           :image-src :cover
                                           :lang      :lang
                                           :name      :title
                                           :slug      :book-id})
                                (assoc :categories (get-in book [:metadata :categories] []))
                                (assoc :keywords (get-in book [:metadata :keywords] []))
                                (dissoc :metadata)))))]
      {:dispatch-n [[::set-available-books books]
                    [::set-state {:loading? false}]]})))

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
    [(re-frame/subscribe [::available-books])
     (re-frame/subscribe [::current-category])])
  (fn [[available-books {:keys [value]}]]
    (if (some? value)
      (filter (fn [{:keys [categories]}]
                (some #{value} categories))
              available-books)
      available-books)))

(re-frame/reg-event-fx
  ::open-book
  (fn [{:keys [db]} [_ book-id]]
    (let [current-course (parent-state/get-current-course db)]
      {:dispatch [::routes/redirect :book-library-read :id current-course :book-id book-id]})))

;; Current category

(def current-category-path (path-to-db [:current-category]))

(defn get-current-category
  [db]
  (get-in db current-category-path))

(re-frame/reg-sub
  ::current-category
  get-current-category)

(re-frame/reg-event-fx
  ::set-current-category
  (fn [{:keys [db]} [_ data]]
    {:db (assoc-in db current-category-path data)}))

(re-frame/reg-event-fx
  ::reset-current-category
  (fn [{:keys [_]} [_]]
    {:dispatch [::set-current-category nil]}))

;; State

(def state-path (path-to-db [:state]))

(re-frame/reg-sub
  ::state
  (fn [db]
    (get-in db state-path)))

(re-frame/reg-sub
  ::loading?
  (fn []
    (re-frame/subscribe [::state]))
  (fn [state]
    (get state :loading? true)))

(re-frame/reg-event-fx
  ::set-state
  (fn [{:keys [db]} [_ data]]
    {:db (assoc-in db state-path data)}))
