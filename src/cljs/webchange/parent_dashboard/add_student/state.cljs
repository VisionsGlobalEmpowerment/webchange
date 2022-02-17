(ns webchange.parent-dashboard.add-student.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.events :as events]
    [webchange.parent-dashboard.state :as parent-state]
    [webchange.state.warehouse :as warehouse]
    [webchange.validation.specs.parent-student :as parent-student-specs]
    [webchange.validation.validate :refer [validate]]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:add-student-form])
       (parent-state/path-to-db)))

(re-frame/reg-event-fx
  ::init
  (fn [{:keys [_]} [_]]
    {:dispatch-n [[::reset-form]
                  [::reset-validation-errors]]}))

;; Form data

(def form-data-path (path-to-db [:form-data]))

(defn- get-form-data
  [db]
  (get-in db form-data-path {}))

(re-frame/reg-sub
  ::form-data
  get-form-data)

(re-frame/reg-event-fx
  ::set-form-field
  (fn [{:keys [db]} [_ field value]]
    {:db (update-in db form-data-path assoc field value)}))

(re-frame/reg-event-fx
  ::reset-form
  (fn [{:keys [db]} [_]]
    {:db (assoc-in db form-data-path {})}))

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

(re-frame/reg-sub
  ::name-validation-error
  (fn []
    (re-frame/subscribe [::validation-error name-key]))
  (fn [error]
    error))

;; Birth date

(def birth-date-key :date-of-birth)

(re-frame/reg-sub
  ::current-birth-date
  (fn []
    (re-frame/subscribe [::form-data]))
  (fn [form-data]
    (get form-data birth-date-key)))

(re-frame/reg-event-fx
  ::set-birth-date
  (fn [{:keys [_]} [_ value]]
    {:dispatch [::set-form-field birth-date-key (or value "")]}))

(re-frame/reg-sub
  ::birth-date-validation-error
  (fn []
    (re-frame/subscribe [::validation-error birth-date-key]))
  (fn [error]
    error))

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
    [{:text "Android Tablet"
      :value "android-tablet"}
     {:text "iPad"
      :value "ipad"}
     {:text "Android Mobile Phone"
      :value "android-mobile-phone"}
     {:text "iPhone"
      :value "iphone"}
     {:text "Desktop Computer: PC"
      :value "desktop-computer-pc"}
     {:text "Desktop Computer: Mac"
      :value "desktop-computer-mac"}
     {:text "Laptop Computer: PC"
      :value "laptop-computer-pc"}
     {:text "Laptop Computer: Mac"
      :value "laptop-computer-mac"}]))

(re-frame/reg-sub
  ::device-validation-error
  (fn []
    (re-frame/subscribe [::validation-error device-key]))
  (fn [error]
    error))

;; Validation

(def validation-data-path (path-to-db [:validation-data]))

(re-frame/reg-event-fx
  ::set-validation-errors
  (fn [{:keys [db]} [_ errors]]
    {:db (assoc-in db validation-data-path errors)}))

(re-frame/reg-event-fx
  ::reset-validation-errors
  (fn [{:keys [_]} [_]]
    {:dispatch [::set-validation-errors nil]}))

(re-frame/reg-sub
  ::validation-errors
  (fn [db]
    (get-in db validation-data-path)))

(re-frame/reg-sub
  ::validation-error
  (fn []
    (re-frame/subscribe [::validation-errors]))
  (fn [errors [_ field]]
    (get errors field)))

;; Save

(defn- data-valid?
  [validation-data]
  (empty? validation-data))

(re-frame/reg-event-fx
  ::save
  (fn [{:keys [db]} [_]]
    (let [form-data (get-form-data db)
          validation-errors (validate ::parent-student-specs/parent-student form-data)]
      (if (nil? validation-errors)
        {:dispatch-n [[::reset-validation-errors]
                      [::set-submit-status {:loading? true}]
                      [::warehouse/add-parent-student
                       {:data form-data}
                       {:on-success [::save-success]
                        :on-failure [::save-failure]}]]}
        {:dispatch [::set-validation-errors validation-errors]}))))

(re-frame/reg-event-fx
  ::save-success
  (fn [{:keys [_]} [_]]
    {:dispatch-n [[::set-submit-status {:loading? false}]
                  [::open-dashboard]]}))

(re-frame/reg-event-fx
  ::save-failure
  (fn [{:keys [_]} [_]]
    {:dispatch [::set-submit-status {:loading? false}]}))

(def submit-status-path (path-to-db [:submit-status]))

(re-frame/reg-sub
  ::submit-status
  (fn [db]
    (get-in db submit-status-path {:loading? false})))

(re-frame/reg-event-fx
  ::set-submit-status
  (fn [{:keys [db]} [_ data]]
    {:db (assoc-in db submit-status-path data)}))

(re-frame/reg-sub
  ::loading?
  (fn []
    (re-frame/subscribe [::submit-status]))
  (fn [status]
    (get status :loading? false)))

(re-frame/reg-event-fx
  ::open-dashboard
  (fn [{:keys [_]} [_]]
    {:dispatch [::events/redirect :parent-dashboard]}))
