(ns webchange.student-dashboard.timeline.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.events :as events]
    [webchange.interpreter.events :as ie]
    [webchange.interpreter.lessons.activity :as lessons-activity]
    [webchange.progress.activity :as common-activity]
    [webchange.state.state-course :as state-course]
    [webchange.subs :as subs]))

(re-frame/reg-event-fx
  ::init
  (fn [{:keys [db]} [_]]
    (let [course-slug (subs/current-course db)]
      {:dispatch [::state-course/load-course-info course-slug]})))

(defn- scene-id->scene [scene-id scenes]
  (let [{:keys [id name preview]} (get scenes (-> scene-id str keyword))]
    {:id    id
     :name  name
     :image preview}))

(defn- activity-letter
  [db {:keys [level lesson] :as activity}]
  (if-let [lesson-set-name (get-in db [:course-data :levels level :lessons lesson :lesson-sets :concepts-single])]
    (let [item-id (-> (get-in db [:lessons lesson-set-name :item-ids])
                      first)
          letter (get-in db [:dataset-items item-id :data :letter])]
      (assoc activity :letter letter))
    activity))

(re-frame/reg-sub
  ::finished-activities
  (fn [db]
    (let [scenes (get-in db [:course-data :scene-list])
          activities (common-activity/flatten-activities (get-in db [:course-data :levels]))]
      (->> activities
           (filter #(lessons-activity/finished? db % activities))
           (map #(activity-letter db %))
           (map (fn [{:keys [activity level lesson letter scene-id] :as activity-data}]
                  (let [{:keys [name preview]} (get scenes (-> scene-id str keyword))]
                    {:id       (str level "-" lesson "-" activity)
                     :title    name
                     :activity activity-data
                     :letter   letter
                     :preview  preview})))
           (map-indexed (fn [idx item]
                          (assoc item :class-names {"position-top"    (-> idx odd?)
                                                    "position-bottom" (-> idx odd? not)})))))))

(re-frame/reg-sub
  ::new-activities
  (fn [db]
    (let [activities (common-activity/flatten-activities (get-in db [:course-data :levels]))]
      (filter #(lessons-activity/new? db % activities) activities))))

(defn get-next-activity
  [db]
  (let [scenes (get-in db [:course-data :scene-list])
        next (lessons-activity/get-progress-next db)
        {:keys [scene-id]} (-> (get-in db [:course-data :levels])
                               (get-in [(:level next) :lessons (:lesson next) :activities (:activity next)]))]
    (merge next (scene-id->scene scene-id scenes))))


(re-frame/reg-event-fx
  ::open-next-activity
  (fn [{:keys [db]} [_]]
    (let [next-activity (get-next-activity db)]
      {:dispatch [::open-activity next-activity]})))

(re-frame/reg-event-fx
  ::open-activity
  (fn [{:keys [db]} [_ activity]]
    (let [course (:current-course db)
          activity (select-keys activity [:level :lesson :activity :activity-name :new? :unique-id :scene-id])]
      {:db         (lessons-activity/add-loaded-activity db activity)
       :dispatch-n (list [::ie/set-current-scene (:scene-id activity)]
                         [::events/redirect (str "/courses/" course)])})))

(re-frame/reg-sub
  ::loading?
  (fn [db]
    (or (get-in db [:loading :load-course])
        (get-in db [:loading :load-progress]))))

(re-frame/reg-sub
  ::course-finished?
  (fn []
    (re-frame/subscribe [::state-course/course-finished?]))
  (fn [course-finished?]
    course-finished?))

(re-frame/reg-sub
  ::course-language
  (fn []
    (re-frame/subscribe [::state-course/course-info]))
  (fn [course-info]
    (get course-info :lang "english")))
