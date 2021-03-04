(ns webchange.interpreter.renderer.scene.components.carousel.component
  (:require
    [webchange.interpreter.pixi :refer [Container Sprite TilingSprite]]
    [webchange.interpreter.renderer.scene.components.utils :as utils]
    [webchange.interpreter.renderer.scene.components.carousel.wrapper :refer [wrap]]
    [webchange.resources.manager :as resources]
    [webchange.interpreter.renderer.scene.app :as app]))

(def default-props {:x      {}
                    :y      {}
                    :width  {}
                    :height {}
                    :name   {}
                    :first  {}
                    :last   {}
                    :next   {}
                    :start  {:default true}
                    :speed  {:default 8}})

(defn- create-container
  [{:keys [name x y]}]
  (doto (Container.)
    (aset "name" (str name "-sprite-container"))
    (utils/set-position {:x x :y y})))

(defn- create-sprite
  [layer-name props]
  (let [resource (resources/get-resource (get props layer-name))]
    (Sprite. (.-texture resource))))

(defn- create-tiling-sprite
  [layer-name {:keys [width height] :as props}]
  (let [resource (resources/get-resource (get props layer-name))]
    (TilingSprite. (.-texture resource) width height)))

(def component-type "carousel")

(defn- init-carousel!
  [first last next {:keys [width]}]
  (let [first-width (-> first .-texture .-width)]
    (set! (.-x first) 0)
    (set! (.-x next) first-width)
    (set! (.-x last) width)))

(defn- start-carousel
  [state first last next {:keys [speed width]}]
  (let [tile-width (-> next .-texture .-width)
        last-width (-> last .-texture .-width)]
    (fn [delta]
      (case @state
        :starting (let [first-x (- (-> first .-x) (* speed delta))
                        next-x (- (-> next .-x) (* speed delta))]
                    (if (> next-x 0)
                      (do
                        (set! (.-x first) first-x)
                        (set! (.-x next) next-x))
                      (do
                        (set! (.-x next) 0)
                        (reset! state :running))))
        :running (let [next-x (- (-> next .-tilePosition .-x) (* speed delta))]
                   (if (< next-x (- tile-width))
                     (-> next .-tilePosition (set! -x (+ next-x tile-width)))
                     (-> next .-tilePosition (set! -x next-x))))
        :stopping (let [next-x (- (-> next .-x) (* speed delta))
                        last-x (- (-> last .-x) (* speed delta))]
                    (if (< (+ last-x last-width) width)
                      (do
                        (set! (.-x next) next-x)
                        (set! (.-x last) last-x))
                      (do
                        (set! (.-x last) 0)
                        (reset! state :stopped))))
        nil))))

(defn create
  "Create `carousel` component.
   Props params:
   :x - component x-position.
   :y - component y-position.
   :width - image width
   :height - image height.
   :name - component name that will be set to sprite and container with corresponding suffixes.
   :first - first image src.
   :last - last image src.
   :next - tiling image src. This image will be repeated multiple times, to cover scene.
   :speed - speed of tile movement {:default 8}
    "
  [{:keys [parent type object-name start] :as props}]
  (let [container (create-container props)
        first (create-sprite :first props)
        last (create-sprite :last props)
        next (create-tiling-sprite :next props)
        state (atom :starting)
        wrapped-image (wrap type object-name container state)]

    (.addChild container first)
    (.addChild container last)
    (.addChild container next)
    (.addChild parent container)

    (init-carousel! first last next props)
    (when start
      (app/add-ticker (start-carousel state first last next props)))

    wrapped-image))
