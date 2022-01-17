(ns webchange.interpreter.renderer.scene.scene
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.interpreter.pixi :refer [Application clear-texture-cache]]
    [webchange.interpreter.renderer.scene.components.create-component :refer [create-component]]
    [webchange.interpreter.renderer.state.scene :as state]
    [webchange.interpreter.renderer.question.overlay :as question]
    [webchange.interpreter.renderer.overlays.index :refer [create-overlays update-viewport]]
    [webchange.interpreter.renderer.scene.app :refer [app-exists? get-app register-app get-renderer get-stage]]
    [webchange.interpreter.renderer.scene.modes.modes :as modes]
    [webchange.interpreter.renderer.scene.scene-mode :refer [init-mode-helpers! apply-mode]]
    [webchange.interpreter.renderer.stage-utils :refer [get-stage-params]]
    [webchange.logger.index :as logger]))

(defn- set-position
  [stage x y]
  (doto (.-position stage)
    (aset "x" x)
    (aset "y" y)))

(defn- set-scale
  ([stage scale]
   (set-scale stage scale scale))
  ([stage scale-x scale-y]
   (doto (.-scale stage)
     (aset "x" scale-x)
     (aset "y" scale-y))))

(defn- init-app
  [viewport mode]
  (if (app-exists?)
    (get-app)
    (let [{:keys [x y width height scale-x scale-y]} viewport]
      (doto (Application. (clj->js (cond-> {;:antialias  true
                                            :autoDensity true
                                            :width       width
                                            :height      height
                                            :transparent true}
                                           (= mode ::modes/editor) (assoc :preserveDrawingBuffer true))))
        (-> get-stage (set-scale scale-x scale-y))
        (-> get-stage (set-position x y))
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

(defn scene
  [{:keys []}]
  (let [container (atom nil)]
    (r/create-class
      {:display-name "web-gl-scene"

       :component-did-mount
                     (fn [this]
                       (re-frame/dispatch [::state/init])
                       (re-frame/dispatch [::state/set-rendering-state true])

                       (let [{:keys [mode on-ready viewport objects metadata]} (r/props this)
                             app (init-app viewport mode)]
                         (logger/trace-folded "scene mounted" viewport mode)
                         (.appendChild @container (.-view app))
                         (-> (create-component {:type        "group"
                                                :object-name :scene
                                                :parent      (.-stage app)
                                                :children    (apply-mode objects mode)
                                                :mode        mode})
                             (init-mode-helpers! mode))

                         (-> (get-renderer)
                             (register-handler "resize" handle-renderer-resize))
                         (create-overlays {:parent   (get-stage)
                                           :viewport viewport
                                           :mode     mode
                                           :metadata metadata})

                         (create-component (question/create {:parent   (get-stage)
                                                             :viewport viewport}))

                         (re-frame/dispatch [::state/set-rendering-state false])
                         (when (fn? on-ready) (on-ready))))

       :component-will-unmount
                     (fn []
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
