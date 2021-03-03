(ns webchange.interpreter.renderer.scene.app
  (:require
    [webchange.interpreter.core :refer [kill-transitions!]]
    [webchange.interpreter.renderer.scene.filters.filters :as filters]
    [webchange.interpreter.pixi :refer [clear-texture-cache destroy-texture-cache sound]]))

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
  []
  (when (app-exists?)
    (kill-transitions!)
    (remove-all-tickers)
    (let [stage (.-stage @pixi-app)
          children (.removeChildren stage)]
      (doall (for [child children]
               (.destroy child (clj->js {:children    true
                                         :texture     true
                                         :baseTexture true}))))
      (.render @pixi-app))
    (destroy-texture-cache)
    (clear-texture-cache)
    (-> sound (.removeAll))
    (filters/destroy)))

(defn get-app
  []
  @pixi-app)

(defn register-app
  [app]
  (reset! pixi-app app))

(defn get-renderer
  []
  (when (some? @pixi-app)
    (.-renderer @pixi-app)))

(defn get-stage
  ([]
   (get-stage (get-app)))
  ([app]
   (.-stage app)))

(defn resize-app!
  [viewport]
  (when (app-exists?)
    (let [{:keys [x y width height scale-x scale-y]} viewport
          stage (-> (get-app) (get-stage))
          renderer (get-renderer)
          position (.-position stage)
          scale (.-scale stage)]
      (aset position "x" x)
      (aset position "y" y)
      (aset scale "x" scale-x)
      (aset scale "y" scale-y)
      (.resize renderer width height))))

(defn take-screenshot
  [callback]
  (let [renderer (get-renderer)
        canvas (.canvas (.-extract renderer) (get-stage))]
  (.toBlob canvas callback)))
