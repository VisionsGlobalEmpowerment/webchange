(ns webchange.utils.browser-history
  (:require
    [webchange.routes :refer [parse-url]]))

(defonce initialized? (atom false))
(defonce event-handlers (atom {}))

(defn- add-handler
  [event handler]
  (swap! event-handlers update event conj handler))

(defn- get-handlers
  [event]
  (get @event-handlers event []))

(defn- call-event-handlers
  [event]
  (doseq [handler (get-handlers event)]
    (when (fn? handler)
      (handler))))

(defn- pop-state-handler
  [_]
  (call-event-handlers :back))

(defn- init
  []
  (when-not @initialized?
    (set! (.-onpopstate js/window) pop-state-handler)
    (reset! initialized? true)))

(defn add-event-handler
  [event handler]
  (init)
  (add-handler event handler))
