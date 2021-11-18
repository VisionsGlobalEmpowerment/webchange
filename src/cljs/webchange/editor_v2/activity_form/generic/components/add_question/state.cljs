(ns webchange.editor-v2.activity-form.generic.components.add-question.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.activity-form.common.interpreter-stage.state :as state-stage]
    [webchange.state.state-activity :as state-activity]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:editor-v2 :add-question-modal])))

;; Modal window

(def modal-state-path (path-to-db [:modal-state]))

(re-frame/reg-event-fx
  ::open
  (fn [{:keys [db]} [_]]
    {:db (assoc-in db modal-state-path true)}))

(re-frame/reg-event-fx
  ::close
  (fn [{:keys [db]} [_]]
    {:db       (assoc-in db modal-state-path false)
     :dispatch [::state-stage/reset-stage]}))

(re-frame/reg-sub
  ::open?
  (fn [db]
    (get-in db modal-state-path false)))


;; Save

(re-frame/reg-event-fx
  ::add-question
  (fn [{:keys [_]} [_ {:keys [data]}]]
    {:dispatch [::state-activity/call-activity-common-action
                {:action :add-question
                 :data   {:question-page-object data}}
                {:on-success [::close]}]}))
