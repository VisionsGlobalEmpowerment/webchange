(ns webchange.interpreter.renderer.stage
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.interpreter.pixi :refer [clear-texture-cache Loader]]
    [webchange.interpreter.renderer.loader-screen :refer [loader-screen]]
    [webchange.interpreter.renderer.waiting-screen :refer [waiting-screen]]
    [webchange.interpreter.renderer.scene.scene :refer [scene]]
    [webchange.interpreter.renderer.scene.app :refer [reset-app! resize-app!]]
    [webchange.resources.manager :as resources]
    [webchange.interpreter.renderer.stage-utils :refer [get-stage-params]]
    [webchange.interpreter.renderer.state.overlays :as overlays]
    [webchange.interpreter.renderer.scene.modes.modes :as modes]
    [webchange.interpreter.variables.core :as vars.core]
    [webchange.state.state-flipbook :as state-flipbook]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn- init-scene
  [{:keys [scene-id resources]} current-scene-id loading reset-resources?]
  (when (or (vars.core/get-global-variable :force-scene-update)
            (not= scene-id @current-scene-id))
    (vars.core/set-global-variable! :force-scene-update false)
    (reset! current-scene-id scene-id)
    (let [load (fn [on-complete-by-progress]
                 (reset! loading {:done false :progress 0})
                 (reset-app! {:reset-textures? reset-resources?})
                 (when reset-resources?
                   (resources/reset-loader!))
                 (resources/load-resources resources
                                           {:on-complete (fn [] (swap! loading assoc :done true))
                                            :on-progress (fn [progress]
                                                           (swap! loading assoc :progress progress)
                                                           (let [e 0.0001
                                                                 left (- 1 progress)]
                                                             (when (and (> e left) on-complete-by-progress)
                                                               (js/setTimeout #(when (not (:done @loading))
                                                                                 (on-complete-by-progress))
                                                                              10000))))}))
          reload #(load nil)]
      (load reload))))

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

(defn- get-screens-state
  [{:keys [loading-state props scene-data show-waiting-screen? viewport force-show-scene?]}]
  (let [{:keys [mode show-loader-screen?]
         :or   {show-loader-screen? true}} props

        show-scene? (and (some? viewport)
                         (:done loading-state)
                         (or force-show-scene?
                             (= mode ::modes/editor)
                             (= mode ::modes/preview)
                             (:started? scene-data)))
        show-loader-screen? (and show-loader-screen?
                                 (some? viewport)
                                 (or (not (:done loading-state))
                                     (and (not (:started? scene-data))
                                          (not= mode ::modes/editor)
                                          (not= mode ::modes/preview))))]
    {:show-scene?          show-scene?
     :show-loader-screen?  show-loader-screen?
     :show-waiting-screen? (and (some? viewport)
                                (:done loading-state)
                                show-waiting-screen?)
     :ready-to-start?      (and (not show-scene?)
                                (:done loading-state))}))

(defn stage
  []
  (let [container (r/atom nil)
        loading (r/atom {:done     false
                         :progress 0})
        current-scene-id (r/atom nil)
        handle-resize (get-handler handle-screen-resize container)
        prev-viewport (atom nil)]
    (r/create-class
      {:display-name "web-gl-stage"
       :component-did-mount
                     (fn [this]
                       (.addEventListener js/window "resize" handle-resize)
                       (resources/init (.-shared Loader))
                       (let [{:keys [reset-resources? scene-data]} (r/props this)]
                         (init-scene scene-data current-scene-id loading reset-resources?)))
       :component-will-unmount
                     (fn []
                       (.removeEventListener js/window "resize" handle-resize))
       :component-did-update
                     (fn [this]
                       (let [{:keys [reset-resources? scene-data]} (r/props this)]
                         (init-scene scene-data current-scene-id loading reset-resources?)))
       :reagent-render
                     (fn [{:keys [mode on-ready on-ready-to-start on-start-click scene-data force-show-scene?] :as props}]
                       (let [viewport (-> (element->viewport @container)
                                          (get-stage-params))
                             show-waiting-screen? @(re-frame/subscribe [::overlays/show-waiting-screen?])
                             current-page @(re-frame/subscribe [::state-flipbook/current-page-side])
                             {:keys [ready-to-start? show-loader-screen? show-scene? show-waiting-screen?]}
                             (get-screens-state {:loading-state        @loading
                                                 :props                props
                                                 :scene-data           scene-data
                                                 :show-waiting-screen? show-waiting-screen?
                                                 :force-show-scene?    force-show-scene?
                                                 :viewport             viewport})]
                         (when (and (some? @prev-viewport)
                                    (not= @prev-viewport viewport))
                           (handle-resize))
                         (when (and ready-to-start?
                                    (fn? on-ready-to-start))
                           (on-ready-to-start))
                         (reset! prev-viewport viewport)
                         [:div {:ref        #(when % (reset! container (.-parentNode %)))
                                :class-name (get-class-name {"stage-container"                  true
                                                             (str "current-page-" current-page) (some? current-page)})
                                :style      {:width    "100%"
                                             :height   "100%"
                                             :position "relative"}}
                          (when show-scene?
                            [scene {:mode     mode
                                    :objects  (:objects scene-data)
                                    :metadata (:metadata scene-data)
                                    :viewport viewport
                                    :on-ready on-ready
                                    :started? (:started? scene-data)}])
                          (when show-loader-screen?
                            [overlay-wrapper {:viewport viewport}
                             [loader-screen {:on-start-click on-start-click
                                             :loading        @loading}]])
                          (when show-waiting-screen?
                            [overlay-wrapper {:viewport viewport}
                             [waiting-screen]])]))})))
