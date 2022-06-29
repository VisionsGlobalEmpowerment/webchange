(ns webchange.admin.pages.class-profile.course-assign.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.admin.pages.class-profile.state :as parent-state]
    [webchange.state.warehouse :as warehouse]))

(def path-to-db :page/class-profile--course-assign)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

;; selected course

(def selected-course-id-key :selected-course-id)

(defn- get-selected-course-id
  [db]
  (get db selected-course-id-key))

(re-frame/reg-sub
  ::selected-course-id
  :<- [path-to-db]
  get-selected-course-id)

(re-frame/reg-event-fx
  ::set-selected-course-id
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ course-id]]
    {:db (assoc db selected-course-id-key course-id)}))

;; courses

(def courses-key :courses)

(defn- set-courses
  [db value]
  (assoc db courses-key value))

(re-frame/reg-sub
  ::courses
  :<- [path-to-db]
  #(get % courses-key []))

(re-frame/reg-sub
  ::courses-list
  :<- [::courses]
  :<- [::selected-course-id]
  (fn [[courses selected-course-id]]
    (map (fn [{:keys [id name]}]
           {:id        id
            :name      name
            :selected? (= id selected-course-id)})
         courses)))

;; loading

(def loading-key :loading?)

(defn- set-loading
  [db value]
  (assoc db loading-key value))

(re-frame/reg-sub
  ::loading?
  :<- [path-to-db]
  #(get % loading-key true))

;; apply enable

(re-frame/reg-sub
  ::apply-enable?
  :<- [::selected-course-id]
  some?)

;; class id

(def class-id-key :class-id)

(defn- get-class-id
  [db]
  (get db class-id-key))

(defn- set-class-id
  [db value]
  (assoc db class-id-key value))

;; --

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [class-id school-id]}]]
    {:db       (-> db
                   (set-class-id class-id)
                   (set-loading true))
     :dispatch [::warehouse/load-school-courses {:school-id school-id}
                {:on-success [::load-school-courses-success]}]}))

(re-frame/reg-event-fx
  ::load-school-courses-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ courses]]
    {:db (-> db
             (set-loading false)
             (set-courses courses))}))

;; change course

(def saving-key :saving?)

(defn- set-saving
  [db value]
  (assoc db saving-key value))

(re-frame/reg-sub
  ::saving?
  :<- [path-to-db]
  #(get % saving-key))

(re-frame/reg-event-fx
  ::change-class-course
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [class-id (get-class-id db)
          course-id (get-selected-course-id db)]
      {:db       (set-saving db true)
       :dispatch [::warehouse/save-class
                  {:class-id class-id
                   :data     {:course-id course-id}}
                  {:on-success [::change-class-course-success]
                   :on-failure [::change-class-course-failure]}]})))

(re-frame/reg-event-fx
  ::change-class-course-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db         (set-saving db false)
     :dispatch-n [[::parent-state/load-class]
                  [::parent-state/open-class-form]]}))

(re-frame/reg-event-fx
  ::change-class-course-failure
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (set-saving db false)}))

(re-frame/reg-event-fx
  ::close
  (fn [{:keys [_]} [_]]
    {:dispatch [::parent-state/open-class-form]}))

(re-frame/reg-event-fx
  ::reset
  (fn [{:keys [db]} [_]]
    {:db (dissoc db path-to-db)}))
