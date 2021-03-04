(ns webchange.interpreter.renderer.scene.components.timer.clock.utils
  (:require
    [webchange.interpreter.pixi :refer [TextMetrics TextStyle]]))

(defn measure
  [text {:keys [font-weight font-size font-family]}]
  (let [style (TextStyle. (clj->js {:fontSize   font-size
                                    :fontFamily font-family
                                    :fontWeight font-weight}))
        metrics (.measureText TextMetrics text style)]
    {:width  (.-width metrics)
     :height (.-height metrics)}))
