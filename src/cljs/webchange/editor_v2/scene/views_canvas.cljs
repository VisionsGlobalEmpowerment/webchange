(ns webchange.editor-v2.scene.views-canvas
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor.index :as e]
    [webchange.subs :as subs]))

(defn scene-canvas
  []
  (let [scale 0.3
        width (* 1920 scale)
        height (* 1080 scale)
        scene-id (re-frame/subscribe [::subs/current-scene])
        loaded (re-frame/subscribe [::subs/scene-loading-complete @scene-id])]
    (if @loaded
      [e/with-stage [e/scene] {:width   width
                               :height  height
                               :scale-x scale
                               :scale-y scale}
       scene-id]
      [e/with-stage [e/preloader] {:width   width
                               :height  height
                               :scale-x scale
                               :scale-y scale}
       scene-id]
      )))
