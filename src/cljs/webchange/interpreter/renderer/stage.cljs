(ns webchange.interpreter.renderer.stage
  (:require
    [cljsjs.pixi]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.interpreter.renderer.loader-screen :refer [loader-screen]]
    [webchange.interpreter.renderer.scene.scene :refer [scene]]
    [webchange.interpreter.renderer.resources :as resources]
    [webchange.subs :as subs]
    [webchange.interpreter.renderer.stage-utils :refer [get-stage-params]]))

(def Loader (.. js/PIXI -Loader))

(defn- init-scene
  [new-scene-data current-scene-data loading]
  (when (and (not (nil? new-scene-data))
             (not= new-scene-data @current-scene-data))
    (reset! current-scene-data new-scene-data)
    (reset! loading {:done false :progress 0})
    (resources/load-resources (:resources @current-scene-data)
                              {:on-complete #(swap! loading assoc :done true)
                               :on-progress #(swap! loading assoc :progress %)})))

(defn stage
  []
  (let [loading (r/atom {:done     false
                         :progress 0})
        current-scene-data (r/atom nil)]
    (r/create-class
      {:display-name "web-gl-scene"
       :component-did-mount
                     (fn [this]
                       (resources/init (Loader.))
                       (let [{:keys [scene-data]} (r/props this)]
                         (init-scene scene-data current-scene-data loading)))
       :component-did-update
                     (fn [this]
                       (let [{:keys [scene-data]} (r/props this)]
                         (init-scene scene-data current-scene-data loading)))
       :reagent-render
                     (fn [{:keys [on-ready on-start-click]}]
                       (let [viewport @(re-frame/subscribe [::subs/viewport])]
                         [:div {:style {:width  "100%"
                                        :height "100%"}}
                          (when (and (:done @loading)
                                     (:started? @current-scene-data))
                            [scene {:objects  (:objects @current-scene-data)
                                    :viewport (get-stage-params viewport)
                                    :on-ready on-ready
                                    :started? (:started? @current-scene-data)}])
                          (when (or (not (:done @loading))
                                    (not (:started? @current-scene-data)))
                            [loader-screen {:on-start-click on-start-click
                                            :loading        @loading}])]))})))
