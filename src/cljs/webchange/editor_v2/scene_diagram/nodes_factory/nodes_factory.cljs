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
  [{:keys [data position]}]
  (let [node (get-custom-model data)]
    (.setPosition node (:x position) (:y position))
    node))

(defn- create-action-node
  [scene-data action-path position]
  (let [action-data (get-in scene-data (concat [:actions] action-path))]
    (create-node {:data     {:data action-data
                             :path action-path}
                  :position position})))

(defn- create-track-label-node
  [track-name track-index]
  (create-node {:data     {:name track-name
                           :type "track"}
                :position {:x (index->coordinate-x 0)
                           :y (index->coordinate-y track-index)}}))

(defn- get-track-nodes
  [scene-data actions-paths track-number]
  (map (fn [[index action-path]]
         (create-action-node scene-data action-path {:x (->> index inc index->coordinate-x)
                                                     :y (->> track-number index->coordinate-y)}))
       (->> actions-paths
            (sort-by (fn [action-path]
                       (->> action-path (map name) (join "-"))))
            (map-indexed (fn [index item] [index item])))))

(defn- get-track-data
  [track-name track-index scene-data actions-paths]
  {:track track-name
   :nodes (concat [(create-track-label-node track-name track-index)]
                  (get-track-nodes scene-data actions-paths track-index))})

(defn get-diagram-items
  [scene-data actions-tracks]
  (let [tracks (reduce (fn [result [track-index [track-name actions-paths]]]
                         (conj result (get-track-data track-name track-index scene-data actions-paths)))
                       []
                       (->> (dissoc actions-tracks default-track)
                            (sort-by first)
                            (map-indexed (fn [index item] [index item]))))
        {:keys [nodes]} (reduce (fn [result {:keys [nodes]}]
                                  (update-in result [:nodes] concat nodes))
                                {:nodes []}
                                (if (contains? actions-tracks default-track)
                                  (conj tracks (get-track-data default-track (dec (count actions-tracks)) scene-data (get actions-tracks default-track)))
                                  tracks))]
    {:nodes nodes
     :links []}))
