(ns webchange.editor-v2.creation-progress.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.concepts.subs :as subs]
    [webchange.editor-v2.creation-progress.translation-progress.overall-progress :refer [get-progress]]
    [webchange.editor-v2.events :as editor-events]
    [webchange.subs :as w-subs]))

(defn path-to-db
  [relative-path]
  (concat [:translation-progress] relative-path))

(re-frame/reg-sub
  ::translation-progress
  (fn [db]
    {:progress 0
     :overall-data nil}
    #_(let [current-scene-data (w-subs/current-scene-data db)
          dataset-items (subs/dataset-items db)
          dataset-id (->> dataset-items first :dataset-id)
          dataset-scheme (subs/dataset db dataset-id)]
      (get-progress current-scene-data dataset-items dataset-scheme))))

(re-frame/reg-event-fx
  ::show-translation-progress
  (fn [{:keys [db]} [_]]
    {:db (assoc-in db (path-to-db [:window-state]) {:show? true
                                                    :mode :short-info})}))

(re-frame/reg-event-fx
  ::hide-translation-progress
  (fn [{:keys [db]} [_]]
    {:db (update-in db (path-to-db [:window-state]) merge {:show? false})}))

(re-frame/reg-event-fx
  ::show-translation-full-progress
  (fn [{:keys [db]} [_]]
    {:db (assoc-in db (path-to-db [:window-state :mode]) :full-info)}))

(re-frame/reg-event-fx
  ::show-translation-short-progress
  (fn [{:keys [db]} [_]]
    {:db (assoc-in db (path-to-db [:window-state :mode]) :short-info)}))

(re-frame/reg-sub
  ::window-state
  (fn [db]
    (get-in db (path-to-db [:window-state]))))

(re-frame/reg-event-fx
  ::edit-concept
  (fn [{:keys [db]} [_ concept-id]]
    (let [course-id (:current-course db)]
      {:redirect [:course-editor-v2-concept :id course-id :concept-id concept-id]})))

(re-frame/reg-event-fx
  ::edit-action
  (fn [{:keys [_]} [_ action-id]]
    {:dispatch [::editor-events/show-translator-form-by-id action-id]}))
