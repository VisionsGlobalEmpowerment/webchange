(ns webchange.dashboard.schools.events
  (:require
    [re-frame.core :as re-frame]
    [day8.re-frame.http-fx]
    [ajax.core :refer [json-request-format json-response-format]]))

(re-frame/reg-event-fx
  ::load-schools
  (fn [{:keys [db]} _]
    {:db (-> db
             (assoc-in [:loading :schools] true))
     :http-xhrio {:method          :get
                  :uri             (str "/api/schools")
                  :format          (json-request-format)
                  :response-format (json-response-format {:keywords? true})
                  :on-success      [::load-schools-success]
                  :on-failure      [:api-request-error :schools]}}))

(re-frame/reg-event-fx
  ::load-schools-success
  (fn [{:keys [db]} [_ result]]
    {:db (assoc-in db [:dashboard :schools] (:schools result))
     :dispatch-n (list [:complete-request :schools])}))

(re-frame/reg-event-fx
  ::add-school
  (fn [{:keys [db]} [_ data]]
    {:db (assoc-in db [:loading :add-school] true)
     :http-xhrio {:method          :post
                  :uri             (str "/api/schools")
                  :params          data
                  :format          (json-request-format)
                  :response-format (json-response-format {:keywords? true})
                  :on-success      [::add-school-success]
                  :on-failure      [:api-request-error :add-school]}}))


(re-frame/reg-event-fx
  ::add-school-success
  (fn [_ _]
    {:dispatch-n (list [:complete-request :add-school]
                       [::load-schools])}))

(re-frame/reg-event-fx
  ::edit-school
  (fn [{:keys [db]} [_ id data]]
    {:db (assoc-in db [:loading :edit-school] true)
     :http-xhrio {:method          :put
                  :uri             (str "/api/schools/" id)
                  :params          data
                  :format          (json-request-format)
                  :response-format (json-response-format {:keywords? true})
                  :on-success      [::edit-school-success]
                  :on-failure      [:api-request-error :edit-school]}}))


(re-frame/reg-event-fx
  ::edit-school-success
  (fn [_ _]
    {:dispatch-n (list [:complete-request :edit-school]
                       [::load-schools])}))

(re-frame/reg-event-fx
  ::delete-school
  (fn [{:keys [db]} [_ id]]
    {:db (assoc-in db [:loading :delete-school] true)
     :http-xhrio {:method          :delete
                  :uri             (str "/api/schools/" id)
                  :format          (json-request-format)
                  :response-format (json-response-format {:keywords? true})
                  :on-success      [::delete-school-success]
                  :on-failure      [:api-request-error :delete-school]}}))

(re-frame/reg-event-fx
  ::sync-school
  (fn [{:keys [db]} [_ id]]
    {:db (assoc-in db [:loading :sync-school] true)
     :http-xhrio {:method          :post
                  :uri             (str "/api/school/sync/" id)
                  :format          (json-request-format)
                  :response-format (json-response-format {:keywords? true})
                  :on-success      [::sync-school-success]
                  :on-failure      [:api-request-error :sync-school]}}))

(re-frame/reg-event-fx
  ::software-update
  (fn [{:keys [db]} _]
    {:db (assoc-in db [:loading :software-update] true)
     :http-xhrio {:method          :post
                  :uri             (str "/api/software/update")
                  :format          (json-request-format)
                  :response-format (json-response-format {:keywords? true})
                  :on-failure      [:api-request-error :software-update]}}))


(re-frame/reg-event-fx
  ::delete-school-success
  (fn [_ _]
    {:dispatch-n (list [:complete-request :delete-school]
                       [::load-schools])}))

(re-frame/reg-event-fx
  ::sync-school-success
  (fn [_ _]
    {:dispatch-n (list [:complete-request :sync-school]
                       [::close-sync-modal]
                       [::load-schools])}))

(re-frame/reg-event-fx
  ::set-current-school
  (fn [{:keys [db]} [_ school-id]]
    {:db (assoc-in db [:dashboard :current-school-id] school-id)}))

(re-frame/reg-event-fx
  ::show-add-school-form
  (fn [{:keys [db]} _]
    {:db (assoc-in db [:dashboard :current-school-id] nil)
     :dispatch-n (list [::open-school-modal :add])}))

(re-frame/reg-event-fx
  ::show-edit-school-form
  (fn [{:keys [db]} [_ id]]
    {:db (assoc-in db [:dashboard :current-school-id] id)
     :dispatch-n (list [::load-schools]
                       [::open-school-modal :edit])}))

(re-frame/reg-event-fx
  ::close-school-modal
  (fn [{:keys [db]} _]
    {:db (assoc-in db [:dashboard :school-modal-state] nil)}))

(re-frame/reg-event-fx
  ::open-school-modal
  (fn [{:keys [db]} [_ state]]
    {:db (assoc-in db [:dashboard :school-modal-state] state)}))

(re-frame/reg-event-fx
  ::open-delete-modal
  (fn [{:keys [db]} _]
    {:db (assoc-in db [:dashboard :delete-school-modal-state] true)}))

(re-frame/reg-event-fx
  ::close-delete-modal
  (fn [{:keys [db]} _]
    {:db (assoc-in db [:dashboard :delete-school-modal-state] nil)}))

(re-frame/reg-event-fx
  ::close-sync-modal
  (fn [{:keys [db]} _]
    {:db (assoc-in db [:dashboard :sync-school-modal-state] nil)}))

(re-frame/reg-event-fx
  ::open-sync-modal
  (fn [{:keys [db]} _]
    {:db (assoc-in db [:dashboard :sync-school-modal-state] true)}))

(re-frame/reg-event-fx
  ::confirm-delete
  (fn [{:keys [db]} [_ school-id]]
    {:db (assoc-in db [:dashboard :delete-school-modal-state] nil)
     :dispatch [::delete-school school-id]}))

(re-frame/reg-event-fx
  ::confirm-sync
  (fn [{:keys [db]} [_ school-id]]
    {:dispatch [::sync-school school-id]}))

(re-frame/reg-event-fx
  ::show-sync-school-form
  (fn [{:keys [db]} [_ school-id]]
    {:db (assoc-in db [:dashboard :current-school-id] school-id)
     :dispatch-n (list
                   [::open-sync-modal])}))
