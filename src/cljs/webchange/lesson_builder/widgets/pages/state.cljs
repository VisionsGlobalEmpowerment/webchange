(ns webchange.lesson-builder.widgets.pages.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.lesson-builder.state-flipbook :as state]))

(def path-to-db :lesson-builder/pages)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

(defn set-last
  [coll x]
  (-> (pop coll)
      (conj x)))

(re-frame/reg-sub
  ::stages
  :<- [::state/activity-stages-filtered]
  (fn [activity-stages]
    (let [last-stage (last activity-stages)
          add-page-data {:title "Add"
                         :id    :add}]
      (if (-> last-stage :right-page nil?)
        (->> (assoc last-stage :right-page add-page-data)
             (set-last activity-stages))
        (conj activity-stages {:id        :new-stage
                               :left-page add-page-data})))))

(re-frame/reg-event-fx
  ::add-page
  (fn [{:keys [_]} [_]]
    {:dispatch [::state/add-page]}))

;;reorder pages

(re-frame/reg-sub
  ::current-page-over
  :<- [path-to-db]
  #(get % :current-page-over))

(re-frame/reg-event-fx
  ::drag-start-page
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ idx]]
    {:db (assoc db :current-page-from idx)}))

(defn- drag-started?
  [db]
  (-> db :current-page-from some?))

(re-frame/reg-event-fx
  ::drag-enter-page
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ idx page-offset]]
    (when (drag-started? db)
      {:db (assoc db :current-page-over (+ idx page-offset))})))

(re-frame/reg-event-fx
  ::drag-drop-page
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (when (drag-started? db)
      (let [page-from (:current-page-from db)
            page-to (:current-page-over db)]
        {:db (dissoc db :current-page-over :current-page-from)
         :dispatch [::state/move-page {:from page-from
                                       :to (if (>= page-from page-to)
                                             page-to
                                             (dec page-to))}]}))))
