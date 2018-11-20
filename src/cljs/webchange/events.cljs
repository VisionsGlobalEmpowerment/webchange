(ns webchange.events
  (:require
   [re-frame.core :as re-frame]
   [webchange.db :as db]
   [day8.re-frame.tracing :refer-macros [fn-traced defn-traced]]
   ))

(re-frame/reg-event-db
 ::initialize-db
 (fn-traced [_ _]
   db/default-db))

(re-frame/reg-event-db
  ::change-viewport
  (fn [db [_ value]]
    (assoc db :viewport value)))

(re-frame/reg-event-db
  ::set-current-scene
  (fn [db [_ value]]
    (assoc db :current-scene value)))

(re-frame/reg-event-db
  ::set-scene
  (fn [db [_ [scene-id scene]]]
    (assoc-in db [:scenes scene-id] scene)))

(re-frame/reg-event-db
  ::set-loading-progress
  (fn [db [_ [scene-id value]]]
    (assoc-in db [:scene-loading-progress scene-id] value)))

(re-frame/reg-event-db
  ::set-scene-loaded
  (fn [db [_ [scene-id value]]]
    (assoc-in db [:scene-loading-complete scene-id] value)))

(re-frame/reg-event-db
  ::start-playing
  (fn [db _]
    (assoc db :playing true)))

(re-frame/reg-event-db
  ::set-object-state
  (fn [db [_ [target state-id]]]
    (let [scene-id (:current-scene db)
          scene (get-in db [:scenes scene-id])
          object (get-in scene [:objects (keyword target)])
          state (get-in object [:states (keyword state-id)])]
      (update-in db [:scenes scene-id :objects (keyword target)] merge state))))

(re-frame/reg-event-db
  ::open-settings
  (fn [db _]
    (js/console.log "open settings")
    (assoc db :ui-screen :settings)))

(re-frame/reg-event-db
  ::close-settings
  (fn [db _]
    (assoc db :ui-screen :default)))