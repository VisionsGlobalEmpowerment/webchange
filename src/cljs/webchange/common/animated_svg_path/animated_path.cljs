(ns webchange.common.animated-svg-path.animated-path
  (:require
    [konva :refer [Animation]]
    [react-konva :refer [Group]]
    [webchange.common.animated-svg-path.animated-path-item :refer [animated-path-item]]
    [webchange.common.animated-svg-path.utils :refer [get-params path-length split-path]]))

(def default-group-params
  {:x        0
   :y        0
   :rotation 0
   :scale-x  1
   :scale-y  1})

(def default-path-params
  {:width        100
   :height       100
   :stroke       "black"
   :stroke-width 1
   :line-cap     "butt"
   :fill         "transparent"})

(def not-nil? (complement nil?))

(def paths (atom {}))

(defn animate-path
  [{:keys [ref set-progress duration cb]}]
  (set-progress 0)
  (when ref
    (let [animation (atom nil)]
      (reset! animation (Animation. (fn [frame]
                                      (let [time (.-time frame)
                                            progress (/ time duration)]
                                        (set-progress progress)
                                        (when (>= progress 1)
                                          (.stop @animation)
                                          (cb))))
                                    (.getLayer ref)))
      (.start @animation))))

(defn run-tasks-sequence
  [tasks]
  (reduce (fn [prev cur] (.then prev cur)) (.resolve js/Promise) tasks))

(defn func->task
  [func params]
  (fn []
    (js/Promise. (fn [resolve]
                   (func (merge params {:cb resolve}))))))

(defn refs-done?
  []
  (reduce #(and %1 (not-nil? (:ref %2))) true (vals @paths)))

(defn reset-progress
  [value]
  (doall (map (fn [path]
                (let [set-progress (get-in path [:ref :set-progress])]
                  (set-progress value))) (vals @paths))))

(defn start-animation
  [cb]
  (reset-progress 0)
  (let [anim-params (map #(merge (:ref %) {:duration (:duration %)}) (vals @paths))
        anim-tasks (map #(func->task animate-path %) anim-params)]
    (.then (run-tasks-sequence anim-tasks) cb)))

(defn constructor
  [{:keys [data duration]}]
  (reset! paths (->> (split-path data)
                     (map-indexed vector)
                     (reduce (fn [result [index current-data]]
                               (assoc result index {:ref      nil
                                                    :data     current-data
                                                    :duration (-> duration
                                                                  (* (path-length current-data))
                                                                  (/ (path-length data)))}))
                             {}))))

(defn render
  [{:keys [animation on-end] :as props}]
  (constructor props)
  [:> Group (get-params props default-group-params)
   (for [[path-id path] (seq @paths)]
     ^{:key path-id}
     [animated-path-item
      (merge
        (get-params props default-path-params)
        {:data     (:data path)
         :progress 1
         :ref      (fn [ref]
                     (swap! paths assoc-in [path-id :ref] ref)
                     (when (refs-done?)
                       (let [callback (or on-end #())]
                         (case animation
                           "play" (start-animation callback)
                           "reset" (do
                                     (reset-progress 0.3)
                                     (callback))
                           "stop" (do
                                    (reset-progress 1)
                                    (callback))
                           "default")))
                     )})])])

(def animated-path
  (with-meta render {}))
