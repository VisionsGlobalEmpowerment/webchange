(ns webchange.interpreter.renderer.scene.components.text-tracing-pattern.utils)

(defn set-enable!
  [state]
  (doseq [{:keys [set-enable]} (:all-letters @state)]
    (set-enable (:enable? @state))))
