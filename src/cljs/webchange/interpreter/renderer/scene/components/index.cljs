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

(def components (apply hash-map [animation/component-type {:constructor   animation/create
                                                           :default-props animation/default-props}
                                 background/component-type {:constructor   background/create
                                                            :default-props background/default-props}
                                 button/component-type {:constructor   button/create
                                                        :default-props button/default-props}
                                 image/component-type {:constructor   image/create
                                                       :default-props image/default-props}
                                 progress/component-type {:constructor   progress/create
                                                          :default-props progress/default-props}
                                 rectangle/component-type {:constructor   rectangle/create
                                                           :default-props rectangle/default-props}
                                 slider/component-type {:constructor   slider/create
                                                        :default-props slider/default-props}
                                 text/component-type {:constructor   text/create
                                                      :default-props text/default-props}]))
