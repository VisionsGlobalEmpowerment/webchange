(ns webchange.parent-dashboard.add-student.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.events :as events]
    [webchange.parent-dashboard.state :as parent-state]
    [webchange.state.warehouse :as warehouse]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:add-student-form])
       (parent-state/path-to-db)))

;; Form data

(def form-data-path (path-to-db [:form-data]))

(defn- get-form-data
  [db]
  (get-in db form-data-path))

(re-frame/reg-sub
  ::form-data
  get-form-data)

(re-frame/reg-event-fx
  ::set-form-field
  (fn [{:keys [db]} [_ field value]]
    {:db (update-in db form-data-path assoc field value)}))

;; Name

(def name-key :name)

(re-frame/reg-sub
  ::current-name
  (fn []
    (re-frame/subscribe [::form-data]))
  (fn [form-data]
    (get form-data name-key)))

(re-frame/reg-event-fx
  ::set-name
  (fn [{:keys [_]} [_ value]]
    {:dispatch [::set-form-field name-key value]}))

;; Age

(def age-key :age)

(re-frame/reg-sub
  ::current-age
  (fn []
    (re-frame/subscribe [::form-data]))
  (fn [form-data]
    (get form-data age-key)))

(re-frame/reg-event-fx
  ::set-age
  (fn [{:keys [_]} [_ value]]
    {:dispatch [::set-form-field age-key value]}))

(re-frame/reg-sub
  ::age-options
  (fn [_]
    (->> (range 1 13)
         (map (fn [age]
                {:text  age
                 :value age})))))

;; Device

(def device-key :device)

(re-frame/reg-sub
  ::current-device
  (fn []
    (re-frame/subscribe [::form-data]))
  (fn [form-data]
    (get form-data device-key)))

(re-frame/reg-event-fx
  ::set-device
  (fn [{:keys [_]} [_ value]]
    {:dispatch [::set-form-field device-key value]}))

(re-frame/reg-sub
  ::device-options
  (fn [_]
    [{:text  "Android Tablet"
      :value "android-tablet"}
     {:text  "iOS Tablet"
      :value "ios-tablet"}
     {:text  "PC"
      :value "web"}]))

;; Save

(defn- data-valid?
  [{:keys [name]}]
  (and (-> name string?)
       (-> name empty? not)))

(re-frame/reg-event-fx
  ::save
  (fn [{:keys [db]} [_]]
    (let [data (get-form-data db)]
      (if (data-valid? data)
        {:dispatch [::warehouse/add-parent-student
                {:data data}
                {:on-success [::open-dashboard]}]}
        {}))))

(re-frame/reg-event-fx
  ::open-dashboard
  (fn [{:keys [_]} [_]]
    {:dispatch [::events/redirect :parent-dashboard]}))
