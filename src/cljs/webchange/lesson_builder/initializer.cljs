(ns webchange.lesson-builder.initializer)

(defonce handlers (atom {}))

(defn on-init
  [key fn]
  (swap! handlers assoc key fn))

(defn init
  []
  (js/console.log :initializer-init)
  (doseq [[_ handler] @handlers]
    (handler)))

(comment
  @handlers)
