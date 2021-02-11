(ns webchange.interpreter.renderer.scene.components.text.chunked-text
  (:require
    [webchange.interpreter.pixi :refer [Container]]
    [webchange.interpreter.renderer.scene.components.text.chunks :refer [lines-with-y chunks-with-x]]
    [webchange.interpreter.renderer.scene.components.text.simple-text :refer [create-simple-text]]
    [webchange.interpreter.renderer.scene.components.utils :as utils]))

(defn- new-container
  [x y]
  (doto (Container.)
    (utils/set-position {:x x :y y})))

(defn- create-chunk
  [chunk line-container]
  (let [text (create-simple-text (merge chunk {:x (:x chunk) :y 0 :align "left"}))]
    (.addChild line-container text)
    {:chunk chunk :text-object text}))

(defn- create-line
  [line text-container props]
  (let [line-container (new-container 0 (:y line))
        chunks (-> (chunks-with-x line props) :chunks)]
    (.addChild text-container line-container)
    (map #(create-chunk % line-container) chunks)))

(defn create-chunked-text
  [{:keys [x y] :as props}]
  (let [text-container (new-container x y)
        lines (lines-with-y props)]
    {:text-container text-container
     :chunks         (mapcat #(create-line % text-container props) lines)}))
