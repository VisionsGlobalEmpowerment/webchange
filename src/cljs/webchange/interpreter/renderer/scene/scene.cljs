(ns webchange.interpreter.renderer.scene.scene
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.interpreter.renderer.pixi :refer [Application]]
    [webchange.interpreter.renderer.scene.components.create-component :refer [create-component]]
    [webchange.interpreter.renderer.state.scene :as state]
    [webchange.interpreter.renderer.overlays.index :refer [create-overlays]]
    [webchange.interpreter.renderer.scene.app :refer [register-app]]))

(defn- get-stage
  [app]
  (.-stage app))

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

(defn init-app
  [viewport]
  (let [{:keys [x y width height scale-x scale-y]} viewport]
    (doto (Application. (clj->js {:antialias true
                                  :width     width
                                  :height    height}))
      (-> get-stage (set-scale scale-x scale-y))
      (-> get-stage (set-position x y))
      (register-app))))

(defn scene
  [{:keys []}]
  (let [container (atom nil)]
    (r/create-class
      {:display-name "web-gl-scene"

       :component-did-mount
                     (fn [this]
                       (re-frame/dispatch [::state/init])

                       (print "SCENE MOUNT")

                       (let [{:keys [on-ready viewport objects]} (r/props this)
                             app (init-app viewport)]
                         (.appendChild @container (.-view app))

                         (create-component (.-stage app) {:type        "group"
                                                          :object-name :scene
                                                          :children    objects})

                         (create-overlays {:parent   (.-stage app)
                                           :viewport viewport})
                         (on-ready)))

       :should-component-update
                     (fn [] false)

       :reagent-render
                     (fn [{:keys []}]
                       [:div {:style {:width  "100%"
                                      :height "100%"}
                              :ref   #(when % (reset! container %))}])})))
