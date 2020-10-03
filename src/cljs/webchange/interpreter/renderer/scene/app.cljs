(ns webchange.interpreter.renderer.scene.app
  (:require
    [webchange.interpreter.renderer.scene.filters.filters :as filters]
    [webchange.interpreter.pixi :refer [clear-texture-cache destroy-texture-cache sound]]))

(def pixi-app (atom nil))

(defn app-exists? [] (not (nil? @pixi-app)))

(defn reset-app!
  []
  (when (app-exists?)
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

(defn add-ticker
  [f]
  (-> @pixi-app
      .-ticker
      (.add f)))

(defn remove-ticker
  [f]
  (-> @pixi-app
      .-ticker
      (.remove f)))

(defn get-renderer
  []
  (.-renderer @pixi-app))
