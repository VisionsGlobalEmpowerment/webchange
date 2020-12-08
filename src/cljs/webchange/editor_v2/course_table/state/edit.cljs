(ns webchange.editor-v2.course-table.state.edit
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.course-table.state.db :as db]
    [webchange.editor-v2.course-table.state.edit-skills :as skills]
    [webchange.editor-v2.course-table.state.edit-tags :as tags]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:edit])
       (db/path-to-db)))

(re-frame/reg-event-fx
  ::open-menu
  (fn [{:keys [db]} [_ {:keys [cell-data title]}]]
    (let [{:keys [field]} cell-data]
      (cond-> {:db (-> db
                       (assoc-in (path-to-db [:open?]) true)
                       (assoc-in (path-to-db [:title]) title))}
              (or (= field :skills)
                  (= field :abbr-global)) (assoc :dispatch [::skills/init-skills cell-data])
              (= field :tags) (assoc :dispatch [::tags/init-tags cell-data])))))

(re-frame/reg-event-fx
  ::close-menu
  (fn [{:keys [db]} [_]]
    {:db (assoc-in db (path-to-db [:open?]) false)}))

(re-frame/reg-sub
  ::open?
  (fn [db]
    (get-in db (path-to-db [:open?]) false)))

(re-frame/reg-sub
  ::title
  (fn [db]
    (get-in db (path-to-db [:title]))))
