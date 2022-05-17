(ns webchange.admin.widgets.add-class-teachers.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.admin.widgets.state :as widgets]
    [webchange.state.warehouse :as warehouse]
    [webchange.utils.list :refer [in-list? toggle-item]]))

(def path-to-db :widget/add-class-teachers)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

;; Indicators

(def indicators-keys :indicators)

(defn- update-indicators
  [db data-patch]
  (update db indicators-keys merge data-patch))

(re-frame/reg-sub
  ::indicators
  :<- [path-to-db]
  #(get % indicators-keys {}))

(def data-loading-key :data-loading?)

(defn- set-data-loading
  [db value]
  (update-indicators db {data-loading-key value}))

(re-frame/reg-sub
  ::data-loading?
  :<- [::indicators]
  #(get % data-loading-key false))

(def data-saving-key :data-saving?)

(defn- set-data-saving
  [db value]
  (update-indicators db {data-saving-key value}))

(re-frame/reg-sub
  ::data-saving?
  :<- [::indicators]
  #(get % data-saving-key false))

;; Selected Teachers

(def selected-teachers-key :selected-teachers)

(defn- get-selected-teachers
  [db]
  (get db selected-teachers-key []))

(defn- set-selected-teachers
  [db value]
  (assoc db selected-teachers-key value))

(defn- reset-selected-teachers
  [db]
  (set-selected-teachers db []))

(re-frame/reg-event-fx
  ::set-selected-teachers
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ selected-teachers-ids]]
    {:db (set-selected-teachers db selected-teachers-ids)}))

(re-frame/reg-sub
  ::selected-teachers
  :<- [path-to-db]
  get-selected-teachers)

;; School Teachers

(def school-teachers-key :unassigned-teachers)

(defn- reset-school-teachers
  [db]
  (assoc db school-teachers-key []))

(defn- set-school-teachers
  [db data]
  (assoc db school-teachers-key data))

(re-frame/reg-sub
  ::school-teachers
  :<- [path-to-db]
  #(get % school-teachers-key []))

;;

(re-frame/reg-sub
  ::teachers
  (fn []
    [(re-frame/subscribe [::school-teachers])
     (re-frame/subscribe [::selected-teachers])])
  (fn [[school-teachers selected-teachers]]
    (->> school-teachers
         (map (fn [{:keys [id user]}]
                {:id        id
                 :avatar    nil
                 :name      (str (:first-name user) " " (:last-name user))
                 :selected? (in-list? selected-teachers id)})))))

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [school-id]}]]
    {:db       (-> db
                   (reset-school-teachers)
                   (reset-selected-teachers)
                   (set-data-loading true))
     :dispatch [::warehouse/load-school-teachers {:school-id school-id}
                {:on-success [::load-school-teachers-success]}]}))

(re-frame/reg-event-fx
  ::load-school-teachers-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ school-teachers]]
    {:db (-> db
             (set-data-loading false)
             (set-school-teachers school-teachers))}))

(re-frame/reg-event-fx
  ::reset
  (fn [{:keys [db]} [_]]
    {:db (dissoc db path-to-db)}))

(re-frame/reg-event-fx
  ::save
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ class-id on-save]]
    (let [selected-teachers (get-selected-teachers db)]

      {:db       (-> db
                     (set-data-saving true))
       :dispatch [::warehouse/assign-teachers-to-class
                  {:class-id class-id
                   :data     selected-teachers}
                  {:on-success [::save-success on-save]}]})))

(re-frame/reg-event-fx
  ::save-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ on-save response]]
    {:db                (-> db
                            (set-data-saving false))
     ::widgets/callback [on-save response]}))
