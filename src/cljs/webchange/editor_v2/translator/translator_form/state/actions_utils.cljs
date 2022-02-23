(ns webchange.editor-v2.translator.translator-form.state.actions-utils
  (:require
    [webchange.utils.list :as utils]))

(defn node->info
  [action-data]
  {:type (case (:source action-data)
           :concept :concept-action
           :scene :scene-action)
   :path (:path action-data)})

(defn get-node-data
  [node current-concept]
  (let [concept-action? (get-in node [:data :concept-action])
        target-path (get-in node [:path])
        target-position (last target-path)
        parent-path (drop-last target-path)]
    (let [parent-action (if concept-action?
                          (get-in current-concept (concat [:data] parent-path)))]
      {:concept-action? concept-action?
       :parent-action   parent-action
       :parent-path     parent-path
       :target-position target-position})))

(defn available-to-edit-actions?
  [node current-concept]
  (let [{:keys [parent-action]} (get-node-data node current-concept)
        parent-action-type (:type parent-action)]
    (or (= parent-action-type "sequence-data")
        (= parent-action-type "parallel"))))

(defn insert-child-action-at-index
  [parent-action child-action index]
  (let [position (cond
                   (number? index) index
                   (= index :first) 0
                   (= index :last) (-> parent-action (get :data) (count)))]
    (update-in parent-action [:data] utils/insert-at-position child-action position)))

(defn replace-child-action-at-index
  [parent-action child-action index]
  (let [position (cond
                   (number? index) index
                   (= index :first) 0
                   (= index :last) (-> parent-action (get :data) (count)))]
    (update-in parent-action [:data] utils/replace-at-position child-action position)))

(defn insert-child-action
  [parent-action child-action target-position relative-position]
  (let [position (case relative-position
                   :before target-position
                   :after (inc target-position))]
    (insert-child-action-at-index parent-action child-action position)))

(defn delete-child-action
  [parent-action child-position]
  (update-in parent-action [:data] utils/remove-at-position child-position))
