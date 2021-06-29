(ns webchange.sw-utils.state.resources
  (:require
    [re-frame.core :as re-frame]
    [webchange.sw-utils.message :refer [cache-resources get-current-state]]
    [webchange.sw-utils.state.db :refer [path-to-db]]))

;; Cached lessons

(def cached-lessons-path (path-to-db [:cached-resources]))

(re-frame/reg-event-fx
  ::init
  (fn [{:keys [_]} [_]]
    {:get-current-state nil}))

(re-frame/reg-fx
  :get-current-state
  (fn []
    (get-current-state)))

(defn cached-resources
  [db]
  (get-in db cached-lessons-path []))

(re-frame/reg-sub ::cached-resources cached-resources)

(re-frame/reg-event-fx
  ::set-cached-resources
  (fn [{:keys [db]} [_ resources]]
    {:db (assoc-in db cached-lessons-path resources)}))

(re-frame/reg-event-fx
  ::cache-resources
  (fn [{:keys [_]} [_ data]]
    {:cache-resources data}))

(re-frame/reg-fx
  :cache-resources
  (fn [data]
    (cache-resources data)))
