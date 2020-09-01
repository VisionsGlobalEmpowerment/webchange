(ns webchange.interpreter.components
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require
    [re-frame.core :as re-frame]
    [webchange.subs :as subs]
    [webchange.interpreter.executor :as e]
    [webchange.interpreter.events :as ie]
    [webchange.interpreter.variables.subs :as vars.subs]
    [webchange.interpreter.variables.events :as vars.events]
    [webchange.interpreter.scene-resources-parser :refer [get-scene-resources]]
    [webchange.interpreter.renderer.stage :refer [stage]]
    [webchange.interpreter.subs :as isubs]
    [webchange.interpreter.object-data.get-object-data :refer [get-object-data]]))

(defn- get-layer-objects-data
  [scene-id layer-objects]
  (reduce (fn [result object-name]
            (conj result (get-object-data scene-id object-name)))
          []
          layer-objects))

(defn- get-scene-objects-data
  [scene-id scene-layers]
  (->> scene-layers
       (reduce (fn [scene-objects-data scene-layer]
                 (concat scene-objects-data (get-layer-objects-data scene-id scene-layer)))
               [])
       (remove nil?)))

(defn- scene-started?
  [scene-id]
  (let [scene-data @(re-frame/subscribe [::subs/scene scene-id])
        scene-started @(re-frame/subscribe [::subs/scene-started])
        auto-start (get-in scene-data [:metadata :autostart] false)
        course-started @(re-frame/subscribe [::subs/playing])]
    (and course-started
         (or scene-started auto-start))))


(defn- get-scene-data
  [scene-id scene-data scene-objects dataset-items]
  (cond
    (nil? scene-id) nil
    (empty? scene-objects) nil
    (empty? scene-data) nil
    (empty? dataset-items) nil                              ;; ToDo: actually do not stat scene until datasets are loaded
    :else {:scene-id  scene-id
           :objects   (get-scene-objects-data scene-id scene-objects)
           :resources (get-scene-resources scene-id scene-data)
           :started?  (scene-started? scene-id)}))


(defn- start-scene
  []
  (e/init)
  (re-frame/dispatch [::ie/start-playing]))

(defn- start-triggers
  [scene-id]
  (let [status (re-frame/subscribe [::vars.subs/variable scene-id "status"])]
    (if (not= @status :running)
      (do
        (re-frame/dispatch [::vars.events/execute-set-variable {:var-name "status" :var-value :running}])
        (re-frame/dispatch [::ie/trigger :start])))))

(defn- stage-wrapper
  [{:keys [scene-id]}]
  (let [scene-data @(re-frame/subscribe [::subs/scene scene-id])
        scene-objects @(re-frame/subscribe [::subs/scene-objects scene-id])
        dataset-items @(re-frame/subscribe [::isubs/dataset-items])]
    ^{:key scene-id}
    [stage {:scene-data     (get-scene-data scene-id scene-data scene-objects dataset-items)
            :on-ready       #(start-triggers scene-id)
            :on-start-click start-scene}]))

(defn course
  [_]
  (let [scene-id @(re-frame/subscribe [::subs/current-scene])]
    [stage-wrapper {:scene-id scene-id}]))
