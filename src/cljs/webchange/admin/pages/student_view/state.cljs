(ns webchange.admin.pages.student-view.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.admin.routes :as routes]))

(def path-to-db :page/student-view)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

;; form

(def form-editable-key :form-editable?)

(defn- set-form-editable
  [db value]
  (assoc db form-editable-key value))

(re-frame/reg-sub
  ::form-editable?
  :<- [path-to-db]
  #(get % form-editable-key false))

(re-frame/reg-event-fx
  ::set-form-editable
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ value]]
    {:db (set-form-editable db value)}))

(re-frame/reg-event-fx
  ::handle-edit-finished
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [{:keys [on-edit-finished]} (:params db)]
      (cond-> {:db (-> db (set-form-editable false))}
              (some? on-edit-finished) (assoc :dispatch on-edit-finished)))))

;;

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [school-id student-id params]}]]
    (let [{:keys [action]} params]
      {:db         (-> db
                       (assoc :school-id school-id)
                       (assoc :student-id student-id)
                       (assoc :params params))
       :dispatch-n (cond-> []
                           (= action "edit") (conj [::set-form-editable true]))})))

(re-frame/reg-event-fx
  ::reset
  (fn [{:keys [db]} [_]]
    {:db (dissoc db path-to-db)}))

(re-frame/reg-event-fx
  ::handle-edit-click
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (-> db (set-form-editable true))}))

(re-frame/reg-event-fx
  ::handle-cancel-click
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (-> db (set-form-editable false))}))

(re-frame/reg-event-fx
  ::handle-close-click
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [{:keys [school-id]} db]
      {:dispatch [::routes/redirect :students :school-id school-id]})))
