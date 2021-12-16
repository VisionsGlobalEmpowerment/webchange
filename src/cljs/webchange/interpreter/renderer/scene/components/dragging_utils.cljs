(ns webchange.interpreter.renderer.scene.components.dragging-utils
  (:require
    [goog.object :as g]))

(def props-key "custom-props")

(defn get-prop
  ([object]
   (get-prop object nil))
  ([object key]
   (let [props-store (or (g/get object props-key) (clj->js {}))]
     (if (some? key)
       (g/get props-store key)
       props-store))))

(defn set-prop
  [object key value]
  (let [props-store (or (g/get object props-key) (clj->js {}))]
    (g/set props-store key value)
    (g/set object props-key props-store)))

(defn call-handler
  ([object handler-name]
   (call-handler object handler-name []))
  ([object handler-name event-params]
   (if-let [handler-data (get-prop object handler-name)]
     (.apply (.-fn handler-data)
             (.-context handler-data)
             event-params))))

(defn set-handler
  [object handler-name handler]
  (when handler
    (set-prop object handler-name (clj->js {:fn      handler
                                            :context object}))))

(defn hide-click-handler
  [object]
  (let [events (.-_events object)]
    (->> (.-click events)
         (set-prop object "click-handler"))
    (js-delete events "click")
    (->> (.-tap events)
         (set-prop object "tap-handler"))
    (js-delete events "tap")))

(defn call-click-handler
  [object event-params]
  (doseq [handler-name ["click-handler" "tap-handler"]]
    (call-handler object handler-name event-params)))

(defn- throttle-handler-by-callback
  [handler]
  (let [in-progress? (atom false)]
    (fn [this]
      (when-not @in-progress?
        (reset! in-progress? true)
        (handler this #(reset! in-progress? false))))))

(defn throttle-handler
  [handler options]
  (case (:throttle options)
    "action-done" (throttle-handler-by-callback handler)
    handler))
