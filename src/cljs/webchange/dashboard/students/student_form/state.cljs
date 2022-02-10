(ns webchange.dashboard.students.student-form.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.common.core :as common]
    [webchange.dashboard.students.state :as parent-state]
    [webchange.state.warehouse :as warehouse]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:student-form])
       (parent-state/path-to-db)))

(re-frame/reg-event-fx
  ::open-edit-student-window
  (fn [{:keys [_]} [_ student-id handlers]]
    {:dispatch-n [[::load-student student-id]
                  [::load-classes]
                  [::set-form-state {:action       "edit"
                                     :student-id   student-id
                                     :handlers     handlers
                                     :window-open? true
                                     :window-title "Edit student"}]]}))

(re-frame/reg-event-fx
  ::open-add-student-window
  (fn [{:keys [_]} [_]]
    {:dispatch-n [[::load-classes]
                  [::set-form-state {:action       "add"
                                     :window-open? true
                                     :window-title "Add student"}]]}))


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
  (fn [{:keys [_]} [_]]
    {:dispatch [::set-form-data {}]}))

(defn get-form-data
  [db]
  (get-in db form-data-path))

(re-frame/reg-sub
  ::form-data
  get-form-data)

;; Class

(def class-key :class-id)

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

(def gender-key :gender)

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

(def first-name-key :first-name)

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

;; Last name

(def last-name-key :last-name)

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

(def birth-date-key :date-of-birth)

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

(def access-code-key :access-code)

(re-frame/reg-sub
  ::access-code
  (fn []
    [(re-frame/subscribe [::form-data])])
  (fn [[form-data]]
    (get form-data access-code-key)))

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
      (print "student-data" student-data)
      {:dispatch [::set-form-data student-data]})))

;; Save

(re-frame/reg-event-fx
  ::save
  (fn [{:keys [db]} [_]]
    (let [action (get-action db)]
      {:dispatch [(case action
                    "add" ::add-student
                    "edit" ::edit-student)
                  {:on-success [::save-success]}]})))

(re-frame/reg-event-fx
  ::save-success
  (fn [{:keys [db]} [_]]
    (let [{:keys [on-success]} (get-handlers db)]           ;; ToDo: remove handlers
      {:dispatch-n (cond-> [[::reset]]
                           (some? on-success) (conj on-success))})))

(re-frame/reg-event-fx
  ::add-student
  (fn [{:keys [db]} [_ handlers]]
    (let [form-data (get-form-data db)]
      {:dispatch [::parent-state/add-student
                  {:data form-data}
                  handlers]})))

(re-frame/reg-event-fx
  ::edit-student
  (fn [{:keys [db]} [_ handlers]]
    (let [student-id (get-student-id db)
          form-data (get-form-data db)]
      {:dispatch [::parent-state/edit-student
                  {:id   student-id
                   :data form-data}
                  handlers]})))

(re-frame/reg-event-fx
  ::reset
  (fn [{:keys [_]} [_]]
    {:dispatch-n [[::reset-form-data]
                  [::reset-form-state]]}))
