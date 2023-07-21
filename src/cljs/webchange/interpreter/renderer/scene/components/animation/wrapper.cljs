(ns webchange.interpreter.renderer.scene.components.animation.wrapper
  (:require
    [webchange.interpreter.renderer.scene.components.wrapper :refer [create-wrapper]]
    [webchange.interpreter.renderer.scene.filters.filters :as f]
    [webchange.interpreter.renderer.scene.components.animation.utils :as utils]
    [webchange.interpreter.renderer.scene.components.animation.utils.idle :as utils-idle]
    [webchange.interpreter.renderer.scene.components.animation.utils.state :as character-state]
    [webchange.interpreter.renderer.scene.components.animation.utils.subscriptions :as subscriptions]
    [webchange.interpreter.renderer.scene.components.utils :as common-utils]))

(defn wrap
  [type name container state]
  (let [subscriptions (subscriptions/init-events (:animation @state))
        char-state (character-state/init-state)]
    (create-wrapper {:name                   name
                     :type                   type
                     :object                 container
                     :state                  char-state
                     :set-highlight          (fn [highlight]
                                               (let [highlight-filter-set (f/has-filter-by-name (:animation @state) "glow")]
                                                 (if (and (not highlight) highlight-filter-set) (f/set-filter (:animation @state) "" {}))
                                                 (if (and highlight (not highlight-filter-set))
                                                   (f/set-filter (:animation @state) "glow" {}))))
                     :set-scale              (fn [scale]
                                               (swap! state update :props merge {:scale scale})
                                               (utils/set-scale (:animation @state) scale)
                                               (common-utils/emit (:container @state) "scaleChanged" scale))
                     :get-scale              (fn [params] (utils/get-scale (:animation @state) params))
                     :set-slot               (fn [slot-name image-src slot-params]
                                               ;; ToDo: Remove double set-animation-slot call
                                               ;; Without this slot is not updated if new skin is created inside the method
                                               ;; If do no crete new skin and hack current skin, then updated slot has incorrect size
                                               ;; See https://trello.com/c/zCet3flh
                                               (utils/set-animation-slot image-src (:animation @state) slot-name slot-params)
                                               (utils/set-animation-slot image-src (:animation @state) slot-name slot-params))
                     :set-skin               (fn [skin-name]
                                               (swap! state update :props dissoc :skin-names)
                                               (swap! state update :props merge {:skin-name :skin-name})
                                               (utils/set-skin (:animation @state) skin-name))
                     :set-combined-skin
                     (fn [skin-names]
                       (swap! state update :props dissoc :skin-name)
                       (swap! state update :props merge {:skin-names skin-names})
                       (utils/reset-skeleton container state))
                     :set-skeleton           (fn [{:keys [name skin skin-names]}]
                                               (swap! state update :props merge {:name name})
                                               (if (some? skin-names)
                                                 (do (swap! state update :props merge {:skin-names skin-names})
                                                     (swap! state update :props dissoc :skin-name))
                                                 (do (swap! state update :props merge {:skin-name skin})
                                                     (swap! state update :props dissoc :skin-names)))

                                               (utils/reset-skeleton container state))
                     :add-animation          (fn [track animation-name loop? delay]
                                               (utils/add-animation (:animation @state) animation-name {:track-index track
                                                                                                        :delay       delay
                                                                                                        :loop?       loop?}))
                     :set-animation          (fn [track animation-name loop? & {:keys [update-pose]}]
                                               (utils/set-animation (:animation @state) animation-name {:track-index track
                                                                                                        :loop?       loop?})

                                               (when update-pose
                                                 (utils/update-pose (:animation @state))))
                     :remove-animation       (fn [track mix-duration]
                                               (utils/set-empty-animation (:animation @state) {:track-index  track
                                                                                               :mix-duration mix-duration}))
                     :start-animation        (fn []
                                               (utils/set-auto-update (:animation @state) true))

                     :subscribe              (fn [event handler] (subscriptions/subscribe subscriptions event handler))
                     :subscribe-once         (fn [event handler] (subscriptions/subscribe-once subscriptions event handler))
                     :unsubscribe            (fn [event handler] (subscriptions/unsubscribe subscriptions event handler))

                     :enable-idle-animation  (fn [] (utils-idle/enable-idle-animation state char-state))
                     :disable-idle-animation (fn [] (utils-idle/disable-idle-animation state))
                     :reset-idle-animation   (fn [] (utils-idle/reset-idle-animation state char-state))
                     :play-animation-once    (fn [track animation-name callback]
                                               (subscriptions/subscribe-to-animation-once
                                                subscriptions animation-name "complete"
                                                (fn []
                                                  (utils/set-empty-animation (:animation @state) {:track-index  track
                                                                                                  :mix-duration 5})
                                                  (when (fn? callback)
                                                    (callback))))
                                               (utils/set-animation (:animation @state) animation-name {:track-index track
                                                                                                        :loop?       false}))})))
