(ns webchange.interpreter.renderer.scene.components.text.component
  (:require
    [webchange.interpreter.pixi :refer [Container Text]]
    [webchange.interpreter.renderer.scene.components.utils :as utils]
    [webchange.interpreter.renderer.scene.components.text.wrapper :refer [wrap]]
    [webchange.interpreter.renderer.scene.components.text.chunks :refer [lines-with-y chunks-with-x chunk-transition-name]]

    [re-frame.core :as re-frame]
    [webchange.interpreter.events-register :as ier]))

(def default-props {:x               {}
                    :y               {}
                    :text            {}
                    :font-family     {:default "Liberation Sans"}
                    :font-size       {}
                    :fill            {:default "#000000"}
                    :shadow-color    {}
                    :skew-x           {:default 0}
                    :skew-y           {:default 0}
                    :shadow-distance {:default 5}
                    :shadow-blur     {}
                    :shadow-opacity  {}
                    :scale           {}
                    :align           {:default "left"}
                    :vertical-align  {:default "bottom"}
                    :font-weight     {:default "normal"}
                    :chunks          {}
                    :width           {:default 0}
                    :word-wrap       {:default false}
                    :height          {:default 0}
                    :on-click        {}
                    :ref             {}})

(defn- calculate-position
  [{:keys [x y width height align vertical-align]}]
  {:x (case align
        "left" x
        "center" (+ x (/ width 2))
        "right" (+ x width))
   :y (case vertical-align
        "top" y
        "middle" (+ y (/ height 2))
        "bottom" (+ y height))})

(defn- set-shadow
  [text {:keys [shadow-color shadow-distance shadow-blur shadow-opacity]}]
  (when-not (nil? shadow-color)
    (aset (.-style text) "dropShadow" true)
    (aset (.-style text) "dropShadowColor" shadow-color)
    (aset (.-style text) "dropShadowDistance" shadow-distance)
    (aset (.-style text) "dropShadowBlur" shadow-blur)
    (aset (.-style text) "dropShadowAlpha" shadow-opacity)))

(defn- set-align
  [text {:keys [align vertical-align]}]
  (case align
    "left" (aset (.-anchor text) "x" 0)
    "center" (aset (.-anchor text) "x" 0.5)
    "right" (aset (.-anchor text) "x" 1))

  (case vertical-align
    "top" (aset (.-anchor text) "y" 0)
    "middle" (aset (.-anchor text) "y" 0.5)
    "bottom" (aset (.-anchor text) "y" 1)))

(defn- set-skew
  [display-object skew-x skew-y]
  (.setTransform display-object 0 0 1 1 0 skew-x skew-y 0 0))


(defn set-scale
  [object scale]
  (when (some? scale)
    (utils/set-scale object scale)))

(defn- create-text
  [{:keys [text font-family font-size font-weight fill scale skew-x skew-y width word-wrap] :as props}]
  (let [position (calculate-position props)]
    (doto (Text. text (clj->js (cond-> {:fontFamily    font-family
                                        :fontWeight    font-weight
                                        :fill          fill}
                                       (some? font-size) (assoc :fontSize font-size)
                                       (true? word-wrap) (-> (assoc :wordWrap true)
                                                             (assoc :wordWrapWidth width)))))
      (set-skew skew-x skew-y)
      (utils/set-position position)
      (set-scale scale)
      (set-shadow props)
      (set-align props))))

(defn- new-container
  [x y]
  (doto (Container.)
    (utils/set-position {:x x :y y})))

(defn- create-chunk
  [chunk line-container]
  (let [text (create-text (merge chunk {:x (:x chunk) :y 0 :align "left"}))]
    (.addChild line-container text)
    {:chunk chunk :text-object text}))

(defn- create-line
  [line text-container props]
  (let [line-container (new-container 0 (:y line))
        chunks (-> (chunks-with-x line props) :chunks)]
    (.addChild text-container line-container)
    (map #(create-chunk % line-container) chunks)))

(defn- create-chunked-text
  [{:keys [x y] :as props}]
  (let [text-container (new-container x y)
        lines (lines-with-y props)]
    {:text-container text-container
     :chunks         (mapcat #(create-line % text-container props) lines)}))

(defn- register-chunk
  [{:keys [chunk text-object]} type object-name]
  (let [chunk-name (chunk-transition-name (name object-name) (:index chunk))
        wrapper (wrap type (keyword chunk-name) text-object nil)]
    (re-frame/dispatch [::ier/register-transition chunk-name (atom wrapper)])))

(def component-type "text")

(defn create
  "Create `text` component.

  Props params:
  :x - component x-position.
  :y - component y-position.
  :scale - image scale. Default: {:x 1 :y 1}.
  :text - text string.
  :font-family - specify font family. Default: Liberation Sans.
  :font-size - font size. Default 12.
  :fill - fill color. Default #000000
  :shadow-color - color of text shadow
  :shadow-distance - distance shadow. Default 5
  :shadow-blur - blur of shadow to make it more smooth.
  :shadow-opacity - opacity of text shadows.
  :align -  horizontally positioning of text element. Default left
  :vertical-align -  vertical positioning of text element. Default bottom.
  :font-weight - sets the weight (or boldness) of the font. Default normal
  :chunks - array of chunks. This is usually margins of words, may be used in text animation component to show what part of text should be animated.
            Every chunk has start and end
     :start - number of symbol where curent chunk starts
     :end - number of symbol where curent chunk starts
  :width - text block width. Default 0
  :height - text block height. Default 0
  :on-click - on click event handler.
  :ref - callback function that must be called with component wrapper.
  "
  [{:keys [parent type object-name chunks on-click ref] :as props}]
  (if chunks
    (let [{:keys [text-container chunks]} (create-chunked-text props)
          wrapped-text (wrap type object-name text-container chunks)]
      (.addChild parent text-container)

      (doall (map #(register-chunk % type object-name) chunks))

      (when-not (nil? on-click) (utils/set-handler text-container "click" on-click))

      wrapped-text)
    (let [text (create-text props)
          wrapped-text (wrap type object-name text nil)]

      (.addChild parent text)

      (when-not (nil? on-click) (utils/set-handler text "click" on-click))
      (when-not (nil? ref) (ref wrapped-text))

      wrapped-text)))
