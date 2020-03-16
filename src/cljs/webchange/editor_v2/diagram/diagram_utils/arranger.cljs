(ns webchange.editor-v2.diagram.diagram-utils.arranger
  (:require
    [dagre :as dagre]))

(defn create-graph
  []
  (let [Graph (->> dagre .-graphlib .-Graph)
        graph (Graph.)]
    (.setGraph graph #js {})
    (.setDefaultEdgeLabel graph (fn [] {}))
    graph))

(defn get-graph-nodes
  [graph]
  (->> (.nodes graph)
       (js->clj)
       (map (fn [node]
              (.node graph node)))))

(defn set-graph-items!
  [graph [nodes edges]]
  (doseq [{:keys [id metadata]} nodes]
    (.setNode graph id metadata))
  (doseq [{:keys [from to]} edges]
    (when-not (or (nil? from) (nil? to))
      (.setEdge graph from to))))

(defn make-graph-layout!
  [graph]
  (.layout dagre graph))

(defn get-node-size
  [node-data engine]
  (let [diagram-model (.getDiagramModel engine)
        node-id (get node-data "id")
        node-model (.getNode diagram-model node-id)
        node-dimensions (.getNodeDimensions engine node-model)
        zoom (.getZoomLevel diagram-model)
        node-width (* (.-width node-dimensions) (/ 100 zoom))
        node-height (* (.-height node-dimensions) (/ 100 zoom))]
    (.updateDimensions node-model node-dimensions)
    (if-not (or (= 0 node-width) (= 0 node-height))
      {:width  node-height                                  ; # have to change X and Y axis for unknown reason
       :height node-width}
      (do (.warn js/console "Node size is not defined")
          {:width  150
           :height 150}))))

(defn get-nodes
  [model engine]
  (->> (.-nodes model)
       (js->clj)
       (map (fn [node]
              (let [id (get node "id")]
                {:id       id
                 :metadata (->> {:id id}
                                (merge (get-node-size node engine))
                                (clj->js))})))))

(defn set-node-position!
  [model id x y]
  (.setPosition
    (.getNode model id)
    x y))

(defn get-edges
  [model]
  (->> (.-links model)
       (js->clj)
       (map (fn [link]
              {:from (get link "source")
               :to   (get link "target")}))))

(defn get-graph-items
  [model engine]
  (let [serialized (.serializeDiagram model)
        nodes (get-nodes serialized engine)
        edges (get-edges serialized)]
    [nodes edges]))

(defn arrange-items
  [model engine]
  (let [graph (create-graph)]
    (set-graph-items! graph (get-graph-items model engine))
    (make-graph-layout! graph)
    (doseq [node (get-graph-nodes graph)]
      (set-node-position! model (.-id node) (.-y node) (.-x node))))) ; # have to change X and Y axis for unknown reason
