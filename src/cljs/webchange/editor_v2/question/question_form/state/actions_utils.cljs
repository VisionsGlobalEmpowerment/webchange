(ns webchange.editor-v2.question.question-form.state.actions-utils
  (:require
    [webchange.editor-v2.translator.translator-form.state.utils :refer [insert-by-index
                                                                        remove-by-index]]))

(defn node->info
  [action-node]
  (let []
    {:type :question
     :path (:path action-node)
     :question-path (get-in action-node [:data :question-path])
     }))

(defn node-path->action-path
  [path]
  (->> path
       (map (fn [path-step]
              (if (number? path-step)
                [:data path-step]
                path-step)))
       (flatten)))
;
;(defn get-node-data
;  [node current-concept]
;  (let [concept-action? (get-in node [:data :concept-action])
;        target-path (get-in node [:path])
;        target-position (last target-path)
;        parent-path (drop-last target-path)]
;    (let [parent-action (if concept-action?
;                          (get-in current-concept (concat [:data] parent-path)))]
;      {:concept-action? concept-action?
;       :parent-action   parent-action
;       :parent-path     parent-path
;       :target-position target-position})))
;
;(defn available-to-edit-actions?
;  [node current-concept]
;  (let [{:keys [parent-action]} (get-node-data node current-concept)
;        parent-action-type (:type parent-action)]
;    (or (= parent-action-type "sequence-data")
;        (= parent-action-type "parallel"))))
;
;(defn insert-child-action
;  [parent-action child-action target-position relative-position]
;  (let [position (case relative-position
;                   :before target-position
;                   :after (inc target-position))]
;    (update-in parent-action [:data] insert-by-index position child-action)))
;
;(defn delete-child-action
;  [parent-action child-position]
;  (update-in parent-action [:data] remove-by-index child-position))
