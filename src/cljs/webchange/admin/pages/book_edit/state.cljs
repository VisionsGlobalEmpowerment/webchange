(ns webchange.admin.pages.book-edit.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.admin.routes :as routes]
    [webchange.state.warehouse :as warehouse]))

(def path-to-db :page/book-edit)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

;; book Loading

(def book-loading-key :book-loading?)

(defn- set-book-loading
  [db value]
  (assoc db book-loading-key value))

(re-frame/reg-sub
  ::book-loading?
  :<- [path-to-db]
  #(get % book-loading-key false))

;; book Data

(def book-key :book)

(defn- get-book
  [db]
  (get db book-key))

(defn- set-book
  [db value]
  (assoc db book-key value))

(re-frame/reg-sub
  ::book
  :<- [path-to-db]
  #(get-book %))

;; Form editable

(def form-editable-key :form-editable?)

(re-frame/reg-sub
  ::form-editable?
  :<- [path-to-db]
  #(get % form-editable-key false))

(re-frame/reg-event-fx
  ::set-form-editable
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ value]]
    {:db (assoc db form-editable-key value)}))

(re-frame/reg-event-fx
  ::toggle-form-editable
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (update db form-editable-key not)}))

;;

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [book-id]}]]
    {:db       (set-book-loading db true)
     :dispatch [::warehouse/load-activity
                {:activity-id book-id}
                {:on-success [::load-book-success]
                 :on-failure [::load-book-failure]}]}))

(re-frame/reg-event-fx
  ::load-book-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ data]]
    {:db (-> db
             (set-book-loading false)
             (set-book data))}))

(re-frame/reg-event-fx
  ::load-book-failure
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (-> db (set-book-loading false))}))

;; Remove

(def removing-key :removing?)

(defn- set-removing
  [db value]
  (assoc db removing-key value))

(re-frame/reg-sub
  ::removing?
  :<- [path-to-db]
  #(get % removing-key false))

(re-frame/reg-event-fx
  ::remove
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [{:keys [id]} (get-book db)]
      {:db       (-> db (set-removing true))
       :dispatch [::warehouse/archive-activity
                  {:activity-id id}
                  {:on-success [::remove-success]
                   :on-failure [::remove-failure]}]})))

(re-frame/reg-event-fx
  ::remove-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db       (-> db (set-removing false))
     :dispatch [::routes/redirect :books]}))

(re-frame/reg-event-fx
  ::remove-failure
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (-> db (set-removing false))}))

;;

(re-frame/reg-event-fx
  ::edit
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [{:keys [id]} (get-book db)]
      {:dispatch [::routes/redirect :lesson-builder :activity-id id]})))

(re-frame/reg-event-fx
  ::play
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [{:keys [id]} (get-book db)
          href (str "/s/" id)] ;; not working for main module [::routes/redirect :activity-sandbox :scene-id id]
      (js/window.open href "_blank"))))

(re-frame/reg-event-fx
  ::duplicate
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [{:keys [id name lang]} (get-book db)]
      {:db       (-> db (set-book-loading true))
       :dispatch [::warehouse/duplicate-activity
                  {:activity-id id
                   :data {:name name
                          :lang lang}}
                  {:on-success [::duplicate-success]
                   :on-failure [::duplicate-failure]}]})))

(re-frame/reg-event-fx
  ::duplicate-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [id]}]]
    {:dispatch-n [[::init {:book-id id}]
                  [::routes/redirect :book-edit :book-id id]]}))

(re-frame/reg-event-fx
  ::duplicate-failure
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (-> db (set-book-loading false))}))

(re-frame/reg-event-fx
  ::open-books-page
  [(i/path path-to-db)]
  (fn [{:keys [_]} [_]]
    {:dispatch [::routes/redirect :books]}))

(re-frame/reg-event-fx
  ::set-locked
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ value]]
    {:db (assoc-in db [:book :metadata :locked] value)}))
