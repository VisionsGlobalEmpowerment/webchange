(ns webchange.interpreter.utils.find-exit)

(defn find-path
  [from to scenes]
  (let [visited (atom #{from})]
    (loop [[head & tail] [[from]]]
      (if head
        (let [node-name (last head)
              scene (get scenes (keyword node-name))
              sibling-names (->> scene :outs (map :name) (into #{}))
              non-visited (clojure.set/difference sibling-names @visited)
              _ (swap! visited #(clojure.set/union % non-visited))
              new-paths (map (fn [next-node] (conj head next-node)) non-visited)]
          (if (= node-name to)
            head
            (recur (concat tail new-paths))))))))


(defn find-exit-position
  [from to scenes]
  (let [scene (get scenes (keyword from))
        [_ second & _] (find-path from to scenes)]
    (->> (:outs scene)
         (filter #(= second (:name %)))
         first)))
