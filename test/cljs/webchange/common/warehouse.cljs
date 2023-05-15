(ns webchange.common.warehouse
  (:require [re-frame.core :as re-frame]))

(defn mock-warehouse
  [requests]
  (re-frame/clear-fx :cached-http-xhrio)
  (re-frame/reg-fx
   :cached-http-xhrio
   (fn [{:keys [uri on-success _on-failure]}]
     (when-let [response (get requests uri)]
       (re-frame/dispatch (conj on-success response))))))
