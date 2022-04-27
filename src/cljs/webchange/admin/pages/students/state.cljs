(ns webchange.admin.pages.students.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.pages.state :as parent-state]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:students])
       (parent-state/path-to-db)))

(re-frame/reg-event-fx
  ::init
  (fn [{:keys [_]} [_ {:keys [school-id]}]]
    {}))
