(ns webchange.interpreter.renderer.scene.components.index
  (:require
    [webchange.interpreter.renderer.scene.components.animation.component :as animation]
    [webchange.interpreter.renderer.scene.components.background.component :as background]
    [webchange.interpreter.renderer.scene.components.button.component :as button]
    [webchange.interpreter.renderer.scene.components.image.component :as image]
    [webchange.interpreter.renderer.scene.components.progress.component :as progress]
    [webchange.interpreter.renderer.scene.components.rectangle.component :as rectangle]
    [webchange.interpreter.renderer.scene.components.slider.component :as slider]
    [webchange.interpreter.renderer.scene.components.text.component :as text]))

(def components (apply hash-map [animation/component-type animation/create
                                 background/component-type background/create
                                 button/component-type button/create
                                 image/component-type image/create
                                 progress/component-type progress/create
                                 rectangle/component-type rectangle/create
                                 slider/component-type slider/create
                                 text/component-type text/create]))
