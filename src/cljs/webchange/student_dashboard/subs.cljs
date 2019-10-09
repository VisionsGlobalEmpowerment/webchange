(ns webchange.student-dashboard.subs
  (:require
    [re-frame.core :as re-frame]))

(defn scene-name->scene [scene-name scenes]
  (let [{:keys [name preview type]} (get scenes (keyword scene-name))]
    {:id scene-name
     :type type
     :name name
     :image preview}))

(re-frame/reg-sub
  ::finished-activities
  (fn [db]
    (let [scenes (get-in db [:course-data :scene-list])
          is-finished? #(-> db
                            (get-in [:progress-data :finished-workflow-actions])
                            set
                            (contains? (:id %)))
          is-activity? #(= "set-activity" (:type %))]
      (->> (get-in db [:course-data :workflow-actions])
           (filter is-finished?)
           (filter is-activity?)
           (map #(assoc % :scene (scene-name->scene (:activity %) scenes)))
           (map #(assoc-in % [:scene :activity-id] (get % :id)))
           (map #(assoc-in % [:scene :lesson] (get % :lesson)))
           (map #(assoc-in % [:scene :level] (get % :level)))
           (map :scene)
           (map #(assoc % :completed true))))))

(re-frame/reg-sub
  ::next-activity
  (fn [db]
    (let [scenes (get-in db [:course-data :scene-list])]
      (-> (get-in db [:progress-data :current-activity])
          (scene-name->scene scenes)))))

(re-frame/reg-sub
  ::assessments
  (fn [db]
    (let [scenes (get-in db [:course-data :scene-list])
          is-finished? #(-> db
                            (get-in [:progress-data :finished-workflow-actions])
                            set
                            (contains? (:id %)))
          is-activity? #(= "set-activity" (:type %))
          is-assessment? #(= "assessment" (:type %))]
      (->> (get-in db [:course-data :workflow-actions])
           (filter is-activity?)
           (map #(assoc % :scene (scene-name->scene (:activity %) scenes)))
           (map #(if (is-finished? %) (assoc-in % [:scene :completed] true) %))
           (map #(assoc-in % [:scene :activity-id] (get % :id)))
           (map :scene)
           (filter is-assessment?)))))

(re-frame/reg-sub
  ::progress-loading
  (fn [db]
    (or
      (get-in db [:loading :load-course])
      (get-in db [:loading :load-progress]))))