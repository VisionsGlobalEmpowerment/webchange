(ns webchange.editor-v2.dialog.dialog-form.state.concepts-utils
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.translator.translator-form.state.db :refer [path-to-db]]
    [webchange.editor-v2.translator.translator-form.state.concepts-utils :refer [get-concepts-audio-assets]]
    [webchange.editor-v2.translator.translator-form.state.actions-shared :as actions]))

(defn- process-action
  [action]
  (if action
    (case (:type action)
      "action" (get-in action [:from-var 0 :var-property])
      "parallel" (map (fn [value] (process-action value)) (:data action))
      "sequence-data" (map (fn [value] (process-action value)) (:data action))
      []) []))

(defn- extract-concept-vars
  [actions]
  (map keyword (filter #(not-empty %) (flatten (map (fn [[name action]] (process-action action)) actions)))))

(defn get-scene-action-vars
  ([db]
   (->> (concat [:scene :data] [:actions])
        (path-to-db)
        (get-in db)
        (extract-concept-vars)))
  ([db action-path]
   (->> (concat [:scene :data] [:actions] action-path)
        (path-to-db)
        (get-in db)
        (process-action)
        (flatten)
        (filter not-empty))))

(re-frame/reg-sub
  ::actions-vars
  (fn [db]
    (let [current-dialog-action (actions/current-dialog-action-info db)
          actions-vars (get-scene-action-vars db (:path current-dialog-action))]
      actions-vars)))
