(ns webchange.editor-v2.dialog.dialog-text-form.prepare-phrase-actions
  (:require
    [webchange.editor-v2.dialog.utils.dialog-action :refer [get-inner-action inner-action-path]]
    [webchange.editor-v2.dialog.dialog-form.diagram.items-factory.nodes-factory :refer [prepare-nodes get-node-data]]))

(defn- set-action-data
  [actions {:keys [concept-data scene-data]}]
  (map (fn [{:keys [action-path node-path concept]}]
         (let [concept-acton? (boolean concept)]
           {:concept-acton? concept-acton?
            :action-data    (if concept-acton?
                              (get-in (:data concept-data) action-path)
                              (get-in (:actions scene-data) action-path))
            :action-path    action-path
            :node-data      (get-node-data {:concept-node?   concept-acton?
                                            :current-concept concept-data
                                            :scene-data      scene-data
                                            :action-path     action-path
                                            :node-path       node-path})}))
       actions))

(defn- get-inner-action-data
  [actions]
  (map (fn [{:keys [action-data action-path] :as action}]
         (-> action
             (assoc :action-data (get-inner-action action-data))
             (assoc :action-path (concat action-path inner-action-path))))
       actions))

(defn set-action-type
  [actions]
  {:post [(every? (fn [{:keys [type]}] (some #{type} [:effect :concept-phrase :scene-phrase])) %)]}
  (map (fn [{:keys [action-data concept-acton?] :as data}]
         (assoc data :type (let [tags (get action-data :tags [])]
                             (cond
                               (some #{"effect"} tags) :effect
                               concept-acton? :concept-phrase
                               :else :scene-phrase))))
       actions))

(defn- get-component-data
  [actions {:keys [concept-data]}]
  (map (fn [{:keys [type action-data action-path node-data]}]
         (let [one-of (partial some #{type})

               character (:target action-data)
               concept-name (:name concept-data)
               effect (:id action-data)
               text (:phrase-text action-data)]
           (cond-> {:type      type
                    :path      action-path
                    :node-data node-data}
                   (one-of [:scene-phrase :concept-phrase]) (merge {:character character
                                                                    :text      text})
                   (one-of [:concept-phrase]) (merge {:concept-name concept-name})
                   (one-of [:effect]) (merge {:effect effect}))))
       actions))

(defn prepare-phrase-actions
  [{:keys [dialog-action-path concept-data scene-data]}]
  (-> (prepare-nodes scene-data concept-data dialog-action-path)
      (set-action-data {:concept-data concept-data
                        :scene-data   scene-data})
      (get-inner-action-data)
      (set-action-type)
      (get-component-data {:concept-data concept-data})))
