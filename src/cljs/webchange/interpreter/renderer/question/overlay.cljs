(ns webchange.interpreter.renderer.question.overlay)

(def menu-padding {:x 20 :y 20})

(defn create
  [{:keys [parent]}]
  {:type        "group"
   :parent      parent
   :object-name :question-overlay
   :visible     false
   :children    []})

