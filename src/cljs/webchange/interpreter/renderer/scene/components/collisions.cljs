(ns webchange.interpreter.renderer.scene.components.collisions
  (:require
    [webchange.interpreter.pixi :refer [shared-ticker]]
    [webchange.logger.index :as logger]))

(def objects (atom {}))

(defn has-collision-handler?
  [props]
  (contains? props :on-collide))

(defn register-object
  [object {:keys [transition-name] :as props}]
  (when (some? transition-name)
    (logger/group-folded "register collidable" (keyword transition-name))
    (logger/trace "object props:" props)
    (logger/trace "display object:" object)

    (swap! objects assoc transition-name {:object object
                                          :params props})

    (logger/group-end "register collidable" (keyword transition-name))))

(defn- get-bounds
  [object]
  (let [bounds (.getBounds object)]
    {:x      (.-x bounds)
     :y      (.-y bounds)
     :width  (.-width bounds)
     :height (.-height bounds)}))

(defn- collided?
  [object1 object2]
  (let [{x1 :x y1 :y width1 :width height1 :height} (get-bounds object1)
        {x2 :x y2 :y width2 :width height2 :height} (get-bounds object2)]
    (and (< x1 (+ x2 width2))
         (> (+ x1 width1) x2)
         (< y1 (+ y2 height2))
         (> (+ y1 height1) y2))))

(defn- get-objects-to-test
  []
  (map (fn [[name {:keys [object params]}]]
         [name object params])
       @objects))

(defn- test-name
  [name target-name]
  (cond
    (clojure.string/starts-with? name "#") (-> (subs name 1) (re-pattern) (re-matches target-name))
    :else (= name target-name)))

(defn- interested-target?
  [object-name target-name interested-names]
  (and (not (= object-name target-name))
       (some (fn [name] (test-name name target-name)) interested-names)))

(defn- test-objects
  [object object-name bumped-into collide-test on-collide]
  (doseq [[target-name target-object target-props] (get-objects-to-test)]
    (if (and (collided? object target-object)
             (interested-target? object-name target-name collide-test))
      (do (when-not (get @bumped-into target-name)
            (logger/trace)
            (on-collide (merge target-props
                               {:target target-name})))
          (swap! bumped-into assoc target-name true))
      (swap! bumped-into assoc target-name false))))

(defn enable-collisions!
  [object {:keys [object-name collide-test on-collide]}]
  (let [bumped-into (atom {})]
    (.add shared-ticker (fn [] (test-objects object object-name bumped-into collide-test on-collide)))))
