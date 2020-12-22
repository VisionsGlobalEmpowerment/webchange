(ns webchange.editor-v2.course-table.state.data
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.course-table.state.data-utils :refer [prepare-course-data]]
    [webchange.editor-v2.course-table.state.db :as db]
    [webchange.editor-v2.course-table.state.selection :as selection-state]
    [webchange.interpreter.subs :as interpreter.subs]
    [webchange.interpreter.events :as interpreter.events]
    [webchange.subs :as subs]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:data])
       (db/path-to-db)))

(re-frame/reg-event-fx
  ::init
  (fn [{:keys [db]} [_ course-id]]
    {:db       (assoc-in db (path-to-db [:course-id]) course-id)
     :dispatch [::interpreter.events/load-course-data course-id]}))

(defn course-id
  [db]
  (get-in db (path-to-db [:course-id])))

(re-frame/reg-sub
  ::table-data
  (fn []
    [(re-frame/subscribe [::subs/course-data])
     (re-frame/subscribe [::subs/scenes-data [:skills]])
     (re-frame/subscribe [::interpreter.subs/lessons-data {:exclude-items-fields [:data]}])])
  (fn [[course-data scenes-data lessons-data]]
    (prepare-course-data course-data scenes-data lessons-data)))

(defn- find-activity-row
  [rows selection]
  (let [fields [:level :lesson :lesson-idx]]
    (some (fn [row]
            (and (= (select-keys row fields)
                    (select-keys selection fields))
                 row))
          rows)))

(re-frame/reg-sub
  ::current-data
  (fn []
    [(re-frame/subscribe [::selection-state/selection])
     (re-frame/subscribe [::table-data])])
  (fn [[selection table-data]]
    {:field    (-> selection (get-in [:data :field]) (keyword))
     :activity (find-activity-row table-data selection)}))

(re-frame/reg-sub
  ::course-activities
  (fn []
    [(re-frame/subscribe [::subs/course-data])])
  (fn [[course-data]]
    (->> (:scene-list course-data)
         (map (fn [[id {:keys [name]}]] {:id id :name name})))))

(re-frame/reg-sub
  ::course-activity
  (fn []
    [(re-frame/subscribe [::course-activities])])
  (fn [[course-activities] [_ activity-id]]
    (some (fn [{:keys [id] :as activity}]
            (and (= id activity-id)
                 activity))
          course-activities)))
