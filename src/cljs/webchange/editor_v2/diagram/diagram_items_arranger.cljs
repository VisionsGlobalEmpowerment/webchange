(ns webchange.editor-v2.diagram.diagram-items-arranger
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
  [node]
  (let [node-width (get node "width")
        node-height (get node "height")
        default-node-size {:width  150
                           :height 150}]
    (if-not (or (= 0 node-width) (= 0 node-height))
      {:width  node-width
       :height node-height}
      (do (.warn js/console "Node size is not defined")
          default-node-size))))

(defn get-nodes
  [model]
  (->> (.-nodes model)
       (js->clj)
       (map (fn [node]
              (let [id (get node "id")]
                {:id       id
                 :metadata (->> {:id id}
                                (merge (get-node-size node))
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
               :to   (get link "target")}))
       ;		.filter(
       ;			item => model.nodes.find(node => node.id === item.from) && model.nodes.find(node => node.id === item.to)
       ;		);
       ))

(defn get-graph-items
  [model]
  (let [serialized (.serializeDiagram model)
        nodes (get-nodes serialized)
        edges (get-edges serialized)]
    [nodes edges]))


(defn arrange-items
  [model]
  (let [graph (create-graph)]
    (set-graph-items! graph (get-graph-items model))
    (make-graph-layout! graph)
    (doseq [node (get-graph-nodes graph)]
      (set-node-position! model (.-id node) (.-y node) (.-x node)))))
