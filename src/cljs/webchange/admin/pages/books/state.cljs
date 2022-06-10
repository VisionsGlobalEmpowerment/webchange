(ns webchange.admin.pages.books.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.admin.routes :as routes]
    [webchange.state.warehouse :as warehouse]))

(def path-to-db :page/books)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

;; books Loading

(def books-loading-key :books-loading?)

(defn- set-books-loading
  [db value]
  (assoc db books-loading-key value))

(re-frame/reg-sub
  ::books-loading?
  :<- [path-to-db]
  #(get % books-loading-key false))

;; books Data

(def books-key :books)

(defn- get-books
  [db]
  (get db books-key))

(defn- set-books
  [db value]
  (assoc db books-key value))

(re-frame/reg-sub
  ::books
  :<- [path-to-db]
  #(get-books %))

;;

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db       (set-books-loading db true)
     :dispatch [::warehouse/load-available-books
                {:on-success [::load-books-success]}]}))

(re-frame/reg-event-fx
  ::load-books-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ data]]
    {:db (-> db
             (set-books-loading false)
             (set-books data))}))

(re-frame/reg-event-fx
  ::open-book
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ book-id]]
    (print "::open-book")
    {}))

(re-frame/reg-event-fx
  ::edit-book
  [(i/path path-to-db)]
  (fn [{:keys [_]} [_ book-id]]
    {:dispatch [::routes/redirect :book-edit :book-id book-id]}))
