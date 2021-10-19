(ns webchange.interpreter.renderer.scene.components.animation.utils.subscriptions)

(defn subscribe
  [subscriptions event handler]
  (let [event-handlers (get @subscriptions event [])]
    (->> (conj event-handlers handler)
         (swap! subscriptions assoc event))))

(defn unsubscribe
  [subscriptions event handler]
  (let [event-handlers (get @subscriptions event [])]
    (->> (remove #(= % handler) event-handlers)
         (swap! subscriptions assoc event))))

(defn subscribe-once
  [subscriptions event handler]
  (let [custom-handler (atom nil)]
    (reset! custom-handler (fn [animation track]
                             (unsubscribe subscriptions event @custom-handler)
                             (handler animation track)))
    (subscribe subscriptions event @custom-handler)))

(defn- call-handlers
  ([subscriptions event-name]
   (call-handlers subscriptions event-name []))
  ([subscriptions event-name params]
   (let [handlers (get @subscriptions event-name [])]
     (doseq [handler handlers]
       (apply handler params)))))

(defn- get-animation-name
  [track]
  (.. track -animation -name))

(defn- subscribe-to-animation
  [subscriptions animation-name event handler]
  (subscribe subscriptions event
             #(when (= animation-name %)
                (handler %1 %2))))

(defn subscribe-to-animation-once
  [subscriptions animation-name event handler]
  (let [custom-handler (atom nil)]
    (reset! custom-handler (fn [animation track]
                             (when (= animation-name animation)
                               (unsubscribe subscriptions event @custom-handler)
                               (handler animation track))))
    (subscribe subscriptions event @custom-handler)))

(defn init-events
  [animation]
  (let [subscriptions (atom {})
        animation-state (.-state animation)]
    (.addListener animation-state (clj->js {:event    (fn [track event]
                                                        (let [event-name (.. event -data -name)]
                                                          (call-handlers subscriptions event-name [(get-animation-name track) track event])))
                                            :complete (fn [track]
                                                        (call-handlers subscriptions "complete" [(get-animation-name track) track]))
                                            :start    (fn [track]
                                                        (call-handlers subscriptions "start" [(get-animation-name track) track]))
                                            :end      (fn [track]
                                                        (call-handlers subscriptions "end" [(get-animation-name track) track]))}))
    subscriptions))
