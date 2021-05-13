(ns webchange.editor-v2.activity-form.common.interpreter-stage.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.subs :as subs]
    [webchange.interpreter.components :as i]
    [webchange.interpreter.subs :as isubs]
    [webchange.state.state-flipbook :as state-flipbook]
    [webchange.interpreter.renderer.scene.modes.modes :as modes]))

(def stage-width 1920)
(def stage-height 1080)

(defn- scene-ready-handler
  [{:keys [scene-data]}]
  (let [metadata (get scene-data :metadata {})
        flipbook? (contains? metadata :flipbook-name)]
    (when flipbook?
      (re-frame/dispatch [::state-flipbook/generate-stages-screenshots]))))

(defn get-stage-size
  [{:keys [width height]}]
  (let [scale-w (if (some? width) (/ width stage-width) ##Inf)
        scale-h (if (some? height) (/ height stage-height) ##Inf)
        scale (or (Math/min scale-w scale-h) 1)]
    {:width  (-> (* stage-width scale) (Math/round))
     :height (-> (* stage-height scale) (Math/round))}))

(defn stage
  []
  (let [scene-id @(re-frame/subscribe [::subs/current-scene])
        scene-data @(re-frame/subscribe [::subs/scene scene-id])
        dataset-items @(re-frame/subscribe [::isubs/dataset-items])
        stage-key @(re-frame/subscribe [::state-flipbook/stage-key])]
    ^{:key stage-key}
    [i/stage-wrapper {:mode          ::modes/editor
                      :scene-id      scene-id
                      :scene-data    scene-data
                      :dataset-items dataset-items
                      :on-ready      #(scene-ready-handler {:scene-data scene-data})}]))

(defn interpreter-stage
  []
  (let [scale 0.3
        width (* stage-width scale)
        height (* stage-height scale)]
    [:div {:style {:position "relative"
                   :top      0
                   :left     0
                   :width    width
                   :height   height}}
     [stage]]))
