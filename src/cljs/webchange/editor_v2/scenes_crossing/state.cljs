(ns webchange.editor-v2.scenes-crossing.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.course-data-utils.utils :as utils]
    [webchange.editor-v2.course-table.state.edit-common :as common]
    [webchange.editor-v2.state :as db]
    [webchange.state.state :as state]
    [webchange.state.warehouse :as warehouse]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:scenes-crossing])
       (db/path-to-db)))

(defn- get-component-data
  [db]
  (get-in db (path-to-db []) {}))

(re-frame/reg-sub
  ::component-data
  (fn [db [_]]
    (get-component-data db)))

(re-frame/reg-event-fx
  ::set-component-data
  (fn [{:keys [db]} [_ data-key data-value]]
    {:db (assoc-in db (path-to-db [data-key]) data-value)}))

(re-frame/reg-event-fx
  ::init
  (fn [{:keys [_]} [_ course-slug]]
    {:dispatch-n [[::set-current-course course-slug]
                  [::warehouse/load-course course-slug]]}))

;; Course slug

(defn get-current-course
  ([db]
   (let [component-data (get-component-data db)]
     (get-current-course component-data nil)))
  ([component-data _]
   (get component-data :current-course)))

(re-frame/reg-event-fx
  ::set-current-course
  (fn [{:keys [_]} [_ course-slug]]
    {:dispatch [::set-component-data :current-course course-slug]}))

;; Scene

(defn- get-course-scenes
  ([db]
   (let [course-data (state/course-data db)]
     (get-course-scenes course-data nil)))
  ([course-data _]
   (->> (utils/course-scenes-list course-data)
        (sort-by :name))))

(re-frame/reg-sub
  ::course-scenes
  (fn []
    [(re-frame/subscribe [::state/course-data])])
  (fn [[course-data]]
    (get-course-scenes course-data nil)))

(defn- get-current-scene
  ([db]
   (let [component-data (get-component-data db)
         course-scenes (get-course-scenes db)]
     (get-current-scene component-data course-scenes)))
  ([component-data course-scenes]
   (get component-data :current-scene (first course-scenes))))

(re-frame/reg-sub
  ::current-scene
  (fn []
    [(re-frame/subscribe [::component-data])
     (re-frame/subscribe [::course-scenes])])
  (fn [[component-data course-scenes]]
    (get-current-scene component-data course-scenes)))

(re-frame/reg-event-fx
  ::set-current-scene
  (fn [{:keys [_]} [_ scene-data]]
    {:dispatch [::set-component-data :current-scene scene-data]}))

;; Scene outs

(re-frame/reg-sub
  ::scene-outs
  (fn []
    [(re-frame/subscribe [::state/course-data])
     (re-frame/subscribe [::current-scene])])
  (fn [[course-data current-scene]]
    (->> (:id current-scene)
         (utils/get-scene-outs course-data)
         (map (fn [{:keys [name object]}] {:object object
                                           :scene  name})))))

(re-frame/reg-sub
  ::scene-out-options
  (fn []
    [(re-frame/subscribe [::course-scenes])
     (re-frame/subscribe [::current-scene])])
  (fn [[course-scenes current-scene]]
    (->> course-scenes
         (remove (fn [{:keys [id]}] (= id (:id current-scene))))
         (map (fn [scene-data] (select-keys scene-data [:id :name])))
         (sort-by :name))))

;; Save

(re-frame/reg-event-fx
  ::change-scene-out
  (fn [{:keys [db]} [_ scene-object out-scene-id]]
    (let [scene-id (-> db get-current-scene :id)
          course-id (get-current-course db)
          course-data (-> (state/course-data db)
                          (utils/set-scene-out scene-id scene-object out-scene-id))]
      {:dispatch [::common/update-course course-id course-data]})))
