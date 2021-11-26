(ns webchange.interpreter.renderer.scene.components.animated-svg-path.utils
  (:require
    [webchange.interpreter.renderer.scene.components.letters-path :refer [alphabet-traceable-path]]))

(defn get-svg-path
  [path]
  (if (contains? alphabet-traceable-path path)
    (get alphabet-traceable-path path)
    path))

(defn set-stroke
  [state params]
  (let [{:keys [ctx texture]} @state]
    (if-let [stroke (:stroke params)]
      (set! ctx -strokeStyle stroke))
    (.update texture)))
