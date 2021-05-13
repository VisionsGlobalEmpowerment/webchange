(ns webchange.editor-v2.layout.components.object-selector.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.state.state :as state]))

(defn- scene-data->objects-list
  [scene-data]
  (->> (:objects scene-data)
       (filter (fn [[_ object-data]] (= "text" (:type object-data))))))

(re-frame/reg-sub
  ::text-objects
  (fn []
    [(re-frame/subscribe [::state/scene-data])])
  (fn [[scene-data]]
    (scene-data->objects-list scene-data)))
