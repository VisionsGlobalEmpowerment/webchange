(ns webchange.admin.widgets.state
  (:require
    [re-frame.core :as re-frame]))

(re-frame/reg-fx
  ::callback
  (fn [[callback & params]]
    (when (fn? callback)
      (apply callback params))))
