(ns webchange.common.animated-svg-path.utils)

(defn get-params
  [current-params default-params]
  (reduce (fn [result param-key]
            (assoc result param-key
                          (get current-params param-key (get default-params param-key))))
          {}
          (keys default-params)))

(defn- run-tasks-sequence
  [tasks]
  (reduce (fn [prev cur] (.then prev cur)) (.resolve js/Promise) tasks))

(defn- func->task
  [func params]
  (fn []
    (js/Promise. (fn [resolve]
                   (func (merge params {:cb resolve}))))))

(defn run-funcs-consistently
  [func params cb]
  (let [tasks (map #(func->task func %) params)]
    (.then (run-tasks-sequence tasks) cb)))
