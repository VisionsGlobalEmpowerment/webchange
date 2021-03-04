(ns webchange.interpreter.renderer.scene.components.timer.utils)

(defn time->min-sec
  [time]
  {:minutes (quot time 60)
   :seconds (mod time 60)})
