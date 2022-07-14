(ns webchange.lesson-builder.tools.stage-actions
  (:require
    [re-frame.core :as re-frame]
    [webchange.interpreter.renderer.state.scene :as state-renderer]
    [webchange.lesson-builder.state :as state]
    [webchange.utils.scene-data :as utils]))

(defn- get-activity-data
  [db]
  (state/get-activity-data db state/path-to-db))

(re-frame/reg-event-fx
  ::change-background
  [(re-frame/inject-cofx :activity-data)]
  (fn [{:keys [activity-data]} [_ background-data]]
    (let [[name] (utils/get-scene-background activity-data)]
      ;; ToDo: update stage state: background
      ;; {:dispatch [::state-renderer/set-scene-object-state name background-data]}
      (print ">" background-data)
      {:dispatch [::state/update-activity-object-data {:object-name name
                                                       :object-data background-data}]})))
