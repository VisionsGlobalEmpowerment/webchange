(ns webchange.interpreter.screens.state
  (:require
    [re-frame.core :as re-frame]))

(defn path-to-db
  []
  [:ui-screen])

;; Subs

(re-frame/reg-sub
  ::ui-screen
  (fn [db]
    (get-in db (path-to-db))))

;; Events

(re-frame/reg-event-fx
  ::set-ui-screen
  (fn [{:keys [db]} [_ value]]
    {:db (assoc-in db (path-to-db) value)}))

(re-frame/reg-event-fx
  ::reset-ui-screen
  (fn [_ _]
    {:dispatch [::set-ui-screen nil]}))

(re-frame/reg-event-fx
  ::show-activity-progress
  (fn [_ _]
    {:dispatch [::set-ui-screen :activity-finished]}))

(re-frame/reg-event-fx
  ::show-course-loading
  (fn [_ _]
    {:dispatch [::set-ui-screen :course-loading]}))

(re-frame/reg-event-fx
  ::show-settings
  (fn [_ _]
    {:dispatch [::set-ui-screen :settings]}))
