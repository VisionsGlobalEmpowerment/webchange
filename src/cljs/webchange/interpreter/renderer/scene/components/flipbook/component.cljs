(ns webchange.interpreter.renderer.scene.components.flipbook.component
  (:require
    [webchange.interpreter.pixi :refer [Container]]
    [webchange.interpreter.renderer.scene.components.flipbook.wrapper :refer [wrap]]
    [webchange.interpreter.renderer.scene.components.utils :as utils]))

(def default-props {:x            {}
                    :y            {}
                    :width        {}
                    :height       {}
                    :ref          {}
                    :pages        {}
                    :prev-control {}
                    :next-control {}
                    :type         {:default "flipbook"}})

(defn- create-container
  [{:keys [x y]}]
  (doto (Container.)
    (utils/set-position {:x x
                         :y y})))

(def component-type "flipbook")

(defn create
  [{:keys [parent type ref] :as props}]
  (let [state (atom props)
        group (create-container props)
        wrapped-flipbook (wrap type (:object-name props) group state)]
    (.addChild parent group)
    (when-not (nil? ref) (ref wrapped-flipbook))
    wrapped-flipbook))
