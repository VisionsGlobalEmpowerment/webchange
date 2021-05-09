(ns webchange.editor-v2.dialog.dialog-form.state.concepts
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.translator.translator-form.state.db :refer [path-to-db]]
    [webchange.editor-v2.translator.translator-form.state.concepts-utils :refer [get-concepts-audio-assets]]
    [webchange.editor-v2.translator.translator-form.state.concepts :as translator-form.concepts]
    [webchange.editor-v2.dialog.dialog-form.state.actions-utils :as actions]
    [webchange.editor-v2.dialog.dialog-form.state.concepts-utils :as utils]
    [webchange.editor-v2.translator.translator-form.state.actions-utils :as au]))

(defn current-dataset-concept
  [db]
  (get-in db (path-to-db [:current-dataset-concept])))

(def current-dataset-concept-path (path-to-db [:current-dataset-concept]))
(def current-dataset-concept-patch-path (path-to-db [:current-dataset-concept-patch]))

;; Subs

(re-frame/reg-event-fx
  ::add-concepts-schema-fields
  (fn [{:keys [db]} [_ data]]
    (let [dataset-concept (get-in db current-dataset-concept-path)
          fields (get-in dataset-concept [:scheme :fields])
          fields (concat fields [data])
          dataset-concept (assoc-in dataset-concept [:scheme :fields] fields)]
      {:db (-> db
               (assoc-in current-dataset-concept-path dataset-concept)
               (update-in (concat current-dataset-concept-patch-path [:add]) conj data))})))

(re-frame/reg-event-fx
  ::remove-concepts-schema-field
  (fn [{:keys [db]} [_ name]]
    (let [current-dataset-concept-path (path-to-db [:current-dataset-concept])
          dataset-concept (get-in db current-dataset-concept-path)
          fields (->> (get-in dataset-concept [:scheme :fields])
                      (filter (fn [field] (not= (:name field) name))))
          dataset-concept (assoc-in dataset-concept [:scheme :fields] fields)]
      {:db (-> db
               (assoc-in current-dataset-concept-path dataset-concept)
               (update-in (concat current-dataset-concept-patch-path [:remove]) conj name))})))

(re-frame/reg-event-fx
  ::remove-var-from-concepts
  (fn [{:keys [db]} [_ var-name]]
    (let [concepts (translator-form.concepts/concepts-data db)
          remove-from-concept (fn [id] [::translator-form.concepts/update-concept id [(keyword var-name)] nil])]
      {:dispatch-n (->> concepts
                        keys
                        (map remove-from-concept))})))

(re-frame/reg-event-fx
  ::add-new-phrase-in-concept-action
  (fn [{:keys [db]} [_ action node relative-position]]
    (let [{:keys [concept-action? base-action base-path target-position]} (actions/get-node-data node)
          data-patch (-> base-action
                         (au/insert-child-action action target-position relative-position)
                         (select-keys [:data]))]
      (if concept-action?
        {:dispatch-n (list [::translator-form.concepts/update-current-concept base-path data-patch])}))))

(re-frame/reg-event-fx
  ::delete-phrase-in-concept-action
  (fn [{:keys [db]} [_ node]]
    (let [{:keys [concept-action? parent-action base-path item-position base-action target-position]} (actions/get-node-data node)]
      (if (and (actions/node-parallel? parent-action) (not= item-position 0))
        (let [parallel-data (-> parent-action
                                (au/delete-child-action item-position))
              parallel-data (if (= (count (:data parallel-data)) 1) (first (:data parallel-data)) parallel-data)
              data-patch (-> base-action
                             (actions/replace-child parallel-data target-position)
                             (select-keys [:data]))]
          (if concept-action?
            {:dispatch-n (list [::translator-form.concepts/update-current-concept base-path data-patch])}))
        (let [data-patch (-> base-action
                             (au/delete-child-action target-position)
                             (select-keys [:data]))]
          (if concept-action?
            {:dispatch-n (list [::translator-form.concepts/update-current-concept base-path data-patch])}))))))

(re-frame/reg-event-fx
  ::add-in-concept-parallel-action
  (fn [{:keys [db]} [_ action node]]
    (let [
          {:keys [concept-action? parent-action base-path base-action target-position]} (actions/get-node-data node)
          list-to-path (if (actions/node-parallel? parent-action)
                         {:type "parallel",
                          :data (vec (concat (:data parent-action) [action]))}
                         {:type "parallel", :data [(:data node)
                                                   action]})
          data-patch (-> base-action
                         (actions/replace-child list-to-path target-position)
                         (select-keys [:data]))
          ]
      (if concept-action?
        {:dispatch-n (list [::translator-form.concepts/update-current-concept (actions/complete-path base-path) data-patch])}))))
