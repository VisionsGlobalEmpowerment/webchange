(ns webchange.admin.widgets.add-class-students.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.state.warehouse :as warehouse]
    [webchange.utils.list :refer [in-list? toggle-item]]))

(def path-to-db :widget/add-class-students)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

;; Selected Students

(def selected-students-key :selected-students)

(defn- reset-selected-students
  [db]
  (assoc db selected-students-key []))

(defn- toggle-selected-student
  [db student-id]
  (update db selected-students-key toggle-item student-id))

(re-frame/reg-event-fx
  ::select-student
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ student-id]]
    {:db (toggle-selected-student db student-id)}))

(re-frame/reg-sub
  ::selected-students
  :<- [path-to-db]
  #(get % selected-students-key []))

;; Available Students

(def available-students-key :unassigned-students)

(defn- reset-available-students
  [db]
  (assoc db available-students-key []))

(defn- set-available-students
  [db data]
  (assoc db available-students-key data))

(re-frame/reg-sub
  ::available-students
  :<- [path-to-db]
  #(get % available-students-key []))

;; Students List

(re-frame/reg-sub
  ::students
  (fn []
    [(re-frame/subscribe [::available-students])
     (re-frame/subscribe [::selected-students])])
  (fn [[available-students selected-students]]
    (->> available-students
         (map (fn [{:keys [id user]}]
                {:id        id
                 :avatar    nil
                 :name      (str (:first-name user) " " (:last-name user))
                 :selected? (in-list? selected-students id)}))
         (concat (->> available-students
                      (map (fn [{:keys [id user]}]
                             {:id        id
                              :avatar    nil
                              :name      (str (:first-name user) " " (:last-name user))
                              :selected? (in-list? selected-students id)})))))))

;;

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db       (-> db
                   (reset-available-students)
                   (reset-selected-students))
     :dispatch [::warehouse/load-unassigned-students
                {:on-success [::load-students-success]}]}))

(re-frame/reg-event-fx
  ::load-students-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [students]}]]
    {:db (set-available-students db students)}))

(re-frame/reg-event-fx
  ::reset
  (fn [{:keys [db]} [_]]
    {:db (dissoc db path-to-db)}))
