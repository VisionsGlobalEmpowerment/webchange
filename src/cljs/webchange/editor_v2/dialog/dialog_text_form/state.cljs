(ns webchange.editor-v2.dialog.dialog-text-form.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.dialog.dialog-form.state.actions :as state-actions]
    [webchange.editor-v2.dialog.dialog-text-form.prepare-phrase-actions :refer [prepare-phrase-actions]]
    [webchange.editor-v2.dialog.state :as parent-state]
    [webchange.editor-v2.translator.translator-form.state.actions :as translator-form.actions]
    [webchange.editor-v2.translator.translator-form.state.concepts :as translator-form.concepts]
    [webchange.editor-v2.translator.translator-form.state.scene :as translator-form.scene]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:core])
       (parent-state/path-to-db)))

(re-frame/reg-sub
  ::phrase-actions
  (fn []
    [(re-frame/subscribe [::translator-form.actions/current-dialog-action-info])
     (re-frame/subscribe [::translator-form.concepts/current-concept])
     (re-frame/subscribe [::translator-form.scene/scene-data])])
  (fn [[current-dialog-action current-concept scene-data]]
    (prepare-phrase-actions {:dialog-action-path (:path current-dialog-action)
                             :concept-data       current-concept
                             :scene-data         scene-data})))

;; Concepts

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
                  [::state-actions/update-inner-action-by-path {:action-path action-path
                                                                :action-type action-type
                                                                :data-patch  {:target target}}]]}))

;; Text

(re-frame/reg-event-fx
  ::set-phrase-action-text
  (fn [{:keys [_]} [_ action-path action-type text]]
    {:dispatch [::state-actions/update-inner-action-by-path {:action-path action-path
                                                             :action-type action-type
                                                             :data-patch  {:phrase-text text}}]}))

;; Actions

(re-frame/reg-event-fx
  ::remove-action
  (fn [{:keys [db]} [_ action-data]]
    (print "::remove-action" action-data)))

(re-frame/reg-event-fx
  ::add-scene-action
  (fn [{:keys [db]} [_ action-data]]
    (print "::add-scene-action" action-data)))

(re-frame/reg-event-fx
  ::add-concepts-action
  (fn [{:keys [db]} [_ action-data]]
    (print "::add-concepts-action" action-data)))