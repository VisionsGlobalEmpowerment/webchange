(ns webchange.interpreter.renderer.scene.components.question.create-common-voice-over
  (:require
    [webchange.interpreter.renderer.scene.components.question.create-utils :refer [add-name-suffix]]))

(def default-size 80)

(defn- create-background
  [{:keys [object-name size]}]
  {:type          "rectangle"
   :object-name   object-name
   :x             0
   :y             0
   :width         size
   :height        size
   :border-radius (/ size 2)
   :fill          0xFFFFFF})

(defn create
  [{:keys [object-name x y size]
    :or   {x    0
           y    0
           size default-size}}]
  {:type        "group"
   :object-name object-name
   :x           x
   :y           y
   :children    [(create-background {:object-name (add-name-suffix object-name "background")
                                     :size        size})]})
