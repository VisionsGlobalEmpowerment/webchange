(ns webchange.editor-v2.course-table.state.edit-tags
  (:require
    [camel-snake-kebab.core :refer [->kebab-case]]
    [re-frame.core :as re-frame]
    [webchange.editor-v2.course-table.state.data :as data-state]
    [webchange.editor-v2.course-table.state.db :as db]
    [webchange.editor-v2.course-table.state.selection :as selection]
    [webchange.interpreter.events :as events]
    [webchange.subs :as subs]
    [webchange.warehouse :as warehouse]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:edit-from :tags])
       (db/path-to-db)))

(defn- get-list-item
  [list field-name field-value]
  (some (fn [item]
          (and (= (get item field-name) field-value)
               item))
        list))

(defn- get-activity-data
  [course-data {:keys [level lesson lesson-idx]}]
  (-> course-data
      (:levels)
      (get-list-item :level level)
      (:lessons)
      (get-list-item :lesson lesson)
      (:activities)
      (nth lesson-idx)))

(defn- activity->tags-appointment
  [activity-data]
  (->> (:tags-by-score activity-data)
       (map (fn [[tag-name [score-low score-high]]]
              [tag-name {:tag        tag-name
                         :score-low  score-low
                         :score-high score-high}]))
       (into {})))

(re-frame/reg-event-fx
  ::init-tags
  (fn [{:keys [db]} [_ selection]]
    (let [activity-data (-> db (subs/course-data) (get-activity-data selection))
          tags-appointment (activity->tags-appointment activity-data)]
      {:dispatch-n (list [::reset-tags-appointment tags-appointment])})))


;; Tags appointment

(defn- tags-appointment
  [db]
  (->> (get-in db (path-to-db [:appointment]) {})
       (vals)
       (sort-by :tag)))

(re-frame/reg-sub ::tags-appointment tags-appointment)

(re-frame/reg-event-fx
  ::add-tag-appointment
  (fn [{:keys [db]} [_ {:keys [tag score-low score-high]
                        :or   {score-low  0
                               score-high 100}}]]
    (let [tag (if (some? tag) tag (->> (tags-appointment db) (count) (str "new-tag-") (keyword)))]
      {:db (assoc-in db (path-to-db [:appointment tag]) {:tag        tag
                                                         :score-low  score-low
                                                         :score-high score-high})})))

(re-frame/reg-event-fx
  ::edit-tag-appointment
  (fn [{:keys [db]} [_ tag tag-data]]
    (if-not (= tag (:tag tag-data))
      (let [new-tag (-> tag-data :tag ->kebab-case keyword)
            tag-data (-> (get-in db (path-to-db [:appointment tag]))
                         (merge tag-data {:tag new-tag}))]
        {:db (-> db
                 (assoc-in (path-to-db [:appointment new-tag]) tag-data)
                 (update-in (path-to-db [:appointment]) dissoc tag))})
      {:db (update-in db (path-to-db [:appointment tag]) merge tag-data)})))

(re-frame/reg-event-fx
  ::delete-tag-appointment
  (fn [{:keys [db]} [_ tag]]
    {:db (update-in db (path-to-db [:appointment]) dissoc tag)}))

(re-frame/reg-event-fx
  ::reset-tags-appointment
  (fn [{:keys [db]} [_ tags-appointment]]
    {:db (assoc-in db (path-to-db [:appointment]) tags-appointment)}))

;; Save

(defn- find-data-idx
  [list field value]
  (some (fn [[idx item-data]]
          (and (= (get item-data field) value)
               [idx item-data]))
        (map-indexed vector list)))

(defn- get-activity-path
  [course-data selection-data]
  (let [[level-idx level-data] (find-data-idx (:levels course-data) :level (:level selection-data))
        [lesson-idx _] (find-data-idx (:lessons level-data) :lesson (:lesson selection-data))]
    [:levels level-idx :lessons lesson-idx :activities (:lesson-idx selection-data)]))

(defn- update-tags-appointment
  [course-data appointments selection-data]
  (let [path (get-activity-path course-data selection-data)
        tags-data (->> appointments
                       (map (fn [{:keys [tag score-low score-high]}]
                              [tag [score-low score-high]]))
                       (into {}))]
    (assoc-in course-data (conj path :tags-by-score) tags-data)))

(re-frame/reg-event-fx
  ::save-tags
  (fn [{:keys [db]} [_]]
    (let [course-id (data-state/course-id db)
          selection-data (-> db selection/selection :data)
          appointments (tags-appointment db)
          course-data (-> (subs/course-data db)
                          (update-tags-appointment appointments selection-data))]
      {:dispatch [::warehouse/save-course
                  {:course-id   course-id
                   :course-data course-data}
                  {:on-success [::save-tags-success]}]})))

(re-frame/reg-event-fx
  ::save-tags-success
  (fn [{:keys [_]} [_ {:keys [data]}]]
    {:dispatch [::events/set-course-data data]}))
