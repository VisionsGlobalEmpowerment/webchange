(ns webchange.interpreter.renderer.scene.components.animated-svg-path.utils)

(defn set-stroke
  [state params]
  (let [{:keys [ctx texture]} @state]
    (if-let [stroke (:stroke params)]
      (set! ctx -strokeStyle stroke))
    (.update texture)))
