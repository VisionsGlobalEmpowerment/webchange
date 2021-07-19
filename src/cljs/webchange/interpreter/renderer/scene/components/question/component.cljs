(ns webchange.interpreter.renderer.scene.components.question.component
  (:require
    [webchange.interpreter.pixi :refer [Container Graphics Sprite WHITE]]
    [webchange.interpreter.renderer.scene.components.utils :as utils]
    [webchange.interpreter.renderer.scene.components.question.wrapper :refer [wrap]]))

(def default-props {:question-type         {}
                    :layout                {}
                    :task                  {}
                    :options               {}
                    :correct-answers-count {}})

(defn- create-container
  []
  (Container.))

(def component-type "question")

(defn create
  [{:keys [parent type object-name] :as props}]
  (let [container (create-container)
        wrapped-component (wrap type object-name container props)]

    (.addChild parent container)

    wrapped-component))
