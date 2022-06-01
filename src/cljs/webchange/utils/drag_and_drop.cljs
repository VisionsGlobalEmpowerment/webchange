(ns webchange.utils.drag-and-drop)

(defn- get-data-set
  [event]
  (->> (.. event -target -dataset)
       (js/Object.entries)
       (js->clj)
       (map (fn [[field value]]
              [(keyword field) value]))
       (into {})))

(defn get-transfer-data
  [event]
  (let [data-transfer (.-dataTransfer event)
        items (.-items data-transfer)]
    (->> (range (.-length items))
         (reduce (fn [result index]
                   (let [item (aget items index)
                         key (.-type item)
                         value (.getData data-transfer key)]
                     (assoc result (keyword key) value)))
                 {}))))

(defn set-transfer-data
  [event data]
  (let [data-transfer (.-dataTransfer event)]
    (doseq [key (keys data)]
      (.setData data-transfer (clojure.core/name key) (get data key)))))

(defn draggable
  []
  )
