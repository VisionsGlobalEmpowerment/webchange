(ns webchange.editor-v2.course-table.state.selection
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.course-table.state.db :as db]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:selection])
       (db/path-to-db)))

(defn selection
  [db]
  (get-in db (path-to-db [:current-selection])))

(re-frame/reg-sub ::selection selection)

(re-frame/reg-event-fx
  ::set-selection
  (fn [{:keys [db]} [_ data]]
    {:db (assoc-in db (path-to-db [:current-selection]) data)}))

(re-frame/reg-event-fx
  ::reset-selection
  (fn [{:keys [db]} [_]]
    {:db (assoc-in db (path-to-db [:current-selection]) nil)}))

(defn saved-selection
  [db]
  (get-in db (path-to-db [:saved-selection])))

(re-frame/reg-sub ::saved-selection saved-selection)

(re-frame/reg-event-fx
  ::save-selection
  (fn [{:keys [db]} [_ selection]]
    {:db (assoc-in db (path-to-db [:saved-selection]) selection)}))

(re-frame/reg-event-fx
  ::reset-saved-selection
  (fn [{:keys [db]} [_]]
    {:db (assoc-in db (path-to-db [:saved-selection]) nil)}))

;; Context menu

(re-frame/reg-sub
  ::menu-open?
  (fn [db]
    (get-in db (path-to-db [:menu-open?]))))

(re-frame/reg-event-fx
  ::open-context-menu
  (fn [{:keys [db]} [_]]
    {:db (assoc-in db (path-to-db [:menu-open?]) true)}))

(re-frame/reg-event-fx
  ::close-context-menu
  (fn [{:keys [db]} [_]]
    {:db (assoc-in db (path-to-db [:menu-open?]) false)}))
