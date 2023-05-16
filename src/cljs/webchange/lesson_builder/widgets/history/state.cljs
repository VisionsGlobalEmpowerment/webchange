(ns webchange.lesson-builder.widgets.history.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.lesson-builder.layout.stage.state :as stage-state]
    [webchange.lesson-builder.state :as state]
    [webchange.state.warehouse :as warehouse]
    [webchange.utils.date :refer [date-str->locale-date date-str->time]]
    [webchange.utils.name :refer [fullname]]))

(def path-to-db :widget/activity-history)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

;; window state

(def window-open-key :window-open?)

(defn- set-window-open
  [db value]
  (assoc db window-open-key value))

(re-frame/reg-sub
  ::window-open?
  :<- [path-to-db]
  #(get % window-open-key false))

(re-frame/reg-event-fx
  ::open-window
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (set-window-open db true)}))

(re-frame/reg-event-fx
  ::close-window
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (set-window-open db false)}))

;; versions

(re-frame/reg-sub
  ::activity-versions
  :<- [::state/activity-versions]
  (fn [versions]
    (->> versions
         (sort-by :created-at)
         (reverse)
         (map-indexed (fn [idx {:keys [id created-at description owner]}]
                        {:id          id
                         :date        (date-str->locale-date created-at)
                         :time        (date-str->time created-at)
                         :description description
                         :owner       (fullname owner)
                         :current?    (= idx 0)})))))

(def restore-loading-key :restore-loading?)

(defn- set-restore-loading
  [db version-id value]
  (assoc-in db [restore-loading-key version-id] value))

(re-frame/reg-sub
  ::restore-loading?
  :<- [path-to-db]
  (fn [db [_ version-id]]
    (get-in db [restore-loading-key version-id])))

(re-frame/reg-event-fx
  ::restore-version
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ version-id]]
    {:db       (set-restore-loading db version-id true)
     :dispatch [::warehouse/restore-version
                {:scene-version-id version-id}
                {:on-success [::restore-version-success version-id]
                 :on-failure [::restore-version-failure version-id]}]}))

(re-frame/reg-event-fx
  ::restore-version-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ version-id {:keys [data]}]]
    {:db         (set-restore-loading db version-id false)
     :dispatch-n [[::state/reset-activity-data data]
                  [::stage-state/reset]
                  [::close-window]]}))

(re-frame/reg-event-fx
  ::restore-version-failure
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ version-id]]
    {:db (set-restore-loading db version-id false)}))
