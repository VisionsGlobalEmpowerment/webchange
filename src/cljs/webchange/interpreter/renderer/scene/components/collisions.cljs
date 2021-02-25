(ns webchange.interpreter.renderer.scene.components.collisions
  (:require
    [webchange.interpreter.pixi :refer [shared-ticker]]))

(def objects (atom {}))

(defn has-collision-handler?
  [props]
  (contains? props :on-collide))

(defn register-object
  [object {:keys [transition-name]}]
  (when (some? transition-name)
    (swap! objects assoc transition-name object)))

(defn- get-bounds
  [object]
  (let [bounds (.getBounds object)]
    {:x      (.-x bounds)
     :y      (.-y bounds)
     :width  (.-width bounds)
     :height (.-height bounds)}))

(defn- test-collision
  [object1 object2]
  (let [{x1 :x y1 :y width1 :width height1 :height} (get-bounds object1)
        {x2 :x y2 :y width2 :width height2 :height} (get-bounds object2)]
    (and (< x1 (+ x2 width2))
         (> (+ x1 width1) x2)
         (< y1 (+ y2 height2))
         (> (+ y1 height1) y2))))

(defn- test-objects
  [object bumped-into collide-test on-collide]

  (doseq [target-name collide-test]
    (let [target-object (get @objects target-name)]
      (if (test-collision object target-object)
        (do (when-not (get @bumped-into target-name)
              (on-collide {:target target-name}))
            (swap! bumped-into assoc target-name true))
        (swap! bumped-into assoc target-name false)))))

(defn enable-collisions!
  [object {:keys [collide-test on-collide]}]
  (let [bumped-into (atom {})]
    (.add shared-ticker (fn [] (test-objects object bumped-into collide-test on-collide)))))
