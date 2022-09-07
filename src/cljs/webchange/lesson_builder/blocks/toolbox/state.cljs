(ns webchange.lesson-builder.blocks.toolbox.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.lesson-builder.state :as state]))

(def path-to-db :lesson-builder/toolbox)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

(def current-widget-key :current-widget)

(defn- get-current-widget
  [db]
  (get db current-widget-key))

(re-frame/reg-sub
  ::current-widget
  :<- [path-to-db]
  :<- [::state/flipbook?]
  (fn [[db flipbook?]]
    (get db current-widget-key (if flipbook? :pages :welcome))))

(re-frame/reg-event-fx
  ::set-current-widget
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ value]]
    {:db (assoc db current-widget-key value)}))
