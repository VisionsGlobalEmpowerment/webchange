(ns webchange.editor-v2.course-dashboard.state
  (:require
   [re-frame.core :as re-frame]
   [webchange.state.warehouse :as warehouse]))


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
