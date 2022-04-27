(ns webchange.admin.pages.school-profile.state
  (:require [re-frame.core :as re-frame]
            [webchange.admin.pages.state :as parent-state]
            [webchange.state.warehouse :as warehouse]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:school-profile])
       (parent-state/path-to-db)))

(re-frame/reg-event-fx
  ::init
  (fn [{:keys [_]} [_ school-id]]
    {:dispatch [::warehouse/load-schools
                {:on-success [::load-schools-success school-id]}]}))

(re-frame/reg-event-fx
  ::load-schools-success
  (fn [{:keys [db]} [_ school-id schools]]
    {:db (assoc-in db (path-to-db [:school])
                   (->> (:schools schools)
                        (filter #(= (str (:id %)) school-id))
                        first))}))

(re-frame/reg-sub
  ::current-school
  (fn [db]
    (get-in db (path-to-db [:school]))))
