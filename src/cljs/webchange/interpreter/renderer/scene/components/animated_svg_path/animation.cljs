(ns webchange.interpreter.renderer.scene.components.animated-svg-path.animation
  (:require
    [webchange.interpreter.renderer.scene.app :refer [add-ticker remove-ticker]]))

(defn- draw-frame
  [elapsed state]
  (let [{:keys [ctx texture paths width height]} @state
        time-left (atom elapsed)
        idx (atom 0)]
    (.clearRect ctx 0 0 width height)
    (.setLineDash ctx #js [])
    (while (and (> @time-left 0) (< @idx (count paths)))
      (let [{:keys [path length delay duration]} (get paths @idx)]
        (cond
          (> @time-left (+ delay duration)) (do
                                              (.stroke ctx (js/Path2D. path))
                                              (swap! time-left - (+ delay duration))
                                              (swap! idx inc))
          (> @time-left delay) (do
                                 (.setLineDash ctx #js [(* length (/ (- @time-left delay) duration)) length])
                                 (.stroke ctx (js/Path2D. path))
                                 (reset! time-left 0))
          :else (reset! time-left 0))))
    (.update texture)))

(defn start
  [state on-end]
  (let [start-time (.getTime (js/Date.))
        f (fn []
            (let [elapsed (- (.getTime (js/Date.)) start-time)]
              (if (< elapsed (:duration @state))
                (draw-frame elapsed state)
                (do (draw-frame elapsed state)
                    (remove-ticker (:ticker @state))
                    (on-end)))))]
    (swap! state assoc :ticker f)
    (add-ticker f)))

(defn stop
  [state]
  (remove-ticker (:ticker @state))
  (let [{:keys [ctx texture paths width height]} @state]
    (.clearRect ctx 0 0 width height)
    (doall (map #(.stroke ctx (js/Path2D. (:path %))) paths))
    (.update texture)))

(defn reset
  [state]
  (remove-ticker (:ticker @state))
  (let [{:keys [ctx texture width height]} @state]
    (.clearRect ctx 0 0 width height)
    (.update texture)))
