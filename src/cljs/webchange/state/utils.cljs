(ns webchange.state.utils
  (:require
    [webchange.state.state :as state]))

(defn get-scene-object
  [db predicate]
  (let [scene-data (state/scene-data db)]
    (some (fn [[object-name object-data]]
            (and (predicate object-name object-data)
                 [object-name object-data]))
          (get-in scene-data [:objects]))))
