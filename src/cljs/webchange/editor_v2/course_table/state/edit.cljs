(ns webchange.editor-v2.course-table.state.edit
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.course-table.state.db :as db]
    [webchange.editor-v2.course-table.state.edit-activity :as activity]
    [webchange.editor-v2.course-table.state.edit-concepts :as concepts]
    [webchange.editor-v2.course-table.state.edit-skills :as skills]
    [webchange.editor-v2.course-table.state.edit-tags :as tags]
    [webchange.editor-v2.course-table.state.selection :as selection-state]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:edit])
       (db/path-to-db)))

(re-frame/reg-event-fx
  ::open-menu
  (fn [{:keys [db]} [_]]
    (let [{:keys [data]} (selection-state/selection db)
          {:keys [field]} data]
      (cond-> {:db (assoc-in db (path-to-db [:open?]) true)}
              (or (= field :skills)
                  (= field :abbr-global)) (assoc :dispatch [::skills/init-skills data])
              (= field :tags) (assoc :dispatch [::tags/init-tags data])
              (= field :activity) (assoc :dispatch [::activity/init-activities data])
              (= field :concepts) (assoc :dispatch [::concepts/init-concepts data])))))

(re-frame/reg-event-fx
  ::close-menu
  (fn [{:keys [db]} [_]]
    {:db (assoc-in db (path-to-db [:open?]) false)}))

(re-frame/reg-sub
  ::open?
  (fn [db]
    (get-in db (path-to-db [:open?]) false)))
