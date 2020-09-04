(ns webchange.interpreter.renderer.scene.components.text.component
  (:require
    [webchange.interpreter.renderer.pixi :refer [Container Text]]
    [webchange.interpreter.renderer.scene.components.utils :as utils]
    [webchange.interpreter.renderer.scene.components.text.wrapper :refer [wrap]]
    [webchange.interpreter.renderer.scene.components.text.chunks :refer [lines-with-y chunks-with-x chunk-transition-name]]

    [re-frame.core :as re-frame]
    [webchange.interpreter.events-register :as ier]))

(def default-props {:x               {}
                    :y               {}
                    :text            {}
                    :font-family     {:default "Liberation Sans"}
                    :font-size       {:default 12}
                    :fill            {:default "#000000"}
                    :shadow-color    {}
                    :shadow-distance {}
                    :shadow-blur     {}
                    :shadow-opacity  {}
                    :align           {:default "left"}
                    :vertical-align  {:default "bottom"}
                    :font-weight     {:default "normal"}
                    :chunks          {}
                    :width           {}
                    :height          {}})

(defn- create-text
  [{:keys [x y text align vertical-align font-family font-size font-weight fill shadow-color shadow-distance shadow-blur shadow-opacity]}]
  (let [text (doto (Text. text (clj->js {:fontSize   font-size
                                         :fontFamily font-family
                                         :fontWeight font-weight
                                         :fill       fill}))
               (utils/set-position {:x x
                                    :y y}))]
    (when-not (nil? shadow-color)
      (aset (.-style text) "dropShadow" true)
      (aset (.-style text) "dropShadowColor" shadow-color)
      (aset (.-style text) "dropShadowDistance" shadow-distance)
      (aset (.-style text) "dropShadowBlur" shadow-blur)
      (aset (.-style text) "dropShadowAlpha" shadow-opacity))

    (case align
      "left" (aset (.-anchor text) "x" 0)
      "center" (aset (.-anchor text) "x" 0.5)
      "right" (aset (.-anchor text) "x" 1))

    (case vertical-align
      "top" (aset (.-anchor text) "y" 0)
      "middle" (aset (.-anchor text) "y" 0.5)
      "bottom" (aset (.-anchor text) "y" 1))
    text))

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
     :chunks (mapcat #(create-line % text-container props) lines)}))

(defn- register-chunk
  [{:keys [chunk text-object]} type object-name]
  (let [chunk-name (chunk-transition-name (name object-name) (:index chunk))
        wrapper (wrap type (keyword chunk-name) text-object nil)]
    (re-frame/dispatch [::ier/register-transition chunk-name (atom wrapper)])))

(def component-type "text")

(defn create
  [parent {:keys [type object-name chunks] :as props}]
  (if chunks
    (let [{:keys [text-container chunks]} (create-chunked-text props)
          wrapped-text (wrap type object-name text-container chunks)]
      (.addChild parent text-container)

      (doall (map #(register-chunk % type object-name) chunks))

      wrapped-text)
    (let [text (create-text props)
          wrapped-text (wrap type object-name text nil)]
      (.addChild parent text)

      wrapped-text)))
