(ns webchange.editor-v2.course-table.state.edit-tags
  (:require
    [camel-snake-kebab.core :refer [->kebab-case]]
    [re-frame.core :as re-frame]
    [webchange.editor-v2.course-table.state.data :as data-state]
    [webchange.editor-v2.course-table.state.db :as db]
    [webchange.editor-v2.course-table.state.edit-common :as common]
    [webchange.editor-v2.course-table.state.edit-utils :as utils]
    [webchange.editor-v2.course-table.state.selection :as selection]
    [webchange.subs :as subs]))

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

(defn- course->available-tags-restriction
  [course-data]
  (->> (:levels course-data)
       (map :lessons)
       (flatten)
       (map :activities)
       (flatten)
       (map :tags-by-score)
       (remove nil?)
       (map keys)
       (flatten)
       (distinct)
       (sort)))

(defn- activity->selected-tags-restriction
  [activity-data]
  (->> (get activity-data :only [])
       (map keyword)
       (map (fn [tag] [tag true]))
       (into {})))

(re-frame/reg-event-fx
  ::init-tags
  (fn [{:keys [db]} [_ selection]]
    (let [course-data (subs/course-data db)
          activity-data (get-activity-data course-data selection)
          tags-appointment (activity->tags-appointment activity-data)
          available-tags-restriction (course->available-tags-restriction course-data)
          selected-tags-restriction (activity->selected-tags-restriction activity-data)]
      {:dispatch-n (list [::reset-tags-appointment tags-appointment]
                         [::reset-available-tags-restriction available-tags-restriction]
                         [::reset-selected-restriction selected-tags-restriction])})))

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

;; Tags restriction

(re-frame/reg-event-fx
  ::add-restriction-tag
  (fn [{:keys [_]} [_ tag]]
    (let [fixed-tag (-> tag ->kebab-case keyword)]
      {:dispatch-n (list [::add-available-tags-restriction fixed-tag]
                         [::add-selected-restriction fixed-tag])})))

; available

(re-frame/reg-sub
  ::available-tags-restriction
  (fn [db]
    (get-in db (path-to-db [:restriction :available]) [])))

(re-frame/reg-event-fx
  ::reset-available-tags-restriction
  (fn [{:keys [db]} [_ tags]]
    {:db (assoc-in db (path-to-db [:restriction :available]) tags)}))

(re-frame/reg-event-fx
  ::add-available-tags-restriction
  (fn [{:keys [db]} [_ tag]]
    {:db (update-in db (path-to-db [:restriction :available]) conj tag)}))

; selected

(defn- selected-tags-restriction
  [db]
  (get-in db (path-to-db [:restriction :selected]) {}))

(re-frame/reg-sub ::selected-tags-restriction selected-tags-restriction)

(re-frame/reg-event-fx
  ::reset-selected-restriction
  (fn [{:keys [db]} [_ tags]]
    {:db (assoc-in db (path-to-db [:restriction :selected]) tags)}))

(re-frame/reg-event-fx
  ::add-selected-restriction
  (fn [{:keys [db]} [_ tag]]
    {:db (assoc-in db (path-to-db [:restriction :selected tag]) true)}))

(re-frame/reg-event-fx
  ::remove-selected-restriction
  (fn [{:keys [db]} [_ tag]]
    {:db (update-in db (path-to-db [:restriction :selected]) dissoc tag)}))

(re-frame/reg-sub
  ::tags-restriction
  (fn []
    [(re-frame/subscribe [::available-tags-restriction])
     (re-frame/subscribe [::selected-tags-restriction])])
  (fn [[available-tags selected-tags]]
    (map (fn [tag]
           {:tag       tag
            :selected? (contains? selected-tags tag)})
         available-tags)))

;; Save

(defn- update-tags-appointment
  [course-data appointments selection-data]
  (let [path (utils/get-activity-path course-data selection-data)
        tags-data (->> appointments
                       (map (fn [{:keys [tag score-low score-high]}]
                              [tag [score-low score-high]]))
                       (into {}))]
    (assoc-in course-data (conj path :tags-by-score) tags-data)))

(defn- update-tags-restriction
  [course-data restrictions selection-data]
  (let [path (utils/get-activity-path course-data selection-data)
        tags-data (keys restrictions)]
    (assoc-in course-data (conj path :only) tags-data)))

(re-frame/reg-event-fx
  ::save-tags
  (fn [{:keys [db]} [_]]
    (let [course-id (data-state/course-id db)
          selection-data (-> db selection/selection :data)
          appointments (tags-appointment db)
          restrictions (selected-tags-restriction db)
          course-data (-> (subs/course-data db)
                          (update-tags-appointment appointments selection-data)
                          (update-tags-restriction restrictions selection-data))]
      {:dispatch [::common/update-course course-id course-data]})))
