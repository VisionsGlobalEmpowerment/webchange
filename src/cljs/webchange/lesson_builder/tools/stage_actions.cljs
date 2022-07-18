(ns webchange.lesson-builder.tools.stage-actions
  (:require
    [clojure.spec.alpha :as s]
    [re-frame.core :as re-frame]
    [webchange.interpreter.renderer.state.scene :as state-renderer]
    [webchange.lesson-builder.state :as state]
    [webchange.lesson-builder.tools.stage-actions-spec :as spec]
    [webchange.utils.scene-action-data :as action-utils]
    [webchange.utils.scene-data :as utils]))

(re-frame/reg-event-fx
  ::change-background
  [(re-frame/inject-cofx :activity-data)]
  (fn [{:keys [activity-data]} [_ background-data]]
    {:pre [(s/valid? ::spec/background-data background-data)]}
    (let [[name] (utils/get-scene-background activity-data)
          updated-activity-data (update-in activity-data [:objects name] merge background-data)]
      ;; ToDo: update stage: change background
      ;; {:dispatch [::state-renderer/set-scene-object-state name background-data]}
      {:dispatch [::state/set-activity-data updated-activity-data]})))

(re-frame/reg-event-fx
  ::set-action-target
  [(re-frame/inject-cofx :activity-data)]
  (fn [{:keys [activity-data]} [_ {:keys [action-path target]}]]
    {:pre [(s/valid? ::spec/action-path action-path)
           (s/valid? ::spec/action-target target)]}
    (let [update-path (concat [:actions] action-path action-utils/inner-action-path [:target])
          updated-activity-data (assoc-in activity-data update-path target)]
      {:dispatch [::state/set-activity-data updated-activity-data]})))

(re-frame/reg-event-fx
  ::set-action-phrase-text
  [(re-frame/inject-cofx :activity-data)]
  (fn [{:keys [activity-data]} [_ {:keys [action-path phrase-text]}]]
    {:pre [(s/valid? ::spec/action-path action-path)
           (s/valid? ::spec/action-target phrase-text)]}
    (let [update-path (concat [:actions] action-path action-utils/inner-action-path [:phrase-text])
          updated-activity-data (assoc-in activity-data update-path phrase-text)]
      {:dispatch [::state/set-activity-data updated-activity-data]})))

(re-frame/reg-event-fx
  ::set-object-text
  [(re-frame/inject-cofx :activity-data)]
  (fn [{:keys [activity-data]} [_ {:keys [object-name text]}]]
    {:pre [(s/valid? ::spec/object-name object-name)
           (s/valid? ::spec/text text)]}
    (let [update-path [:objects (keyword object-name) :text]
          updated-activity-data (assoc-in activity-data update-path text)]
      {:dispatch [::state/set-activity-data updated-activity-data]})))
