(ns webchange.editor-v2.course-table.state.pagination
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.course-table.state.db :as db]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:pagination])
       (db/path-to-db)))

(defn- page-rows
  [db]
  (get-in db (path-to-db [:rows]) 1))

(re-frame/reg-sub ::page-rows page-rows)

(re-frame/reg-event-fx
  ::set-page-rows
  (fn [{:keys [db]} [_ rows-number]]
    {:db (assoc-in db (path-to-db [:rows]) (if (some? rows-number) rows-number 1))}))

(defn- skip-rows
  [db]
  (get-in db (path-to-db [:skip]) 0))

(re-frame/reg-sub ::skip-rows skip-rows)

(re-frame/reg-event-fx
  ::set-skip-rows
  (fn [{:keys [db]} [_ rows-number]]
    {:db (assoc-in db (path-to-db [:skip]) rows-number)}))

(re-frame/reg-event-fx
  ::shift-skip-rows
  (fn [{:keys [db]} [_ delta-rows-number total-data-rows]]
    (let [current-skip-rows (skip-rows db)
          page-rows (page-rows db)
          new-skip (cond-> (+ current-skip-rows delta-rows-number)
                           (< delta-rows-number 0) (Math/max 0)
                           (> delta-rows-number 0) (Math/min (if (> total-data-rows page-rows)
                                                               (- total-data-rows page-rows)
                                                               current-skip-rows)))]
      {:dispatch [::set-skip-rows new-skip]})))
