(ns webchange.interpreter.object-data.with-transition
  (:require
    [re-frame.core :as re-frame]
    [webchange.interpreter.events-register :as ier]))

(defn with-transition
  [{:keys [transition] :as object}]
  (if transition
    (assoc object :ref (fn [ref] (when ref (re-frame/dispatch [::ier/register-transition transition (atom ref)]))))
    object))
