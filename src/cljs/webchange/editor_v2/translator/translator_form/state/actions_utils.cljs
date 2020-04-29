(ns webchange.editor-v2.translator.translator-form.state.actions-utils)

(defn node->info
  [action-node]
  (let [concept-action? (get-in action-node [:data :concept-action])]
    {:type (if concept-action?
             :concept-action
             :scene-action)
     :path (:path action-node)}))
