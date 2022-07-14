(ns webchange.lesson-builder.tools.stage-actions
  (:require
    [clojure.spec.alpha :as s]
    [re-frame.core :as re-frame]
    [webchange.interpreter.renderer.state.scene :as state-renderer]
    [webchange.lesson-builder.state :as state]
    [webchange.lesson-builder.tools.stage-actions-spec :as spec]
    [webchange.utils.scene-data :as utils]))

(re-frame/reg-event-fx
  ::change-background
  [(re-frame/inject-cofx :activity-data)]
  (fn [{:keys [activity-data]} [_ background-data]]
    {:pre [(s/valid? ::spec/background-data background-data)]}
    (let [[name] (utils/get-scene-background activity-data)]
      ;; ToDo: update stage state: background
      ;; {:dispatch [::state-renderer/set-scene-object-state name background-data]}
      {:dispatch [::state/update-activity-object-data {:object-name name
                                                       :object-data background-data}]})))
