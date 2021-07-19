(ns webchange.interpreter.renderer.scene.components.question.create-utils)

(defn add-name-suffix
  [name suffix]
  (-> name (clojure.core/name) (str "-" suffix) (keyword)))
