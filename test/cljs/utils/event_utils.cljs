(ns utils.event-utils)

(defn get-change-event-object
  [value]
  #js {:target #js {:value value}})