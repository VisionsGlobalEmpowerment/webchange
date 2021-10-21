(ns webchange.editor-v2.activity-form.generic.components.add-character.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.components.character-form.data :refer [animation->character-data]]
    [webchange.state.state-activity :as state-activity]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:editor-v2 :add-character-modal])))

;; Modal window

(def modal-state-path (path-to-db [:modal-state]))

(re-frame/reg-event-fx
  ::open
  (fn [{:keys [db]} [_]]
    {:db (assoc-in db modal-state-path true)}))

(re-frame/reg-event-fx
  ::close
  (fn [{:keys [db]} [_]]
    {:db (assoc-in db modal-state-path false)}))

(re-frame/reg-sub
  ::open?
  (fn [db]
    (get-in db modal-state-path false)))

;; Character

(def current-character-path (path-to-db [:current-skeleton]))

(defn- get-current-character
  [db]
  (get-in db current-character-path))

(re-frame/reg-sub
  ::current-character-name
  (fn [db]
    (-> (get-in db current-character-path)
        (animation->character-data)
        (get :character))))

(re-frame/reg-event-fx
  ::set-current-character
  (fn [{:keys [db]} [_ {:keys [name skin-params]}]]
    {:db       (assoc-in db current-character-path (cond-> {:name name}
                                                           (string? skin-params) (assoc :skin skin-params)
                                                           (map? skin-params) (assoc :skin-names skin-params)))}))

;; Save

(re-frame/reg-event-fx
  ::add-character
  (fn [{:keys [db]} [_]]
    (let [action-data (get-current-character db)]
      {:dispatch [::state-activity/call-activity-common-action
                  {:action :add-character
                   :data   action-data}
                  {:on-success [::close]}]})))
