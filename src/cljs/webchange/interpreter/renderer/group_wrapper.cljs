(ns webchange.interpreter.renderer.group-wrapper
  (:require
    [webchange.interpreter.renderer.group-utils :as utils]))

(defn wrap
  [name container]
  {:name         name
   :container    container
   :get-position (fn []
                   (utils/get-position container))
   :set-position (fn [position]
                   (utils/set-position container position))})
