(ns webchange.lesson-builder.tools.template-options.select-book.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.lesson-builder.tools.template-options.state :refer [path-to-db] :as template-options-state]
    [webchange.state.warehouse :as warehouse]))

(re-frame/reg-sub
  ::book-options
  :<- [path-to-db]
  (fn [db]
    (get db :book-options)))

(re-frame/reg-sub
  ::selected-book
  :<- [path-to-db]
  (fn [db]
    (get-in db [:form :book-id])))

(re-frame/reg-sub
  ::books-loading?
  :<- [path-to-db]
  (fn [db]
    (get db :books-loading?)))

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (assoc db :books-loading? true)
     :dispatch [::warehouse/load-books {}
                {:on-success [::load-books-success]
                 :on-failure [::load-books-failure]}]}))

(re-frame/reg-event-fx
  ::load-books-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ books]]
    (let [book-options (map (fn [{:keys [id name]}] {:text name
                                                     :value id})
                            books)]
      {:db (-> db
               (assoc :book-options book-options)
               (assoc :books-loading? false))})))

(re-frame/reg-event-fx
  ::load-books-failure
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (-> db (assoc :books-loading? false))}))

(re-frame/reg-event-fx
  ::select-book
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ book-id]]
    {:db (assoc-in db [:form :book-id] book-id)}))
