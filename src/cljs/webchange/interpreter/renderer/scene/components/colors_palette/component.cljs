(ns webchange.interpreter.renderer.scene.components.colors-palette.component
  (:require
    [webchange.interpreter.pixi :refer [Container Graphics string2hex]]
    [webchange.interpreter.renderer.scene.components.colors-palette.wrapper :refer [wrap]]
    [webchange.interpreter.renderer.scene.components.utils :as utils]
    [webchange.interpreter.renderer.scene.filters.filters :refer [apply-filters]]))

(def default-props {:x             {:default 0}
                    :y             {:default 50}
                    :width         {:default 1080}
                    :height        {:default 180}
                    :name          {}
                    :colors        {:default ["#4479bb" "#92bd4a" "#ed91aa" "#fdc531" "#010101"]}
                    :on-change     {}
                    :ref           {}
                    :default-color {:default "#4479bb"}
                    :filters       {:default [{:name "brightness" :value 0}]}
                    :scale         {:default {:x 1 :y 1}}})

(def active-y 20)
(def inactive-y 0)

(defn- create-container
  [{:keys [x y scale filters]}]
  (doto (Container.)
    (utils/set-position {:x x :y y})
    (utils/set-scale scale)
    (apply-filters filters)))

(defn- create-inner-container
  [x]
  (doto (Container.)
    (utils/set-position {:x x :y 0})))

(defn- activate
  [state active-color]
  (doall
    (for [[color-name graphics] (:colors @state)]
      (if (= active-color color-name)
        (utils/set-position graphics {:y active-y})
        (utils/set-position graphics {:y inactive-y})))))

(defn- create-item!
  [height on-click {:keys [x color]} state]
  (let [padding 20
        radius-outer (/ height 2)
        radius-inner (- radius-outer padding)
        shadow-position (+ radius-outer (/ padding 3))
        item (doto (Graphics.)
               (utils/set-position {:x x :y inactive-y})
               (utils/set-handler "click" (fn []
                                            (activate state color)
                                            (on-click {:color (string2hex color)})))

               (.beginFill 0xe0e0e0)
               (.drawCircle shadow-position shadow-position radius-outer)
               (.endFill)

               (.beginFill 0xffffff)
               (.drawCircle radius-outer radius-outer radius-outer)
               (.endFill)

               (.beginFill (string2hex color))
               (.drawCircle radius-outer radius-outer radius-inner)
               (.endFill))]
    (swap! state assoc-in [:colors color] item)
    item))

(defn- left-list-margin
  "(width - ((height + padding) * n - padding)) / 2"
  [width height padding n]
  (let [total-items-width (+ (* height n) (* padding (- n 1)))]
    (/ (- width total-items-width) 2)))

(def component-type "colors-palette")

(defn create
  "Create `colors-palette` component.

  Props params:
  :x - component x-position.
  :y - component y-position.
  :width - image width.
  :height - image height.
  :scale - image scale. Default: {:x 1 :y 1}.
  :name - component name that will be set to sprite and container with corresponding suffixes.
  :on-change - on change event handler.
  :colors - array of colors in palette."
  [{:keys [parent type object-name width height colors on-change ref default-color] :as props}]
  (let [padding 10
        idx->params (fn [idx] {:x     (* idx (+ height padding))
                               :color (get colors idx)})
        n (count colors)
        params (->> (range n)
                    (map idx->params))

        container (create-container props)
        state (atom {:colors {}})
        inner-container (create-inner-container (left-list-margin width height padding n))
        wrapped-container (wrap type object-name container)]

    (doall (->> params
                (map #(create-item! height on-change % state))
                (map #(.addChild inner-container %))))

    (.addChild container inner-container)
    (.addChild parent container)

    (when-not (nil? ref) (ref wrapped-container))

    (activate state default-color)
    wrapped-container))
