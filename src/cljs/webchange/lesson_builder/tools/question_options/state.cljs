(ns webchange.lesson-builder.tools.question-options.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.lesson-builder.blocks.state :as layout-state]
    [webchange.lesson-builder.state :as state]
    [webchange.lesson-builder.stage-actions :as stage-actions]
    [webchange.lesson-builder.widgets.confirm.state :as confirm-state]
    [webchange.utils.scene-data :as utils]))

(def path-to-db :lesson-builder/question-options)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

;;

(re-frame/reg-sub
  ::question-options
  :<- [::state/activity-data]
  (fn [activity-data]
    (->> (utils/get-available-effects activity-data)
         (filter #(= (:type %) "question"))
         (map #(select-keys % [:name :action])))))

(re-frame/reg-event-fx
  ::add-question
  (fn [{:keys [_]} [_]]
    {:dispatch [::layout-state/open-tool :question-form]}))

(re-frame/reg-event-fx
  ::edit-question
  (fn [{:keys [_]} [_ action]]
    {:dispatch [::layout-state/open-tool :question-form {:question-id action}]}))

;; remove

(def removing-key :removing)

(defn- get-removing
  [db id]
  (get-in db [id removing-key] false))

(defn- set-removing
  [db id value]
  (assoc-in db [id removing-key] value))

(re-frame/reg-sub
  ::removing?
  :<- [path-to-db]
  (fn [db [_ id]]
    (get-removing db id)))

(re-frame/reg-event-fx
  ::remove-question
  (fn [{:keys [_]} [_ action]]
    {:dispatch [::confirm-state/show-confirm-window {:title      "Are you sure you want to delete this question?"
                                                     :on-confirm [::remove-question-confirm action]}]}))

(re-frame/reg-event-fx
  ::remove-question-confirm
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ action]]
    {:db       (set-removing db action true)
     :dispatch [::stage-actions/call-activity-action
                {:action         :remove-question
                 :data           {:name action}
                 :common-action? true}
                {:on-success [::remove-question-success action]
                 :on-failure [::remove-question-failure action]}]}))

(re-frame/reg-event-fx
  ::remove-question-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ action]]
    {:db (set-removing db action false)}))

(re-frame/reg-event-fx
  ::remove-question-failure
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ action]]
    {:db (set-removing db action false)}))
