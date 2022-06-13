(ns webchange.interpreter.components
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.subs :as subs]
    [webchange.interpreter.renderer.scene.full-screen :refer [request-fullscreen exit-fullscreen lock-screen-orientation]]
    [webchange.interpreter.sound :as sound]
    [webchange.interpreter.utils.video :as video]
    [webchange.interpreter.events :as ie]
    [webchange.interpreter.variables.core :as vars.core]
    [webchange.resources.scene-parser :as scene-parser]
    [webchange.interpreter.renderer.stage :refer [stage]]
    [webchange.interpreter.subs :as isubs]
    [webchange.interpreter.object-data.get-object-data :refer [get-object-data]]
    [webchange.interpreter.renderer.scene.components.group.propagate]
    [webchange.interpreter.renderer.scene.modes.modes :as modes]
    [webchange.state.state :as state]))

(defn- get-layer-objects-data
  [scene-id layer-objects]
  (->> layer-objects
       (map #(get-object-data scene-id %))
       (into [])))

(def get-activity-resources scene-parser/get-activity-resources)

(defn- get-scene-objects-data
  [scene-id scene-layers]
  (->> scene-layers
       (mapcat #(get-layer-objects-data scene-id %))
       (remove nil?)
       (into [])))

(defn get-scene-objects-data-by-scene-data
  [scene-data]
  (->> (get scene-data :scene-objects)
       (flatten)
       (map #(get-object-data nil % (:objects scene-data) (:metadata scene-data)))))

(defn- scene-started?
  [scene-id]
  (let [scene-data @(re-frame/subscribe [::subs/scene scene-id])
        scene-started @(re-frame/subscribe [::subs/scene-started])
        auto-start (get-in scene-data [:metadata :autostart] false)
        course-started @(re-frame/subscribe [::subs/playing])]
    (and course-started
         (or scene-started auto-start))))

(defn- get-scene-data
  [scene-id scene-data]
  (cond
    (nil? scene-id) nil
    (empty? scene-data) nil
    :else {:scene-id  scene-id
           :objects   (get-scene-objects-data scene-id (:scene-objects scene-data))
           :resources (get-activity-resources scene-id scene-data)
           :metadata  (:metadata scene-data)
           :started?  (scene-started? scene-id)}))

(defn start-scene
  []
  (sound/init)
  (re-frame/dispatch [::ie/start-playing])
  (-> (request-fullscreen)
      (.then lock-screen-orientation)
      (.catch #())))

(defn- start-triggers
  []
  (let [status (vars.core/get-variable "status")]
    (if (not= status :running)
      (do
        (vars.core/set-variable! "status" :running)
        (re-frame/dispatch [::ie/trigger :start])))))

(defn stage-wrapper
  [{:keys [mode scene-id scene-data on-ready reset-resources? stage-props]
    :or   {mode             ::modes/game
           on-ready         #()
           reset-resources? true
           stage-props      {}}}]
  (let [prepared-scene-data (get-scene-data scene-id scene-data)]
    ^{:key scene-id}
    [stage (merge {:mode             mode
                   :scene-data       prepared-scene-data
                   :on-ready         (fn []
                                       (re-frame/dispatch [::ie/trigger :render])
                                       (when (modes/start-on-ready? mode)
                                         (start-triggers))
                                       (when (fn? on-ready)
                                         (on-ready)))
                   :on-start-click   start-scene
                   :reset-resources? reset-resources?}
                  stage-props)]))

(defn- component-will-unmount
  []
  (re-frame/dispatch [::ie/stop-playing])
  (sound/stop-all-audio!)
  (video/stop-all-video!)
  (-> (exit-fullscreen)
      (.catch #())))

(defn interpreter
  [{:keys [mode on-ready stage-props]}]
  (r/with-let []
    (let [scene-id @(re-frame/subscribe [::state/current-scene-id])
          scene-data @(re-frame/subscribe [::state/scene-data scene-id])
          user-mode (-> @(re-frame/subscribe [::isubs/user-mode])
                        (modes/get-mode))]
      [stage-wrapper {:mode          (or user-mode mode)
                      :scene-id      scene-id
                      :scene-data    scene-data
                      :stage-props   stage-props
                      :on-ready      on-ready}])
    (finally (component-will-unmount))))

(defn course
  [props]
  [:div {:style {:position "fixed"
                 :top      0
                 :left     0
                 :width    "100%"
                 :height   "100%"}}
   [:style "html, body {margin: 0; max-width: 100%; overflow: hidden; background-color: black;}"]
   [interpreter props]])
