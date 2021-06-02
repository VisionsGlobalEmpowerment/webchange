(ns webchange.editor-v2.dialog.dialog-text-form.prepare-phrase-actions
  (:require
    [webchange.editor-v2.dialog.utils.dialog-action :refer [get-inner-action inner-action-path]]
    [webchange.editor-v2.dialog.dialog-form.diagram.items-factory.nodes-factory :refer [prepare-nodes get-node-data]]))

(defn- set-action-data
  [actions {:keys [concept-data scene-data]}]
  (map (fn [{:keys [action-path concept] :as node}]
         {:concept      concept
          :concept-name (:name concept-data)
          :data         (if concept
                          (get-in (:data concept-data) action-path)
                          (get-in (:actions scene-data) action-path))
          :path         action-path
          :node-data    (get-node-data {:concept-node?   concept
                                        :current-concept concept-data
                                        :scene-data      scene-data
                                        :action-path     (:action-path node)
                                        :node-path       (:node-path node)})})
       actions))

(defn- get-inner-action-data
  [actions]
  (map (fn [{:keys [data path] :as action}]
         (-> action
             (assoc :data (get-inner-action data))
             (assoc :path (concat path inner-action-path))))
       actions))

(defn- get-component-data
  [actions]
  (map (fn [{:keys [concept concept-name data path node-data]}]
         (cond-> {:character (:target data)
                  :text      (:phrase-text data)
                  :path      path
                  :type      (if concept :concept :scene)
                  :node-data node-data}
                 concept (assoc :concept-name concept-name)))
       actions))

(defn prepare-phrase-actions
  [{:keys [dialog-action-path concept-data scene-data]}]
  (-> (prepare-nodes scene-data concept-data dialog-action-path)
      (set-action-data {:concept-data concept-data
                        :scene-data   scene-data})
      (get-inner-action-data)
      (get-component-data)))
