(ns webchange.editor-v2.scene.views-canvas
  (:require
    [re-frame.core :as re-frame]
    [webchange.subs :as subs]
    [webchange.interpreter.components :as i]))

(defn scene-canvas
  []
  (let [scale 0.3
        width (* 1920 scale)
        height (* 1080 scale)
        scene-id (re-frame/subscribe [::subs/current-scene])]
    [:div {:style {:position "relative"
                   :top      0
                   :left     0
                   :width    width
                   :height   height}}
     [i/stage-wrapper {:scene-id scene-id}]]))
