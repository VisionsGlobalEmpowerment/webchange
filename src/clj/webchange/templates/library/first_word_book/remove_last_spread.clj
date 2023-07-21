(ns webchange.templates.library.first-word-book.remove-last-spread
  (:require
    [webchange.templates.library.first-word-book.add-spread :refer [get-spread-info]]
    [webchange.utils.scene-data :refer [remove-actions remove-object remove-scene-object remove-view]]))

(defn remove-spread
  [activity-data id]
  (let [{:keys [object actions]} (get-spread-info id)]
    (-> activity-data
        (remove-object object {:remove-children? true})
        (remove-scene-object object)
        (remove-actions actions)
        (remove-view object))))

(defn remove-last-spread
  [activity-data]
  (let [last-spread-number (get-in activity-data [:metadata :last-spread-idx])
        {:keys [object actions]} (get-spread-info last-spread-number)]
    (-> activity-data
        (update-in [:actions :set-total-spreads-number :var-value] dec)
        (update-in [:metadata :last-spread-idx] dec)
        (remove-object object {:remove-children? true})
        (remove-scene-object object)
        (remove-actions actions)
        (remove-view object))))
