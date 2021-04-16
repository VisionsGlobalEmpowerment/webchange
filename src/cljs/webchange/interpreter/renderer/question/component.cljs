(ns webchange.interpreter.renderer.question.component
  (:require
    [webchange.interpreter.pixi :refer [Container Graphics Sprite WHITE]]
    [webchange.interpreter.renderer.scene.components.utils :as utils :refer [set-handler]]
    [webchange.interpreter.renderer.scene.filters.filters :refer [apply-filters]]
    [webchange.interpreter.renderer.question.wrapper :refer [wrap]]
    [webchange.interpreter.renderer.question.question-page.type-1 :as type-1]
    [webchange.interpreter.renderer.question.question-page.type-2 :as type-2]))

(def default-props {:x        {}
                    :y        {}
                    :ref      {}
                    :on-click {}
                    :type     {:default "group"}
                    :pages    []})

(def component-type "questions")

(defn object-name [name object-name] (keyword (str name "-" object-name)))

(defn create-page [page db]
  (case (:type page)
    "type-1" (type-1/create-page page db)
    "type-2" (type-2/create-page page db)))

(defn create
  [props db]
  (create-page props db))
