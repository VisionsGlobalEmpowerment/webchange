(ns webchange.interpreter.renderer.scene.components.colors-palette.component
  (:require
    [webchange.interpreter.renderer.pixi :refer [Container Graphics string2hex]]
    [webchange.interpreter.renderer.scene.components.colors-palette.wrapper :refer [wrap]]
    [webchange.interpreter.renderer.scene.components.utils :as utils]))

(def default-props {:x         {}
                    :y         {}
                    :width     {}
                    :height    {}
                    :name      {}
                    :colors    {}
                    :on-change {}
                    :scale     {:default {:x 1 :y 1}}})

(defn- create-container
  [{:keys [x y scale]}]
  (doto (Container.)
    (utils/set-position {:x x :y y})
    (utils/set-scale scale)))

(defn- create-inner-container
  [y]
  (doto (Container.)
    (utils/set-position {:x 0 :y y})))

(defn- create-item
  [width on-click {:keys [x y color]}]
  (let [padding 20
        radius-outer (/ width 2)
        radius-inner (- radius-outer padding)
        shadow-position (+ radius-outer (/ padding 3))]
    (doto (Graphics.)
      (utils/set-position {:x x :y y})
      (utils/set-handler "click" #(on-click {:color (string2hex color)}))

      (.beginFill 0xe0e0e0)
      (.drawCircle shadow-position shadow-position radius-outer)
      (.endFill)

      (.beginFill 0xffffff)
      (.drawCircle radius-outer radius-outer radius-outer)
      (.endFill)

      (.beginFill (string2hex color))
      (.drawCircle radius-outer radius-outer radius-inner)
      (.endFill))))

(defn- top-list-margin
  "(height - ((width + padding) * n - padding)) / 2"
  [width height padding n]
  (let [total-items-height (+ (* width n) (* padding (- n 1)))]
    (/ (- height total-items-height) 2)))

(def component-type "colors-palette")

(defn create
  [parent {:keys [type object-name width height colors on-change] :as props}]
  (let [padding 10
        idx->params (fn [idx] {:x     0
                               :y     (* idx (+ width padding))
                               :color (get colors idx)})
        n (count colors)
        params (->> (range n)
                    (map idx->params))

        container (create-container props)
        inner-container (create-inner-container (top-list-margin width height padding n))
        wrapped-container (wrap type object-name container)]

    (doall (->> params
                (map #(create-item width on-change %))
                (map #(.addChild inner-container %))))

    (.addChild container inner-container)
    (.addChild parent container)

    wrapped-container))
