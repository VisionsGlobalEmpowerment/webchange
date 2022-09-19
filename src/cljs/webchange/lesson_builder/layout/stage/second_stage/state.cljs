(ns webchange.lesson-builder.layout.stage.second-stage.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.lesson-builder.layout.stage.state :as stage-state]))

(def path-to-db :lesson-builder/second-stage)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

;; source

(def source-key :source)

(defn- get-source
  [db]
  (get db source-key))

(defn- set-source
  [db value]
  (assoc db source-key value))

(re-frame/reg-sub
  ::source
  :<- [path-to-db]
  #(get-source %))

;; scene data

(re-frame/reg-sub
  ::empty-source
  (constantly nil))

(re-frame/reg-sub
  ::activity-data
  (fn [[_ source]]
    (re-frame/subscribe [(or source ::empty-source)]))
  (fn [activity-data]
    (when-not (empty? activity-data)
      activity-data)))

;; events

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ source-namespace]]
    {:db (set-source db source-namespace)}))

(re-frame/reg-event-fx
  ::reset
  (fn [{:keys [db]} [_]]
    {:db (dissoc db path-to-db)
     :dispatch [::stage-state/reset]}))
