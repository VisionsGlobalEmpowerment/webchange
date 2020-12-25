(ns webchange.editor-v2.scenes-crossing.state-locations
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.scenes-crossing.state :as crossing-state]
    [webchange.editor-v2.course-data-utils.utils :as utils]
    [webchange.editor-v2.course-table.state.edit-common :as common]
    [webchange.state :as state]))

(re-frame/reg-sub
  ::has-locations?
  (fn []
    [(re-frame/subscribe [::state/course-data])])
  (fn [[course-data] [_ scene-id]]
    (->> (keyword scene-id)
         (utils/scene-locations course-data)
         (some?))))

(re-frame/reg-event-fx
  ::init-scene-locations
  (fn [{:keys [db]} [_ scene-id]]
    (let [course-id (crossing-state/get-current-course db)
          course-data (-> (state/course-data db)
                          (utils/init-scene-locations (keyword scene-id)))]
      {:dispatch [::common/update-course course-id course-data]})))

(re-frame/reg-sub
  ::scene-locations
  (fn []
    [(re-frame/subscribe [::state/course-data])])
  (fn [[course-data] [_ scene-id]]
    (->> (keyword scene-id)
         (utils/scene-locations course-data))))

(re-frame/reg-sub
  ::location-options
  (fn []
    [(re-frame/subscribe [::crossing-state/scene-out-options])])
  (fn [[options]]
    options))

(re-frame/reg-event-fx
  ::save-scene-location
  (fn [{:keys [db]} [_ scene-id level location]]
    (let [course-id (crossing-state/get-current-course db)
          course-data (-> (state/course-data db)
                          (utils/add-scene-location (keyword scene-id) level location))]
      {:dispatch [::common/update-course course-id course-data]})))

;;; Level
;
;(re-frame/reg-sub
;  ::levels
;  (fn []
;    [(re-frame/subscribe [::state/course-data])])
;  (fn [[course-data]]
;    (->> (utils/get-levels course-data)
;         (map-indexed (fn [index _] {:idx  index
;                                     :name (str "Level " (inc index))})))))
;
;(defn- get-current-level
;  ([db]
;   (let [component-data (get-component-data db)]
;     (get-current-level component-data nil)))
;  ([component-data _]
;   (get component-data :current-level 0)))
;
;(re-frame/reg-sub
;  ::current-level
;  (fn []
;    [(re-frame/subscribe [::component-data])])
;  (fn [[component-data]]
;    (get-current-level component-data nil)))
;
;(re-frame/reg-event-fx
;  ::set-current-level
;  (fn [{:keys [_]} [_ level-idx]]
;    {:dispatch [::set-component-data :current-level level-idx]}))

(defn- get-level-location
  [course-data scene-name current-level]
  (->> (utils/scene-locations course-data (keyword scene-name))
       (some (fn [{:keys [level scene]}]
               (and (= level current-level)
                    scene)))))