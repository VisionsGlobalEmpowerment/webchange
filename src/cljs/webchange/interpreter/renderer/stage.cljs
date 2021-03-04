(ns webchange.interpreter.renderer.stage
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.interpreter.pixi :refer [Loader]]
    [webchange.interpreter.renderer.loader-screen :refer [loader-screen]]
    [webchange.interpreter.renderer.waiting-screen :refer [waiting-screen]]
    [webchange.interpreter.renderer.scene.scene :refer [scene]]
    [webchange.interpreter.renderer.scene.app :refer [reset-app! resize-app!]]
    [webchange.resources.manager :as resources]
    [webchange.interpreter.renderer.stage-utils :refer [get-stage-params]]
    [webchange.interpreter.renderer.state.overlays :as overlays]
    [webchange.interpreter.renderer.scene.modes.modes :as modes]
    [webchange.interpreter.variables.core :as vars.core]))

(defn- init-scene
  [{:keys [scene-id resources]} current-scene-id loading]
  (when (or (vars.core/get-global-variable :force-scene-update)
            (not= scene-id @current-scene-id))
    (vars.core/set-global-variable! :force-scene-update false)
    (reset! current-scene-id scene-id)
    (reset! loading {:done false :progress 0})
    (reset-app!)
    (resources/reset-loader!)
    (resources/load-resources resources
                              {:on-complete #(swap! loading assoc :done true)
                               :on-progress #(swap! loading assoc :progress %)})))

(defn- element->viewport
  [el]
  (when-not (nil? el)
    (let [bound-rect (.getBoundingClientRect el)]
      {:width  (.-width bound-rect)
       :height (.-height bound-rect)})))

(defn- handle-screen-resize
  [container]
  (let [viewport (-> (element->viewport @container)
                     (get-stage-params))]
    (resize-app! viewport)))

(defn- get-handler
  [f & args]
  (apply partial (concat [f] args)))

(defn- overlay-wrapper
  [{:keys [viewport]}]
  (let [scale-x (:scale-x viewport)
        scale-y (:scale-y viewport)
        translate-x (* -100 (- (/ 1 (* 2 scale-x)) 0.5))
        translate-y (* -100 (- (/ 1 (* 2 scale-y)) 0.5))
        this (r/current-component)]
    (into [:div {:style {:position  "absolute"
                         :x         0
                         :y         0
                         :transform (str "scale(" scale-x ", " scale-y ")"
                                         "translate(" translate-x "%, " translate-y "%)"
                                         "translate(" (:x viewport) "px, " (:y viewport) "px)")
                         :width     (:target-width viewport)
                         :height    (:target-height viewport)}}]
          (r/children this))))

(defn stage
  []
  (let [container (r/atom nil)
        loading (r/atom {:done     false
                         :progress 0})
        current-scene-id (r/atom nil)
        handle-resize (get-handler handle-screen-resize container)]
    (r/create-class
      {:display-name "web-gl-scene"
       :component-did-mount
                     (fn [this]
                       (.addEventListener js/window "resize" handle-resize)
                       (resources/init (.-shared Loader))
                       (let [{:keys [scene-data]} (r/props this)]
                         (init-scene scene-data current-scene-id loading)))
       :component-will-unmount
                     (fn []
                       (.removeEventListener js/window "resize" handle-resize))
       :component-did-update
                     (fn [this]
                       (let [{:keys [scene-data]} (r/props this)]
                         (init-scene scene-data current-scene-id loading)))
       :reagent-render
                     (fn [{:keys [mode on-ready on-start-click scene-data]}]
                       (let [viewport (-> (element->viewport @container)
                                          (get-stage-params))
                             show-waiting-screen? @(re-frame/subscribe [::overlays/show-waiting-screen?])]
                         [:div {:ref   #(when % (reset! container (.-parentNode %)))
                                :style {:width  "100%"
                                        :height "100%"}}
                          (when (and (some? viewport)
                                     (:done @loading)
                                     (or (= mode ::modes/editor)
                                         (:started? scene-data)))
                            [scene {:mode     mode
                                    :objects  (:objects scene-data)
                                    :viewport viewport
                                    :on-ready on-ready
                                    :started? (:started? scene-data)}])
                          (when (and (some? viewport)
                                     (or (not (:done @loading))
                                         (and (not (:started? scene-data))
                                              (not= mode ::modes/editor))))
                            [overlay-wrapper {:viewport viewport}
                             [loader-screen {:on-start-click on-start-click
                                             :loading        @loading}]])
                          (when (and (some? viewport)
                                     (:done @loading)
                                     show-waiting-screen?)
                            [overlay-wrapper {:viewport viewport}
                             [waiting-screen {:on-start-click on-start-click
                                              :loading        @loading}]])]))})))
