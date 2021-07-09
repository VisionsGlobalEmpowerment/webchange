(ns webchange.editor-v2.dialog.dialog-form.state.actions-utils
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.translator.translator-form.state.scene :as translator-form.scene]
    [webchange.editor-v2.translator.translator-form.state.concepts :as translator-form.concepts]
    [webchange.editor-v2.translator.translator-form.state.utils :refer [insert-by-index
                                                                        remove-by-index]]
    [webchange.utils.scene-action-data :as action-utils]))


(defn complete-path
  [path]
  (apply concat (map (fn [item] (if (number? item) [:data item] [item])) path)))

(defn node-parallel?
  [node]
  (= (get-in node [:type]) "parallel"))

(defn get-concept-node-data
  [node]
  (let [current-concept @(re-frame/subscribe [::translator-form.concepts/current-concept])
        concept-action? (get-in node [:data :concept-action])
        target-path (get-in node [:path])
        parent-path (drop-last target-path)
        parent-action (if concept-action?
                        (get-in current-concept (concat [:data] (complete-path parent-path))))
        target-position (if (node-parallel? parent-action) (last (drop-last target-path)) (last target-path))
        item-position (last target-path)
        base-path (if (node-parallel? parent-action) (drop-last parent-path) parent-path)
        base-action (if (node-parallel? parent-action) (get-in current-concept (concat [:data] (complete-path base-path))) parent-action)]

    {:concept-action? concept-action?
     :parent-action   parent-action
     :parent-path     parent-path
     :base-action     base-action
     :base-path       base-path
     :item-position   item-position
     :target-position target-position}))

(defn get-dialog-node-data
  [node]
  (let [concept-action? (get-in node [:data :concept-action])
        target-path (if (get-in node [:action-node-data :path]) (get-in node [:action-node-data :path]) (get-in node [:path]))
        parent-path (drop-last target-path)
        scene-data @(re-frame/subscribe [::translator-form.scene/scene-data])
        parent-action (get-in (:actions scene-data) (complete-path parent-path))
        base-path (if (node-parallel? parent-action) (drop-last parent-path) parent-path)
        target-position (if (node-parallel? parent-action) (last (drop-last target-path)) (last target-path))
        item-position (last target-path)
        base-action (if (node-parallel? parent-action) (get-in (:actions scene-data) (complete-path base-path)) parent-action)
        target-node (get-in (:actions scene-data) (complete-path target-path))
        parent-action (get-in (:actions scene-data) (complete-path parent-path))]
    {:concept-action? concept-action?
     :base-action     base-action
     :base-path       base-path
     :parent-action   parent-action
     :parent-path     parent-path
     :target-position target-position
     :item-position   item-position
     :target-node     target-node
     :target-path     target-path}))

(defn get-available-effects
  [node]
  (-> (get-dialog-node-data node)
      (get :base-action)
      (action-utils/get-available-effects)))

(defn delete-in-concept-available?
  [node-data]
  (let [{:keys [base-action item-position parent-action]} (get-concept-node-data node-data)
        items (count (:data base-action))]
    (not (and
           (or
             (and (node-parallel? parent-action) (= 0 item-position))
             (not (node-parallel? parent-action)))
           (= 1 items)))))

(defn get-node-data
  [node]
  (if (get-in node [:data :concept-action])
    (get-concept-node-data node)
    (get-dialog-node-data node)))

(defn replace-child
  [parent-action action position]
  (update-in parent-action [:data position] (fn [] action)))

(defn unique-var-name []
  (str "dialog-field-" (random-uuid)))
