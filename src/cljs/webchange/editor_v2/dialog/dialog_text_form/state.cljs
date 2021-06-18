(ns webchange.editor-v2.dialog.dialog-text-form.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.dialog.dialog-text-form.prepare-phrase-actions :refer [prepare-phrase-actions]]
    [webchange.editor-v2.dialog.dialog-text-form.state-actions :as state-actions]
    [webchange.editor-v2.dialog.state :as parent-state]
    [webchange.editor-v2.translator.translator-form.state.actions :as translator-form.actions]
    [webchange.editor-v2.translator.translator-form.state.concepts :as translator-form.concepts]
    [webchange.editor-v2.translator.translator-form.state.scene :as translator-form.scene]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:dialog-text-form])
       (parent-state/path-to-db)))

(defn- scene-available-actions
  [scene-data]
  (as-> scene-data x
        (get-in x [:metadata :available-actions])
        (map :action x)))

(re-frame/reg-sub
  ::phrase-actions
  (fn []
    [(re-frame/subscribe [::translator-form.actions/current-dialog-action-info])
     (re-frame/subscribe [::translator-form.actions/current-dialog-action-data])
     (re-frame/subscribe [::translator-form.concepts/current-concept])
     (re-frame/subscribe [::translator-form.scene/scene-data])])
  (fn [[current-dialog-action {:keys [available-activities]} current-concept scene-data]]
    (let [available-actions (concat available-activities (scene-available-actions scene-data))]
      (prepare-phrase-actions {:dialog-action-path (:path current-dialog-action)
                               :concept-data       current-concept
                               :scene-data         scene-data
                               :available-effects  available-actions}))))

(re-frame/reg-sub
  ::scene-available-actions
  (fn []
    [(re-frame/subscribe [::translator-form.scene/scene-data])])
  (fn [[scene-data]]
    (scene-available-actions scene-data)))

;; Concepts

(re-frame/reg-sub
  ::show-concepts?
  (fn []
    (re-frame/subscribe [::translator-form.concepts/has-concepts?]))
  (fn [has-concepts?]
    has-concepts?))

(re-frame/reg-sub
  ::current-concept
  (fn []
    [(re-frame/subscribe [::translator-form.concepts/current-concept])])
  (fn [[current-concept]]
    (get current-concept :id "")))

(re-frame/reg-sub
  ::concepts-options
  (fn []
    [(re-frame/subscribe [::translator-form.concepts/concepts-list])])
  (fn [[concepts]]
    (map (fn [{:keys [id name]}]
           {:text  name
            :value id})
         concepts)))

(re-frame/reg-event-fx
  ::set-current-concept
  (fn [{:keys [_]} [_ concept-id]]
    {:dispatch [::translator-form.concepts/set-current-concept concept-id]}))

;; Target

(re-frame/reg-sub
  ::available-targets
  (fn [_]
    [(re-frame/subscribe [::translator-form.scene/available-animation-targets])])
  (fn [[targets]]
    targets))

(def current-target-path :current-target)

(re-frame/reg-sub
  ::current-target
  (fn [db [_ action-path]]
    (get-in db (path-to-db [current-target-path action-path]))))

(re-frame/reg-event-fx
  ::set-current-target
  (fn [{:keys [db]} [_ action-path target]]
    {:db (assoc-in db (path-to-db [current-target-path action-path]) target)}))

(re-frame/reg-event-fx
  ::set-phrase-action-target
  (fn [{:keys [_]} [_ action-path action-type target]]
    {:dispatch-n [[::set-current-target action-path target]
                  [::state-actions/set-phrase-target {:action-path action-path
                                                      :action-type action-type
                                                      :value       target}]]}))
