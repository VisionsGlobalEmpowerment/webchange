(ns webchange.interpreter.renderer.stage
  (:require
    [reagent.core :as r]
    [webchange.interpreter.pixi :refer [Loader]]
    [webchange.interpreter.renderer.loader-screen :refer [loader-screen]]
    [webchange.interpreter.renderer.scene.scene :refer [scene]]
    [webchange.resources.manager :as resources]
    [webchange.interpreter.renderer.stage-utils :refer [get-stage-params]]
    [webchange.interpreter.renderer.scene.components.modes :as modes]))

(defn- init-scene
  [{:keys [scene-id resources]} current-scene-id loading]
  (when-not (= scene-id @current-scene-id)
    (reset! current-scene-id scene-id)
    (reset! loading {:done false :progress 0})
    (resources/reset-loader!)
    (resources/load-resources resources
                              {:on-complete #(swap! loading assoc :done true)
                               :on-progress #(swap! loading assoc :progress %)})))

(defn- element->viewport
  [el]
  (if-not (nil? el)
    (let [bound-rect (.getBoundingClientRect el)]
      {:width  (.-width bound-rect)
       :height (.-height bound-rect)})
    {:width  1920
     :height 1080}))

(defn stage
  []
  (let [container (r/atom nil)
        loading (r/atom {:done     false
                         :progress 0})
        current-scene-id (r/atom nil)]
    (r/create-class
      {:display-name "web-gl-scene"
       :component-did-mount
                     (fn [this]
                       (resources/init (Loader.))
                       (let [{:keys [scene-data]} (r/props this)]
                         (init-scene scene-data current-scene-id loading)))
       :component-did-update
                     (fn [this]
                       (let [{:keys [scene-data]} (r/props this)]
                         (init-scene scene-data current-scene-id loading)))
       :reagent-render
                     (fn [{:keys [mode on-ready on-start-click scene-data]}]
                       (let [viewport (-> (element->viewport @container)
                                          (get-stage-params))]
                         [:div {:ref   #(when % (reset! container (.-parentNode %)))
                                :style {:width  "100%"
                                        :height "100%"}}
                          (when (and (:done @loading)
                                     (or (= mode ::modes/editor)
                                         (:started? scene-data)))
                            [scene {:mode     mode
                                    :objects  (:objects scene-data)
                                    :viewport viewport
                                    :on-ready on-ready
                                    :started? (:started? scene-data)}])
                          (when (or (not (:done @loading))
                                    (and (not (:started? scene-data))
                                         (not= mode ::modes/editor)))
                            (let [scale-x (:scale-x viewport)
                                  scale-y (:scale-y viewport)
                                  translate-x (* -100 (- (/ 1 (* 2 scale-x)) 0.5))
                                  translate-y (* -100 (- (/ 1 (* 2 scale-y)) 0.5))]
                              [:div {:style {:position  "absolute"
                                             :transform (str "scale(" scale-x ", " scale-y ")"
                                                             "translate(" translate-x "%, " translate-y "%)"
                                                             "translate(" (:x viewport) "px, " (:y viewport) "px)")
                                             :width     (:display-width viewport)
                                             :height    (:display-height viewport)}}
                               [loader-screen {:on-start-click on-start-click
                                               :loading        @loading}]]))]))})))
