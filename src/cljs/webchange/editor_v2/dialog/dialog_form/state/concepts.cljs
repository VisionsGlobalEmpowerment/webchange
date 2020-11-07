(ns webchange.editor-v2.dialog.dialog-form.state.concepts
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.translator.translator-form.state.db :refer [path-to-db]]
    [webchange.editor-v2.translator.translator-form.state.concepts-utils :refer [get-concepts-audio-assets]]
        [webchange.editor-v2.translator.translator-form.state.concepts :as translator-form.concepts]
    [webchange.editor-v2.dialog.dialog-form.state.actions-utils :as actions]
    [webchange.editor-v2.translator.translator-form.state.actions-utils :as au]))

(defn current-concept
  [db]
  (let [current-concept-id (get-in db (path-to-db [:concepts :current-concept]))]
    (get-in db (path-to-db [:concepts :data current-concept-id]))))

(defn current-dataset-concept
  [db]
  (get-in db (path-to-db [:current-dataset-concept])))

(defn process-action
  [action]
  (if action
    (case (:type action)
      "action" (get-in action [:from-var 0 :var-property])
      "parallel" (map (fn [value] (process-action value)) (:data action) )
      "sequence-data" (map (fn [value] (process-action value)) (:data action))
      []) []))

(defn exctract-concept-vars [actions]
  (map keyword (filter #(not-empty %) (flatten (map (fn [[name action]] (process-action action)) actions)))))

(defn get-schema-template-by-name [db var-name]
  (first (filter (fn [field] (= (keyword (:name field)) var-name))  (get-in (current-dataset-concept db) [:scheme :fields]))))



(defn get-scene-action-vars
  [db]
  (exctract-concept-vars (get-in db (path-to-db (concat [:scene :data] [:actions])))))

;; Subs
(re-frame/reg-sub
  ::incomplete-concepts
  (fn [db]
    (let [concepts (vals (get-in db (path-to-db [:concepts :data] )))
          actions-vars (get-scene-action-vars db)
          incomplete (map (fn [concept]
                            (if (not= 0 (count (clojure.set/difference (set actions-vars) (set (keys (:data concept))))))
                              (:name concept)
                              )
                            ) concepts)
          ]
      (filter not-empty incomplete)
      )))

(re-frame/reg-event-fx
  ::add-concepts-schema-fields
  (fn [{:keys [db]} [_ data]]
    (let [
          current-dataset-concept-path (path-to-db [:current-dataset-concept])
          dataset-concept (get-in db current-dataset-concept-path)
          fields (get-in dataset-concept [:scheme :fields])
          fields (concat fields [data])
          dataset-concept (assoc-in dataset-concept [:scheme :fields] fields)
          ]
      {:db (assoc-in db current-dataset-concept-path dataset-concept)})))

(re-frame/reg-event-fx
  ::remove-concepts-schema-field
  (fn [{:keys [db]} [_ name]]
    (let [
          current-dataset-concept-path (path-to-db [:current-dataset-concept])
          dataset-concept (get-in db current-dataset-concept-path)
          fields (->> (get-in dataset-concept [:scheme :fields])
                      (filter (fn [field] (not= (:name field) name))))
          dataset-concept (assoc-in dataset-concept [:scheme :fields] fields)
          ]
      {:db (assoc-in db current-dataset-concept-path dataset-concept)})))

(re-frame/reg-event-fx
  ::remove-var-from-concepts
  (fn [{:keys [db]} [_ var-name]]
    (let [concepts (->>
                     (get-in db (path-to-db [:concepts :data]))
                     (into {} (map (fn [[id concept]]
                          [id  (assoc-in concept [:data] (dissoc (:data concept) (keyword var-name)))]))))
          current-list (translator-form.concepts/edited-concepts db)
          new-list (map (fn [[id concept]] id) concepts)
          list-total (concat current-list new-list)]
      {:db (assoc-in db (path-to-db [:concepts :data]) concepts)
       :dispatch-n (list [::translator-form.concepts/set-edited-concepts list-total])})))

(re-frame/reg-event-fx
  ::update-current-concept
  (fn [{:keys [db]} [_ action-path data-patch]]
    (let [current-concept (current-concept db)
          action-data (get-in current-concept (concat [:data] action-path))
          updated-data (merge action-data data-patch)]
      {:db (assoc-in db (path-to-db (concat [:concepts :data] [(:id current-concept) :data] action-path)) updated-data)
       :dispatch-n (list [::translator-form.concepts/add-edited-concepts (:id current-concept)])})))

(re-frame/reg-event-fx
  ::prepare-concept
  (fn [{:keys [db]} [_ concept-id]]
    (let [actions-vars (get-scene-action-vars db)
          concept-vars (keys (get-in db (path-to-db (concat [:concepts :data] [concept-id :data] ))))
          to-add (clojure.set/difference (set actions-vars) (set concept-vars))]
      {:dispatch-n (vec (map (fn [name] [::update-current-concept [name] (:template (get-schema-template-by-name db name))]) to-add) )})))

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
