(ns webchange.editor-v2.course-table.fields.tags.state
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
  [relative-path component-id]
  (->> relative-path
       (concat [:edit-from :tags component-id])
       (db/path-to-db)))

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
  ::init
  (fn [{:keys [db]} [_ selection component-id]]
    (let [course-data (subs/course-data db)
          activity-data (utils/get-activity-data course-data selection)
          tags-appointment (activity->tags-appointment activity-data)
          available-tags-restriction (course->available-tags-restriction course-data)
          selected-tags-restriction (activity->selected-tags-restriction activity-data)]
      {:dispatch-n (list [::reset-tags-appointment tags-appointment component-id]
                         [::reset-available-tags-restriction available-tags-restriction component-id]
                         [::reset-selected-restriction selected-tags-restriction component-id])})))

;; Tags appointment

(defn- tags-appointment
  [db component-id]
  (->> (get-in db (path-to-db [:appointment] component-id) {})
       (vals)
       (sort-by :tag)))

(re-frame/reg-sub
  ::tags-appointment
  (fn [db [_ component-id]]
    (tags-appointment db component-id)))

(re-frame/reg-event-fx
  ::add-tag-appointment
  (fn [{:keys [db]} [_ component-id]]
    (let [tag (->> (tags-appointment db component-id) (count) (str "new-tag-") (keyword))]
      {:db (assoc-in db (path-to-db [:appointment tag] component-id) {:tag        tag
                                                                      :score-low  0
                                                                      :score-high 100})})))

(re-frame/reg-event-fx
  ::edit-tag-appointment
  (fn [{:keys [db]} [_ tag tag-data component-id]]
    (if (some? (:tag tag-data))
      (let [new-tag (-> tag-data :tag ->kebab-case keyword)
            tag-data (-> (get-in db (path-to-db [:appointment tag] component-id))
                         (merge tag-data {:tag new-tag}))]
        {:db (-> db
                 (assoc-in (path-to-db [:appointment new-tag] component-id) tag-data)
                 (update-in (path-to-db [:appointment] component-id) dissoc tag))})
      {:db (update-in db (path-to-db [:appointment tag] component-id) merge tag-data)})))

(re-frame/reg-event-fx
  ::delete-tag-appointment
  (fn [{:keys [db]} [_ tag component-id]]
    {:db (update-in db (path-to-db [:appointment] component-id) dissoc tag)}))

(re-frame/reg-event-fx
  ::reset-tags-appointment
  (fn [{:keys [db]} [_ tags-appointment component-id]]
    {:db (assoc-in db (path-to-db [:appointment] component-id) tags-appointment)}))

;; Tags restriction

(re-frame/reg-event-fx
  ::add-restriction-tag
  (fn [{:keys [_]} [_ tag component-id]]
    (let [fixed-tag (-> tag ->kebab-case keyword)]
      {:dispatch-n (list [::add-available-tags-restriction fixed-tag component-id]
                         [::add-selected-restriction fixed-tag component-id])})))

; available

(re-frame/reg-sub
  ::available-tags-restriction
  (fn [db [_ component-id]]
    (get-in db (path-to-db [:restriction :available] component-id) [])))

(re-frame/reg-event-fx
  ::reset-available-tags-restriction
  (fn [{:keys [db]} [_ tags component-id]]
    {:db (assoc-in db (path-to-db [:restriction :available] component-id) tags)}))

(re-frame/reg-event-fx
  ::add-available-tags-restriction
  (fn [{:keys [db]} [_ tag component-id]]
    {:db (update-in db (path-to-db [:restriction :available] component-id) conj tag)}))

; selected

(defn- selected-tags-restriction
  [db component-id]
  (get-in db (path-to-db [:restriction :selected] component-id) {}))

(re-frame/reg-sub
  ::selected-tags-restriction
  (fn [db [_ component-id]]
    (selected-tags-restriction db component-id)))

(re-frame/reg-event-fx
  ::reset-selected-restriction
  (fn [{:keys [db]} [_ tags component-id]]
    {:db (assoc-in db (path-to-db [:restriction :selected] component-id) tags)}))

(re-frame/reg-event-fx
  ::add-selected-restriction
  (fn [{:keys [db]} [_ tag component-id]]
    {:db (assoc-in db (path-to-db [:restriction :selected tag] component-id) true)}))

(re-frame/reg-event-fx
  ::remove-selected-restriction
  (fn [{:keys [db]} [_ tag component-id]]
    {:db (update-in db (path-to-db [:restriction :selected] component-id) dissoc tag)}))

(re-frame/reg-sub
  ::tags-restriction
  (fn [[_ component-id]]
    [(re-frame/subscribe [::selected-tags-restriction component-id])])
  (fn [[selected-tags]]
    (keys selected-tags)))

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
  (fn [{:keys [db]} [_ component-id]]
    (let [course-id (data-state/course-id db)
          selection-data (-> db selection/selection :data)
          appointments (tags-appointment db component-id)
          restrictions (selected-tags-restriction db component-id)
          course-data (-> (subs/course-data db)
                          (update-tags-appointment appointments selection-data)
                          (update-tags-restriction restrictions selection-data))]
      {:dispatch [::common/update-course course-id course-data]})))
