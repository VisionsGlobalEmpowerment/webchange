(ns webchange.editor-v2.dialog.dialog-text-form.state-actions
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.dialog.dialog-form.state.actions :as state-actions]
    [webchange.editor-v2.dialog.dialog-form.state.common :as state-actions-common]
    [webchange.editor-v2.text-animation-editor.state :as chunks]
    [webchange.editor-v2.translator.translator-form.state.actions :as translator-form.actions]))

(defn- pre_action-type [value] (some #{value} [:concept :scene]))
(defn- pre_node-data [value] (and (map? value)
                                  (vector? (:path value))
                                  (map? (:data value))))

;; Delay

(re-frame/reg-event-fx
  ::set-action-delay
  (fn [{:keys []} [_ {:keys [action-path action-type value]}]]
    {:pre [(pre_action-type action-type)]}
    {:dispatch [::state-actions/update-empty-action-by-path {:action-path action-path
                                                             :action-type action-type
                                                             :data-patch  {:duration (float value)}}]}))

;; Phrase

(re-frame/reg-event-fx
  ::set-phrase-text
  (fn [{:keys [_]} [_ {:keys [action-path action-type value]}]]
    {:pre [(pre_action-type action-type)]}
    {:dispatch [::state-actions/update-inner-action-by-path {:action-path action-path
                                                             :action-type action-type
                                                             :data-patch  {:phrase-text value}}]}))

(re-frame/reg-event-fx
  ::set-phrase-target
  (fn [{:keys [_]} [_ {:keys [action-path action-type value]}]]
    {:pre [(pre_action-type action-type)]}
    {:dispatch [::state-actions/update-inner-action-by-path {:action-path action-path
                                                             :action-type action-type
                                                             :data-patch  {:target value}}]}))

;; Actions

(re-frame/reg-event-fx
  ::remove-action
  (fn [{:keys [_]} [_ {:keys [node-data source]}]]
    {:pre [(pre_node-data node-data)]}
    {:dispatch [::state-actions-common/remove-action {:concept-action? (= source :concept)
                                                      :node-data       node-data}]}))

(re-frame/reg-event-fx
  ::add-scene-action
  (fn [{:keys [_]} [_ {:keys [node-data relative-position]}]]
    {:pre [(pre_node-data node-data)]}
    {:dispatch [::state-actions/add-new-empty-phrase-action {:node-data         node-data
                                                             :relative-position relative-position}]}))

(re-frame/reg-event-fx
  ::add-scene-parallel-action
  (fn [{:keys [_]} [_ {:keys [node-data]}]]
    {:pre [(pre_node-data node-data)]}
    {:dispatch [::state-actions/add-new-empty-phrase-parallel-action {:node-data node-data}]}))

(re-frame/reg-event-fx
  ::add-concept-action
  (fn [{:keys [_]} [_ {:keys [node-data relative-position]}]]
    {:pre [(pre_node-data node-data)]}
    {:dispatch [::state-actions/add-new-empty-phrase-concept-action {:node-data         node-data
                                                                     :relative-position relative-position}]}))

(re-frame/reg-event-fx
  ::add-effect-action
  (fn [{:keys [_]} [_ {:keys [effect node-data relative-position]}]]
    {:pre [(pre_node-data node-data)]}
    {:dispatch [::state-actions/add-effect-action {:node-data         node-data
                                                   :effect            effect
                                                   :relative-position relative-position}]}))

(re-frame/reg-event-fx
  ::open-text-animation-window
  (fn [{:keys [_]} [_ {:keys [node-data]}]]
    {:pre [(pre_node-data node-data)]}
    {:dispatch-n [[::translator-form.actions/set-current-phrase-action node-data]
                  [::chunks/open]]}))

(re-frame/reg-event-fx
  ::add-text-animation-action
  (fn [{:keys [_]} [_ {:keys [node-data relative-position]}]]
    {:pre [(pre_node-data node-data)]}
    {:dispatch [::state-actions/add-new-empty-text-animation-action {:node-data         node-data
                                                                     :relative-position relative-position}]}))
