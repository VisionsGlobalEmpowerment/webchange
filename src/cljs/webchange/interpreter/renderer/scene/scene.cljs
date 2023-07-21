(ns webchange.interpreter.renderer.scene.scene
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.interpreter.pixi :refer [Application]]
    [webchange.interpreter.renderer.overlays.index :refer [create-overlays
                                                           update-viewport]]
    [webchange.interpreter.renderer.question.overlay :as question]
    [webchange.interpreter.renderer.scene.app :refer [app-exists? get-app
                                                      get-renderer get-stage
                                                      register-app remove-all-tickers resize-app!]]
    [webchange.interpreter.renderer.scene.components.collisions :as collisions]
    [webchange.interpreter.renderer.scene.components.create-component :refer [create-component]]
    [webchange.interpreter.renderer.scene.components.utils :as utils]
    [webchange.interpreter.renderer.scene.modes.modes :as modes]
    [webchange.interpreter.renderer.scene.scene-mode :refer [apply-mode
                                                             init-mode-helpers!]]
    [webchange.interpreter.renderer.stage-utils :refer [get-stage-params]]
    [webchange.interpreter.renderer.state.scene :as state]
    [webchange.logger.index :as logger]))

(defn- init-app
  [viewport mode]
  (if (app-exists?)
    (get-app)
    (let [{:keys [width height]} viewport]
      (doto (Application. (clj->js (cond-> {;:antialias  true
                                            :autoDensity true
                                            :width       width
                                            :height      height
                                            :transparent true}
                                           (= mode ::modes/editor) (assoc :preserveDrawingBuffer true))))
        (resize-app! viewport)
        (register-app)))))

(defn- handle-renderer-resize
  [new-width new-height]
  (let [viewport (get-stage-params {:width  new-width
                                    :height new-height})]
    (update-viewport viewport)))

(defn- register-handler
  [object event handler]
  (when (some? object)
    (.on object event handler)))

(defn- unregister-handler
  [object event handler]
  (when (some? object)
    (.off object event handler)))

(defn- enable-gestures!
  [container trigger]
  (let [last-pos (atom nil)]
    (utils/set-handler container "pointerdown" #(reset! last-pos {:x (-> % .-data .-global .-x)
                                                                  :y (-> % .-data .-global .-y)}))
    (utils/set-handler container "pointerup" #(let [new-pos (-> % .-data .-global)
                                                    x (- (.-x new-pos) (:x @last-pos))
                                                    y (- (.-y new-pos) (:y @last-pos))]
                                                (cond
                                                  (and (> x 150) (< y 50) (> y -50)) (trigger :swipe-right)
                                                  (and (< x -150) (< y 50) (> y -50)) (trigger :swipe-left)
                                                  (and (> y 150) (< x 50) (> x -50)) (trigger :swipe-down)
                                                  (and (< y -150) (< x 50) (> x -50)) (trigger :swipe-up)
                                                  :else nil)))))

(defn scene
  [{:keys []}]
  (let [container (atom nil)
        scene-container (atom nil)]
    (r/create-class
     {:display-name "web-gl-scene"

      :component-did-mount
      (fn [this]
        (re-frame/dispatch [::state/init (-> (r/props this) :activity-data)])
        (re-frame/dispatch [::state/set-rendering-state true])

        (let [{:keys [mode on-ready trigger stage-id viewport objects metadata]} (r/props this)
              app (init-app viewport mode)]
          (logger/trace-folded "scene mounted" viewport mode)
          (.appendChild @container (.-view app))
          (reset! scene-container (-> (create-component {:type        "group"
                                                         :object-name :scene
                                                         :parent      (.-stage app)
                                                         :children    (apply-mode objects mode)
                                                         :mode        mode})
                                      (init-mode-helpers! mode {:stage-id stage-id})
                                      (get-in [:wrapper :object])))

          (when trigger
            (enable-gestures! @scene-container trigger))

          (-> (get-renderer)
              (register-handler "resize" handle-renderer-resize))
          (create-overlays {:parent   (get-stage)
                            :viewport viewport
                            :mode     mode
                            :metadata metadata})

          (create-component (question/create {:parent   (get-stage)
                                              :viewport viewport}))

          (re-frame/dispatch [::state/set-scene-layered-objects (map :object-name objects)])
          (re-frame/dispatch [::state/set-rendering-state false])
          (when (fn? on-ready) (on-ready))))

      :component-will-unmount
      (fn []
        (remove-all-tickers)
        (collisions/reset-ticker)
        (.destroy @scene-container (clj->js {:children true}))
        (-> (get-renderer)
            (unregister-handler "resize" handle-renderer-resize)))

      :should-component-update
      (fn [] false)

      :reagent-render
      (fn [{:keys []}]
        [:div {:style {:position "absolute"
                       :width    "100%"
                       :height   "100%"}
               :ref   #(when % (reset! container %))}])})))
