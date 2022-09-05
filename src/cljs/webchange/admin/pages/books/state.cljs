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

(re-frame/reg-sub
  ::books-loading?
  :<- [path-to-db]
  (fn [db]
    (or (:visible-books-loading db)
        (:my-books-loading db))))

;; books Data

(re-frame/reg-sub
  ::show-my-global?
  :<- [path-to-db]
  (fn [db]
    (get db :show-my-global? true)))

(re-frame/reg-sub
  ::selected-type
  :<- [path-to-db]
  (fn [db]
    (get db :selected-type :visible)))

(defn- with-library-type
  [{:keys [status owner-id] :as activity} user-id]
  (let [is-global? (= "visible" status)
        is-owner? (= owner-id user-id)]
    (cond
      (and is-global? is-owner?) (assoc activity :library-type "my")
      is-global? (assoc activity :library-type "global")
      :else activity)))

(re-frame/reg-sub
  ::books
  :<- [path-to-db]
  :<- [::selected-type]
  :<- [::show-my-global?]
  :<- [::search-string]
  :<- [::current-language]
  (fn [[db selected-type show-my-global search-string current-language]]
    (let [current-user-id (get-in db [:current-user :id])
          books (if (= selected-type :visible)
                  (:visible-books db)
                  (:my-books db))]
      (cond->> books
               :always (map #(with-library-type % current-user-id))
               :always (filter (fn [{:keys [lang]}]
                                 (= lang current-language)))
               (not show-my-global) (remove #(and
                                               (= (:status %) "visible")
                                               (= (:owner-id %) current-user-id)))
               (-> search-string empty? not) (filter (fn [{:keys [name]}]
                                                       (clojure.string/includes?
                                                         (clojure.string/lower-case name)
                                                         (clojure.string/lower-case search-string))))
               :always (sort-by :name)))))

(re-frame/reg-sub
  ::books-counter
  :<- [path-to-db]
  (fn [db]
    {:my      (-> db :my-books count)
     :visible (-> db :visible-books count)}))
;;
(def default-language "english")

(re-frame/reg-sub
  ::current-language
  :<- [path-to-db]
  #(get % :current-language))

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db         (-> db
                     (assoc :visible-books-loading true)
                     (assoc :my-books-loading true)
                     (assoc :current-language default-language))
     :dispatch-n [[::warehouse/load-books
                   {:lang default-language}
                   {:on-success [::load-visible-books-success]}]
                  [::warehouse/load-my-books
                   {:lang default-language}
                   {:on-success [::load-my-books-success]}]
                  [::warehouse/load-current-user
                   {:on-success [::load-account-success]}]]}))

(re-frame/reg-event-fx
  ::load-account-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ data]]
    {:db (-> db
             (assoc :current-user data))}))

(re-frame/reg-event-fx
  ::load-visible-books-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ data]]
    {:db (-> db
             (assoc :visible-books-loading false)
             (assoc :visible-books data))}))

(re-frame/reg-event-fx
  ::load-my-books-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ data]]
    {:db (-> db
             (assoc :my-books-loading false)
             (assoc :my-books data))}))

(re-frame/reg-event-fx
  ::open-book
  [(i/path path-to-db)]
  (fn [{:keys [_]} [_ book-id]]
    {:dispatch [::routes/redirect :book-edit :activity-id book-id]}))

(re-frame/reg-event-fx
  ::edit-book
  [(i/path path-to-db)]
  (fn [{:keys [_]} [_ book-id]]
    {:dispatch [::routes/redirect :book-edit :activity-id book-id]}))

(re-frame/reg-event-fx
  ::select-language
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ language]]
    {:db         (-> db
                     (assoc :visible-books-loading true)
                     (assoc :my-books-loading true)
                     (assoc :current-language language))
     :dispatch-n [[::warehouse/load-books
                   {:lang language}
                   {:on-success [::load-visible-books-success]}]
                  [::warehouse/load-my-books
                   {:lang language}
                   {:on-success [::load-my-books-success]}]]}))

(re-frame/reg-event-fx
  ::select-type
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ type]]
    {:db (assoc db :selected-type type)}))

(re-frame/reg-event-fx
  ::set-show-global
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ value]]
    {:db (assoc db :show-my-global? value)}))

;; search string

(def search-string-key :search-string)

(defn- get-search-string
  [db]
  (get db search-string-key ""))

(defn- set-search-string
  [db value]
  (assoc db search-string-key value))

(re-frame/reg-sub
  ::search-string
  :<- [path-to-db]
  get-search-string)

(re-frame/reg-event-fx
  ::set-search-string
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ value]]
    {:db (set-search-string db value)}))

