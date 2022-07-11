(ns webchange.parent.pages.student-add.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.parent.routes :as routes]
    [webchange.state.warehouse :as warehouse]
    [webchange.utils.devices :refer [devices-options]]
    [webchange.utils.map :refer [map-keys]]))

(def path-to-db :page/student-add)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

;; available-courses

(def courses-loading-key :courses-loading?)

(defn- set-courses-loading
  [db value]
  (assoc db courses-loading-key value))

(re-frame/reg-sub
  ::courses-loading?
  :<- [path-to-db]
  #(get % courses-loading-key true))

(def available-courses-key :available-courses)

(defn- set-available-courses
  [db value]
  (assoc db available-courses-key value))

(re-frame/reg-sub
  ::available-courses
  :<- [path-to-db]
  #(get % available-courses-key []))

(re-frame/reg-sub
  ::course-options
  (fn []
    (re-frame/subscribe [::available-courses]))
  (fn [available-courses]
    (->> available-courses
         (map #(map-keys % {:name :text :slug :value}))
         (concat [{:text  "Choose course"
                   :value ""}]))))

(re-frame/reg-sub
  ::device-options
  (fn [_]
    (concat [{:text  "Choose device"
              :value ""}]
            devices-options)))

;;

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db       (set-courses-loading db true)
     :dispatch [::warehouse/load-parent-courses
                {:on-success [::load-parent-courses-success]}]}))

(re-frame/reg-event-fx
  ::load-parent-courses-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ courses]]
    {:db (-> db
             (set-courses-loading false)
             (set-available-courses courses))}))

(re-frame/reg-event-fx
  ::open-students-list-page
  [(i/path path-to-db)]
  (fn [{:keys []} [_]]
    {:dispatch [::routes/redirect :students]}))

;; save

(def saving-key :saving?)

(defn- set-saving
  [db value]
  (assoc db saving-key value))

(re-frame/reg-sub
  ::saving?
  :<- [path-to-db]
  #(get % saving-key false))

(re-frame/reg-event-fx
  ::save
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ form-data]]
    {:db       (set-saving db true)
     :dispatch [::warehouse/add-parent-student
                {:data form-data}
                {:on-success [::save-success]
                 :on-failure [::save-failure]}]}))

(re-frame/reg-event-fx
  ::save-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db       (set-saving db false)
     :dispatch [::open-students-list-page]}))

(re-frame/reg-event-fx
  ::save-failure
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (set-saving db false)}))