(ns webchange.editor-v2.sandbox.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.sandbox.parse-actions :refer [find-all-actions]]
    [webchange.subs :as subs]))

(def modal-state-path [:editor-v2 :sandbox :share-modal-state])
(def modal-share-link-state [:editor-v2 :sandbox :share-link-state])

;; Subs

(re-frame/reg-sub
  ::modal-state
  (fn [db]
    (-> db
        (get-in modal-state-path)
        boolean)))

(re-frame/reg-sub
  ::lesson-set-data
  (fn [db]
    (get-in db modal-share-link-state {})))

(re-frame/reg-sub
  ::lesson-sets
  (fn [db]
    (let [scene-data (subs/current-scene-data db)]
      (->> (find-all-actions scene-data {:type ["lesson-var-provider"]})
           (map second)
           (map :from)))))

(re-frame/reg-sub
  ::link
  (fn [db]
    (let [course-slug (:current-course db)
          scene-slug (:current-scene db)
          lessons @(re-frame/subscribe [::lesson-set-data]) ;{:concepts {:item-ids [188 196 208], :dataset-id 4}}
          encoded-lessons (-> lessons clj->js js/JSON.stringify js/btoa js/encodeURIComponent)]
      (if (seq lessons)
        (str js/location.protocol "//" js/location.host "/s/" course-slug "/" scene-slug "/" encoded-lessons)
        (str js/location.protocol "//" js/location.host "/s/" course-slug "/" scene-slug)))))

;; Events

(re-frame/reg-event-fx
  ::open
  (fn [{:keys [db]} [_]]
    {:db (-> db
             (assoc-in modal-state-path true)
             (assoc-in modal-share-link-state {}))}))

(re-frame/reg-event-fx
  ::close
  (fn [{:keys [db]} [_]]
    {:db (assoc-in db modal-state-path false)}))

(re-frame/reg-event-fx
  ::set-lesson-set-data
  (fn [{:keys [db]} [_ lesson-set-name dataset-id item-ids]]
    {:db (cond-> (assoc-in db (concat modal-share-link-state [lesson-set-name]) {})
                 (some? dataset-id) (assoc-in (concat modal-share-link-state [lesson-set-name :dataset-id]) dataset-id)
                 (some? item-ids) (assoc-in (concat modal-share-link-state [lesson-set-name :item-ids]) item-ids))}))
