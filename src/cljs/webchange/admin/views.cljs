(ns webchange.admin.views
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [reagent.core :as r]
    [webchange.admin.pages.index :refer [pages]]
    [webchange.admin.routes :as routes]
    [webchange.admin.state :as state]
    [webchange.admin.widgets.layout.views :refer [layout]]
    [webchange.state.warehouse :as warehouse]))

(def path-to-db :admin/core)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

(re-frame/reg-sub
  ::authenticated?
  :<- [path-to-db]
  #(get % :authenticated? false))

(defn- set-authenticated
  [db value]
  (assoc db :authenticated? value))

(re-frame/reg-event-fx
  ::load-current-user
  (fn [{:keys [_]} [_]]
    (print ">>> ::load-current-user")
    {:dispatch [::warehouse/load-current-user {:on-success [::load-current-user-success]
                                               :on-failure [::load-current-user-failure]}]}))

(re-frame/reg-event-fx
  ::load-current-user-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (set-authenticated db true)}))

(re-frame/reg-event-fx
  ::load-current-user-failure
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (set-authenticated db false)
     :dispatch [::routes/redirect :login]}))

(defn index
  []
  (r/create-class
    {:display-name "Admin App Index"
     :component-did-mount
     (fn [this]
       (let [{:keys [route]} (r/props this)]
         (re-frame/dispatch [::load-current-user])
         (routes/init! (:path route))))

     :reagent-render
     (fn []
       (let [authenticated? @(re-frame/subscribe [::authenticated?])
             {:keys [handler props] :as page-params} @(re-frame/subscribe [::state/current-page])
             page-component (get pages handler (:404 pages))]
         (routes/set-title! page-params)
         [:div#tabschool-admin
          (if (= handler :login)
            [page-component props]
            (when authenticated?
              [layout
               [page-component props]]))]))}))
