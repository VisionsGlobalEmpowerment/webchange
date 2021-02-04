(ns webchange.editor-v2.course-table.fields.skills.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.course-data-utils.utils :as utils]
    [webchange.editor-v2.course-table.state.db :as db]
    [webchange.editor.events :as editor]
    [webchange.editor-v2.course-table.state.data :as data-state]
    [webchange.editor-v2.course-table.state.selection :as selection]
    [webchange.subs :as subs]
    [webchange.state.warehouse :as warehouse]))

(defn path-to-db
  [relative-path component-id]
  (->> relative-path
       (concat [:edit-from :skills component-id])
       (db/path-to-db)))

(re-frame/reg-event-fx
  ::init
  (fn [{:keys [db]} [_ {:keys [activity]} component-id]]
    (let [scene-skills (-> (subs/scene-skills db)
                           (get activity))
          prepared-skills (->> scene-skills
                               (map (fn [{:keys [id]}] [id true]))
                               (into {}))
          selection-data (selection/selection db)]
      {:db         (-> db
                       (assoc-in (path-to-db [:initial-value] component-id) (keys prepared-skills))
                       (assoc-in (path-to-db [:selection-data] component-id) selection-data))
       :dispatch-n (list [::warehouse/load-skills {:on-success [::load-skills-success component-id]}]
                         [::reset-selected-skills prepared-skills component-id])})))

(re-frame/reg-event-fx
  ::load-skills-success
  (fn [{:keys [db]} [_ component-id {:keys [skills]}]]
    {:db (assoc-in db (path-to-db [:data :skills] component-id) (->> skills
                                                                     (map (fn [{:keys [id] :as skill}] [id skill]))
                                                                     (into {})))}))

(re-frame/reg-sub
  ::skills
  (fn [db [_ component-id]]
    (->> (path-to-db [:data :skills] component-id)
         (get-in db))))

;; Selected skills

(defn- selected-skills
  [db component-id]
  (get-in db (path-to-db [:data :selected-skills] component-id) {}))

(re-frame/reg-sub
  ::selected-skills
  (fn [db [_ component-id]]
    (->> (selected-skills db component-id)
         (keys))))

(re-frame/reg-sub
  ::current-skills
  (fn [[_ component-id]]
    [(re-frame/subscribe [::skills component-id])
     (re-frame/subscribe [::selected-skills component-id])])
  (fn [[all-skills selected-skills-ids]]
    (->> selected-skills-ids
         (map (fn [skill-id] (get all-skills skill-id)))
         (remove nil?))))

(re-frame/reg-event-fx
  ::reset-selected-skills
  (fn [{:keys [db]} [_ init-skills component-id]]
    {:db (assoc-in db (path-to-db [:data :selected-skills] component-id) init-skills)}))

(re-frame/reg-event-fx
  ::add-selected-skill
  (fn [{:keys [db]} [_ skill-id component-id]]
    {:db (assoc-in db (path-to-db [:data :selected-skills skill-id] component-id) true)}))

(re-frame/reg-event-fx
  ::remove-selected-skill
  (fn [{:keys [db]} [_ skill-id component-id]]
    {:db (update-in db (path-to-db [:data :selected-skills] component-id) dissoc skill-id)}))

;; Skills list

(re-frame/reg-sub
  ::skills-list
  (fn [[_ component-id]]
    [(re-frame/subscribe [::skills component-id])])
  (fn [[skills]]
    (->> skills
         (vals)
         (sort-by :name))))

;; Save

(defn- get-scene-id
  [selection course-data]
  (let [level (some (fn [{:keys [level] :as level-data}] (and (= level (:level selection)) level-data))
                    (:levels course-data))
        lesson (some (fn [{:keys [lesson] :as lesson-data}] (and (= lesson (:lesson selection)) lesson-data))
                     (:lessons level))
        activity (nth (:activities lesson) (:lesson-idx selection))]
    (:activity activity)))

(re-frame/reg-event-fx
  ::save
  (fn [{:keys [db]} [_ component-id]]
    (let [initial-value (get-in db (path-to-db [:initial-value] component-id))
          current-value (-> (selected-skills db component-id) (keys))]
      (if-not (= initial-value current-value)
        (let [selection-data (get-in db (path-to-db [:selection-data] component-id))
              scene-id (-> (subs/course-data db)
                           (utils/get-activity-name selection-data))
              course-id (data-state/course-id db)]
          {:dispatch [::warehouse/update-scene-skills
                      {:course-id  course-id
                       :scene-id   scene-id
                       :skills-ids current-value}
                      {:on-success [::save-skills-success component-id]}]})
        {}))))

(re-frame/reg-event-fx
  ::save-skills-success
  (fn [{:keys [_]} [_ component-id {:keys [scene skills]}]]
    (let [selected-skills-map (->> skills
                                   (map (fn [{:keys [id]}] [id true]))
                                   (into {}))]
      {:dispatch-n (list [::editor/reset-scene-skills scene skills]
                         [::reset-selected-skills selected-skills-map component-id])})))
