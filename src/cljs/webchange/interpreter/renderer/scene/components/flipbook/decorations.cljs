(ns webchange.interpreter.renderer.scene.components.flipbook.decorations
  (:require
    [webchange.interpreter.pixi :refer [Container]]
    [webchange.interpreter.renderer.scene.components.flipbook.wrapper :refer [wrap]]
    [webchange.interpreter.renderer.scene.components.utils :as utils]))

(defn- left-pages-name
  [idx]
  (str "flipbook-decorations-left-" idx))

(defn right-pages-name
  [idx]
  (str "flipbook-decorations-right-" idx))

(defn crease-name
  [idx]
  (str "flipbook-decorations-crease-" idx))

(defn create-for
  [idx]
  (if (even? idx)
    [{:object-name (right-pages-name idx)
      :transition-name (right-pages-name idx)
      :type        "image"
      :src         "/raw/img/flipbook/pages_1.png"
      :x           913
      :y           0}
     {:object-name (crease-name idx)
      :transition-name (crease-name idx)
      :type        "image"
      :x           0
      :y           0
      :src         "/raw/img/flipbook/shadow_page.png"}]
    [{:object-name (left-pages-name idx)
      :transition-name (left-pages-name idx)
      :type        "image"
      :src         "/raw/img/flipbook/pages_2.png"
      :x           0
      :y           0}]))
