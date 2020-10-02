(ns webchange.interpreter.renderer.scene.app
  (:require
    [webchange.interpreter.renderer.scene.filters.filters :as filters]))

(def pixi-app (atom nil))

(defn register-app
  [app]
  (reset! pixi-app app))

(defn destroy-app
  []
  (filters/destroy)
  (.destroy @pixi-app true (clj->js {:children    true
                                     :texture     true
                                     :baseTexture true}))
  (reset! pixi-app nil))

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
