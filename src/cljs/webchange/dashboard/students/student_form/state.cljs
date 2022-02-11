(ns webchange.dashboard.students.student-form.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.common.core :as common]
    [webchange.common.validation.state :as validation-state]
    [webchange.dashboard.students.state :as parent-state]
    [webchange.state.warehouse :as warehouse]
    [webchange.validation.specs.student :as student-specs]
    [webchange.validation.validate :refer [validate]]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:student-form])
       (parent-state/path-to-db)))

(def class-key :class-id)
(def gender-key :gender)
(def first-name-key :first-name)
(def last-name-key :last-name)
(def birth-date-key :date-of-birth)
(def access-code-key :access-code)

(def validation-state-id :dashboard-student-form)

(re-frame/reg-event-fx
  ::open-edit-student-window
  (fn [{:keys [_]} [_ {:keys [student-id]} handlers]]
    {:dispatch-n [[::load-student student-id]
                  [::load-classes]
                  [::set-form-state {:action       "edit"
                                     :student-id   student-id
                                     :handlers     handlers
                                     :window-open? true
                                     :window-title "Edit student"}]]}))

(re-frame/reg-event-fx
  ::open-add-student-window
  (fn [{:keys [_]} [_ {:keys [class-id]} handlers]]
    {:dispatch-n [[::load-classes]
                  [::reset-form-data {class-key class-id}]
                  [::generate-access-code]
                  [::set-form-state {:action       "add"
                                     :handlers     handlers
                                     :window-open? true
                                     :window-title "Add student"}]]}))

;; Load student

(re-frame/reg-event-fx
  ::load-student
  (fn [{:keys [_]} [_ student-id]]
    {:dispatch [::warehouse/load-student {:student-id student-id} {:on-success [::load-student-success]}]}))

(re-frame/reg-event-fx
  ::load-student-success
  (fn [{:keys [_]} [_ {:keys [student]}]]
    (let [{:keys [user]} student
          student-data (merge
                         (select-keys user [first-name-key last-name-key])
                         (select-keys student [gender-key birth-date-key access-code-key])
                         {:class-id (get-in student [:class :id])})]
      {:dispatch [::set-form-data student-data]})))

;; Form state

(def form-state-path (path-to-db [:form-state]))

(re-frame/reg-event-fx
  ::set-form-state
  (fn [{:keys [db]} [_ data]]
    {:db (assoc-in db form-state-path data)}))

(re-frame/reg-event-fx
  ::reset-form-state
  (fn [{:keys [_]} [_]]
    {:dispatch [::set-form-state {:window-open? false}]}))

(defn- get-form-state
  [db]
  (get-in db form-state-path))

(re-frame/reg-sub
  ::form-state
  get-form-state)

(re-frame/reg-sub
  ::window-open?
  (fn []
    [(re-frame/subscribe [::form-state])])
  (fn [[form-state]]
    (get form-state :window-open? false)))

(re-frame/reg-sub
  ::window-title
  (fn []
    [(re-frame/subscribe [::form-state])])
  (fn [[form-state]]
    (get form-state :window-title "Student form")))

(defn get-student-id
  [db]
  (-> (get-form-state db)
      (get :student-id)))

(defn get-action
  [db]
  (-> (get-form-state db)
      (get :action)))

(defn get-handlers
  [db]
  (-> (get-form-state db)
      (get :handlers)))

;; Form data

(def form-data-path (path-to-db [:form-data]))

(re-frame/reg-event-fx
  ::set-form-data
  (fn [{:keys [db]} [_ data]]
    {:db (assoc-in db form-data-path data)}))

(re-frame/reg-event-fx
  ::update-form-data
  (fn [{:keys [db]} [_ field-name value]]
    {:db (update-in db form-data-path assoc field-name value)}))

(re-frame/reg-event-fx
  ::reset-form-data
  (fn [{:keys [_]} [_ default-data]]
    {:dispatch [::set-form-data (or default-data {})]}))

(defn get-form-data
  [db]
  (get-in db form-data-path))

(re-frame/reg-sub
  ::form-data
  get-form-data)

;; Class

(re-frame/reg-sub
  ::class
  (fn []
    [(re-frame/subscribe [::form-data])])
  (fn [[form-data]]
    (get form-data class-key)))

(re-frame/reg-event-fx
  ::set-class
  (fn [{:keys [_]} [_ value]]
    {:dispatch [::update-form-data class-key value]}))

(re-frame/reg-sub
  ::class-error
  (fn []
    [(re-frame/subscribe [::validation-state/error validation-state-id class-key])])
  (fn [[error]]
    error))

;; Classes options

(def classes-path (path-to-db [:classes]))

(re-frame/reg-sub
  ::classes
  (fn [db]
    (get-in db classes-path)))

(re-frame/reg-event-fx
  ::set-classes
  (fn [{:keys [db]} [_ data]]
    {:db (assoc-in db classes-path data)}))

(re-frame/reg-event-fx
  ::load-classes
  (fn [{:keys [_]} [_]]
    {:dispatch [::warehouse/load-classes {:on-success [::load-classes-success]}]}))

(re-frame/reg-event-fx
  ::load-classes-success
  (fn [{:keys [_]} [_ {:keys [classes]}]]
    {:dispatch [::set-classes classes]}))

(re-frame/reg-sub
  ::class-options
  (fn []
    [(re-frame/subscribe [::classes])])
  (fn [[classes]]
    (map (fn [{:keys [id name]}]
           {:text  name
            :value id})
         classes)))

;; Gender

(re-frame/reg-sub
  ::gender
  (fn []
    [(re-frame/subscribe [::form-data])])
  (fn [[form-data]]
    (get form-data gender-key)))

(re-frame/reg-event-fx
  ::set-gender
  (fn [{:keys [_]} [_ value]]
    {:dispatch [::update-form-data gender-key value]}))


(re-frame/reg-sub
  ::gender-options
  (fn []
    [{:value 1 :text "Male"}
     {:value 2 :text "Female"}]))

;; First name

(re-frame/reg-sub
  ::first-name
  (fn []
    [(re-frame/subscribe [::form-data])])
  (fn [[form-data]]
    (get form-data first-name-key)))

(re-frame/reg-event-fx
  ::set-first-name
  (fn [{:keys [_]} [_ value]]
    {:dispatch [::update-form-data first-name-key value]}))

(re-frame/reg-sub
  ::first-name-error
  (fn []
    [(re-frame/subscribe [::validation-state/error validation-state-id first-name-key])])
  (fn [[error]]
    error))

;; Last name

(re-frame/reg-sub
  ::last-name
  (fn []
    [(re-frame/subscribe [::form-data])])
  (fn [[form-data]]
    (get form-data last-name-key)))

(re-frame/reg-event-fx
  ::set-last-name
  (fn [{:keys [_]} [_ value]]
    {:dispatch [::update-form-data last-name-key value]}))

;; Birth date

(re-frame/reg-sub
  ::birth-date
  (fn []
    [(re-frame/subscribe [::form-data])])
  (fn [[form-data]]
    (-> (get form-data birth-date-key)
        (common/format-date-string)
        (or ""))))

(re-frame/reg-event-fx
  ::set-birth-date
  (fn [{:keys [_]} [_ value]]
    {:dispatch [::update-form-data birth-date-key value]}))

;; Access code

(re-frame/reg-sub
  ::access-code
  (fn []
    [(re-frame/subscribe [::form-data])])
  (fn [[form-data]]
    (get form-data access-code-key "")))

(re-frame/reg-event-fx
  ::set-access-code
  (fn [{:keys [_]} [_ value]]
    {:dispatch [::update-form-data access-code-key value]}))

(re-frame/reg-event-fx
  ::generate-access-code
  (fn [{:keys [_]} [_]]
    {:dispatch [::warehouse/generate-access-code {:on-success [::generate-access-code-success]}]}))

(re-frame/reg-event-fx
  ::generate-access-code-success
  (fn [{:keys [_]} [_ {:keys [access-code]}]]
    {:dispatch [::set-access-code access-code]}))

(re-frame/reg-sub
  ::access-code-error
  (fn []
    [(re-frame/subscribe [::validation-state/error validation-state-id access-code-key])])
  (fn [[error]]
    error))

;; Save

(re-frame/reg-event-fx
  ::save
  (fn [{:keys [db]} [_]]
    (let [action (get-action db)
          form-data (get-form-data db)
          validation-errors (validate ::student-specs/student form-data)]
      (if (nil? validation-errors)
        {:dispatch-n [[::validation-state/reset-errors validation-state-id]
                      [(case action
                         "add" ::add-student
                         "edit" ::edit-student)
                       {:on-success [::save-success]}]]}
        {:dispatch [::validation-state/set-errors validation-state-id validation-errors]}))))

(re-frame/reg-event-fx
  ::save-success
  (fn [{:keys [db]} [_]]
    (let [{:keys [on-success]} (get-handlers db)]           ;; ToDo: remove handlers
      {:dispatch-n (cond-> [[::reset]]
                           (some? on-success) (conj on-success))})))

(re-frame/reg-event-fx
  ::add-student
  (fn [{:keys [db]} [_ handlers]]
    (let [form-data (get-form-data db)
          class-id (get form-data class-key)]
      {:dispatch [::parent-state/add-student
                  {:data     form-data
                   :class-id class-id}
                  handlers]})))

(re-frame/reg-event-fx
  ::edit-student
  (fn [{:keys [db]} [_ handlers]]
    (let [student-id (get-student-id db)
          form-data (get-form-data db)
          class-id (get form-data class-key)]
      {:dispatch [::parent-state/edit-student
                  {:id       student-id
                   :data     form-data
                   :class-id class-id}
                  handlers]})))

(re-frame/reg-event-fx
  ::reset
  (fn [{:keys [_]} [_]]
    {:dispatch-n [[::reset-form-data]
                  [::reset-form-state]]}))
