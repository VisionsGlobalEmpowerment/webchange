(ns webchange.interpreter.variables.subs
  (:require
    [re-frame.core :as re-frame]))

(re-frame/reg-sub
  ::variable
  (fn [db [_ scene-id var-name]]
    (get-in db [:scenes scene-id :variables var-name])))