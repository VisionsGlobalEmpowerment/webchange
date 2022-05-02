(ns webchange.common.warehouse
  (:require [re-frame.core :as re-frame]))

(defn warehouse [requests]
  (re-frame/reg-fx
    :http-xhrio
    (fn [{:keys [uri on-success on-failure]}]
      (when-let [response (get requests uri)]
        (re-frame/dispatch (conj on-success response))))))
