(ns webchange.editor-v2.scene-diagram.nodes-factory.nodes-factory
  (:require
    [clojure.string :refer [join]]
    [webchange.editor-v2.diagram-utils.diagram-model.custom-nodes.custom-model :refer [get-custom-model]]
    [webchange.editor-v2.scene-diagram.scene-parser.actions-tracks :refer [default-track]]))

(def coordinate-params {:x-offset 50
                        :y-offset 50
                        :x-step   200
                        :y-step   100})

(defn- index->coordinate
  [index offset step]
  (+ offset (* index step)))

(defn- index->coordinate-x
  [index]
  (index->coordinate index (:x-offset coordinate-params) (:x-step coordinate-params)))

(defn- index->coordinate-y
  [index]
  (index->coordinate index (:y-offset coordinate-params) (:y-step coordinate-params)))

(defn- create-node
  [{:keys [type data position]}]
  (let [node (get-custom-model (merge data {:type type}))]
    (.setPosition node (:x position) (:y position))
    node))

(defn- create-dialog-node
  [scene-data {:keys [action-id]} position]
  (let [action-id (keyword action-id)
        action-data (get-in scene-data [:actions action-id])]
    (create-node {:type     "dialog"
                  :data     {:data action-data
                             :path [action-id]}
                  :position position})))

(defn- create-question-node
  [scene-data {:keys [action-id]} position]
  (let [action-id (keyword action-id)
        action-data (get-in scene-data [:actions action-id])]
    (create-node {:type     "question"
                  :data     {:data action-data
                             :path [action-id]}
                  :position position})))

(defn- create-prompt-node
  [node position]
  (create-node {:type     "prompt"
                :data     node
                :position position}))

(defn- create-empty-node
  [_ position]
  (create-node {:type     "empty"
                :position position}))

(defn- create-action-node
  [scene-data {:keys [type] :as node} position]
  (case type
    "dialog" (create-dialog-node scene-data node position)
    "question" (create-question-node scene-data node position)
    "prompt" (create-prompt-node node position)
    (create-empty-node node position)))

(defn- create-track-label-node
  [track-name track-index]
  (create-node {:type     "track"
                :data     {:name track-name}
                :position {:x (index->coordinate-x 0)
                           :y (index->coordinate-y track-index)}}))

(defn- get-track-nodes
  [scene-data nodes track-number]
  (->> nodes
       (map-indexed (fn [index node]
                      (create-action-node scene-data node {:x (->> index inc index->coordinate-x)
                                                           :y (->> track-number index->coordinate-y)})))))

(defn- get-track-data
  [track-name track-index scene-data nodes]
  {:track track-name
   :nodes (concat [(create-track-label-node track-name track-index)]
                  (get-track-nodes scene-data nodes track-index))})

(defn get-diagram-items
  [scene-data actions-tracks]
  (let [tracks (->> actions-tracks
                    (map-indexed (fn [track-index {:keys [title nodes]}]
                                   (get-track-data title track-index scene-data nodes))))
        {:keys [nodes]} (reduce (fn [result {:keys [nodes]}]
                                  (update-in result [:nodes] concat nodes))
                                {:nodes []}
                                (if (contains? actions-tracks default-track)
                                  (conj tracks (get-track-data default-track (dec (count actions-tracks)) scene-data (get actions-tracks default-track)))
                                  tracks))]
    {:nodes nodes
     :links []}))
