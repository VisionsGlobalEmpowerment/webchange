(ns webchange.admin.pages.course-edit.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.state.warehouse :as warehouse]
    [webchange.utils.course-data :as utils]))

(def path-to-db :page/edit-course)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

;; Course Data

(def course-data-key :course-data)

(defn- set-course-data
  [db value]
  (assoc db course-data-key value))

(re-frame/reg-sub
  ::course-data
  :<- [path-to-db]
  #(get % course-data-key))

(re-frame/reg-sub
  ::course-levels
  :<- [::course-data]
  (fn [course-data]
    (->> (get course-data :levels [])
         (map-indexed (fn [idx level-data]
                        (let [level-index (inc idx)]
                          {:idx            level-index
                           :name           (str "Level " level-index)
                           :lessons-number (-> (utils/get-lessons-data level-data)
                                               (count))}))))))

(re-frame/reg-sub
  ::level-lessons
  :<- [::course-data]
  (fn [course-data [_ level-idx]]
    (->> (dec level-idx)
         (utils/get-lessons-data course-data)
         (map-indexed (fn [idx lesson-data]
                        (let [lesson-index (inc idx)]
                          {:idx               lesson-index
                           :name              (str "Lesson " lesson-index)
                           :activities-number (-> (utils/get-activities-data lesson-data)
                                                  (count))}))))))

(re-frame/reg-sub
  ::lesson-activities
  :<- [::course-data]
  (fn [course-data [_ level-idx lesson-idx]]
    (->> (utils/get-activities-data course-data (dec level-idx) (dec lesson-idx))
         (map-indexed (fn [idx {:keys [activity unique-id]}]
                        (let [activity-index (inc idx)]
                          (merge {:id   unique-id
                                  :idx  activity-index}
                                 (utils/get-activity-info course-data activity))))))))

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)]
  (fn [{:keys [_]} [_ {:keys [course-slug]}]]
    {:dispatch [::warehouse/load-course course-slug {:on-success [::load-course-success]}]}))

(re-frame/reg-event-fx
  ::load-course-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ course-data]]
    {:db (-> db (set-course-data course-data))}))
