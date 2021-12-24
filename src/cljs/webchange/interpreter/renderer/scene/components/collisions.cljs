(ns webchange.interpreter.renderer.scene.components.collisions
  (:require
    [webchange.interpreter.renderer.scene.app :as app]
    [webchange.interpreter.renderer.scene.components.dragging :refer [get-mouse-position]]
    [webchange.interpreter.renderer.scene.components.dragging-utils :as u]
    [webchange.logger.index :as logger]))

(def objects (atom {}))

(defn has-collision-handler?
  [props]
  (or (contains? props :on-collide-enter)
      (contains? props :on-collide-leave)))

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
  ([object1 object2]
   (collided? object1 object2 {}))
  ([object1 object2 {:keys [type]}]
   (let [type (or type "mouse")
         {mouse-x :x mouse-y :y} (get-mouse-position)
         {x1 :x y1 :y width1 :width height1 :height} (get-bounds object1)
         {x2 :x y2 :y width2 :width height2 :height} (get-bounds object2)]
     (case type
       "mouse" (and (u/get-prop object1 "dragging")
                    (> mouse-x x2)
                    (< mouse-x (+ x2 width2))
                    (> mouse-y y2)
                    (< mouse-y (+ y2 height2)))
       "bounds" (and (< x1 (+ x2 width2))
                     (> (+ x1 width1) x2)
                     (< y1 (+ y2 height2))
                     (> (+ y1 height1) y2))))))

(defn- get-objects-to-test
  []
  (->> @objects
       (map (fn [[name {:keys [object params]}]]
              [name object params]))
       (filter (fn [[_ object _]]
                 (.-visible object)))))

(defn- test-name
  [name target-name]
  (cond
    (clojure.string/starts-with? name "#") (-> (subs name 1) (re-pattern) (re-matches target-name))
    :else (= name target-name)))

(defn- interested-target?
  [target-name {:keys [object-name on-collide-enter on-collide-leave]}]
  (let [interested-names (concat (get on-collide-enter :test [])
                                 (get on-collide-leave :test []))]
    (and (not (= object-name target-name))
         (some (fn [name] (test-name name target-name)) interested-names))))

(defn- call-event-handler
  [event-handler-data params]
  (let [handler (get event-handler-data :handler)]
    (when (fn? handler)
      (handler params))))

(defn- test-objects
  [{:keys [object bumped-into on-collide-enter on-collide-leave] :as props}]
  (doseq [[target-name target-object target-props] (get-objects-to-test)]
    (when (and (.-parent object) (.-parent target-object)
               (interested-target? target-name props))
      (let [prev-value (get @bumped-into target-name)
            new-value (collided? object target-object on-collide-enter)
            handler-props (merge target-props {:target target-name})]
        (swap! bumped-into assoc target-name new-value)
        (when (and (true? prev-value)
                   (false? new-value))
          (call-event-handler on-collide-leave handler-props))
        (when (and (not= prev-value new-value)
                   (true? new-value))
          (call-event-handler on-collide-enter handler-props))))))

(defonce ticker-created? (atom false))
(defonce callbacks (atom []))

(defn- init-ticker
  []
  (when-not @ticker-created?
    (app/add-ticker (fn []
                      (doseq [{:keys [object props bumped-into]} @callbacks]
                        (test-objects (merge props
                                             {:object      object
                                              :bumped-into bumped-into})))))
    (reset! ticker-created? true)))

(defn- add-callback
  [object props bumped-into]
  (swap! callbacks conj {:object      object
                         :props       props
                         :bumped-into bumped-into}))

(defn enable-collisions!
  [object props]
  (logger/trace "enable-collisions! for" object)
  (init-ticker)
  (add-callback object props (atom {})))

(defn- collided-with-mouse?
  [object]
  (let [{mouse-x :x mouse-y :y} (get-mouse-position)
        {x :x y :y width :width height :height} (get-bounds object)]
    (and (> mouse-x x)
         (< mouse-x (+ x width))
         (> mouse-y y)
         (< mouse-y (+ y height)))))

(defn get-top-object-at-mouse
  []
  (->> (get-objects-to-test)
       (filter (fn [[_ object _]]
                 (collided-with-mouse? object)))
       (reduce (fn [one another]
                 (if one
                   (if (> (-> one second .-z) (-> another second .-z))
                     one
                     another)
                   another)))
       first))
