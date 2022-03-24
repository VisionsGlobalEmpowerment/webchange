(ns webchange.editor-v2.course-dashboard.state
  (:require
   [re-frame.core :as re-frame]
   [webchange.state.warehouse :as warehouse]
   [webchange.subs :as subs]
   [webchange.editor-v2.course-table.state.edit-common :as common]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:editor-v2 :course-dashboard])
       (vec)))

(re-frame/reg-event-fx
  ::save-scene-info
  (fn [{:keys [db]} [_ {:keys [scene-id data]}]]
    (let [course-data (-> (get-in db [:course-data])
                          (update-in [:scene-list (keyword scene-id)] merge data))
          current-course (:current-course db)]
      {:db (assoc-in db [:course-data] course-data)
       :dispatch [::warehouse/save-course {:course-slug current-course
                                           :course-data course-data}]})))

(re-frame/reg-event-fx
  ::create-new-activity
  (fn [{:keys [db]} [_ activity-name course-id]]
    (let [activity {:name activity-name}]
      {:dispatch [::warehouse/create-activity-placeholder
                  {:course-id course-id
                   :data      activity}
                  {:on-success [::create-new-success course-id true]}]})))

(re-frame/reg-event-fx
  ::duplicate-activity
  (fn [{:keys [db]} [_ old-name new-name course-id]]
    {:dispatch [::warehouse/duplicate-activity
                {:course-id course-id
                 :data {:old-name old-name
                        :new-name new-name}}
                {:on-success [::create-new-success course-id false]}]}))

(re-frame/reg-event-fx
  ::create-new-success
  (fn [{:keys [db]} [_ course-id placeholder? {:keys [name scene-slug]}]]
    (let [activity-data {:name name}
          scene-key (keyword scene-slug)
          course-data (-> (subs/course-data db)
                        (assoc-in [:scene-list scene-key] activity-data))]
      {:db (assoc-in db [:scene-placeholders scene-slug] placeholder?)
       :dispatch [::common/update-course course-id course-data]})))
