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
  (fn [{:keys [db]} [_ type data]]
    {:db (assoc-in db (path-to-db [:current-selection]) {:type type :data data})}))

(re-frame/reg-event-fx
  ::reset-selection
  (fn [{:keys [db]} [_]]
    {:db (assoc-in db (path-to-db [:current-selection]) nil)}))

(defn saved-selection
  [db]
  (get-in db (path-to-db [:saved-selection])))

(re-frame/reg-event-fx
  ::save-selection
  (fn [{:keys [db]} [_]]
    (let [current-selection (selection db)]
      {:db (assoc-in db (path-to-db [:saved-selection]) current-selection)})))

;;

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
