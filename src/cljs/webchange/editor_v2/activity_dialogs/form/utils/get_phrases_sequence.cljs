(ns webchange.editor-v2.activity-dialogs.form.utils.get-phrases-sequence)

(defn- get-nodes-from-concept
  ([concept var-name node-path] (get-nodes-from-concept concept var-name node-path 0 0))
  ([concept var-name node-path startx starty]
   (let [path [(keyword var-name)]
         concept-action (get-in concept (concat [:data] path))
         nodes (->> (get-in concept-action [:data])
                    (map-indexed (fn [idx]
                                   (let [action-path (concat path [:data idx])
                                         action (get-in concept (concat [:data] action-path))]
                                     (case (:type action)
                                       "sequence-data" {:concept-action-path action-path
                                                        :scene-action-path   node-path
                                                        :parallel-level      starty}
                                       "parallel" (map-indexed (fn [idy]
                                                                 {:concept-action-path    (concat action-path [:data idy])
                                                                  :scene-action-path      node-path
                                                                  :parallel-level (+ idy starty)})
                                                               (:data action))
                                       {}))))
                    (flatten))]
     {:nodes    nodes
      :offset-x (count (get-in concept-action [:data]))
      :offset-y (if (= (:type (get-in concept-action [:data 0])) "parallel") (count (get-in concept-action [:data 0 :data])) 1)})))

(defn get-phrases-sequence
  [{:keys [path scene-data concept]}]
  (let [offsets-x (atom [])
        offsets-y (atom [])]
    (->> (get-in scene-data (concat [:actions] path [:data]))
         (map-indexed (fn [idx]
                        (let [action-path (concat path [:data idx])
                              x (+ idx (reduce + 0 @offsets-x))
                              action (get-in scene-data (concat [:actions] action-path))]
                          (case (:type action)
                            "sequence-data" {:scene-action-path    action-path
                                             :parallel-level 0}
                            "parallel" (do
                                         (reset! offsets-y [])
                                         (doall (map-indexed (fn [idy]
                                                               (let [inparallel-action-path (concat action-path [:data idy])
                                                                     inparallel-action (get-in scene-data (concat [:actions] inparallel-action-path))
                                                                     y (+ idy (reduce + 0 @offsets-y))]
                                                                 (case (:type inparallel-action)
                                                                   "sequence-data" {:scene-action-path    inparallel-action-path
                                                                                    :parallel-level y}
                                                                   "parallel" {:scene-action-path inparallel-action-path}
                                                                   "action" (let [{nodes :nodes offset-x :offset-x offset-y :offset-y}
                                                                                  (get-nodes-from-concept concept
                                                                                                          (get-in inparallel-action [:from-var 0 :var-property])
                                                                                                          inparallel-action-path x y)]
                                                                              (swap! offsets-x conj (- offset-x 1))
                                                                              (swap! offsets-y conj (- offset-y 1))
                                                                              nodes))))
                                                             (:data action))))
                            "action" (let [{nodes :nodes offset-x :offset-x} (get-nodes-from-concept concept
                                                                                                     (get-in action [:from-var 0 :var-property])
                                                                                                     action-path x 0)]
                                       (swap! offsets-x conj (- offset-x 1))
                                       nodes)
                            {}))))
         (doall)
         (flatten))))
