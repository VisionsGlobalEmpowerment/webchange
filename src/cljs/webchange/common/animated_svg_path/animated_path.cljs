(ns webchange.common.animated-svg-path.animated-path
  (:require
    [konva :refer [Animation]]
    [react-konva :refer [Group]]
    [webchange.common.animated-svg-path.animated-path-item :refer [animated-path-item]]
    [webchange.common.animated-svg-path.utils :refer [get-params run-funcs-consistently]]
    [webchange.common.svg-path.path-length :refer [path-length]]
    [webchange.common.svg-path.path-splitter :refer [split-path]]
    [webchange.common.svg-path.path-to-transitions :refer [get-moves-lengths]]))

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
  [{:keys [ref set-progress duration delay cb]}]
  (set-progress 0)
  (when ref
    (let [animation (atom nil)]
      (reset! animation (Animation. (fn [frame]
                                      (let [time (- (.-time frame) delay)
                                            progress (max (/ time duration) 0)]
                                        (set-progress progress)
                                        (when (>= progress 1)
                                          (.stop @animation)
                                          (cb))))
                                    (.getLayer ref)))
      (.start @animation))))

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
  (let [anim-params (map #(merge (:ref %) {:duration (:duration %)
                                           :delay (:delay %)}) (vals @paths))]
    (run-funcs-consistently animate-path anim-params cb)))

(defn constructor
  [{:keys [data duration use-delay?]}]
  (reset! paths (let [moves-lengths (get-moves-lengths data)
                      visible-length (path-length data)
                      total-length (+ visible-length (reduce + 0 moves-lengths))
                      use-delay? (if (nil? use-delay?) true use-delay?)]
                  (->> (split-path data)
                       (map #(vec [%1 %2]) moves-lengths)
                       (map-indexed vector)
                       (reduce (fn [result [index [delay current-data]]]
                                 (assoc result index {:ref      nil
                                                      :data     current-data
                                                      :delay    (if use-delay? (-> duration
                                                                                   (* delay)
                                                                                   (/ total-length)) 0)
                                                      :duration (-> duration
                                                                    (* (path-length current-data))
                                                                    (/ (if use-delay? total-length visible-length)))}))
                               {})))))

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
                                     (reset-progress 0)
                                     (callback))
                           "stop" (do
                                    (reset-progress 1)
                                    (callback))
                           "default")))
                     )})])])

(def animated-path
  (with-meta render {}))
