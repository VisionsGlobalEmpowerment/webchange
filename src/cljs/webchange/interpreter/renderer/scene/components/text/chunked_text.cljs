(ns webchange.interpreter.renderer.scene.components.text.chunked-text
  (:require
    [webchange.interpreter.pixi :refer [Container]]
    [webchange.interpreter.object-data.group-params :refer [prepare-actions]]
    [webchange.interpreter.renderer.scene.components.text.chunks :refer [lines-with-y chunks-with-x]]
    [webchange.interpreter.renderer.scene.components.text.simple-text :refer [create-simple-text]]
    [webchange.interpreter.renderer.scene.components.utils :as utils]))

(defn new-container
  [x y]
  (doto (Container.)
    (utils/set-position {:x x :y y})))

(defn- create-chunk
  [chunk line-container]
  (let [text (-> chunk
                 (merge {:y     0
                         :align "left"})
                 (prepare-actions)
                 (create-simple-text))]
    (.addChild line-container text)
    {:chunk chunk :text-object text}))

(defn- create-line
  [line text-container props]
  (let [line-container (new-container 0 (:y line))
        chunks (-> (chunks-with-x line props) :chunks)]
    (.addChild text-container line-container)
    (map #(create-chunk % line-container) chunks)))

(defn- map-chunk-keywords
  [chunks {:keys [text]}]
  (map (fn [{:keys [end] :as chunk}]
         (cond-> chunk
                 (= end "last") (assoc :end (count text))))
       chunks))

(defn create-chunked-text
  [text-container props]
  (let [fixed-props (-> props
                        (update :chunks map-chunk-keywords props))
        lines (lines-with-y fixed-props)]
    (mapcat #(create-line % text-container fixed-props) lines)))
