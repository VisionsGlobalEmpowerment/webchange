(ns webchange.editor-v2.dialog.dialog-form.diagram.items-factory.nodes-factory
  (:require
    [clojure.string :refer [join]]
    [re-frame.core :as re-frame]
    [webchange.editor-v2.diagram-utils.diagram-model.custom-nodes.custom-model :refer [get-custom-model]]
    [webchange.editor-v2.scene-diagram.scene-parser.actions-tracks :refer [default-track]]
    [webchange.editor-v2.translator.translator-form.state.concepts :as translator-form.concepts]
    [webchange.editor-v2.graph-builder.scene-parser.concepts-replacer.replacer :refer [override-concept-actions]]
    [webchange.editor-v2.graph-builder.scene-parser.scene-parser :refer [parse-data]]
    [webchange.editor-v2.graph-builder.graph-normalizer.graph-normalizer :refer [normalize-graph]]
    ))

(def coordinate-params {:x-offset 50
                        :y-offset 50
                        :x-step   300
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

(defn- clean-path [path]
  (vec (filter (fn [key] (not= key :data)) path)))

(defn- create-concept-action-node
  [concept node-path scene-data action-path position]
  (let [action-data (-> (get-in concept (concat [:data] action-path))
                    (assoc :concept-action true))
        node-data (get-in scene-data (concat [:actions] node-path))
        ]
  (create-node {:data     {:data action-data
                           :entity :action
                           :path (clean-path action-path)
                           :action-node-data {
                                              :data node-data
                                              :path (clean-path node-path)
                                              }
                           }
                :position position})
  ))

(defn- create-action-node
  [scene-data action-path position]
  (let [action-data (get-in scene-data (concat [:actions] action-path))]
    (create-node {:data     {:data action-data
                             :path (clean-path action-path)}
                  :position position})))

(defn get-nodes-from-concept
  ([concept var-name node-path] (get-nodes-from-concept concept var-name node-path 0 0))
  ( [concept var-name node-path startx starty]
  (let [
        path [(keyword var-name)]
        concept-action (get-in concept (concat [:data] path))
        nodes (flatten (map-indexed (fn [idx]
                                      (let [action-path (concat path [:data idx])
                                            action (get-in concept (concat [:data] action-path))]
                                        (case (:type action)
                                          "sequence-data" {:action-path action-path
                                                                :concept true
                                                                :node-path node-path
                                                                :x (+ idx startx)
                                                                :y starty}
                                          "parallel"  (map-indexed (fn [idy]
                                                                     {:action-path (concat action-path [:data idy])
                                                                      :concept true
                                                                      :node-path node-path
                                                                      :x (+ idx startx)
                                                                      :y (+ idy starty)}
                                                                     )
                                                                   (:data action) )
                                          {})))
                                    (get-in concept-action [:data])
                                    ))
        ]
    {:nodes nodes :offset-x (count (get-in concept-action [:data]))
     :offset-y (if (= (:type (get-in concept-action [:data 0])) "parallel") (count (get-in concept-action [:data 0 :data])) 1)
     }
    )
  ))

(defn prepare-nodes
  [scene-data concept path]
  (let [
        offsets-x (atom [])
        offsets-y (atom [])
        nodes (flatten (doall (map-indexed (fn [idx]
                                      (let [action-path (concat path [:data idx])
                                            x (+ idx (reduce + 0 @offsets-x))
                                            action (get-in scene-data (concat [:actions] action-path))]
                                        (case (:type action)
                                          "sequence-data" {:action-path action-path
                                                                :x x
                                                                :y 0}
                                          "parallel" (do
                                                       (reset! offsets-y [])
                                                       (doall (map-indexed (fn [idy]
                                                                      (let [inparallel-action-path (concat action-path [:data idy])
                                                                            inparallel-action (get-in scene-data (concat [:actions] inparallel-action-path))
                                                                            y (+ idy (reduce + 0 @offsets-y))]
                                                                        (case (:type inparallel-action)
                                                                          "sequence-data" {:action-path inparallel-action-path
                                                                                                :x x
                                                                                                :y y}
                                                                          "action" (let [{nodes :nodes offset-x :offset-x offset-y :offset-y}
                                                                                         (get-nodes-from-concept concept
                                                                                                                 (get-in inparallel-action [:from-var 0 :var-property])
                                                                                                                 inparallel-action-path x y)]
                                                                                     (swap! offsets-x conj (- offset-x 1))
                                                                                     (swap! offsets-y conj (- offset-y 1))
                                                                                     nodes)
                                                                          )
                                                                     ))
                                                                   (:data action))))

                                          "action" (let [{nodes :nodes offset-x :offset-x}  (get-nodes-from-concept concept
                                                                                 (get-in action [:from-var 0 :var-property])
                                                                                 action-path x 0)]
                                                     (swap! offsets-x conj (- offset-x 1))
                                                           nodes)
                                          {})))
                                    (get-in scene-data (concat [:actions] path [:data]))
                                    )))]
    nodes))

(defn get-diagram-items
  [scene-data path]
  (let [current-concept @(re-frame/subscribe [::translator-form.concepts/current-concept])
        prepare-nodes (prepare-nodes scene-data current-concept path)
        nodes (flatten (map (fn [node]
                              (if (get-in node [:concept])
                                (create-concept-action-node current-concept (:node-path node) scene-data (:action-path node)
                                                            {:x (index->coordinate-x (:x node))
                                                             :y (index->coordinate-y (:y node))})
                                (create-action-node scene-data (:action-path node) {:x (index->coordinate-x (:x node))
                                                                                    :y (index->coordinate-y (:y node))})))
                            prepare-nodes))]
    {:nodes nodes
     :links []}))
