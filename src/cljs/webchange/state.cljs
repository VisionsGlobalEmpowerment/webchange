(ns webchange.state
  (:require
    [re-frame.core :as re-frame]))

;; Lesson sets

(defn lesson-sets
  [db]
  (get db :lessons {}))

(defn lesson-set-id->name
  [db lesson-set-id]
  (->> (lesson-sets db)
       (some (fn [[lesson-set-name {:keys [id]}]]
               (and (= id lesson-set-id)
                    lesson-set-name)))))

(defn- prepare-lesson-set-data [{data :data :as prepare-lesson-set}]
  (assoc prepare-lesson-set :item-ids (map #(:id %) (:items data))))

(re-frame/reg-sub
  ::lesson-sets
  lesson-sets)

(re-frame/reg-event-fx
  ::update-lesson-set
  (fn [{:keys [db]} [_ lesson-set-name lesson-set-data]]
    {:db (->> (prepare-lesson-set-data lesson-set-data)
              (update-in db [:lessons lesson-set-name] merge))}))

(re-frame/reg-event-fx
  ::delete-lesson-set-by-name
  (fn [{:keys [db]} [_ lesson-set-name]]
    {:db (update db :lessons dissoc lesson-set-name)}))

(re-frame/reg-event-fx
  ::delete-lesson-set-by-id
  (fn [{:keys [db]} [_ lesson-set-id]]
    {:dispatch [::delete-lesson-set-by-name (lesson-set-id->name db lesson-set-id)]}))
