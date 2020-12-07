(ns webchange.editor-v2.course-table.state.edit-skills
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.course-table.state.db :as db]
    [webchange.editor.events :as editor]
    [webchange.editor-v2.course-table.state.data :as data-state]
    [webchange.editor-v2.course-table.state.selection :as selection-state]
    [webchange.warehouse :as warehouse]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:edit-from :skills])
       (db/path-to-db)))

;; Available skills

(re-frame/reg-event-fx
  ::init-skills
  (fn [{:keys [db]} [_ {:keys [activity]}]]
    (let [scene-skills (->> (editor/scene-skills db activity)
                            (map (fn [{:keys [id]}] [id true]))
                            (into {}))]
      {:dispatch-n (list [::warehouse/load-skills {:on-success [::load-skills-success]}]
                         [::reset-selected-skills scene-skills])})))

(re-frame/reg-event-fx
  ::load-skills-success
  (fn [{:keys [db]} [_ {:keys [skills]}]]
    {:db (assoc-in db (path-to-db [:data :skills]) skills)}))

(re-frame/reg-sub
  ::skills
  (fn [db]
    (get-in db (path-to-db [:data :skills]))))

;; Selected skills

(defn- selected-skills
  [db]
  (get-in db (path-to-db [:data :selected-skills]) {}))

(re-frame/reg-sub ::selected-skills selected-skills)

(re-frame/reg-event-fx
  ::reset-selected-skills
  (fn [{:keys [db]} [_ init-skills]]
    {:db (assoc-in db (path-to-db [:data :selected-skills]) init-skills)}))

(re-frame/reg-event-fx
  ::add-selected-skill
  (fn [{:keys [db]} [_ skill-id]]
    {:db (assoc-in db (path-to-db [:data :selected-skills skill-id]) true)}))

(re-frame/reg-event-fx
  ::remove-selected-skill
  (fn [{:keys [db]} [_ skill-id]]
    {:db (update-in db (path-to-db [:data :selected-skills]) dissoc skill-id)}))

;; Skills list

(re-frame/reg-sub
  ::skills-list
  (fn []
    [(re-frame/subscribe [::skills])
     (re-frame/subscribe [::selected-skills])])
  (fn [[skills selected-skills]]
    (map (fn [{:keys [id] :as skill}]
           (assoc skill :selected? (contains? selected-skills id)))
         skills)))

;; Save

(re-frame/reg-event-fx
  ::save-skills
  (fn [{:keys [db]} [_]]
    (let [skills-ids (-> db (selected-skills) (keys))
          scene-id (-> db (selection-state/selection) (get-in [:data :activity]))
          course-id (data-state/course-id db)]
      {:dispatch [::warehouse/update-scene-skills
                  {:course-id  course-id
                   :scene-id   scene-id
                   :skills-ids skills-ids}
                  {:on-success [::save-skills-success]}]})))

(re-frame/reg-event-fx
  ::save-skills-success
  (fn [{:keys [_]} [_ {:keys [scene skills]}]]
    {:dispatch [::editor/reset-scene-skills scene skills]}))
