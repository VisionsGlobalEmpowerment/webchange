(ns webchange.interpreter.renderer.scene.app
  (:require
    [webchange.interpreter.core :refer [kill-transitions!]]
    [webchange.interpreter.renderer.scene.filters.filters :as filters]
    [webchange.interpreter.pixi :refer [clear-texture-cache destroy-texture-cache sound Graphics]]))

(def pixi-app (atom nil))
(def ticker-handlers (atom {}))

(defn app-exists? [] (not (nil? @pixi-app)))

(defn add-ticker
  [f]
  (swap! ticker-handlers assoc f f)
  (-> @pixi-app
      .-ticker
      (.add f)))

(defn remove-ticker
  [f]
  (swap! ticker-handlers dissoc f)
  (-> @pixi-app
      .-ticker
      (.remove f)))

(defn- remove-all-tickers
  []
  (doall (for [[ticker] @ticker-handlers]
           (remove-ticker ticker)))
  (reset! ticker-handlers {}))

(defn reset-app!
  [{:keys [reset-textures?]
    :or   {reset-textures? true}}]
  (when (app-exists?)
    (kill-transitions!)
    (remove-all-tickers)
    (let [stage (.-stage @pixi-app)
          children (.removeChildren stage)]
      (doall (for [child children]
               (.destroy child (-> (cond-> {:children true}
                                           reset-textures? (merge {:texture     true
                                                                   :baseTexture true}))
                                   (clj->js)))))
      (.render @pixi-app))
    (when reset-textures?
      (destroy-texture-cache)
      (clear-texture-cache))
    (-> sound (.removeAll))
    (filters/destroy)))

(defn get-app
  []
  @pixi-app)

(defn register-app
  [app]
  (reset! pixi-app app))

(defn get-renderer
  ([]
   (when (some? @pixi-app)
     (get-renderer @pixi-app)))
  ([app]
   (.-renderer app)))

(defn get-stage
  ([]
   (get-stage (get-app)))
  ([app]
   (.-stage app)))

(defn- create-mask
  [x y width height]
  (doto (Graphics.)
    (.beginFill 0x000000)
    (.drawRect x y width height)
    (.endFill 0x000000)))

(defn resize-app!
  ([viewport]
   (when (app-exists?)
     (-> (get-app)
         (resize-app! viewport))))
  ([app viewport]
   (let [stage (get-stage app)
         renderer (get-renderer app)

         position (.-position stage)
         scale (.-scale stage)

         {:keys [x y width height scale-x scale-y]} viewport]
     (aset position "x" x)
     (aset position "y" y)
     (aset scale "x" scale-x)
     (aset scale "y" scale-y)
     (.resize renderer width height)
     (->> (create-mask x
                       y
                       (- width (* 2 x))
                       (- height (* 2 y)))
          (aset stage "mask")))))

(defn take-screenshot
  ([callback]
   (take-screenshot callback {}))
  ([callback {:keys [extract-canvas? render?] :or {extract-canvas? true render? false}}]
   (when render?
     (.render @pixi-app))
   (let [renderer (get-renderer)
         canvas (if extract-canvas?
                  (.canvas (.-extract renderer) (get-stage))
                  (.-view renderer))]
     (.toBlob canvas callback))))
