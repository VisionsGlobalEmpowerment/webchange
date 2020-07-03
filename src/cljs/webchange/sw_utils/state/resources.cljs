(ns webchange.sw-utils.state.resources
  (:require
    [re-frame.core :as re-frame]
    [webchange.sw-utils.message :refer [cache-lessons]]
    [webchange.sw-utils.state.db :refer [path-to-db]]))

;; Cached lessons

(def cached-lessons-path (path-to-db [:cached-lessons]))

(re-frame/reg-sub
  ::cached-lessons
  (fn [db]
    (get-in db cached-lessons-path [])))

(re-frame/reg-event-fx
  ::set-cached-lessons
  (fn [{:keys [db]} [_ lessons]]
    {:db (assoc-in db cached-lessons-path lessons)}))

(re-frame/reg-fx
  :cache-lessons
  (fn [lessons]
    (cache-lessons lessons)))

(re-frame/reg-event-fx
  ::cache-lessons
  (fn [{:keys [_]} [_ lessons]]
    {:cache-lessons lessons}))
