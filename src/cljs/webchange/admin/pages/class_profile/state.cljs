(ns webchange.admin.pages.class-profile.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.state.warehouse :as warehouse]
    [webchange.validation.specs.class-spec :as class-spec]
    [webchange.validation.validate :refer [validate]]))

(def path-to-db :class-profile)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

(defn- set-initial-class-data
  [db class-data]
  (assoc db :initial-class-data class-data))

(defn- set-class-data
  [db class-data]
  (assoc db :class-data class-data))

(defn- reset-class-data
  [db class-data]
  (-> db
      (set-initial-class-data class-data)
      (set-class-data class-data)))

(defn- set-validation-errors
  [db errors]
  (assoc db :validation-errors errors))

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [class-id]}]]
    {:db (assoc db :class-id class-id)
     :dispatch [::warehouse/load-class {:class-id class-id}
                {:on-success [::load-class-success]}]}))

(re-frame/reg-event-fx
  ::load-class-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [class]}]]
    (let [class-data (select-keys class [:course-id :name])]
      {:db (reset-class-data db class-data)})))

;; Class Data

(re-frame/reg-event-fx
  ::set-class-data
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ value]]
    (let [validation-errors (validate ::class-spec/class value)]
      {:db       (-> db
                     (set-class-data value)
                     (set-validation-errors validation-errors))})))

(re-frame/reg-sub
  ::class-data
  :<- [path-to-db]
  :class-data)

;; Save

(re-frame/reg-sub
  ::save-button-enabled?
  :<- [path-to-db]
  (fn [{:keys [current-data initial-data validation-errors]}]
    (and (not= current-data initial-data)
         (not validation-errors))))

(re-frame/reg-event-fx
  ::save-class
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [{:keys [class-id class-data]} db
          validation-errors (validate ::class-spec/class class-data)]
      (if (nil? validation-errors)
        {:db (set-validation-errors db nil)
         :dispatch [::warehouse/save-class {:class-id class-id
                                            :data     class-data}
                    {:on-success [::save-class-success]}]}
        {:db (set-validation-errors db validation-errors)}))))

(re-frame/reg-event-fx
  ::save-class-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [data]}]]
    {:db (reset-class-data db data)}))
