(ns webchange.admin.pages.school-courses.state
  (:require
    [goog.string :as gstring]
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.admin.routes :as routes]
    [webchange.admin.widgets.confirm.state :as confirm]
    [webchange.state.warehouse :as warehouse]
    [webchange.validation.specs.course-spec :as course-spec]
    [webchange.validation.validate :refer [validate]]))

(def path-to-db :school-courses)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

(comment
  @(re-frame/subscribe [path-to-db])
  @(re-frame/subscribe [::course-options]))

(defn- get-school-data
  [db]
  (get db :school-data))

(defn- set-school-data
  [db school-data]
  (assoc db :school-data school-data))

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [school-id]}]]
    {:db         (assoc db :school-id school-id)
     :dispatch-n [[::warehouse/load-school {:school-id school-id} {:on-success [::load-school-success]}]
                  [::warehouse/load-available-courses {:on-success [::load-available-courses-success]}]
                  [::load-school-courses]]}))

(re-frame/reg-event-fx
  ::load-school-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [school]}]]
    {:db (set-school-data db school)}))

(re-frame/reg-event-fx
  ::load-school-courses
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [on-success on-failure]}]]
    (let [school-id (:school-id db)]
      {:dispatch [::warehouse/load-school-courses
                  {:school-id school-id}
                  {:on-success [::load-school-courses-success on-success]
                   :on-failure [::load-school-courses-failure on-failure]}]})))

(re-frame/reg-event-fx
  ::load-school-courses-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ on-success courses]]
    (cond-> {:db (assoc db :courses courses)}
            (some? on-success) (assoc :dispatch on-success))))

(re-frame/reg-event-fx
  ::load-school-courses-failure
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ on-failure]]
    (cond-> {:db (assoc db :courses [])}
            (some? on-failure) (assoc :dispatch on-failure))))

(re-frame/reg-event-fx
  ::load-available-courses-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ courses]]
    {:db (assoc db :available-courses courses)}))

(re-frame/reg-sub
  ::school-data
  :<- [path-to-db]
  get-school-data)

(re-frame/reg-sub
  ::school-name
  :<- [::school-data]
  (fn [data]
    (get data :name)))

(defn- get-courses
  [db]
  (get db :courses []))

(defn- get-course
  [db course-id]
  (->> (get-courses db)
       (some (fn [{:keys [id] :as course}]
               (and (= id course-id) course)))))

(re-frame/reg-sub
  ::courses
  :<- [path-to-db]
  #(get-courses %))

(re-frame/reg-sub
  ::courses-number
  :<- [::courses]
  (fn [courses]
    (count courses)))

;; available-courses

(def selected-courses-key :selected-courses)

(defn- select-course
  [db course-id]
  (assoc-in db [selected-courses-key course-id] true))

(defn- deselect-course
  [db course-id]
  (update db selected-courses-key dissoc course-id))

(defn- course-selected?
  [db course-id]
  (get-in db [selected-courses-key course-id] false))

(defn- get-selected-courses
  [db]
  (-> (get db selected-courses-key)
      (keys)))

(defn- reset-selected-courses
  [db]
  (dissoc db selected-courses-key))

(re-frame/reg-event-fx
  ::select-available-course
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ course-id selected?]]
    {:db (if selected?
           (select-course db course-id)
           (deselect-course db course-id))}))

(re-frame/reg-sub
  ::selected-courses-number
  :<- [path-to-db]
  #(-> (get-selected-courses %)
       (count)))

(re-frame/reg-sub
  ::available-course-options
  :<- [path-to-db]
  (fn [db]
    (let [assigned-courses (get-courses db)]
      (->> db
           :available-courses
           (map (fn [{:keys [id] :as data}]
                  (assoc data :selected? (course-selected? db id))))
           (filter (fn [{:keys [id]}]
                     (->> assigned-courses
                          (some (fn [assigned-course]
                                  (= id (:id assigned-course))))
                          (not))))))))

;;

(re-frame/reg-sub
  ::errors
  :<- [path-to-db]
  (fn [data]
    (get data :errors)))

(re-frame/reg-event-fx
  ::show-assign-course-list
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (assoc db :assign-course-list-open true)}))

(re-frame/reg-event-fx
  ::close-assign-course-list
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (-> db
             (assoc :assign-course-list-open false)
             (reset-selected-courses))}))

(re-frame/reg-sub
  ::show-assign-course-list?
  :<- [path-to-db]
  (fn [data]
    (get data :assign-course-list-open false)))

(def assign-loading-key :assign-loading?)

(defn- set-assign-loading
  [db value]
  (assoc db assign-loading-key value))

(re-frame/reg-sub
  ::assign-loading?
  :<- [path-to-db]
  #(get % assign-loading-key false))

(re-frame/reg-event-fx
  ::assign-courses
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [school-id (:school-id db)
          selected-courses (get-selected-courses db)]
      {:db       (-> (assoc db :errors nil)
                     (set-assign-loading true))
       :dispatch [::warehouse/assign-school-courses {:school-id  school-id
                                                     :courses-id selected-courses}
                  {:on-success [::assign-course-success]}]})))

(re-frame/reg-event-fx
  ::assign-course-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db         (-> (assoc db :assign-course-modal-open false)
                     (set-assign-loading false))
     :dispatch-n [[::close-assign-course-list]
                  [::load-school-courses]]}))

(re-frame/reg-event-fx
  ::remove-course
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ course-id]]
    (let [course (get-course db course-id)
          school (get-school-data db)]
      {:dispatch [::confirm/show-confirm-window {:message    (gstring/format "Delete \"%s\" from %s?" (:name course) (:name school))
                                                 :on-confirm [::remove-course-confirmed {:course-id course-id
                                                                                         :school-id (:id school)}]}]})))

(def course-removing-key :course-removing?)

(defn- get-course-removing
  [db course-id]
  (get-in db [course-removing-key course-id] false))

(defn- set-course-removing
  [db course-id value]
  (assoc-in db [course-removing-key course-id] value))

(re-frame/reg-sub
  ::course-removing?
  :<- [path-to-db]
  (fn [db [_ course-id]]
    (get-course-removing db course-id)))

(re-frame/reg-event-fx
  ::remove-course-confirmed
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [course-id school-id]}]]
    {:db       (set-course-removing db course-id true)
     :dispatch [::warehouse/unassign-school-course {:school-id school-id
                                                    :course-id course-id}
                {:on-success [::remove-course-success course-id]
                 :on-failure [::remove-course-failure course-id]}]}))

(re-frame/reg-event-fx
  ::remove-course-success
  [(i/path path-to-db)]
  (fn [{:keys [_]} [_ course-id]]
    {:dispatch [::load-school-courses {:on-success [::remove-course-updates course-id]}]}))

(re-frame/reg-event-fx
  ::remove-course-updates
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ course-id]]
    {:db (set-course-removing db course-id false)}))

(re-frame/reg-event-fx
  ::remove-course-failure
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ course-id]]
    {:db (set-course-removing db course-id false)}))

(re-frame/reg-event-fx
  ::open-school-profile
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [school-id (:id (get-school-data db))]
      {:dispatch [::routes/redirect :school-profile :school-id school-id]})))
