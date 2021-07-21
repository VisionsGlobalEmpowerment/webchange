(ns webchange.interpreter.renderer.scene.components.animation.animation-params
  (:require
    [webchange.logger.index :as logger]))

(def animations-params {:vera       {:width  380,
                                     :height 537,
                                     :scale  {:x 1, :y 1},
                                     :speed  1
                                     :meshes true
                                     :skin   "01 Vera_1"}
                        :senoravaca {:width  351,
                                     :height 717,
                                     :scale  {:x 1, :y 1}
                                     :speed  1
                                     :meshes true
                                     :skin   "vaca"}
                        :teacher    {:width  630,
                                     :height 1308,
                                     :scale  {:x 0.5, :y 0.5}
                                     :speed  1
                                     :meshes true
                                     :skin   "default"}
                        :mari       {:width  910,
                                     :height 601,
                                     :scale  {:x 0.5, :y 0.5}
                                     :speed  0.8
                                     :meshes true
                                     :skin   "01 mari"}
                        :guide      {:width  591,
                                     :height 591,
                                     :scale  {:x 0.5, :y 0.5}
                                     :speed  1
                                     :meshes true
                                     :skin   "default"}
                        :boxes      {:speed 1}
                        :child      {:width  342,
                                     :height 691,
                                     :scale  {:x 0.5 :y 0.5}
                                     :speed  1
                                     :meshes true}})

(defn get-animations-resource-path
  [anim-name file]
  (logger/trace "get animations resource path" anim-name file)
  (let [resources-prefix "/raw/anim/"]
    (str resources-prefix (name anim-name) "/" file)))

(defn get-animations-resources
  []
  (logger/group-folded "get animations resources")
  (let [animations {:book       {:skeleton-images 3}
                    :boxes      {:skeleton-images 3}
                    :hat        {:skeleton-images 1}
                    :mari       {:skeleton-images 3}
                    :pinata     {:skeleton-images 1}
                    :rock       {:skeleton-images 1}
                    :senoravaca {:skeleton-images 6}
                    :vera       {:skeleton-images 5}
                    :vera-45    {:skeleton-images 8}
                    :vera-90    {:skeleton-images 4}
                    :vera-go    {:skeleton-images 3}}]
    (->> animations
         (map (fn [[anim {:keys [skeleton-images]}]]
                [anim (concat [(get-animations-resource-path (name anim) "skeleton.json")
                               (get-animations-resource-path (name anim) "skeleton.atlas")
                               (get-animations-resource-path (name anim) "skeleton.png")]
                              (map #(get-animations-resource-path (name anim) (str "skeleton" % ".png"))
                                   (range 2 (inc skeleton-images))))]))
         (into {})
         (logger/->>with-group-end "get animations resources"))))
