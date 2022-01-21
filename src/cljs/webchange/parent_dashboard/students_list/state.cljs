(ns webchange.parent-dashboard.students-list.state
  (:require
    [re-frame.core :as re-frame]))

(re-frame/reg-sub
  ::students-list
  (fn [_]
    (->> {:name   "Ivan"
          :level  1
          :lesson 1}
         (repeat 6)
         (map-indexed (fn [idx data]
                        (assoc data :id idx))))))
