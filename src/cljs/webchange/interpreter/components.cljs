(ns webchange.interpreter.components
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require
    [re-frame.core :as re-frame]
    [webchange.subs :as subs]
    [webchange.interpreter.sound :as sound]
    [webchange.interpreter.events :as ie]
    [webchange.interpreter.variables.core :as vars.core]
    [webchange.resources.scene-parser :refer [get-scene-resources]]
    [webchange.interpreter.renderer.stage :refer [stage]]
    [webchange.interpreter.subs :as isubs]
    [webchange.interpreter.object-data.get-object-data :refer [get-object-data]]
    [webchange.interpreter.renderer.scene.scene :as scene-renderer]

    [webchange.interpreter.renderer.scene.components.group.propagate]))

(defn- get-layer-objects-data
  [scene-id layer-objects]
  (->> layer-objects
       (map #(get-object-data scene-id %))
       (into [])))

(defn- get-scene-objects-data
  [scene-id scene-layers]
  (->> scene-layers
       (mapcat #(get-layer-objects-data scene-id %))
       (remove nil?)
       (into [])))

(defn- scene-started?
  [scene-id]
  (let [scene-data @(re-frame/subscribe [::subs/scene scene-id])
        scene-started @(re-frame/subscribe [::subs/scene-started])
        auto-start (get-in scene-data [:metadata :autostart] false)
        course-started @(re-frame/subscribe [::subs/playing])]
    (and course-started
         (or scene-started auto-start))))


(defn- get-scene-data
  [scene-id scene-data dataset-items]
  (cond
    (nil? scene-id) nil
    (empty? scene-data) nil
    (nil? dataset-items) nil   ;; ToDo: actually do not start scene until datasets are loaded, there might be no datasets at all
    :else {:scene-id  scene-id
           :objects   (get-scene-objects-data scene-id (:scene-objects scene-data))
           :resources (get-scene-resources scene-id scene-data)
           :started?  (scene-started? scene-id)}))


(defn- start-scene
  []
  (sound/init)
  (re-frame/dispatch [::ie/start-playing]))

(defn- start-triggers
  []
  (let [status (vars.core/get-variable "status")]
    (if (not= status :running)
      (do
        (vars.core/set-variable! "status" :running)
        (re-frame/dispatch [::ie/trigger :start])))))

(defn stage-wrapper
  [{:keys [mode scene-id scene-data dataset-items]}]
  ^{:key scene-id}
  [stage {:mode           mode
          :scene-data     (get-scene-data scene-id scene-data dataset-items)
          :on-ready       start-triggers
          :on-start-click start-scene}])

(defn course
  [{:keys [mode]}]
  (let [scene-id @(re-frame/subscribe [::subs/current-scene])
        scene-data @(re-frame/subscribe [::subs/scene scene-id])
        dataset-items @(re-frame/subscribe [::isubs/dataset-items])]
    [:div {:style {:position "fixed"
                   :top      0
                   :left     0
                   :width    "100%"
                   :height   "100%"}}
     [:style "html, body {margin: 0; max-width: 100%; overflow: hidden;}"]
     [stage-wrapper {:mode          mode
                     :scene-id      scene-id
                     :scene-data    scene-data
                     :dataset-items dataset-items}]]))
