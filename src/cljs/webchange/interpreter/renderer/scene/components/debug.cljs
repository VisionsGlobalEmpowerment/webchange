(ns webchange.interpreter.renderer.scene.components.debug
  (:require [webchange.interpreter.pixi :refer [Graphics]]))

(def debug-enabled? false)
(def state (atom {}))

(defn mark
  [name x y radius color parent]
  (when debug-enabled?
    (when (get @state name)
      (.destroy (get @state name)))
    (let [mark (doto (Graphics.)
                 (.lineStyle 0)
                 (.beginFill color)
                 (.drawCircle x y radius)
                 (.endFill))]
      (.addChild parent mark)
      (swap! state assoc name mark))))

(defn show-bounds
  [display-object]
  (.on display-object "added"
       (fn [parent]
         (let [random-color (rand-int 16581375)
               bounds (.getLocalBounds display-object)
               x (+ (.-x display-object) (.-x bounds))
               y (+ (.-y display-object) (.-y bounds))
               rect (doto (Graphics.)
                      (.lineStyle 4 random-color 1)
                      (.drawRect x y (.-width bounds) (.-height bounds)))]
           (.addChild parent rect)))))

(defn with-debug
  [wrapped-object]
  (when (= "animated-svg-path" (:type wrapped-object))
    (let [object (:object wrapped-object)]
      (show-bounds object)))
  wrapped-object)
