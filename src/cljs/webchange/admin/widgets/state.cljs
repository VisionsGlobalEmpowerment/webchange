(ns webchange.admin.widgets.state
  (:require
    [re-frame.core :as re-frame]))

;; Callbacks

(def callbacks-key :callbacks)

(defn set-callbacks
  [db data]
  (assoc db callbacks-key data))

(defn get-callback
  [db callback-name]
  (get-in db [callbacks-key callback-name] #()))


(re-frame/reg-fx
  ::callback
  (fn [[callback & params]]
    (when (fn? callback)
      (apply callback params))))
