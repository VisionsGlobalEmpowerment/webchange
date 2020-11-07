(ns webchange.editor-v2.graph-builder.graph.utils
  (:require
    [cljs.test :refer [is]]
    [utils.compare-maps :refer [print-maps-comparison]]))

(defn remove-actions-data
  [graph keep-phrase?]
  (reduce (fn [result [node-name node-data]]
            (let [node-data (if keep-phrase?
                              (assoc node-data :data {:phrase-text (get-in node-data [:data :phrase-text])})
                              (dissoc node-data :data))]
              (assoc result node-name node-data)))
          {}
          graph))

(defn- compare-results
  ([actual-result expected-result]
   (compare-results actual-result expected-result {:pick-data?   true
                                                   :keep-phrase? true}))
  ([actual-result expected-result {:keys [pick-data? keep-phrase?]}]
   (let [result (if pick-data? (:data actual-result) actual-result)
         actual (remove-actions-data result keep-phrase?)]
     (when-not (= actual expected-result)
       (print-maps-comparison actual expected-result))
     (is (= actual expected-result)))))
