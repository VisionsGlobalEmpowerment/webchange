(ns webchange.interpreter.renderer.scene.components.animation.wrapper
  (:require
    [webchange.interpreter.renderer.scene.components.wrapper :refer [create-wrapper]]
    [webchange.interpreter.renderer.scene.filters.filters :as f]
    [webchange.interpreter.renderer.scene.components.animation.animation-params :refer [animations-params]]
    [webchange.interpreter.renderer.scene.components.animation.utils :as utils]
    [webchange.interpreter.renderer.scene.components.utils :as common-utils]))

(defn- get-default-skeleton-params
  [skeleton-name]
  (let [default-params (->> (keyword skeleton-name)
                            (get animations-params))]
    (select-keys default-params [:scale :speed])))

(defn wrap
  [type name container state]
  (create-wrapper {:name             name
                   :type             type
                   :object           container
                   :set-highlight    (fn [highlight]
                                       (let [highlight-filter-set (f/has-filter-by-name (:animation @state) "glow")]
                                         (if (and (not highlight) highlight-filter-set) (f/set-filter (:animation @state) "" {}))
                                         (if (and highlight (not highlight-filter-set))
                                           (f/set-filter (:animation @state) "glow" {}))))
                   :set-scale        (fn [scale]
                                       (utils/set-scale (:animation @state) scale)
                                       (common-utils/emit (:container @state) "scaleChanged" scale))
                   :set-slot         (fn [slot-name image-src slot-params]
                                       ;; ToDo: Remove double set-animation-slot call
                                       ;; Without this slot is not updated if new skin is created inside the method
                                       ;; If do no crete new skin and hack current skin, then updated slot has incorrect size
                                       ;; See https://trello.com/c/zCet3flh
                                       (utils/set-animation-slot image-src (:animation @state) slot-name slot-params)
                                       (utils/set-animation-slot image-src (:animation @state) slot-name slot-params))
                   :set-skin         (fn [skin-name]
                                       (utils/set-skin (:animation @state) skin-name))
                   :set-combined-skin
                                     (fn [skin-names]
                                       (utils/set-combined-skin (:animation @state) skin-names))
                   :set-skeleton
                                     (fn [{:keys [name skin skin-names]}]

                                       (swap! state update :props merge {:name name} (get-default-skeleton-params name))
                                       (if (some? skin-names)
                                         (do (swap! state update :props merge {:skin-names skin-names})
                                             (swap! state update :props dissoc :skin-name))
                                         (do (swap! state update :props merge {:skin-name skin})
                                             (swap! state update :props dissoc :skin-names)))

                                       (utils/reset-skeleton container state))
                   :add-animation    (fn [track animation-name loop? delay]
                                       (utils/add-animation (:animation @state) animation-name {:track-index track
                                                                                                :delay       delay
                                                                                                :loop?       loop?}))
                   :set-animation    (fn [track animation-name loop?]
                                       (utils/set-animation (:animation @state) animation-name {:track-index track
                                                                                                :loop?       loop?}))
                   :remove-animation (fn [track mix-duration]
                                       (utils/set-empty-animation (:animation @state) {:track-index  track
                                                                                       :mix-duration mix-duration}))
                   :start-animation  (fn []
                                       (utils/set-auto-update (:animation @state) true))}))
