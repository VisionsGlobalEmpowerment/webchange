(ns webchange.editor-v2.scene.views-canvas
  (:require
    [re-frame.core :as re-frame]
    [webchange.subs :as subs]
    [webchange.interpreter.components :as i]
    [webchange.interpreter.subs :as isubs]
    [webchange.editor-v2.scene.state.stage :as scene-state]
    [webchange.interpreter.renderer.scene.components.modes :as modes]))

(defn scene-canvas
  []
  (let [scale 0.3
        width (* 1920 scale)
        height (* 1080 scale)
        scene-id @(re-frame/subscribe [::subs/current-scene])
        scene-data @(re-frame/subscribe [::subs/scene scene-id])
        dataset-items @(re-frame/subscribe [::isubs/dataset-items])
        stage-key @(re-frame/subscribe [::scene-state/stage-key])]
    ^{:key stage-key}
    [:div {:style {:position "relative"
                   :top      0
                   :left     0
                   :width    width
                   :height   height}}
     [i/stage-wrapper {:mode          ::modes/editor
                       :scene-id      scene-id
                       :scene-data    scene-data
                       :dataset-items dataset-items}]]))
