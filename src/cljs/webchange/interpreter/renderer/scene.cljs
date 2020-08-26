(ns webchange.interpreter.renderer.scene
  (:require
    [cljsjs.pixi]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.interpreter.renderer.group :refer [create-group]]
    [webchange.interpreter.renderer.state.scene :as state]

    [webchange.interpreter.renderer.navigation-menu :refer [create-navigation-menu]]))

(def Application (.. js/PIXI -Application))

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
    (doto (Application. (clj->js {:width  width
                                  :height height}))
      (-> get-stage (set-scale scale-x scale-y))
      (-> get-stage (set-position x y)))))

(defn scene
  [{:keys []}]
  (let [container (atom nil)]
    (r/create-class
      {:display-name "web-gl-scene"

       :component-did-mount
                     (fn [this]
                       (print "[Scene] :component-did-mount")
                       (re-frame/dispatch [::state/init])

                       (let [{:keys [on-ready viewport objects]} (r/props this)
                             app (init-app viewport)]
                         (.appendChild @container (.-view app))

                         (create-group (.-stage app) {:object-name :scene
                                                      :children    objects})
                         (create-navigation-menu {:parent   (.-stage app)
                                                  :viewport viewport})

                         (on-ready)))

       :should-component-update
                     (fn [] false)

       :reagent-render
                     (fn [{:keys []}]
                       (print "[Scene] :reagent-render")
                       [:div {:style {:width  "100%"
                                      :height "100%"}
                              :ref   #(when % (reset! container %))}])})))
